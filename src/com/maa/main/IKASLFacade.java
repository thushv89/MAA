/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.main;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.enums.GenType;
import com.maa.algo.enums.MiningType;
import com.maa.algo.ikasl.auxi.HitThresholdGenerator;
import com.maa.algo.ikasl.core.IKASLGeneralizer;
import com.maa.algo.ikasl.core.IKASLLearner;
import com.maa.algo.ikasl.links.IKASLGlobalLinkUpdater;
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
    private String dir;
    private IKASLOutputXMLWriter ikaslXMLWriter;
    private String jobID;
    private String currTimeFrame;
    private AlgoParameters algoParams;
    private DefaultValueListener defListener;
    private IKASLStepListener ikaslListener;
    private int currLC;

    IKASLGlobalLinkUpdater lnkUpdater;

    public IKASLFacade(String streamID, AlgoParamModel aPModel, DefaultValueListener defListener, IKASLStepListener ikaslListener) {
        this.aPModel = aPModel;
        ikaslXMLWriter = new IKASLOutputXMLWriter();
        this.jobID = streamID;
        this.defListener = defListener;

        algoParams = readAndSetAlgoParameters(aPModel);

        learner = new IKASLLearner(algoParams);
        generalizer = new IKASLGeneralizer(algoParams);

        lnkUpdater = new IKASLGlobalLinkUpdater();
        
        this.ikaslListener = ikaslListener;
    }

    public void runSingleStep() {

        LastGenLayer lastGLayer = retrieveLastGLayer();
        if (lastGLayer == null) {
            currLC = 0;
        } else {
            currLC = lastGLayer.getLC() + 1;
        }

        InputParser iParser = new InputParser();
        String inputFileName = "input" + (currLC + 1) + ".txt";
        iParser.parseInput(ImportantFileNames.DATA_DIRNAME + File.separator + jobID + File.separator + inputFileName);
        System.out.println("\nProcessing " + inputFileName + " file");
        ArrayList<double[]> iWeights = iParser.getIWeights();
        ArrayList<String> iNames = iParser.getINames();
        currTimeFrame = iParser.getTimeFrame();
        iWeights = Normalizer.normalizeWithBounds(iWeights, algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(), algoParams.getDIMENSIONS());

        LearnLayer currLLayer;
        GenLayer currGLayer;
        ArrayList<GenLayer> prevGLayers;
        Map<String, String> currInputMap;
        ArrayList<Map<String, String>> prevInputMaps;

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
            lastGLayer = new LastGenLayer();
            lastGLayer.addData(currGLayer, currInputMap, currLC);
            saveLastGLayer(lastGLayer);

            ikaslListener.IKASLStepCompleted(jobID);
        } else {
            //get currLC-1 genLayer
            prevGLayers = lastGLayer.getgLayer();
            prevInputMaps = lastGLayer.getInputMap();

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayers.get(prevGLayers.size() - 1).getCopyMap());
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

            connectGNodesToBelowLayer(currGLayer, prevGLayers, currInputMap, prevInputMaps);
            updateCurrInputMap(currInputMap, currGLayer);

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
            lastGLayer.addData(currGLayer, currInputMap, currLC);
            saveLastGLayer(lastGLayer);

            ikaslListener.IKASLStepCompleted(jobID);

        }
    }


    //update the inputMap (after generating links) to have the correct parent
    private void updateCurrInputMap(Map<String, String> inputMap, GenLayer currGLayer) {
        Map<String, String> newInputMap = new HashMap<>();

        for (Map.Entry<String, String> e : inputMap.entrySet()) {
            String key = e.getKey();
            String[] keyTokens = key.split(Constants.NODE_TOKENIZER);
            String newKey = keyTokens[0] + Constants.NODE_TOKENIZER + currGLayer.getMap().get(keyTokens[0]).getParentID();
            String newValue = e.getValue();

            newInputMap.put(newKey, newValue);
        }

        inputMap.clear();
        inputMap.putAll(newInputMap);
    }

    private void connectGNodesToBelowLayer(GenLayer currLayer, ArrayList<GenLayer> prevLayers, Map<String, String> currInputMap, ArrayList<Map<String, String>> prevInputMaps) {

        //do the following operation(s) for each node in the current GLayer
        for (Map.Entry<String, GNode> e1 : currLayer.getMap().entrySet()) {
            GNode gn1 = e1.getValue();
            String gn1Key = Utils.generateIndexString(gn1.getLc(), gn1.getId()) + Constants.NODE_TOKENIZER + gn1.getParentID();
            String input1Str = currInputMap.get(gn1Key);

            //if there are some inputs in the considered GNode
            if (input1Str != null && !input1Str.isEmpty()) {
                //convert inputString to an ArrayList
                ArrayList<String> input1 = new ArrayList<>(Arrays.asList(input1Str.split(Constants.I_J_TOKENIZER)));

                ArrayList<String> parentIDs = new ArrayList<>();    //parentIDs of the considered node
                ArrayList<Double> strengths = new ArrayList<>();    //corresponding strength to each parent

                boolean noMatchInBelowLayer = false;    //this keeps a boolean to check if there are no matching nodes in the layer below

                //do the following operation(s) for each node in the previous GLayer
                for (Map.Entry<String, GNode> e2 : prevLayers.get(prevLayers.size() - 1).getMap().entrySet()) {
                    GNode gn2 = e2.getValue();
                    String gn2Key = Utils.generateIndexString(gn2.getLc(), gn2.getId()) + Constants.NODE_TOKENIZER + gn2.getParentID();
                    String input2Str = prevInputMaps.get(prevInputMaps.size() - 1).get(gn2Key);

                    if (input2Str != null && !input2Str.isEmpty()) {
                        ArrayList<String> input2 = new ArrayList<>(Arrays.asList(input2Str.split(Constants.I_J_TOKENIZER)));

                        if (getNodeIntersectStrength(gn1.getLc(), input1, gn2.getLc(), input2) > DefaultValues.STD_INTERSECT_STRENGTH) {
                            //if (getNodeIntersectStrength(input1, input2) > maxStrength) {
                            parentIDs.add(Utils.generateIndexString(gn2.getLc(), gn2.getId()));
                            strengths.add(getNodeIntersectStrength(gn1.getLc(), input1, gn2.getLc(), input2));
                            //}
                        } else {
                            noMatchInBelowLayer = true;
                        }
                    }
                }

                //if we can't find a good match from the layer immediately below, we take the one with maximum
                //strength and try to find if a best match parent exist
                if (noMatchInBelowLayer) {
                    String bestPgn;
                    double maxStrength = 0;
                    //take node by node from the previous layer
                    for (Map.Entry<String, GNode> e2 : prevLayers.get(prevLayers.size() - 1).getMap().entrySet()) {
                        GNode gn2 = e2.getValue();
                        String gn2Key = Utils.generateIndexString(gn2.getLc(), gn2.getId()) + Constants.NODE_TOKENIZER + gn2.getParentID();
                        String input2Str = prevInputMaps.get(prevInputMaps.size() - 1).get(gn2Key);

                        //if there is atleast 1 input in the gn2
                        if (input2Str != null && !input2Str.isEmpty()) {
                            ArrayList<String> input2 = new ArrayList<>(Arrays.asList(input2Str.split(Constants.I_J_TOKENIZER)));

                            //if strength gn1 gn2 is greater than MIN_STRENGTH, get the bestParent which gives maximum strength
                            if (getNodeIntersectStrength(gn1.getLc(), input1, gn2.getLc(), input2) > DefaultValues.MIN_INTERSECT_STRENGTH) {
                                if (getNodeIntersectStrength(gn1.getLc(), input1, gn2.getLc(), input2) > maxStrength) {
                                    bestPgn = Utils.generateIndexString(gn2.getLc(), gn2.getId());
                                    maxStrength = getNodeIntersectStrength(gn1.getLc(), input1, gn2.getLc(), input2);
                                }
                            }
                        }
                    }
                }
                //if there is atleast one matching parent, then we check whether there are better parents
                //in the earlier layers than the last previous layer.
                if (!parentIDs.isEmpty()) {
                    //need to write a recursive method to find the best matching parent
                    for (int i = 0; i < parentIDs.size(); i++) {
                        String cPGn = parentIDs.get(i);

                        String bestPGn = cPGn;
                        if (strengths.get(i) < DefaultValues.FIND_PARENT_THRESHOLD) {
                            bestPGn = findBestParent(cPGn, input1, prevLayers, prevInputMaps, currLC, cPGn, strengths.get(i));
                        }
                        parentIDs.set(i, bestPGn);
                    }

                    String fullParentID = parentIDs.get(0);
                    for (int i = 1; i < parentIDs.size(); i++) {
                        fullParentID += Constants.PARENT_TOKENIZER + parentIDs.get(i);
                    }

                    gn1.setParentID(fullParentID);
                    e1.setValue(gn1);

                }
            }
        }        
        
        lnkUpdater.updateGlobalLinkList(currLayer, currLC);
    }

    private String findBestParent(String pgnID, ArrayList<String> inputs, ArrayList<GenLayer> gLayers, ArrayList<Map<String, String>> inputMaps,
            int currLC, String bestPgnID, double maxStrength) {

        int pgnLC = Integer.parseInt(pgnID.split(Constants.I_J_TOKENIZER)[0]);
        int pgnIdx = Math.min(gLayers.size(), DefaultValues.IKASL_LINK_DEPTH) - (currLC - pgnLC);

        if (pgnIdx >= 0) {
            if (!pgnID.contains(Constants.PARENT_TOKENIZER)) {
                GNode pgn = gLayers.get(pgnIdx).getMap().get(pgnID);
                String ppgnID = pgn.getParentID();

                if (!ppgnID.contains(Constants.PARENT_TOKENIZER)) {
                    int ppgnLC = Integer.parseInt(ppgnID.split(Constants.I_J_TOKENIZER)[0]);
                    int ppgnIdx = Math.min(gLayers.size(), DefaultValues.IKASL_LINK_DEPTH) - (currLC - ppgnLC);

                    if (ppgnIdx >= 0) {
                        GNode ppgn = gLayers.get(ppgnIdx).getMap().get(ppgnID);

                        String ppgnKey = ppgnID + Constants.NODE_TOKENIZER + ppgn.getParentID();

                        ArrayList<String> ppgnInputs = new ArrayList<>(Arrays.asList(inputMaps.get(ppgnIdx).get(ppgnKey).split(Constants.I_J_TOKENIZER)));
                        double currStrength = getNodeIntersectStrength(currLC, inputs, ppgnLC, ppgnInputs);
                        if (currStrength > maxStrength) {
                            maxStrength = currStrength;
                            bestPgnID = ppgnID;
                        }
                        findBestParent(ppgnID, ppgnInputs, gLayers, inputMaps, currLC, bestPgnID, maxStrength);
                    }
                } else {
                    String[] ppgnIDTokens = ppgnID.split(Constants.PARENT_TOKENIZER);
                    for (String ppgnIDTok : ppgnIDTokens) {
                        int ppgnLC = Integer.parseInt(ppgnIDTok.split(Constants.I_J_TOKENIZER)[0]);
                        int ppgnIdx = Math.min(gLayers.size(), DefaultValues.IKASL_LINK_DEPTH) - (currLC - ppgnLC);

                        if (ppgnIdx >= 0) {
                            GNode ppgn = gLayers.get(ppgnIdx).getMap().get(ppgnIDTok);

                            String ppgnKey = ppgnIDTok + Constants.NODE_TOKENIZER + ppgn.getParentID();

                            ArrayList<String> ppgnInputs = new ArrayList<>(Arrays.asList(inputMaps.get(ppgnIdx).get(ppgnKey).split(Constants.I_J_TOKENIZER)));
                            double currStrength = getNodeIntersectStrength(currLC, inputs, ppgnLC, ppgnInputs);
                            if (currStrength > maxStrength) {
                                maxStrength = currStrength;
                                bestPgnID = ppgnIDTok;
                            }
                            findBestParent(ppgnIDTok, ppgnInputs, gLayers, inputMaps, currLC, bestPgnID, maxStrength);
                        }
                    }
                }
            } else {
                String[] pgnIDTokens = pgnID.split(Constants.PARENT_TOKENIZER);

                for (String pgnIDTok : pgnIDTokens) {
                    GNode pgn = gLayers.get(pgnIdx).getMap().get(pgnIDTok);
                    String ppgnID = pgn.getParentID();

                    if (!ppgnID.contains(Constants.PARENT_TOKENIZER)) {
                        int ppgnLC = Integer.parseInt(ppgnID.split(Constants.I_J_TOKENIZER)[0]);
                        int ppgnIdx = Math.min(gLayers.size(), DefaultValues.IKASL_LINK_DEPTH) - (currLC - ppgnLC);

                        if (ppgnIdx >= 0) {
                            GNode ppgn = gLayers.get(ppgnIdx).getMap().get(ppgnID);
                            String ppgnKey = ppgnID + Constants.NODE_TOKENIZER + ppgn.getParentID();


                            ArrayList<String> ppgnInputs = new ArrayList<>(Arrays.asList(inputMaps.get(ppgnIdx).get(ppgnKey).split(Constants.I_J_TOKENIZER)));
                            double currStrength = getNodeIntersectStrength(currLC, inputs, ppgnLC, ppgnInputs);
                            if (currStrength > maxStrength) {
                                maxStrength = currStrength;
                                bestPgnID = ppgnID;
                            }
                            findBestParent(ppgnID, ppgnInputs, gLayers, inputMaps, currLC, bestPgnID, maxStrength);
                        }
                    } else {
                        String[] ppgnIDTokens = ppgnID.split(Constants.PARENT_TOKENIZER);
                        for (String ppgnIDTok : ppgnIDTokens) {
                            int ppgnLC = Integer.parseInt(ppgnIDTok.split(Constants.I_J_TOKENIZER)[0]);
                            int ppgnIdx = Math.min(gLayers.size(), DefaultValues.IKASL_LINK_DEPTH) - (currLC - ppgnLC);

                            if (ppgnIdx >= 0) {
                                GNode ppgn = gLayers.get(ppgnIdx).getMap().get(ppgnIDTok);
                                String ppgnKey = ppgnIDTok + Constants.NODE_TOKENIZER + ppgn.getParentID();


                                ArrayList<String> ppgnInputs = new ArrayList<>(Arrays.asList(inputMaps.get(ppgnIdx).get(ppgnKey).split(Constants.I_J_TOKENIZER)));
                                double currStrength = getNodeIntersectStrength(currLC, inputs, ppgnLC, ppgnInputs);
                                if (currStrength > maxStrength) {
                                    maxStrength = currStrength;
                                    bestPgnID = ppgnIDTok;
                                }
                                findBestParent(ppgnIDTok, ppgnInputs, gLayers, inputMaps, currLC, bestPgnID, maxStrength);
                            }
                        }

                    }

                }
            }
        }
        return bestPgnID;
    }

    private double getNodeIntersectStrength(int lc1, ArrayList<String> l1, int lc2, ArrayList<String> l2) {
        ArrayList<String> list1 = new ArrayList<>(l1);
        ArrayList<String> list2 = new ArrayList<>(l2);

        if (list1.isEmpty() || list2.isEmpty()) {
            return 0;
        }

        int minVal = Math.min(list1.size(), list2.size());
        list1.retainAll(list2);
        int common = list1.size();

        //normalize the value to be in between 0.5 & 1
        double numOfInputDiffCoeff = (double) (Math.max(l1.size(), l2.size()) - Math.abs(l1.size() - l2.size())) / Math.max(l1.size(), l2.size());
        numOfInputDiffCoeff = 0.5 + (numOfInputDiffCoeff * (1 - 0.5));

        double timeDiffCoeff = (double) (DefaultValues.IKASL_LINK_DEPTH - Math.abs(lc1 - lc2) - 1) / DefaultValues.IKASL_LINK_DEPTH;
        timeDiffCoeff = 0.5 + (timeDiffCoeff * (1 - 0.5));
        return (double) common * numOfInputDiffCoeff * 1.0 / minVal;
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

    public IKASLGlobalLinkUpdater getGlobalLinkUpdater(){
        return lnkUpdater;
    }
    
}
