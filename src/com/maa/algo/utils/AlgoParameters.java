package com.maa.algo.utils;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.enums.GenType;
import com.maa.algo.enums.MiningType;

public class AlgoParameters {

    private int DIMENSIONS;
    private double GT;
    private double SPREAD_FACTOR;
    private double MAX_NEIGHBORHOOD_RADIUS;
    private double FD;
    private double START_LEARNING_RATE;
    private int MAX_ITERATIONS;
    private DistanceType dType;
    
    private GenType gType;
    private int HIT_THRESHOLD;

    private double MERGE_THRESHOLD;
    
    private double[] MIN_BOUNDS;
    private double[] MAX_BOUNDS;
    private double[] RANGE_GRANULARITY;
    private double THRESH_MAX_BOUND = 5;
    
    private double[] ATTR_WEIGHTS;
    
    private double[] ANOMALY_THRESHOLDS;
    
    private double MERGE_ONE_DIM_THRESHOLD=0.4;
    private double MERGE_ALL_DIM_THRESHOLD = 0.15;
    
    private MiningType MINING_TYPE;
    
    public AlgoParameters(int dimensions, double sf, int nr, double lr, int iter, int ht, 
            double[] minBounds, double[] maxBounds, double[] weights, GenType gType, MiningType mType,DistanceType dType){
        this.SPREAD_FACTOR = sf;
        this.DIMENSIONS = dimensions;
        this.MAX_NEIGHBORHOOD_RADIUS = nr;
        this.START_LEARNING_RATE = lr;
        this.MAX_ITERATIONS = iter;
        this.HIT_THRESHOLD = ht;
        this.MIN_BOUNDS = minBounds;
        this.MAX_BOUNDS = maxBounds;
        this.ATTR_WEIGHTS = weights;
        this.MINING_TYPE = mType;
        this.dType = dType;
        this.gType = gType;
    }
    
    public double getMergeThreshold(){
        MERGE_THRESHOLD = AlgoUtils.calcDist(AlgoUtils.getUniformVector(MERGE_ALL_DIM_THRESHOLD, getDIMENSIONS()), AlgoUtils.getZeroVector(getDIMENSIONS()), getDIMENSIONS(), this.getATTR_WEIGHTS(), getDistType());
        return getMERGE_THRESHOLD();
    }
    
    public double getGT() {
        GT = -getDIMENSIONS() * getDIMENSIONS() * Math.log(getSPREAD_FACTOR());
        return GT;
    }

    public double getFD() {
        FD = getSPREAD_FACTOR() / getDIMENSIONS();
        return FD;
    }

    /**
     * @return the DIMENSIONS
     */
    public int getDIMENSIONS() {
        return DIMENSIONS;
    }

    /**
     * @return the SPREAD_FACTOR
     */
    public double getSPREAD_FACTOR() {
        return SPREAD_FACTOR;
    }

    /**
     * @return the MAX_NEIGHBORHOOD_RADIUS
     */
    public double getMAX_NEIGHBORHOOD_RADIUS() {
        return MAX_NEIGHBORHOOD_RADIUS;
    }

    /**
     * @return the START_LEARNING_RATE
     */
    public double getSTART_LEARNING_RATE() {
        return START_LEARNING_RATE;
    }

    /**
     * @return the MAX_ITERATIONS
     */
    public int getMAX_ITERATIONS() {
        return MAX_ITERATIONS;
    }

    /**
     * @return the HIT_THRESHOLD
     */
    public int getHIT_THRESHOLD() {
        return HIT_THRESHOLD;
    }

    /**
     * @return the MERGE_THRESHOLD
     */
    public double getMERGE_THRESHOLD() {
        return MERGE_THRESHOLD;
    }

    /**
     * @return the MIN_BOUNDS
     */
    public double[] getMIN_BOUNDS() {
        return MIN_BOUNDS;
    }

    /**
     * @return the MAX_BOUNDS
     */
    public double[] getMAX_BOUNDS() {
        return MAX_BOUNDS;
    }

    /**
     * @return the ATTR_WEIGHTS
     */
    public double[] getATTR_WEIGHTS() {
        return ATTR_WEIGHTS;
    }

    /**
     * @return the MERGE_ONE_DIM_THRESHOLD
     */
    public double getMERGE_ONE_DIM_THRESHOLD() {
        return MERGE_ONE_DIM_THRESHOLD;
    }

    /**
     * @return the MINING_TYPE
     */
    public MiningType getMINING_TYPE() {
        return MINING_TYPE;
    }

    /**
     * @return the gType
     */
    public GenType getGType() {
        return gType;
    }

    /**
     * @return the dType
     */
    public DistanceType getDistType() {
        return dType;
    }

    /**
     * @param MAX_NEIGHBORHOOD_RADIUS the MAX_NEIGHBORHOOD_RADIUS to set
     */
    public void setMAX_NEIGHBORHOOD_RADIUS(double MAX_NEIGHBORHOOD_RADIUS) {
        this.MAX_NEIGHBORHOOD_RADIUS = MAX_NEIGHBORHOOD_RADIUS;
    }

    /**
     * @param START_LEARNING_RATE the START_LEARNING_RATE to set
     */
    public void setSTART_LEARNING_RATE(double START_LEARNING_RATE) {
        this.START_LEARNING_RATE = START_LEARNING_RATE;
    }

    /**
     * @return the RANGE_GRANULARITY
     */
    public double[] getRANGE_GRANULARITY() {
        return RANGE_GRANULARITY;
    }

    /**
     * @param RANGE_GRANULARITY the RANGE_GRANULARITY to set
     */
    public void setRANGE_GRANULARITY(double[] RANGE_GRANULARITY) {
        this.RANGE_GRANULARITY = RANGE_GRANULARITY;
    }

    /**
     * @return the THRESH_MAX_BOUND
     */
    public double getTHRESH_MAX_BOUND() {
        return THRESH_MAX_BOUND;
    }
    
    
}

