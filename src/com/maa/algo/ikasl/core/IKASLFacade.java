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
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.FileReader;
import com.maa.algo.utils.LogMessages;
import com.maa.algo.utils.Utils;
import com.maa.enums.AggregationType;
import com.maa.listeners.DefaultValueListener;
import com.maa.models.AlgoParamModel;
import com.maa.utils.DefaultValues;
import com.maa.utils.ImportantFileNames;
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
    private DefaultValueListener defListener;

    public IKASLFacade(String streamID, AlgoParamModel aPModel, DefaultValueListener defListener) {
        learner = new IKASLLearner();
        generalizer = new IKASLGeneralizer();
        linkGen = new InterLinkGenerator();
        this.aPModel = aPModel;
        ikaslXMLWriter = new IKASLOutputXMLWriter();
        this.streamID = streamID;
        this.defListener = defListener;
        readAndSetAlgoParameters(aPModel);
    }

    public void runSingleStep(int currLC, ArrayList<double[]> iWeights, ArrayList<String> iNames) {

        iWeights = Normalizer.normalizeWithBounds(iWeights, AlgoParameters.MIN_BOUNDS, AlgoParameters.MAX_BOUNDS);

        if (currLC == 0) {
            //run the GSOM algorithm and output LearnLayer
            LearnLayer initLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, null);

            //run IKASL aggregation and output GenLayer
            //ArrayList<String> bestHits = getHitNodeIDs(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer initGLayer = null;
            if (AlgoParameters.MINING_TYPE == MiningType.GENERAL) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, AlgoParameters.gType);
            } else if (AlgoParameters.MINING_TYPE == MiningType.ANOMALY) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(initLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                initGLayer = generalizer.generalize(currLC, initLLayer, bestHits, AlgoParameters.gType);

            }

            if (initGLayer == null) {
                //error initlayer null
            }

            //add it to allGLayers
            saveLastGLayer(initGLayer);

            mapInputsToGNodes(currLC, initGLayer, iWeights, iNames);

        } else {
            //get currLC-1 genLayer
            GenLayer prevGLayer = retrieveLastGLayer();

            //create a copy of prevGLayer to avoid modificatiosn to existing layer
            //call IKASLLearner.learn(genLayer(currLC-1)) and output LearnLayer
            GenLayer copyOfPrevGLayer = new GenLayer(prevGLayer.getCopyMap());
            LearnLayer currLLayer = learner.trainAndGetLearnLayer(currLC, iWeights, iNames, copyOfPrevGLayer);

            //call IKASLAggregator.aggregate(learnLayer) and output Genlayer(currLC)
            //ArrayList<String> bestHits = getHitNodeIDs(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
            GenLayer currGLayer = null;
            if (AlgoParameters.MINING_TYPE == MiningType.GENERAL) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsGeneral(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);
            } else if (AlgoParameters.MINING_TYPE == MiningType.ANOMALY) {
                ArrayList<String> bestHits = HitThresholdGenerator.getHitNodeIDsAnomalies(currLLayer, AlgoParameters.HIT_THRESHOLD, AlgoParameters.MAX_NEIGHBORHOOD_RADIUS);
                currGLayer = generalizer.generalize(currLC, currLLayer, bestHits, AlgoParameters.gType);
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
            saveLastGLayer(currGLayer);

            //getClusterPurityVector(currGLayer, prevGLayer, currLC);

            //ArrayList<String> links = linkGen.getAllIntsectLinks(currGLayer, allGNodeInputs.get(currLC), prevGLayer, allGNodeInputs.get(currLC - 1), 50);
            //allIntSectLinks.add(links);

        }
    }

    public void saveLastGLayer(GenLayer gLayer) {
        try (OutputStream file = new FileOutputStream(Constants.LAST_LAYER_FILE_NAME);
                OutputStream buffer = new BufferedOutputStream(file);
                ObjectOutput output = new ObjectOutputStream(buffer);) {
            output.writeObject(gLayer);
        } catch (IOException ex) {
            //SERIALIZE ERROR
        }
    }

    public GenLayer retrieveLastGLayer() {
        try (
                InputStream file = new FileInputStream(Constants.LAST_LAYER_FILE_NAME);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);) {
            //deserialize the List
            return (GenLayer) input.readObject();

        } catch (ClassNotFoundException ex) {
            //error
        } catch (IOException ex) {
            //error
        }
        return null;
    }

    private void mapInputsToGNodes(int currLC, GenLayer gLayer, ArrayList<double[]> prevIWeights, ArrayList<String> prevINames) {

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
        String loc = ImportantFileNames.DATA_DIRNAME + File.separator + streamID + File.separator + "LC" + currLC + ".xml";
        ikaslXMLWriter.writeXML(loc, testResultMap);

    }

    private void readAndSetAlgoParameters(AlgoParamModel aPModel) {
        AlgoParameters.dType = DistanceType.EUCLIDEAN;


        AlgoParameters.SPREAD_FACTOR = aPModel.getSpreadFactor();
        AlgoParameters.START_LEARNING_RATE = aPModel.getLearningRate();
        AlgoParameters.MAX_NEIGHBORHOOD_RADIUS = aPModel.getNeighRad();
        AlgoParameters.MAX_ITERATIONS = aPModel.getIterations();

        AlgoParameters.HIT_THRESHOLD = aPModel.getHitThreshold();

        if (aPModel.getAggrType() == AggregationType.AVERAGE) {
            AlgoParameters.gType = GenType.AVG;
        } else if (aPModel.getAggrType() == AggregationType.FUZZY) {
            AlgoParameters.gType = GenType.FUZZY;
        }

        FileReader fr = new FileReader();
        ArrayList<String> minMaxList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + streamID + File.separator + Constants.BOUNDS_FILE);

        if (minMaxList != null && !minMaxList.isEmpty()) {
            String[] minStr = minMaxList.get(0).split(Constants.INPUT_TOKENIZER);
            double[] min = new double[minStr.length];

            String[] maxStr = minMaxList.get(1).split(Constants.INPUT_TOKENIZER);
            double[] max = new double[maxStr.length];

            for (int i = 0; i < minStr.length; i++) {
                min[i] = Double.parseDouble(minStr[i]);
                max[i] = Double.parseDouble(maxStr[i]);
            }

            AlgoParameters.MIN_BOUNDS = min;
            AlgoParameters.MAX_BOUNDS = max;
        } else {
            double[] min = new double[AlgoParameters.DIMENSIONS];
            double[] max = new double[AlgoParameters.DIMENSIONS];

            for (int i = 0; i < min.length; i++) {
                min[i] = DefaultValues.MIN_DEFAULT;
                max[i] = DefaultValues.MAX_DEFAULT;
            }

            AlgoParameters.MIN_BOUNDS = min;
            AlgoParameters.MAX_BOUNDS = max;
            
            defListener.useDefaultBounds(streamID);
        }

        ArrayList<String> weightsStrList = fr.readLines(ImportantFileNames.DATA_DIRNAME + File.separator + streamID + File.separator + Constants.WEIGHT_FILE);
        if (weightsStrList != null && !weightsStrList.isEmpty()) {
            String weightsStr = weightsStrList.get(0);
            String[] weightTokens = weightsStr.split(Constants.INPUT_TOKENIZER);
            double[] weights = new double[weightTokens.length];

            for (int i = 0; i < weightTokens.length; i++) {
                weights[i] = Double.parseDouble(weightTokens[i]);
            }

            AlgoParameters.ATTR_WEIGHTS = weights;
        } else {
            double[] weights = new double[AlgoParameters.DIMENSIONS];

            for (int i = 0; i < weights.length; i++) {
                weights[i] = DefaultValues.MIN_DEFAULT;
            }

            AlgoParameters.ATTR_WEIGHTS = weights;
            
            defListener.useDefaultWeights(streamID);
        }
    }
}
