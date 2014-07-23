/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.vis.objects;

/**
 *
 * @author Thush
 */
public class ReducedNode {
    
    private int[] id;
    private String pID;

    public ReducedNode(int[] id, String pID){
        this.id=id;
        this.pID=pID;
    }

    /**
     * @return the id
     */
    public int[] getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int[] id) {
        this.id = id;
    }

    /**
     * @return the pID
     */
    public String getpID() {
        return pID;
    }

    /**
     * @param pID the pID to set
     */
    public void setpID(String pID) {
        this.pID = pID;
    }
    
    
}
