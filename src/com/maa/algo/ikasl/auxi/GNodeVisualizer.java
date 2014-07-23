/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.auxi;

import com.maa.algo.enums.VisGNodeType;
import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.vis.objects.VisGNode;
import com.maa.algo.utils.Constants;
import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;

/**
 *
 * @author thushang
 */
public class GNodeVisualizer {

    public ArrayList<VisGNode> assignVisCoordinatesToGNodes(ArrayList<ArrayList<ReducedNode>> layers) {

        ArrayList<ArrayList<ReducedNode>> gNodesByLC = new ArrayList<ArrayList<ReducedNode>>();
        for (int i = 0; i < layers.size(); i++) {
            ArrayList<ReducedNode> lyr = layers.get(i);
            ArrayList<ReducedNode> singleLC = new ArrayList<ReducedNode>();
            for (ReducedNode rn : lyr) {
                if (rn.getId()[0] == i) {
                    singleLC.add(rn);
                } else {
                    ArrayList<ReducedNode> layerForLC = gNodesByLC.get(rn.getId()[0]);
                    layerForLC.add(rn);
                }
            }
            gNodesByLC.add(singleLC);
        }

        ArrayList<VisGNode> allVisNodes = new ArrayList<VisGNode>();
        for (int i = 0; i < gNodesByLC.size(); i++) {
            

            //for each layer (!= 0) get the parent layer and increase child count
            //child count is important to adjust node positions
            if (i > 0) {
                for (ReducedNode rn : gNodesByLC.get(i)) {

                    String key = rn.getpID();

                    //if nodes is not a multi-parent node
                    if (!key.contains(Constants.PARENT_TOKENIZER)) {
                        int lc = Integer.parseInt(key.split(Constants.I_J_TOKENIZER)[0]);
                        int id = Integer.parseInt(key.split(Constants.I_J_TOKENIZER)[1]);
                        VisGNode parentVisNode = getVisGNodeWithID(allVisNodes, lc, id);
                        if (parentVisNode != null) {
                            parentVisNode.incrementChildCount();
                        }

                    } 
                    //if node is a multi-parent node
                    else {
                        String[] allParents = key.split(Constants.PARENT_TOKENIZER);
                        //increase the childcount variable of all the parents
                        for (String p : allParents) {
                            int lc = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[0]);
                            int id = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[1]);
                            VisGNode parentVisNode = getVisGNodeWithID(allVisNodes, lc, id);
                            if (parentVisNode != null) {
                                parentVisNode.incrementChildCount();
                            }
                        }
                    }
                }

                //for all VisNodes in the layer immediately below the current (i)
                //if child count is greater than 1, adjust the positions of all the nodes to the right and below
                for (VisGNode gn : allVisNodes) {
                    if (gn.getCoordinates()[1] == i - 1 && gn.getChildCount() > 1) {
                        int offset = gn.getChildCount() - 1;
                        //change the coordinates of all the nodes below the considered layer (i)
                        for (int j = i - 1; j >= 0; j--) {
                            ArrayList<Integer> idxToRight = getNodeIdxToRightWithLC(allVisNodes, j, gn);    //nodes to the right of the considered node at level j
                            //for each node to the right of considered node, update the cordinates to accomodate the new node
                            for (int idx : idxToRight) {
                                VisGNode vgn = allVisNodes.get(idx);
                                int currX = vgn.getCoordinates()[0];
                                vgn.setCoordinates(currX + offset, j);
                                
                                //if node's parent has coordinates (currX,j) update node's parent coordinate value to the new coordinates
                                //TODO: This needs to be done for all the 'otherParentNodes' too because merged nodes have otherParentNodes
                                
                            }
                        }
                    }
                }
            }

            int count = 0;
            //creating and inserting VisGNodes
            for (ReducedNode rn : gNodesByLC.get(i)) {
                
                //if its layer 0, insert nodes by increasing count variable
                if (i == 0) {
                    allVisNodes.add(new VisGNode(rn.getId(), count, i, null, VisGNodeType.HIT));
                    count++;
                } else {
                    String parentID = rn.getpID();

                    if (!parentID.contains(Constants.PARENT_TOKENIZER)) {
                        int lc = Integer.parseInt(parentID.split(Constants.I_J_TOKENIZER)[0]);
                        int id = Integer.parseInt(parentID.split(Constants.I_J_TOKENIZER)[1]);
                        VisGNode pVgn = getVisGNodeWithID(allVisNodes, lc, id);

                        //non hit nodes from previous levels can come to upper levels
                        //this causes sometimes pVgn to be null
                        if (pVgn != null) {
                            int newX = pVgn.getCoordinates()[0];
                            while (allVisNodes.contains(new VisGNode(null, newX, i, null, VisGNodeType.HIT))) {
                                newX++;
                            }
                            allVisNodes.add(new VisGNode(rn.getId(), newX, i, pVgn, VisGNodeType.HIT));
                        }
                    } else {
                        String[] allParentIDs = parentID.split(Constants.PARENT_TOKENIZER);
                        ArrayList<VisGNode> allParents = new ArrayList<VisGNode>();

                        for (String p : allParentIDs) {
                            int lc = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[0]);
                            int id = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[1]);

                            allParents.add(getVisGNodeWithID(allVisNodes, lc, id));
                        }

                        VisGNode pVgn = allParents.get(0);
                        //non hit nodes from previous levels can come to upper levels
                        //this causes sometimes pVgn to be null
                        if (pVgn != null) {
                            int newX = pVgn.getCoordinates()[0];
                            while (allVisNodes.contains(new VisGNode(null, newX, i, null, VisGNodeType.HIT))) {
                                newX++;
                            }
                            
                            VisGNode newVisNode = new VisGNode(rn.getId(), newX, i, pVgn, VisGNodeType.HIT);
                            ArrayList<int[]> allParentCoords = new ArrayList<int[]>();
                            for(VisGNode n : allParents){
                                allParentCoords.add(n.getCoordinates());
                            }
                            newVisNode.setOtherParentCoords(allParentCoords);
                            
                            allVisNodes.add(newVisNode);
                        }
                    }
                }
            }

            /*
            //adding non-hit nodes
            for (GNode gn : layers.get(i).getCopyMap().values()) {
                if (gn.getLc() != i) {
                    VisGNode sameNode = getVisGNodeWithID(allVisNodes, gn.getLc(), gn.getId());
                    allVisNodes.add(new VisGNode(gn, sameNode.getCoordinates()[0], i,
                            sameNode.getCoordinates()[0], sameNode.getCoordinates()[1], VisGNodeType.NON_HIT));
                }
            }*/
        }

        return allVisNodes;
    }

    private VisGNode getVisGNodeWithID(ArrayList<VisGNode> list, int lc, int id) {
        for (VisGNode vgn : list) {
            if (vgn.getID()[0] == lc && vgn.getID()[1] == id) {
                return vgn;
            }
        }
        return null;
    }

    private ArrayList<Integer> getNodeIdxToRightWithLC(ArrayList<VisGNode> list, int lc, VisGNode node) {
        ArrayList<Integer> rightNodes = new ArrayList<Integer>();
        int[] coords = node.getCoordinates();
        for (VisGNode n : list) {
            int x = n.getCoordinates()[0];
            int y = n.getCoordinates()[1];
            if (y == lc && x > coords[0]) {
                rightNodes.add(list.indexOf(n));
            }
        }

        return rightNodes;
    }
}
