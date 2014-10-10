/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import com.maa.utils.DefaultValues;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class LastGenLayer implements Serializable{
    
    private ArrayList<GenLayer> gLayers;
    private ArrayList<Map<String, String>> inputMaps;
    private int lastLC;

    public LastGenLayer(){
        this.gLayers = new ArrayList<>();
        this.inputMaps = new ArrayList<>();
    }
    
    public void addData(GenLayer gLayer, Map<String,String> inputMap, int lastLC){
        if(gLayers.size()< DefaultValues.IKASL_LINK_DEPTH && inputMaps.size()< DefaultValues.IKASL_LINK_DEPTH){
            this.gLayers.add(gLayer);
            this.inputMaps.add(inputMap);
        } else {
            this.gLayers.remove(0);
            this.inputMaps.remove(0);
            this.gLayers.add(gLayer);
            this.inputMaps.add(inputMap);
        }
        this.lastLC = lastLC;
    }
    
    /**
     * @return the gLayer
     */
    public ArrayList<GenLayer> getgLayer() {
        return gLayers;
    }

    /**
     * @return the LC
     */
    public int getLC() {
        return lastLC;
    }

    /**
     * @param LC the LC to set
     */
    public void setLC(int LC) {
        this.lastLC = LC;
    }

    /**
     * @return the inputMap
     */
    public ArrayList<Map<String, String>> getInputMap() {
        return inputMaps;
    }

    
}
