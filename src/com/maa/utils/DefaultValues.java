/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.utils;

/**
 *
 * @author Thush
 */
public class DefaultValues {
 
    public static final int MIN_DEFAULT = 0;
    public static final int MAX_DEFAULT = 1;
    public static final int WEIGHTS_DEFAULT = 1;
    public static final int IN_MEMORY_LAYER_COUNT = 500;
    public static final double ANOMALY_HIGH_THRESHOLD_DEFAULT = 0.75;
    public static final double POT_ANOMALY_HIGH_THRESHOLD_DEFAULT = 0.6;
    public static final double NORMAL_THRESHOLD_DEFAULT = 0.25;
    public static final int MAX_NORMAL_LINKS = 5;
    
    public static final double ANOMALY_LOW_THRESHOLD_DEFAULT = 0.0;
    public static final int MIN_COUNT_FOR_INTRSCT_LINKS = 5;
    
    //local intersection links related constants
    public static final double STD_INTERSECT_STRENGTH = 0.4;
    public static final double MIN_INTERSECT_STRENGTH = 0.2;
    public static final double FIND_PARENT_THRESHOLD = 0.95;
    public static final int IKASL_LINK_DEPTH = 5;
    
    //global intersection line extractor related constants
    public static final int MIN_EXT_LINK_LENGTH = 5;
    public static final int MIN_EXT_COMMON_THRESH = 5;
    
    public static final int PAST_NODES_PRE_ANOMALY = 5;
}
