/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maa.vis.objects;

import com.maa.algo.enums.NodeHitType;
import com.maa.algo.objects.GNode;
import java.util.ArrayList;

/**
 *
 * @author thushang
 */
public class VisGNode {
    private int[] id;
    private int[] coordinates;
    private VisGNode pVgn;
    private ArrayList<int[]> otherParentCoords;
    private int childCount;
    private NodeHitType vgnType;
    
    public VisGNode(int[] id, int x, int y, VisGNode pVgn, NodeHitType vgnType){
        this.id = id;
        this.coordinates = new int[]{x,y};
        this.pVgn = pVgn;
        this.vgnType=vgnType;
    }
    
    public void setCoordinates(int x, int y){
        this.coordinates = new int[]{x,y};
    }
    
    public int[] getCoordinates(){
        return coordinates;
    }
    
    public void setParent(VisGNode pVgn){
        this.pVgn = pVgn;
    }
    
    public VisGNode getParent(){
        return this.pVgn;
    }
    
    public int[] getID(){
        return id;
    }

    /**
     * @return the childCount
     */
    public int getChildCount() {
        return childCount;
    }

    /**
     * @param childCount the childCount to set
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
    
    public void incrementChildCount(){
        this.childCount++;
    }
    
    
    @Override
    public boolean equals(Object o1){
        if(o1 instanceof VisGNode){
            VisGNode node = (VisGNode) o1;
            if(this.coordinates[0]==node.getCoordinates()[0] &&
                    this.coordinates[1]==node.getCoordinates()[1]){
                return true;
            }
        }
        return false;
    }

    /**
     * @return the vgnType
     */
    public NodeHitType getVgnType() {
        return vgnType;
    }

    /**
     * @param vgnType the vgnType to set
     */
    public void setVgnType(NodeHitType vgnType) {
        this.vgnType = vgnType;
    }

    /**
     * @return the otherParentCoords
     */
    public ArrayList<int[]> getOtherParentCoords() {
        return otherParentCoords;
    }

    /**
     * @param otherParentCoords the otherParentCoords to set
     */
    public void setOtherParentCoords(ArrayList<int[]> otherParentCoords) {
        this.otherParentCoords = otherParentCoords;
    }
    
}
