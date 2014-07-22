/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Utils;

/**
 *
 * @author Thushan Ganegedara
 */
public class LNode extends Node {

    private int x;
    private int y;

    public LNode(int x, int y, double[] weights) {
        super(weights);
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    public void calcAndUpdateErr(double[] iWeight, int dim, double[] weights, DistanceType dType) {
        this.errorValue += Utils.calcDist(this.weights, iWeight, dim, weights, dType);
    }
}
