/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;

import com.maa.algo.gsom.GCoordAdjuster;
import com.maa.algo.gsom.GSOMSmoothner;
import com.maa.algo.gsom.GSOMTester;
import com.maa.algo.gsom.GSOMTrainer;
import com.maa.algo.listeners.TaskListener;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.objects.LNode;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.LogMessages;
import com.maa.algo.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLLearner {

    private ArrayList<GNode> nonHitNodes;


    public LearnLayer trainAndGetLearnLayer(int currLC, ArrayList<double[]> iWeights, ArrayList<String> iNames, GenLayer prevGLayer) {

        //if currLC ==0 there is no prevGLayer. So ignore that parameter
        if (currLC == 0) {
            GSOMTrainer gTrainer = new GSOMTrainer();
            gTrainer.trainNetwork(iNames, iWeights);
            Map<String, LNode> trainedMap = gTrainer.getCopyMap();

            GSOMSmoothner gSmoother = new GSOMSmoothner();
            gSmoother.smoothGSOM(trainedMap, iWeights);

            GCoordAdjuster gAdjuster = new GCoordAdjuster();
            Map<String, LNode> adjustedMap = gAdjuster.adjustMapCoords(trainedMap);

            GSOMTester gTester = new GSOMTester();
            gTester.testGSOM(adjustedMap, iWeights, iNames);

            LearnLayer lLayer = new LearnLayer();
            lLayer.addMap(Constants.INIT_PARENT, adjustedMap);
            return lLayer;
        } //else, prevGLayer is required
        else {

            restoreHitValues(prevGLayer);

            if (prevGLayer == null) {
                //error no GLayer
            }

            Map<String, ArrayList<double[]>> gNodeIWeights = new HashMap<String, ArrayList<double[]>>();
            Map<String, ArrayList<String>> gNodeINames = new HashMap<String, ArrayList<String>>();
            assignInputsToGNodes(prevGLayer, iWeights, iNames, gNodeIWeights, gNodeINames);

            //need to update nodes before Training GSOM 
            //becuase prevGLayer gets modified when GSOMTrain is called
            updateNonHitList(prevGLayer.getMap());

            LearnLayer lLayer = new LearnLayer();

            for (Map.Entry<String, GNode> n : prevGLayer.getMap().entrySet()) {
                if (!nonHitNodes.contains(n.getValue())) {
                    GSOMTrainer gTrainer = new GSOMTrainer();
                    gTrainer.trainNetwork(gNodeINames.get(n.getKey()), gNodeIWeights.get(n.getKey()));
                    Map<String, LNode> trainedMap = gTrainer.getCopyMap();

                    GSOMSmoothner gSmoother = new GSOMSmoothner();
                    gSmoother.smoothGSOM(trainedMap, gNodeIWeights.get(n.getKey()));

                    GCoordAdjuster gAdjuster = new GCoordAdjuster();
                    Map<String, LNode> adjustedMap = gAdjuster.adjustMapCoords(trainedMap);

                    GSOMTester gTester = new GSOMTester();
                    gTester.testGSOM(adjustedMap, gNodeIWeights.get(n.getKey()), gNodeINames.get(n.getKey()));

                    lLayer.addMap(n.getKey(), adjustedMap);
                }
            }

            return lLayer;

        }
    }

    private void restoreHitValues(GenLayer gLayer) {
        for (Map.Entry<String, GNode> e : gLayer.getMap().entrySet()) {
            e.getValue().setHitValue(0);
        }
    }

    private void assignInputsToGNodes(GenLayer prevGLayer, ArrayList<double[]> iWeights, ArrayList<String> iNames,
            Map<String, ArrayList<double[]>> gNodeIWeights, Map<String, ArrayList<String>> gNodeINames) {

        Map<String, GNode> prevGNodes = prevGLayer.getMap();

        for (String s : prevGNodes.keySet()) {
            gNodeIWeights.put(s, new ArrayList<double[]>());
            gNodeINames.put(s, new ArrayList<String>());
        }

        for (int i = 0; i < iWeights.size(); i++) {
            double minDist = Double.MAX_VALUE;
            String gID = "";
            for (Map.Entry<String, GNode> n : prevGNodes.entrySet()) {
                double distance = Utils.calcDist(iWeights.get(i), n.getValue().getWeights(), 
                        AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);
                if (distance < minDist) {
                    minDist = distance;
                    gID = n.getKey();
                }
            }

            //get relevant entry from gNodeIweights and gNodeInames, update the entry and put it back to map
            ArrayList<double[]> weightArrForInput = gNodeIWeights.get(gID);
            weightArrForInput.add(iWeights.get(i));
            gNodeIWeights.put(gID, weightArrForInput);

            ArrayList<String> nameArrForInput = gNodeINames.get(gID);
            nameArrForInput.add(iNames.get(i));
            gNodeINames.put(gID, nameArrForInput);

            //update hit value
            GNode node = prevGNodes.get(gID);
            node.increaseHitVal();
            prevGNodes.put(gID, node);

        }
    }

    //previously this method was implemented to keep all the non-hit nodes recorded
    //in 'nonHitNodes' list. But this is incorrect because at the end we're adding non hit nodes 
    //to GLayer. Therefore, according to this implementation, if a node becomes a non hit node
    //once, it'll remain a non-hit node for the rest of learning cycles
    public void updateNonHitList(Map<String, GNode> map) {

        nonHitNodes = new ArrayList<GNode>();

        for (Map.Entry<String, GNode> entry : map.entrySet()) {
            if (entry.getValue().getHitValue() == 0) {
                nonHitNodes.add(entry.getValue());
            }
        }
    }

    public ArrayList<GNode> getNonHitNodes(int currLC) {
        return nonHitNodes;
    }
}
