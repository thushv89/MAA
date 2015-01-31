package com.maa.algo.objects;

import com.maa.algo.utils.AlgoParameters;
import java.io.Serializable;

public abstract class Node implements Serializable {

    protected double[] weights;
    protected double errorValue;
    protected int hitValue;

    public Node() {
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

    public void adjustWeights(double[] iWeights, double influence, double learningRate, int dim, 
            boolean boost, double[] gran, double[] minBound, double[] maxBound, double maxBoundThresh) {

        for (int i = 0; i < dim; i++) {
            weights[i] += influence * learningRate * (iWeights[i] - weights[i]);
            //if the max bound is less than some value, do the sigmoid boost
            if (boost && maxBound[i]-minBound[i] <= maxBoundThresh) {
                double[] range = getRangeOfWeightInput(weights[i], gran[i]);
                weights[i] = adjustWeightsWithSigmoid(gran[i], maxBound[i]*20, (range[0]+range[1])/2, range[0], weights[i]);
            }
        }
    }

    public double[] getRangeOfWeightInput(double d, double gran) {
        double minR = 0.0;
        double maxR = 0.0;
        for (int j = 0; gran * j <= 1; j++) {
            if (gran * j > d) {
                maxR = gran * j;
                break;
            }
            minR = gran * j;
        }

        return new double[]{minR, maxR};
    }

    public double adjustWeightsWithSigmoid(double L, double k, double x0, double start, double x) {
        double y = start + (L / (1 + Math.exp(-k * (x - x0))));
        return y;
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

    public void increaseHitVal() {
        hitValue++;
    }
}
