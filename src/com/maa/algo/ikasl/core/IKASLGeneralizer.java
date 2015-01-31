/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;

import com.maa.algo.objects.Node;
import com.maa.algo.objects.LNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.LearnLayer;
import com.maa.algo.enums.GenType;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.AlgoUtils;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class IKASLGeneralizer {

    private AlgoParameters algoParam;
    public IKASLGeneralizer(AlgoParameters algoParam){
        this.algoParam = algoParam;
    }
    
    public GenLayer generalize(int currLC, LearnLayer lLayer, ArrayList<String> bestHits, GenType gType) {

        GenLayer gLayer = new GenLayer();

        int gID = 0;
        IKASLGenType genType = null;
        if (gType == GenType.AVG) {
            genType = new AvgGenType();
        } else if (gType == GenType.FUZZY) {
            genType = new FuzzyGenType();
        } else if (gType == GenType.NONE) {
            genType = new NoneGenType();
        }

        for (String hit : bestHits) {
            String parentID;
            String loc;

            //tokenize string 'hit' and get parent ID and Map x,y of hit
            String[] hTokens = hit.split(Constants.NODE_TOKENIZER);
            parentID = hTokens[0];
            loc = hTokens[1];

            Map<String, LNode> mapWithHit = lLayer.getSingleMapCopyWithParent(parentID);
            LNode hitnode = mapWithHit.get(loc);

            ArrayList<String> neigh1 = get1stLvlNeighbors(mapWithHit, loc);
            ArrayList<String> neigh2 = get2ndLvlNeighbors(mapWithHit, loc);

            ArrayList<Node> neigh1Nodes = new ArrayList<Node>();
            ArrayList<Node> neigh2Nodes = new ArrayList<Node>();

            for (String s1 : neigh1) {
                neigh1Nodes.add(mapWithHit.get(s1));
            }
            for (String s2 : neigh2) {
                neigh2Nodes.add(mapWithHit.get(s2));
            }

            if (genType != null) {
                double[] gWeight = genType.generalize(hitnode, neigh1Nodes, neigh2Nodes,algoParam);

                if (currLC == 0) {
                    GNode node = new GNode(currLC, gID, gWeight, Constants.INIT_PARENT);
                    gLayer.addNode(node);
                    gID++;
                } else {
                    boolean willMerge = false;
                    GNode mergeNode = null;
                    boolean haveLargeDim = false;

                    //Check for potential mergeable nodes
                    for (Map.Entry<String, GNode> e1 : gLayer.getMap().entrySet()) {
                        
                        if (!parentID.equals(e1.getValue().getParentID()) &&
                                AlgoUtils.calcDist(gWeight, e1.getValue().getWeights(), algoParam.getDIMENSIONS(),algoParam.getATTR_WEIGHTS(),algoParam.getDistType()) < algoParam.getMergeThreshold()) {
                            
                            //Find whether any of the dimension difference between gweight and current GNode in map
                            //is greater than some threshold
                            //REMEMBER: Multiply one_dim_threshold by corresponding attribute weight
                            for (int i = 0; i < algoParam.getDIMENSIONS(); i++) {
                                if (Math.abs(gWeight[i] - e1.getValue().getWeights()[i]) > algoParam.getMERGE_ONE_DIM_THRESHOLD()*algoParam.getATTR_WEIGHTS()[i]) {
                                    haveLargeDim = true;
                                    break;
                                }
                            }

                            //if atleast one dim is large, do not merge
                            if (!haveLargeDim) {
                                mergeNode = e1.getValue();
                                //if parent of both nodes are the same, do not merge
                                if(parentID.equals(mergeNode.getParentID())){
                                    willMerge = true;
                                    break;
                                }else{
                                    willMerge = false;
                                    mergeNode = null;
                                }
                                
                            }
                        }
                    }

                    //We increase the gID only if creating a new node. Therefore, if a node is merged
                    //with an existing node, we give that node the same id as the merged node
                    if (!willMerge) {
                        GNode node = new GNode(currLC, gID, gWeight, Constants.INIT_PARENT);
                        gLayer.addNode(node);
                        gID++;
                    } else {
                        GNode node = null;
                        double[] newWeight = mergeWeights(gWeight, mergeNode.getWeights(), new double[]{0.5, 0.5});

                        String newParentID = parentID + Constants.PARENT_TOKENIZER + mergeNode.getParentID();
                        node = new GNode(mergeNode.getLc(), mergeNode.getId(), newWeight, Constants.INIT_PARENT);
                        
                        gLayer.addNode(node);
                        
                        System.out.println("Nodes merged: "+ mergeNode.getLc()+","+mergeNode.getId() + " with hitNode");
                        System.out.println("New parent: "+newParentID);
                    }
                }

            } else {
                //no gen type
            }
        }

        return gLayer;
    }

    private double[] mergeWeights(double[] in1, double[] in2, double[] weights) {
        double[] newWeight = new double[algoParam.getDIMENSIONS()];
        for (int i = 0; i < algoParam.getDIMENSIONS(); i++) {
            newWeight[i] = ((weights[0] * in1[i]) + (weights[1] * in2[i])) / (weights[0] + weights[1]);
        }
        return newWeight;
    }

    //return all the neighbors 1step away from the primnode
    private ArrayList<String> get1stLvlNeighbors(Map<String, LNode> map, String loc) {

        ArrayList<String> neigh1 = new ArrayList<String>();
        int X = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[0]);
        int Y = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[1]);

        String lStr = AlgoUtils.generateIndexString(X - 1, Y);
        String rStr = AlgoUtils.generateIndexString(X + 1, Y);
        String bStr = AlgoUtils.generateIndexString(X, Y - 1);
        String tStr = AlgoUtils.generateIndexString(X, Y + 1);
        String lbStr = AlgoUtils.generateIndexString(X - 1, Y - 1);
        String rbStr = AlgoUtils.generateIndexString(X + 1, Y - 1);
        String rtStr = AlgoUtils.generateIndexString(X + 1, Y + 1);
        String ltStr = AlgoUtils.generateIndexString(X - 1, Y + 1);
        String[] neighbors = {lStr, rStr, bStr, tStr, lbStr, rbStr, rtStr, ltStr};

        for (String neighbor : neighbors) {
            if (map.containsKey(neighbor)) {
                neigh1.add(neighbor);
            }
        }
        return neigh1;
    }

    //return all the neighbors 2 steps away from the primNode
    private ArrayList<String> get2ndLvlNeighbors(Map<String, LNode> map, String loc) {

        int X = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[0]);
        int Y = Integer.parseInt(loc.split(Constants.I_J_TOKENIZER)[1]);

        String llbbStr = X - 2 + "," + (Y - 2);
        String lbbStr = X - 1 + "," + (Y - 2);
        String bbStr = X + "," + (Y - 2);
        String rbbStr = X + 1 + "," + (Y - 2);
        String rrbbStr = X + 2 + "," + (Y - 2);
        String llbStr = X - 2 + "," + (Y - 1);
        String rrbStr = X + 2 + "," + (Y - 1);
        String rrStr = X + 2 + "," + Y;
        String llStr = X - 2 + "," + Y;
        String lltStr = X - 2 + "," + (Y + 1);
        String rrtStr = X + 2 + "," + (Y + 1);
        String llttStr = X - 2 + "," + (Y + 2);
        String lttStr = X - 1 + "," + (Y + 2);
        String ttStr = X + "," + (Y + 2);
        String rttStr = X + 1 + "," + (Y + 2);
        String rrttStr = X + 2 + "," + (Y + 2);

        String[] neighbors2Str = {llbbStr, lbbStr, bbStr, rbbStr, rrbbStr, llbStr, rrbStr, rrStr, llStr, lltStr, rrtStr, llttStr, lttStr, ttStr, rttStr, rrttStr};

        ArrayList<String> neigh2 = new ArrayList<String>();
        for (String neigh2Str : neighbors2Str) {
            if (map.containsKey(neigh2Str)) {
                neigh2.add(neigh2Str);
            }
        }

        return neigh2;
    }
}
