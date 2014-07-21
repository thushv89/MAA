/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.models;

import com.maa.enums.ExecFrequency;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class BasicParamModel extends ParamModel{
    
    private ExecFrequency freq;
    private int numStreams;
    private ArrayList<String> streamIDs;
    
    public BasicParamModel(ExecFrequency freq, int numStreams,ArrayList<String> streamIDs){
        this.freq=freq;
        this.numStreams=numStreams;
        this.streamIDs=streamIDs;
    }

    /**
     * @return the freq
     */
    public ExecFrequency getFreq() {
        return freq;
    }

    /**
     * @param freq the freq to set
     */
    public void setFreq(ExecFrequency freq) {
        this.freq = freq;
    }

    /**
     * @return the numStreams
     */
    public int getNumStreams() {
        return numStreams;
    }

    /**
     * @param numStreams the numStreams to set
     */
    public void setNumStreams(int numStreams) {
        this.numStreams = numStreams;
    }

    /**
     * @return the streamIDs
     */
    public ArrayList<String> getStreamIDs() {
        return streamIDs;
    }

    /**
     * @param streamIDs the streamIDs to set
     */
    public void setStreamIDs(ArrayList<String> streamIDs) {
        this.streamIDs = streamIDs;
    }
    
    
}
