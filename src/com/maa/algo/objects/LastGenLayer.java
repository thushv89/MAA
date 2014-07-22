/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import java.io.Serializable;

/**
 *
 * @author Thush
 */
public class LastGenLayer implements Serializable{
    
    private GenLayer gLayer;
    private int LC;

    public LastGenLayer(GenLayer gLayer, int LC){
        this.gLayer = gLayer;
        this.LC = LC;
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
}
