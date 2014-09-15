/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;

import com.maa.algo.enums.GenType;
import com.maa.algo.enums.MiningType;
import com.maa.vis.main.GNodeVisualizer;
import com.maa.algo.ikasl.auxi.HitThresholdGenerator;
import com.maa.vis.main.InterLinkGenerator;
import com.maa.algo.input.NumericalDataParser;
import com.maa.algo.listeners.TaskListener;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.objects.LNode;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.ClusterQualityUtils;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.LogMessages;
import com.maa.algo.utils.Utils;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLRun {

    private int currLC = 0;
    private ArrayList<GenLayer> allGLayers;
    private ArrayList<Map<String, String>> allGNodeInputs;
    private ArrayList<ArrayList<double[]>> allIWeights;
    private ArrayList<ArrayList<String>> allINames;
    private ArrayList<ArrayList<String>> allIntSectLinks;
    private NumericalDataParser parser;
    private IKASLLearner learner;
    private IKASLGeneralizer generalizer;
    InterLinkGenerator linkGen;
    private TaskListener tListener;
    private String dir;
    
    public IKASLRun(TaskListener tListener) {
        this.tListener = tListener;

        allGLayers = new ArrayList<GenLayer>();
        allGNodeInputs = new ArrayList<Map<String, String>>();

        allINames = new ArrayList<ArrayList<String>>();
        allIWeights = new ArrayList<ArrayList<double[]>>();

        allIntSectLinks = new ArrayList<ArrayList<String>>();
        
        parser = new NumericalDataParser(tListener);
        learner = new IKASLLearner(tListener);
        generalizer = new IKASLGeneralizer(tListener);
        linkGen = new InterLinkGenerator();
                
    }

    public IKASLRun(TaskListener tListener, String dir) {
        allGLayers = new ArrayList<GenLayer>();
        allGNodeInputs = new ArrayList<Map<String, String>>();

        allINames = new ArrayList<ArrayList<String>>();
        allIWeights = new ArrayList<ArrayList<double[]>>();

        parser = new NumericalDataParser(tListener);
        learner = new IKASLLearner(tListener);
        generalizer = new IKASLGeneralizer(tListener);

        this.dir = dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void runAllSteps() {
        for (int i = 0; i < AlgoParameters.LEARN_CYCLES; i++) {
        }
    }

    public void runSingleStep() {

        parser.parseInput(dir + File.separator + "input" + (currLC + 1) + ".txt");
        ArrayList<double[]> iWeights = parser.getWeights();

        iWeights = parser.normalizeWithBounds(iWeights, AlgoParameters.MIN_BOUNDS, AlgoParameters.MAX_BOUNDS);
        tListener.logMessage(LogMessages.INPUTS_NORMALIZED);

        ArrayList<String> iNames = parser.getStrForWeights();
        allIWeights.add(iWeights);
        allINames.add(iNames);

        tListener.logMessage(LogMessages.SEPARATOR);
        tListener.logMessage(LogMessages.LEARN_CYCLE_PREFIX + " " + currLC);

        if (currLC == 0) {
            //run the GSOM algorithm and output LearnLayer
            LearnLayer initLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, null);

            //run IKASL aggregation and output GenLayer
            //ArrayList<String> bestHits = getHitNodeIDs(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer initGLayer=null;
            if(AlgoParameters.MINING_TYPE == MiningType.GENERAL){
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, AlgoParameters.gType);
            }else if(AlgoParameters.MINING_TYPE == MiningType.ANOMALY){
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, AlgoParameters.gType);
            
            }
            
            if(initGLayer == null){
                tListener.logMessage(LogMessages.NULL_GLAYER);
            }
            
            //add it to allGLayers
            allGLayers.add(initGLayer);

            mapInputsToGNodes(initGLayer, allIWeights.get(currLC), allINames.get(currLC));
            
            printClusterQualityMeasures();

        } else {
            //get currLC-1 genLayer
            GenLayer prevGLayer = allGLayers.get(currLC - 1);

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            LearnLayer currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);

            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            //ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer currGLayer=null;
            if(AlgoParameters.MINING_TYPE == MiningType.GENERAL){
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);
            } else if(AlgoParameters.MINING_TYPE == MiningType.ANOMALY){
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);
            }
            
            if(currGLayer == null){
                tListener.logMessage(LogMessages.NULL_GLAYER);
            }

            //We call mapping inputs to GNodes before adding non-hit nodes from prev GLayer to new layer
            //because otherwise it is possible to non-hit node to have inputs assigned
            //Intuitively it should not happen, because it appeared as a non-hit node at the first place, 
            //because there were no inputs similar to that.
            mapInputsToGNodes(currGLayer, allIWeights.get(currLC), allINames.get(currLC));

            ArrayList<GNode> nonHitNodes = learner.getNonHitNodes(currLC);
            for(GNode gn : nonHitNodes){
                currGLayer.addNode(gn);
                if(allGLayers.get(currLC-1).getMap().containsValue(gn)){
                    allGLayers.get(currLC-1).getMap().remove(Utils.generateIndexString(gn.getLc(), gn.getId()));
                }
            }

            //add Genlayer(currLC) to allGLayers
            allGLayers.add(currGLayer);

            getClusterPurityVector(currGLayer, prevGLayer, currLC);
            
            ArrayList<String> links = linkGen.getAllIntsectLinks(currGLayer, allGNodeInputs.get(currLC), prevGLayer, allGNodeInputs.get(currLC-1), 50);
            allIntSectLinks.add(links);
            
            tListener.logMessage(LogMessages.SEPARATOR);
            tListener.logMessage("Intersection Links");
            for(String s : links) {
                tListener.logMessage(s);
            }
            tListener.logMessage(LogMessages.SEPARATOR);

            printClusterQualityMeasures();
        }
        currLC++;
    }

    public void clearAll() {
        currLC = 0;
        allGLayers.clear();
        allGNodeInputs.clear();
        allINames.clear();
        allIWeights.clear();
        allIntSectLinks.clear();
        
        parser = new NumericalDataParser(tListener);
        learner = new IKASLLearner(tListener);
        generalizer = new IKASLGeneralizer(tListener);
    }

    //This method prints the Cluster Quality Measure values (RMSSTD and RS)
    private void printClusterQualityMeasures(){
        String result = "RMSSTD: ";
        result += ClusterQualityUtils.getRMSSTD(allGNodeInputs.get(currLC), allIWeights.get(currLC), allINames.get(currLC)) + ", ";
        result += "R-Squared: ";
        result += ClusterQualityUtils.getRS(allGNodeInputs.get(currLC), allIWeights.get(currLC), allINames.get(currLC))+" ";
        
        tListener.logMessage(result);
    }
    //WE haven't considered what happens when the parent node is not from the immediate previous layer, but from a layer below that
    //SOLVED Above
    private HashMap<String, Double> getClusterPurityVector(GenLayer currLayer, GenLayer prevLayer, int currLC) {
        HashMap<String, Double> purityMap = new HashMap<String, Double>();
        tListener.logMessage(LogMessages.SEPARATOR);
        tListener.logMessage("Cluster Purity Information");

        //find total number of inputs
        int totalInputs = 0;
        for (String s : allGNodeInputs.get(currLC).values()) {
            totalInputs += s.split(Constants.INPUT_TOKENIZER).length;
        }

        for (GNode n : currLayer.getMap().values()) {
            String currNodeInputs = allGNodeInputs.get(currLC).get(Utils.generateIndexString(n.getLc(), n.getId()));
            String pNodeInputs = null;

            if (!n.getParentID().contains(Constants.PARENT_TOKENIZER)) {
                for (int j = currLC - 1; j >= 0; j--) {
                    pNodeInputs = allGNodeInputs.get(j).get(n.getParentID());
                    if (pNodeInputs != null && !pNodeInputs.isEmpty()) {
                        break;
                    }
                }

            } else {
                //logic when the node has two parents
                String[] pIDs = n.getParentID().split(Constants.PARENT_TOKENIZER);
                for (int j = currLC - 1; j >= 0; j--) {
                    pNodeInputs = allGNodeInputs.get(j).get(pIDs[0]);
                    if (pNodeInputs != null && !pNodeInputs.isEmpty()) {
                        break;
                    }
                }

                for (int i = 1; i < pIDs.length; i++) {
                    for (int j = currLC - 1; j >= 0; j--) {

                        if (allGNodeInputs.get(j).get(pIDs[i]) != null && !allGNodeInputs.get(j).get(pIDs[i]).isEmpty()) {
                            pNodeInputs += "," + allGNodeInputs.get(j).get(pIDs[i]);
                            break;
                        }
                    }
                }
            }



            if (currNodeInputs != null && !currNodeInputs.isEmpty()
                    && pNodeInputs != null && !pNodeInputs.isEmpty()) {
                String key = n.getParentID() + Constants.NODE_TOKENIZER + Utils.generateIndexString(n.getLc(), n.getId());
                double value = getStringArrIntersectionPercent(currNodeInputs.split(Constants.INPUT_TOKENIZER), pNodeInputs.split(Constants.INPUT_TOKENIZER));
                purityMap.put(key, value);
                
                double weight = getIntersectionWeight(currNodeInputs.split(Constants.INPUT_TOKENIZER).length, totalInputs);
                
                DecimalFormat df = new DecimalFormat("#.###");
                tListener.logMessage(key + ": " + df.format(value) + "%" + " ("+df.format(weight)+")");
            }
        }
        tListener.logMessage(LogMessages.SEPARATOR);
        return purityMap;
    }

    //When finding intersection of current and previous, remember to consider situations like
    //if curr node has 10 units and prev node has 5 units, but curr node has all 5 of the prev node
    //introduce a penalty for additional units the curr node having
    //We do not have to worry about that according to current implementation. Because if the above scenario occur,
    //percentage will be (5/10)*100 = 50% so it's automaticall taken care of.
    
    //Taking the max as denominator can result in a low percentage if there's a branching from parent node to 2 current nodes
    //because the denominator would be the parent nodes value

    private double getStringArrIntersectionPercent(String[] curr, String[] prev) {
        int downcount = 0;
        int extraInCurr = 0;
        for (String s1 : curr) {
            for (String s2 : prev) {
                if (s1.equals(s2)) {
                    downcount++;
                    break;
                }
            }
        }

        extraInCurr = curr.length - downcount;
        
        //downPercent can go negative if the curr node has no intersection with it's parent
        double downPercent = ((downcount*1.0/prev.length)-(extraInCurr*1.0/Math.max(curr.length,prev.length))) * 100.0;
        
        return downPercent;
    }
    
    private double getIntersectionWeight(int curr, int total) {
        return curr*1.0 / total;
    }

    private void mapInputsToGNodes(GenLayer gLayer, ArrayList<double[]> prevIWeights, ArrayList<String> prevINames) {

        Map<String, String> testResultMap = new HashMap<String, String>();
        Map<String, GNode> nodeMap = gLayer.getMap();

        for (int i = 0; i < prevIWeights.size(); i++) {

            GNode winner = Utils.selectGWinner(nodeMap, prevIWeights.get(i));

            String winnerStr = Utils.generateIndexString(winner.getLc(), winner.getId());
            String testResultKey = winnerStr;
            GNode winnerNode = nodeMap.get(winnerStr);
            winnerNode.increasePrevHitVal();

            if (!testResultMap.containsKey(testResultKey)) {
                testResultMap.put(testResultKey, prevINames.get(i));
            } else {
                String currStr = testResultMap.get(testResultKey);
                String newStr = currStr + "," + prevINames.get(i);
                testResultMap.remove(winnerStr);
                testResultMap.put(testResultKey, newStr);
            }
        }

        allGNodeInputs.add(testResultMap);

        String results = "";
        for (Map.Entry<String, String> entry : testResultMap.entrySet()) {
            String parentID = gLayer.getMap().get(entry.getKey()).getParentID();
            results += parentID + Constants.NODE_TOKENIZER + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        tListener.logMessage(results);
    }

    private ArrayList<String> getHitNodeIDs(LearnLayer lLayer, int hitThresh, double neighRad) {
        Map<String, Map<String, LNode>> lMap = lLayer.getCopyMap();

        ArrayList<String> selectedNodeIDs = new ArrayList<String>();

        //double threshold = hitThresh * Math.sqrt(neighRad) * Math.sqrt(AlgoParameters.MERGE_ONE_DIM_THRESHOLD*AlgoParameters.DIMENSIONS);
        double distThreshold = Utils.calcDist(Utils.getUniformVector(AlgoParameters.MERGE_ONE_DIM_THRESHOLD, AlgoParameters.DIMENSIONS),
                Utils.getZeroVector(AlgoParameters.DIMENSIONS), AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);
        double threshold = hitThresh * Math.sqrt(neighRad) * Math.pow(distThreshold, 2);

        for (Entry<String, Map<String, LNode>> eMap : lMap.entrySet()) {
            Map<String, Double> mapHitVals = new HashMap<String, Double>();
            //Check whether atleast one node exceed hit Threshold 
            //if not just ignore that map
            boolean noNodeExceedHT = true;
            for (LNode n : eMap.getValue().values()) {
                if (n.getHitValue() >= hitThresh) {
                    noNodeExceedHT = false;
                    break;
                }
            }

            if (noNodeExceedHT) {
                continue;
            }

            ArrayList<LNode> nodes = new ArrayList<LNode>(eMap.getValue().values());
            Collections.sort(nodes, new Comparator<LNode>() {

                @Override
                public int compare(LNode o1, LNode o2) {
                    if (o1.getHitValue() > o2.getHitValue()) {
                        return -1;
                    } else if (o1.getHitValue() == o2.getHitValue()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });

            for (int i = 0; i < nodes.size(); i++) {
                LNode n = nodes.get(i);
                String fullNodeID = eMap.getKey() + Constants.NODE_TOKENIZER + Utils.generateIndexString(n.getX(), n.getY());

                //if the current node hit value is not atleaset half of hit threshold -> ignore the node
                if (n.getHitValue() < hitThresh / 2) {
                    continue;
                }

                if (i == 0) {
                    mapHitVals.put(fullNodeID, Double.MAX_VALUE);
                } else {
                    double minPDist = Double.MAX_VALUE;
                    double minEDist = Double.MAX_VALUE;

                    for (int j = 0; j < i; j++) {
                        double pDistance = (nodes.get(i).getX() - nodes.get(j).getX()) * (nodes.get(i).getX() - nodes.get(j).getX())
                                + (nodes.get(i).getY() - nodes.get(j).getY()) * (nodes.get(i).getY() - nodes.get(j).getY());
                        pDistance = Math.sqrt(pDistance);

                        double eDistance = Utils.calcDist(nodes.get(i).getWeights(), nodes.get(j).getWeights(), 
                                AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);

                        if (pDistance < minPDist) {
                            minPDist = pDistance;
                        }
                        if (pDistance < minEDist) {
                            minEDist = eDistance;
                        }
                    }

                    if (minPDist < neighRad / 2) {
                        continue;
                    }

                    double hitValue = nodes.get(i).getHitValue() * Math.sqrt(minPDist) * Math.pow(minEDist, 2);
                    mapHitVals.put(fullNodeID, hitValue);
                }

            }

            ValueComparator bvc = new ValueComparator(mapHitVals);
            TreeMap<String, Double> sortedHitVals = new TreeMap<String, Double>(bvc);
            sortedHitVals.clear();
            sortedHitVals.putAll(mapHitVals);

            for (Entry<String, Double> e : sortedHitVals.entrySet()) {
                if (e.getValue() >= threshold) {
                    selectedNodeIDs.add(e.getKey());
                } else {
                    break;
                }
            }
        }

        return selectedNodeIDs;
    }

    public int getCurrLC() {
        return currLC;
    }

    public ArrayList<GenLayer> getAllGenLayers() {
        return allGLayers;
    }

    public ArrayList<Map<String, String>> getAllGenLayerInputs() {
        return allGNodeInputs;
    }

    public ArrayList<ArrayList<String>> getAllIntSectLinks(){
        return allIntSectLinks;
    }
    
    public void printFullIntersectionLinks(int minLength, int minCount){
        ArrayList<ArrayList<String>> gNodes = new ArrayList<ArrayList<String>>();
        for(GenLayer layer : allGLayers){
            ArrayList<String> currLayerGnodes = new ArrayList<String>();
            for(GNode gn : layer.getCopyMap().values()){
                currLayerGnodes.add(Utils.generateIndexString(gn.getLc(), gn.getId()));
            }
            gNodes.add(currLayerGnodes);
        }
        ArrayList<String> links = linkGen.getFullLinks3(gNodes, minLength, minCount, allGNodeInputs);
        
        tListener.logMessage("\n");
        tListener.logMessage("Full Intersection Links -----------------------");
        for(String s : links){
            tListener.logMessage(s);
        }
        tListener.logMessage(LogMessages.SEPARATOR);
    }
    
    public void getVisNodes(){
        GNodeVisualizer viser = new GNodeVisualizer();
        viser.assignVisCoordinatesToGNodes(allGLayers);
    }
    
    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
