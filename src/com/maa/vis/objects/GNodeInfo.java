/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.vis.objects;

import com.maa.utils.Tokenizers;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class GNodeInfo {
    private String nodeID;
    private String parentID;
    private String nodeCoords;
    private String timeFrame;
    private String synopsis;
    private String inputs;
    private int inputPercentage;

    /**
     * @return the nodeID
     */
    public String getNodeID() {
        return nodeID;
    }

    /**
     * @param nodeID the nodeID to set
     */
    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    /**
     * @return the nodeCoords
     */
    public String getNodeCoords() {
        return nodeCoords;
    }

    /**
     * @param nodeCoords the nodeCoords to set
     */
    public void setNodeCoords(int[] coords) {
        this.nodeCoords = coords[0]+ Tokenizers.I_J_TOKENIZER+coords[1];
    }

    /**
     * @return the timeFrame
     */
    public String getTimeFrame() {
        return timeFrame;
    }

    /**
     * @param timeFrame the timeFrame to set
     */
    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    /**
     * @return the synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * @param synopsis the synopsis to set
     */
    public void setSynopsis(ArrayList<String> dims, double[] weights) {
        DecimalFormat df = new DecimalFormat("#.##");
        this.synopsis = "<html>" + dims.get(0) + ": " +df.format(weights[0]);
        for(int i=1;i<dims.size();i++){
            this.synopsis += "  " + dims.get(i) +": "+ df.format(weights[i]);
            if(i%3==0){
                this.synopsis += "<br/>";
            }
        }
        this.synopsis += "</html>";
    }

    /**
     * @return the inputs
     */
    public String getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(ArrayList<String> inputs) {        
        this.inputs = "<html>"+inputs.get(0);
        for(int i=1; i<inputs.size();i++){
            this.inputs += Tokenizers.INPUT_TOKENIZER + " " + inputs.get(i);
            if(i%10==0){
                this.inputs += "<br/>";
            }
        }
        this.inputs += "<html/>";
        
    }

    /**
     * @return the inputPercentage
     */
    public int getInputPercentage() {
        return inputPercentage;
    }

    /**
     * @param inputPercentage the inputPercentage to set
     */
    public void setInputPercentage(int inputPercentage) {
        this.inputPercentage = inputPercentage;
    }

    /**
     * @return the parentID
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * @param parentID the parentID to set
     */
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
    
    
}
