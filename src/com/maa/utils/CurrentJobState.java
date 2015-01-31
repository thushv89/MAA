/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.utils;

import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Thush
 */
public class CurrentJobState {
    
    public static HashMap<String,ArrayList<String>> ANOM_GNODES_BY_TYPE;
    public static ArrayList<String> ALL_ANOM_GNODES;
    public static ArrayList<String> ALL_NORM_GNODES;
    
    public static HashMap<String,ArrayList<String>> FILT_LINKS;
    public static String CURR_JOB_ID;
    public static int CURR_LC;
    public static ArrayList<ReducedNode> ALL_NODES;
    public static ArrayList<Map<String,String>> ALL_NODE_INPUTS;
    public static ArrayList<String> ALL_TIME_FRMS;
    
    public static Map<String,ArrayList<Double>> ANOM_SUMMARY;
}
