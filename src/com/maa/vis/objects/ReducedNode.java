/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.vis.objects;

import com.maa.algo.enums.NodeHitType;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class ReducedNode {

    private int[] id;
    private String pID;
    private double[] weights;
    private ArrayList<String> inputs;
    private NodeHitType hType;

    public ReducedNode(int[] id, String pID, double[] weights, ArrayList<String> inputs) {
        this.id = id;
        this.pID = pID;
        this.weights = weights;
        this.inputs = inputs;
        setNodeHitType();
    }

    private void setNodeHitType() {
        if (inputs != null && !inputs.isEmpty()) {
            this.hType = NodeHitType.HIT;
        } else {
            this.hType = NodeHitType.NON_HIT;
        }
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

    /**
     * @return the weights
     */
    public double[] getWeights() {
        return weights;
    }

    /**
     * @param weights the weights to set
     */
    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    /**
     * @return the inputs
     */
    public ArrayList<String> getInputs() {
        return inputs;
    }

    /**
     * @return the hType
     */
    public NodeHitType gethType() {
        return hType;
    }
}
