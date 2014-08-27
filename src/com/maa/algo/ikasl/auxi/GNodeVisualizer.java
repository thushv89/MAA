/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.auxi;

import com.maa.algo.enums.NodeHitType;
import com.maa.vis.objects.VisGNode;
import com.maa.algo.utils.Constants;
import com.maa.ui.VisualizeUIUtils;
import com.maa.utils.Tokenizers;
import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;

/**
 *
 * @author thushang
 */
public class GNodeVisualizer {

    public ArrayList<VisGNode> assignVisCoordinatesToGNodes(ArrayList<ArrayList<ReducedNode>> layers, int startLC) {

        //create an arraylist of arraylist sorted by learning cycle
        //there will be one arraylist for each learn cycle
        ArrayList<ArrayList<ReducedNode>> gNodesByLC = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++) {
            ArrayList<ReducedNode> lyr = layers.get(i);
            ArrayList<ReducedNode> singleLC = new ArrayList<>();
            for (ReducedNode rn : lyr) {
                if (rn.getId()[0] == i + startLC) {
                    singleLC.add(rn);
                }
            }
            gNodesByLC.add(singleLC);
        }

        ArrayList<VisGNode> allVisNodes = new ArrayList<>();
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
                        VisGNode parentVisNode = VisualizeUIUtils.getVisGNodeWithID(allVisNodes, lc, id);
                        if (parentVisNode != null) {
                            parentVisNode.incrementChildCount();
                        }
                    } //if node is a multi-parent node
                    else {
                        String[] pTokens = key.split(Constants.PARENT_TOKENIZER);
                        int mostLeftX = -1;
                        int mostLeftY = -1;
                        for (String p : pTokens) {
                            int lc = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[0]);
                            int id = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[1]);
                            VisGNode parentVisNode = VisualizeUIUtils.getVisGNodeWithID(allVisNodes, lc, id);
                            if (parentVisNode != null) {
                                if (mostLeftX < parentVisNode.getCoordinates()[0]) {
                                    mostLeftX = parentVisNode.getCoordinates()[0];
                                    mostLeftY = parentVisNode.getCoordinates()[1];
                                }
                            }
                        }
                        VisualizeUIUtils.getVisGNodeWithXY(allVisNodes, mostLeftX, mostLeftY).incrementChildCount();
                    }
                }

                //for all VisNodes in the layer immediately below the current (i)
                //if child count is greater than 1, adjust the positions of all the nodes to the right and below
                for (VisGNode gn : allVisNodes) {
                    if (gn.getCoordinates()[1] < i && gn.getChildCount() > 1) {
                        int offset = gn.getChildCount() - 1;
                        //change the coordinates of all the nodes below the considered layer (i)
                        for (int j = i - 1; j >= 0; j--) {
                            ArrayList<Integer> idxToRight = VisualizeUIUtils.getNodeIdxToRightWithLC(allVisNodes, j, gn);    //nodes to the right of the considered node at level j
                            //for each node to the right of considered node, update the cordinates to accomodate the new node
                            for (int idx : idxToRight) {
                                VisGNode vgn = allVisNodes.get(idx);
                                int currX = vgn.getCoordinates()[0];
                                vgn.setCoordinates(currX + offset, j);

                                //if node's parent has coordinates (currX,j) update node's parent coordinate value to the new coordinates
                                //TODO: This needs to be done for all the 'otherParentNodes' too because merged nodes have otherParentNodes
                            }
                        }
                        gn.setChildCount(0);
                    }
                }
            }

            int count = 0;
            //creating and inserting VisGNodes
            for (ReducedNode rn : gNodesByLC.get(i)) {

                //if its layer 0, insert nodes by increasing count variable
                if (i == 0) {
                    allVisNodes.add(new VisGNode(rn.getId(), count, i, null, rn.gethType()));
                    count++;
                } else {
                    String parentID = rn.getpID();

                    if (!parentID.contains(Constants.PARENT_TOKENIZER)) {
                        int lc = Integer.parseInt(parentID.split(Constants.I_J_TOKENIZER)[0]);
                        int id = Integer.parseInt(parentID.split(Constants.I_J_TOKENIZER)[1]);
                        VisGNode pVgn = VisualizeUIUtils.getVisGNodeWithID(allVisNodes, lc, id);

                        //non hit nodes from previous levels can come to upper levels
                        //this causes sometimes pVgn to be null
                        if (pVgn != null) {
                            int newX = pVgn.getCoordinates()[0];
                            while (allVisNodes.contains(new VisGNode(null, newX, i, null, null))) {
                                newX++;
                            }
                            allVisNodes.add(new VisGNode(rn.getId(), newX, i, pVgn, rn.gethType()));
                        } else {
                            int idx = VisualizeUIUtils.getRightMostIndexWithLC(allVisNodes, i) + 1;
                            allVisNodes.add(new VisGNode(rn.getId(), idx, i, null, rn.gethType()));
                        }
                    } //if node is a multi-parent node
                    else {
                        VisGNode primaryPVgn = null;
                        ArrayList<VisGNode> otherPVgn = new ArrayList<>();
                        int leftMostX = -1;
                        int leftMostY = -1;
                        String[] pTokens = parentID.split(Constants.PARENT_TOKENIZER);
                        for (String p : pTokens) {
                            int lc = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[0]);
                            int id = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[1]);
                            VisGNode tempPVgn = VisualizeUIUtils.getVisGNodeWithID(allVisNodes, lc, id);
                            if(tempPVgn.getCoordinates()[0]>leftMostX){
                                leftMostX = tempPVgn.getCoordinates()[0];
                                leftMostY = tempPVgn.getCoordinates()[1];
                            }
                        }
                        
                        for (String p : pTokens) {
                            int lc = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[0]);
                            int id = Integer.parseInt(p.split(Constants.I_J_TOKENIZER)[1]);
                            VisGNode tempPVgn = VisualizeUIUtils.getVisGNodeWithID(allVisNodes, lc, id);
                            if(tempPVgn.getCoordinates()[0]==leftMostX && tempPVgn.getCoordinates()[1]==leftMostY){
                                primaryPVgn = tempPVgn;
                            }else{
                                otherPVgn.add(tempPVgn);
                            }
                        }
                        //non hit nodes from previous levels can come to upper levels
                        //this causes sometimes pVgn to be null
                        if (primaryPVgn != null) {
                            int newX = primaryPVgn.getCoordinates()[0];
                            while (allVisNodes.contains(new VisGNode(null, newX, i, null, null))) {
                                newX++;
                            }
                            VisGNode newVgn = new VisGNode(rn.getId(), newX, i, primaryPVgn, rn.gethType());
                            newVgn.setOtherParentCoords(otherPVgn);
                            allVisNodes.add(newVgn);
                        } else {
                            int idx = VisualizeUIUtils.getRightMostIndexWithLC(allVisNodes, i) + 1;
                            allVisNodes.add(new VisGNode(rn.getId(), idx, i, null, rn.gethType()));
                        }
                    }
                }
            }
        }

        return allVisNodes;
    }
}
