/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class LastGenLayer implements Serializable{
    
    private GenLayer gLayer;
    private Map<String, String> inputMap;
    private int LC;

    public LastGenLayer(GenLayer gLayer, int LC, Map<String, String> inputMap){
        this.gLayer = gLayer;
        this.LC = LC;
        this.inputMap = inputMap;
    }
    /**
     * @return the gLayer
     */
    public GenLayer getgLayer() {
        return gLayer;
    }

    /**
     * @param gLayer the gLayer to set
     */
    public void setgLayer(GenLayer gLayer) {
        this.gLayer = gLayer;
    }

    /**
     * @return the LC
     */
    public int getLC() {
        return LC;
    }

    /**
     * @param LC the LC to set
     */
    public void setLC(int LC) {
        this.LC = LC;
    }

    /**
     * @return the inputMap
     */
    public Map<String, String> getInputMap() {
        return inputMap;
    }

    /**
     * @param inputMap the inputMap to set
     */
    public void setInputMap(Map<String, String> inputMap) {
        this.inputMap = inputMap;
    }
    
    
}
