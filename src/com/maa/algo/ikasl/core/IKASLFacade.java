/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.enums.GenType;
import com.maa.algo.enums.MiningType;
import com.maa.algo.ikasl.auxi.HitThresholdGenerator;
import com.maa.algo.ikasl.auxi.InterLinkGenerator;
import com.maa.algo.input.Normalizer;
import com.maa.algo.listeners.TaskListener;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.objects.LastGenLayer;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.FileReader;
import com.maa.algo.utils.LogMessages;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

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
    private String streamID;
    private String currTimeFrame;
    private AlgoParameters algoParams;
    private DefaultValueListener defListener;
    private IKASLStepListener ikaslListener;

    public IKASLFacade(String streamID, AlgoParamModel aPModel, DefaultValueListener defListener, IKASLStepListener ikaslListener) {
        linkGen = new InterLinkGenerator();
        this.aPModel = aPModel;
        ikaslXMLWriter = new IKASLOutputXMLWriter();
        this.streamID = streamID;
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
        iParser.parseInput(ImportantFileNames.DATA_DIRNAME + File.separator + streamID + File.separator + inputFileName);
        System.out.println("Processing " + inputFileName + " file");
        ArrayList<double[]> iWeights = iParser.getIWeights();
        ArrayList<String> iNames = iParser.getINames();
        currTimeFrame = iParser.getTimeFrame();
        iWeights = Normalizer.normalizeWithBounds(iWeights, algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(), algoParams.getDIMENSIONS());

        if (currLC == 0) {
            //run the GSOM algorithm and output LearnLayer
            LearnLayer initLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, null);

            //run IKASL aggregation and output GenLayer
            //ArrayList<String> bestHits = getHitNodeIDs(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer initGLayer = null;
            if (algoParams.getMINING_TYPE() == MiningType.ANOMALY) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(initLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, algoParams.getGType());
            } else {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(initLLayer, algoParams.getHIT_THRESHOLD(), algoParams.getMAX_NEIGHBORHOOD_RADIUS(), algoParams);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, algoParams.getGType());
            }

            if (initGLayer == null) {
                //error initlayer null
            }

            //add it to allGLayers
            saveLastGLayer(new LastGenLayer(initGLayer, currLC));

            mapInputsToGNodes(currLC, initGLayer, iWeights, iNames);

            ikaslListener.IKASLStepCompleted(streamID);

        } else {
            //get currLC-1 genLayer
            GenLayer prevGLayer = lastGLayer.getgLayer();

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            LearnLayer currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);

            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            //ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer currGLayer = null;

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
            mapInputsToGNodes(currLC, currGLayer, iWeights, iNames);

            ArrayList<GNode> nonHitNodes = learner.getNonHitNodes(currLC);
            for (GNode gn : nonHitNodes) {
                currGLayer.addNode(gn);
                if (prevGLayer.getMap().containsValue(gn)) {
                    prevGLayer.getMap().remove(Utils.generateIndexString(gn.getLc(), gn.getId()));
                }
            }

            //add Genlayer(currLC) to allGLayers
            saveLastGLayer(new LastGenLayer(currGLayer, currLC));

            ikaslListener.IKASLStepCompleted(streamID);
            //getClusterPurityVector(currGLayer, prevGLayer, currLC);

            //ArrayList<String> links = linkGen.getAllIntsectLinks(currGLayer, allGNodeInputs.get(currLC), prevGLayer, allGNodeInputs.get(currLC - 1), 50);
            //allIntSectLinks.add(links);

        }
    }

    public void saveLastGLayer(LastGenLayer gLayer) {
        try (OutputStream file = new FileOutputStream(ImportantFileNames.DATA_DIRNAME + File.separator + getStreamID() + File.separator + Constants.LAST_LAYER_FILE_NAME);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(gLayer);
        } catch (IOException ex) {
            //SERIALIZE ERROR
        }
    }

    public LastGenLayer retrieveLastGLayer() {
        try (
                InputStream file = new FileInputStream(ImportantFileNames.DATA_DIRNAME + File.separator + getStreamID() + File.separator + Constants.LAST_LAYER_FILE_NAME);
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

    private void mapInputsToGNodes(int currLC, GenLayer gLayer, ArrayList<double[]> iWeights, ArrayList<String> iNames) {

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
        String loc = ImportantFileNames.DATA_DIRNAME + File.separator + getStreamID() + File.separator + "LC" + currLC + ".xml";
        ikaslXMLWriter.writeXML(loc, testResultMap, currTimeFrame);

    }

    private AlgoParameters readAndSetAlgoParameters(AlgoParamModel aPModel) {

        GenType selectedGType = GenType.FUZZY;
        if (aPModel.getAggrType() == AggregationType.AVERAGE) {
            selectedGType = GenType.AVG;
        } else if (aPModel.getAggrType() == AggregationType.FUZZY) {
            selectedGType = GenType.FUZZY;
        }

        double[] min = new double[aPModel.getDimensions()];
        double[] max = new double[aPModel.getDimensions()];
        FileReader fr = new FileReader();
        ArrayList<String> minMaxList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + getStreamID() + File.separator + Constants.BOUNDS_FILE);

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
            defListener.useDefaultBounds(getStreamID());
        }

        double[] weights = new double[aPModel.getDimensions()];
        ArrayList<String> weightsStrList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + getStreamID() + File.separator + Constants.WEIGHT_FILE);
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
            defListener.useDefaultWeights(getStreamID());
        }

        AlgoParameters params = new AlgoParameters(aPModel.getDimensions(), aPModel.getSpreadFactor(), aPModel.getNeighRad(), aPModel.getLearningRate(), aPModel.getIterations(),
                aPModel.getHitThreshold(), min, max, weights, selectedGType, MiningType.GENERAL, DistanceType.EUCLIDEAN);

        return params;
    }

    /**
     * @return the streamID
     */
    public String getStreamID() {
        return streamID;
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
