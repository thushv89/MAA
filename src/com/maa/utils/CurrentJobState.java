/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.utils;

import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class CurrentJobState {
    
    public static ArrayList<String> ANOM_GNODES;
    public static ArrayList<String> INT_LINKS;
    public static Map<String,ArrayList<Integer>> INT_LINK_STRENGTHS;
    public static int CURR_LC;
    public static ArrayList<ReducedNode> ALL_NODES;
    public static ArrayList<Map<String,String>> ALL_NODE_INPUTS;
    public static ArrayList<String> ALL_TIME_FRMS;
}
