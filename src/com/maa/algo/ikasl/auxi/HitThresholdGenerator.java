/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.auxi;

import com.maa.algo.ikasl.core.IKASLRun;
import com.maa.algo.objects.LNode;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Thush
 */
public class HitThresholdGenerator {
    
    public static ArrayList<String> getHitNodeIDsGeneral(LearnLayer lLayer, int hitThresh, double neighRad) {
        Map<String, Map<String, LNode>> lMap = lLayer.getCopyMap();

        ArrayList<String> selectedNodeIDs = new ArrayList<String>();

        //double threshold = hitThresh * Math.sqrt(neighRad) * Math.sqrt(AlgoParameters.MERGE_ONE_DIM_THRESHOLD*AlgoParameters.DIMENSIONS);
        double distThreshold = Utils.calcDist(Utils.getUniformVector(AlgoParameters.MERGE_ONE_DIM_THRESHOLD, AlgoParameters.DIMENSIONS),
                Utils.getZeroVector(AlgoParameters.DIMENSIONS), AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);
        double threshold = hitThresh * Math.sqrt(neighRad) * Math.pow(distThreshold, 2);

        for (Map.Entry<String, Map<String, LNode>> eMap : lMap.entrySet()) {
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
                    if (o1.getHitValue() < o2.getHitValue()) {
                        return 1;
                    } else if (o1.getHitValue() == o2.getHitValue()) {
                        return 0;
                    } else {
                        return -1;
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

            HitThresholdGenerator.ValueComparator bvc = new HitThresholdGenerator.ValueComparator(mapHitVals);
            TreeMap<String, Double> sortedHitVals = new TreeMap<String, Double>(bvc);
            sortedHitVals.clear();
            sortedHitVals.putAll(mapHitVals);

            for (Map.Entry<String, Double> e : sortedHitVals.entrySet()) {
                if (e.getValue() >= threshold) {
                    selectedNodeIDs.add(e.getKey());
                } else {
                    break;
                }
            }
        }

        return selectedNodeIDs;
    }
    
    public static ArrayList<String> getHitNodeIDsAnomalies(LearnLayer lLayer, int hitThresh, double neighRad) {
        Map<String, Map<String, LNode>> lMap = lLayer.getCopyMap();

        ArrayList<String> selectedNodeIDs = new ArrayList<String>();

        //double threshold = hitThresh * Math.sqrt(neighRad) * Math.sqrt(AlgoParameters.MERGE_ONE_DIM_THRESHOLD*AlgoParameters.DIMENSIONS);
        double distThreshold = Utils.calcDist(Utils.getUniformVector(AlgoParameters.MERGE_ONE_DIM_THRESHOLD, AlgoParameters.DIMENSIONS),
                Utils.getZeroVector(AlgoParameters.DIMENSIONS), AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);
        double threshold = Math.sqrt(neighRad) * Math.pow(distThreshold, 2)/hitThresh;

        for (Map.Entry<String, Map<String, LNode>> eMap : lMap.entrySet()) {
            Map<String, Double> mapHitVals = new HashMap<String, Double>();
            //Check whether atleast one node exceed hit Threshold 
            //if not just ignore that map
            boolean noNodeLessThanHT = true;
            for (LNode n : eMap.getValue().values()) {
                if (n.getHitValue() <= hitThresh) {
                    noNodeLessThanHT = false;
                    break;
                }
            }

            if (noNodeLessThanHT) {
                continue;
            }

            ArrayList<LNode> nodes = new ArrayList<LNode>(eMap.getValue().values());
            Iterator<LNode> iterator = nodes.iterator();
            while(iterator.hasNext()){
                LNode n = iterator.next();
                if(n.getHitValue()==0){
                    iterator.remove();
                }
            }
            
            Collections.sort(nodes, new Comparator<LNode>() {

                @Override
                public int compare(LNode o1, LNode o2) {
                    if (o1.getHitValue() < o2.getHitValue()) {
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

                //if the current node hit value is not atmost twice the hit threshold -> ignore the node
                if (n.getHitValue() > hitThresh * 2) {
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

                    double hitValue = Math.sqrt(minPDist) * Math.pow(minEDist, 2)/nodes.get(i).getHitValue();
                    mapHitVals.put(fullNodeID, hitValue);
                }

            }

            HitThresholdGenerator.ValueComparator bvc = new HitThresholdGenerator.ValueComparator(mapHitVals);
            TreeMap<String, Double> sortedHitVals = new TreeMap<String, Double>(bvc);
            sortedHitVals.clear();
            sortedHitVals.putAll(mapHitVals);

            for (Map.Entry<String, Double> e : sortedHitVals.entrySet()) {
                if (e.getValue() >= threshold) {
                    selectedNodeIDs.add(e.getKey());
                } else {
                    break;
                }
            }
        }

        return selectedNodeIDs;
    }
    
    static class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        public ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        @Override
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
