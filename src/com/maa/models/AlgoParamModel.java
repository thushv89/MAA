/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.models;

import com.maa.enums.AggregationType;

/**
 *
 * @author Thush
 */
public class AlgoParamModel extends ParamModel{
    
    private String streamID;
    private double spreadFactor;
    private int neighRad;
    private double learningRate;
    private int iterations;
    private int hitThreshold;
    private AggregationType aggrType;
    private int dimensions;
    private int currLC;
    
    public AlgoParamModel(String streamID, double spreadFactor, int neighRad, double learningRate, int iterations, int hitThreshold,AggregationType aggrType){
        this.streamID = streamID;
        this.spreadFactor=spreadFactor;
        this.neighRad=neighRad;
        this.learningRate=learningRate;
        this.iterations=iterations;
        this.aggrType=aggrType;
        this.hitThreshold=hitThreshold;
    }
    
    public AlgoParamModel(String streamID, double spreadFactor, int neighRad, double learningRate, int iterations, int hitThreshold,AggregationType aggrType, int dimensions){
        this.streamID = streamID;
        this.spreadFactor=spreadFactor;
        this.neighRad=neighRad;
        this.learningRate=learningRate;
        this.iterations=iterations;
        this.aggrType=aggrType;
        this.hitThreshold=hitThreshold;
        this.dimensions = dimensions;
    }

    /**
     * @return the spreadFactor
     */
    public double getSpreadFactor() {
        return spreadFactor;
    }

    /**
     * @param spreadFactor the spreadFactor to set
     */
    public void setSpreadFactor(double spreadFactor) {
        this.spreadFactor = spreadFactor;
    }

    /**
     * @return the neighRad
     */
    public int getNeighRad() {
        return neighRad;
    }

    /**
     * @param neighRad the neighRad to set
     */
    public void setNeighRad(int neighRad) {
        this.neighRad = neighRad;
    }

    /**
     * @return the learningRate
     */
    public double getLearningRate() {
        return learningRate;
    }

    /**
     * @param learningRate the learningRate to set
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * @return the iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * @param iterations the iterations to set
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * @return the aggrType
     */
    public AggregationType getAggrType() {
        return aggrType;
    }

    /**
     * @param aggrType the aggrType to set
     */
    public void setAggrType(AggregationType aggrType) {
        this.aggrType = aggrType;
    }

    /**
     * @return the hitThreshold
     */
    public int getHitThreshold() {
        return hitThreshold;
    }

    /**
     * @param hitThreshold the hitThreshold to set
     */
    public void setHitThreshold(int hitThreshold) {
        this.hitThreshold = hitThreshold;
    }

    /**
     * @return the currLC
     */
    public int getCurrLC() {
        return currLC;
    }

    /**
     * @param currLC the currLC to set
     */
    public void setCurrLC(int currLC) {
        this.currLC = currLC;
    }

    /**
     * @return the dimensions
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * @param dimensions the dimensions to set
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * @return the streamID
     */
    public String getStreamID() {
        return streamID;
    }

    /**
     * @param streamID the streamID to set
     */
    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }
    
    
}
