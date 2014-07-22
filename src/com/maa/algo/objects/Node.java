package com.maa.algo.objects;

import com.maa.algo.utils.AlgoParameters;
import java.io.Serializable;

public abstract class Node{


    protected double[] weights;
    protected double errorValue;
    protected int hitValue;

    public Node(){
        
    }
    protected Node(double[] weights) {
        this.weights = weights.clone();
    }


    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights.clone();
    }

    public void adjustWeights(double[] iWeights, double influence, double learningRate, int dim) {

        for (int i = 0; i < dim; i++) {
            weights[i] += influence * learningRate * (iWeights[i] - weights[i]);
        }
    }

    

    /**
     * @return the hitValue
     */
    public int getHitValue() {
        return hitValue;
    }

    /**
     * @param hitValue the hitValue to set
     */
    public void setHitValue(int hitValue) {
        this.hitValue = hitValue;
    }

    /**
     * @return the errorValue
     */
    public double getErrorValue() {
        return errorValue;
    }

    /**
     * @param errorValue the errorValue to set
     */
    public void setErrorValue(double errorValue) {
        this.errorValue = errorValue;
    }

    public void increaseHitVal(){
        hitValue++;
    }
}
