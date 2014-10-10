/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.main;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.enums.GenType;
import com.maa.algo.enums.MiningType;
import com.maa.algo.ikasl.auxi.HitThresholdGenerator;
import com.maa.vis.main.InterLinkGenerator;
import com.maa.algo.ikasl.core.IKASLGeneralizer;
import com.maa.algo.ikasl.core.IKASLLearner;
import com.maa.algo.input.Normalizer;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.objects.LastGenLayer;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.FileReader;
import com.maa.algo.utils.Utils;
import com.maa.enums.AggregationType;
import com.maa.listeners.DefaultValueListener;
import com.maa.listeners.IKASLStepListener;
import com.maa.models.AlgoParamModel;
import com.maa.utils.DefaultValues;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.InputParser;
import com.maa.xml.IKASLOutputXMLWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class IKASLFacade {

    private AlgoParamModel aPModel;
    private IKASLLearner learner;
    private IKASLGeneralizer generalizer;
    private InterLinkGenerator linkGen;
    private String dir;
    private IKASLOutputXMLWriter ikaslXMLWriter;
    private String jobID;
    private String currTimeFrame;
    private AlgoParameters algoParams;
    private DefaultValueListener defListener;
    private IKASLStepListener ikaslListener;

    public IKASLFacade(String streamID, AlgoParamModel aPModel, DefaultValueListener defListener, IKASLStepListener ikaslListener) {
        linkGen = new InterLinkGenerator();
        this.aPModel = aPModel;
        ikaslXMLWriter = new IKASLOutputXMLWriter();
        this.jobID = streamID;
        this.defListener = defListener;

        algoParams = readAndSetAlgoParameters(aPModel);

        learner = new IKASLLearner(algoParams);
        generalizer = new IKASLGeneralizer(algoParams);

        this.ikaslListener = ikaslListener;
    }

    public void runSingleStep() {

        int currLC;
        LastGenLayer lastGLayer = retrieveLastGLayer();
        if (lastGLayer == null) {
            currLC = 0;
        } else {
            currLC = lastGLayer.getLC() + 1;
            System.out.println(currLC);
        }

        InputParser iParser = new InputParser();
        String inputFileName = "input" + (currLC + 1) + ".txt";
        iParser.parseInput(ImportantFileNames.DATA_DIRNAME + File.separator + jobID + File.separator + inputFileName);
        System.out.println("Processing " + inputFileName + " file");
        ArrayList<double[]> iWeights = iParser.getIWeights();
        ArrayList<String> iNames = iParser.getINames();
        currTimeFrame = iParser.getTimeFrame();
        iWeights = Normalizer.normalizeWithBounds(iWeights, algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(), algoParams.getDIMENSIONS());

        LearnLayer currLLayer;
        GenLayer currGLayer;
        GenLayer prevGLayer;
        Map<String, String> currInputMap;
        Map<String, String> prevInputMap;

        if (currLC == 0) {
            //run the GSOM algorithm and output LearnLayer
            currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, null);

            //run IKASL aggregation and output GenLayer
            //ArrayList<String> bestHits = getHitNodeIDs(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);

            if (algoParams.getMINING_TYPE() == MiningType.ANOMALY) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(currLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, algoParams.getGType());
            } else {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(currLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, algoParams.getGType());
            }

            if (currGLayer == null) {
                //error initlayer null
            }

            currInputMap = mapInputsToGNodes(currLC, currGLayer, iWeights, iNames);
            Map<String, String> weights = getMapGNodeWeights(currGLayer);
            for (GNode gn : currGLayer.getMap().values()) {
                if (gn.getPrevHitVal() == 0) {
                    currInputMap.put(Utils.generateIndexString(gn.getLc(), gn.getId()) + Constants.NODE_TOKENIZER + gn.getParentID(), "");
                }
            }

            String loc = ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + "LC" + currLC + ".xml";
            ikaslXMLWriter.writeXML(loc, currInputMap, weights, currTimeFrame);

            //add it to allGLayers
            saveLastGLayer(new LastGenLayer(currGLayer, currLC, currInputMap));

            ikaslListener.IKASLStepCompleted(jobID);
        } else {
            //get currLC-1 genLayer
            prevGLayer = lastGLayer.getgLayer();
            prevInputMap = lastGLayer.getInputMap();

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);

            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            //ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            currGLayer = null;

            if (algoParams.getMINING_TYPE() == MiningType.ANOMALY) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(currLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, algoParams.getGType());
            } else {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(currLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, algoParams.getGType());
            }

            if (currGLayer == null) {
                //error null glayer
            }

            //We call mapping inputs to GNodes before adding non-hit nodes from prev GLayer to new layer
            //because otherwise it is possible to non-hit node to have inputs assigned
            //Intuitively it should not happen, because it appeared as a non-hit node at the first place, 
            //because there were no inputs similar to that.
            currInputMap = mapInputsToGNodes(currLC, currGLayer, iWeights, iNames);

            for (GNode gn : currGLayer.getMap().values()) {
                if (gn.getPrevHitVal() == 0) {
                    currInputMap.put(Utils.generateIndexString(gn.getLc(), gn.getId()) + Constants.NODE_TOKENIZER + gn.getParentID(), "");
                }
            }

            connectGNodesToBelowLayer(currGLayer, prevGLayer, currInputMap, prevInputMap);
            //add non hit nodes to the GLyaer, but if the node is present in a previous layer, remove it
            /*ArrayList<GNode> nonHitNodes = learner.getNonHitNodes(currLC);
             for (GNode gn : nonHitNodes) {
             currGLayer.addNode(gn);
             if (prevGLayer.getMap().containsValue(gn)) {
             prevGLayer.getMap().remove(Utils.generateIndexString(gn.getLc(), gn.getId()));
             }
             }*/

            Map<String, String> weights = getMapGNodeWeights(currGLayer);
            String loc = ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + "LC" + currLC + ".xml";
            ikaslXMLWriter.writeXML(loc, currInputMap, weights, currTimeFrame);

            //add Genlayer(currLC) to allGLayers
            saveLastGLayer(new LastGenLayer(currGLayer, currLC, currInputMap));

            ikaslListener.IKASLStepCompleted(jobID);

        }
    }

    private void connectGNodesToBelowLayer(GenLayer currLayer, GenLayer prevLayer, Map<String, String> currInputMap, Map<String, String> prevInputMap) {
        for (Map.Entry<String,GNode> e1 : currLayer.getMap().entrySet()) {
            GNode gn1 = e1.getValue();
            String input1Str = currInputMap.get(Utils.generateIndexString(gn1.getLc(), gn1.getId()));
            
            double maxStrength = 0;
            GNode maxPNode = null;
                    
            if (input1Str != null && !input1Str.isEmpty()) {
                ArrayList<String> input1 = new ArrayList<>(Arrays.asList(input1Str.split(Constants.I_J_TOKENIZER)));
                for (Map.Entry<String,GNode> e2 : prevLayer.getMap().entrySet()) {
                    GNode gn2 = e2.getValue();
                    String input2Str = currInputMap.get(Utils.generateIndexString(gn2.getLc(), gn2.getId()));
                    
                    if (input2Str != null && !input2Str.isEmpty()) {
                        ArrayList<String> input2 = new ArrayList<>(Arrays.asList(input2Str.split(Constants.I_J_TOKENIZER)));
                        
                        if(getNodeIntersectStrength(input1, input2)>0.75){
                            if(getNodeIntersectStrength(input1, input2)>maxStrength){
                                maxStrength = getNodeIntersectStrength(input1, input2);
                                maxPNode = gn2;
                            }
                        }
                    }
                }
                
                if(maxPNode != null){
                    gn1.setParentID(Utils.generateIndexString(maxPNode.getLc(), maxPNode.getId()));
                    e1.setValue(gn1);
                }
            }
        }
    }

    private double getNodeIntersectStrength(ArrayList<String> list1, ArrayList<String> list2) {
        int minVal = Math.min(list1.size(), list2.size());
        list1.retainAll(list2);
        int common = list1.size();

        return (double) common * 1.0 / minVal;
    }

    public void saveLastGLayer(LastGenLayer gLayer) {
        try (OutputStream file = new FileOutputStream(ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + Constants.LAST_LAYER_FILE_NAME);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(gLayer);
        } catch (IOException ex) {
            //SERIALIZE ERROR
        }
    }

    public LastGenLayer retrieveLastGLayer() {
        try (
                InputStream file = new FileInputStream(ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + Constants.LAST_LAYER_FILE_NAME);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);) {
            //deserialize the List
            return (LastGenLayer) input.readObject();

        } catch (ClassNotFoundException ex) {
            //error
        } catch (IOException ex) {
            //error
        }
        return null;
    }

    private Map<String, String> getMapGNodeWeights(GenLayer gLayer) {
        Map<String, String> gNodeWeights = new HashMap<>();

        for (Map.Entry<String, GNode> e : gLayer.getMap().entrySet()) {
            String s = e.getValue().getWeights()[0] + "";
            for (int i = 1; i < e.getValue().getWeights().length; i++) {
                s += "," + e.getValue().getWeights()[i];
            }
            gNodeWeights.put(e.getKey(), s);
        }
        return gNodeWeights;
    }

    private Map<String, String> mapInputsToGNodes(int currLC, GenLayer gLayer, ArrayList<double[]> iWeights, ArrayList<String> iNames) {

        //for all GNodes, restore the PrevHitValue
        for (Map.Entry<String, GNode> e : gLayer.getMap().entrySet()) {
            if (e.getValue().getPrevHitVal() > 0) {
                e.getValue().setPrevHitVal(0);
            }
        }

        Map<String, String> testResultMap = new HashMap<String, String>();
        Map<String, GNode> nodeMap = gLayer.getMap();


        for (int i = 0; i < iWeights.size(); i++) {

            GNode winner = Utils.selectGWinner(nodeMap, iWeights.get(i), algoParams.getDIMENSIONS(), algoParams.getATTR_WEIGHTS(), algoParams.getDistType());

            String winnerStr = Utils.generateIndexString(winner.getLc(), winner.getId());
            String testResultKey = winnerStr + Constants.NODE_TOKENIZER + winner.getParentID();
            GNode winnerNode = nodeMap.get(winnerStr);
            winnerNode.increasePrevHitVal();

            if (!testResultMap.containsKey(testResultKey)) {
                testResultMap.put(testResultKey, iNames.get(i));
            } else {
                String currStr = testResultMap.get(testResultKey);
                String newStr = currStr + "," + iNames.get(i);
                testResultMap.remove(testResultKey);
                testResultMap.put(testResultKey, newStr);
            }
        }

        return testResultMap;
    }

    private AlgoParameters readAndSetAlgoParameters(AlgoParamModel aPModel) {

        GenType selectedGType = GenType.FUZZY;
        if (aPModel.getAggrType() == AggregationType.AVERAGE) {
            selectedGType = GenType.AVG;
        } else if (aPModel.getAggrType() == AggregationType.FUZZY) {
            selectedGType = GenType.FUZZY;
        } else if (aPModel.getAggrType() == AggregationType.NONE) {
            selectedGType = GenType.NONE;
        }

        double[] min = new double[aPModel.getDimensions()];
        double[] max = new double[aPModel.getDimensions()];
        FileReader fr = new FileReader();
        ArrayList<String> minMaxList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + Constants.BOUNDS_FILE);

        if (minMaxList != null && !minMaxList.isEmpty()) {
            String[] minStr = minMaxList.get(0).split(Constants.INPUT_TOKENIZER);
            String[] maxStr = minMaxList.get(1).split(Constants.INPUT_TOKENIZER);

            for (int i = 0; i < minStr.length; i++) {
                min[i] = Double.parseDouble(minStr[i]);
                max[i] = Double.parseDouble(maxStr[i]);
            }

        } else {
            for (int i = 0; i < min.length; i++) {
                min[i] = DefaultValues.MIN_DEFAULT;
                max[i] = DefaultValues.MAX_DEFAULT;
            }
            defListener.useDefaultBounds(getJobID());
        }

        double[] weights = new double[aPModel.getDimensions()];
        ArrayList<String> weightsStrList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + getJobID() + File.separator + Constants.WEIGHT_FILE);
        if (weightsStrList != null && !weightsStrList.isEmpty()) {
            String weightsStr = weightsStrList.get(0);
            String[] weightTokens = weightsStr.split(Constants.INPUT_TOKENIZER);

            for (int i = 0; i < weightTokens.length; i++) {
                weights[i] = Double.parseDouble(weightTokens[i]);
            }
        } else {
            for (int i = 0; i < weights.length; i++) {
                weights[i] = DefaultValues.MIN_DEFAULT;
            }
            defListener.useDefaultWeights(getJobID());
        }

        AlgoParameters params = new AlgoParameters(aPModel.getDimensions(), aPModel.getSpreadFactor(), aPModel.getNeighRad(), aPModel.getLearningRate(), aPModel.getIterations(),
                aPModel.getHitThreshold(), min, max, weights, selectedGType, MiningType.GENERAL, DistanceType.EUCLIDEAN);

        return params;
    }

    /**
     * @return the streamID
     */
    public String getJobID() {
        return jobID;
    }

    public int getCurrLC() {
        LastGenLayer lastLayer = retrieveLastGLayer();
        if (lastLayer != null) {
            return lastLayer.getLC();
        } else {
            return 0;
        }
    }
}
