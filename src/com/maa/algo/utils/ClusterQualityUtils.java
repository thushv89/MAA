/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class ClusterQualityUtils {
    
    //RMSSTD measures the homogenity of clusters
    //large values of RMSSTD mean that clusters are not homogeneous
    public static double getRMSSTD(Map<String,String> gNodeInputs, ArrayList<double[]> iVals, ArrayList<String> iNames, int dim){
    
        Map<String,double[]> inputs = new HashMap<String, double[]>();
        
        //put all inputs to a hashmap (to make it easy to get items)
        for(int i=0;i<iNames.size();i++){
            inputs.put(iNames.get(i), iVals.get(i));
        }
        
        if(inputs.isEmpty()){
            return -1;
        }
        
        double sSum = getSqrSum(gNodeInputs, inputs,dim);
        
        //denominator of the RMSSTD calculation
        double sumInputsOfGNodes = 0;
        
        for(int i=0;i<dim;i++){
            for(Map.Entry<String,String> e : gNodeInputs.entrySet()){
                sumInputsOfGNodes += (e.getValue().split(Constants.INPUT_TOKENIZER).length-1);
            }
        }
        double RMSSTD = Math.sqrt(sSum/sumInputsOfGNodes);
        
        return RMSSTD;
    }
    
    //RS calculate the difference between clusters. The value produced is between 0 and 1
    public static double getRS(Map<String,String> gNodeInputs, ArrayList<double[]> iVals, ArrayList<String> iNames,int dim){
        
        Map<String,double[]> inputs = new HashMap<String, double[]>();
        
        //put all inputs to a hashmap (to make it easy to get items)
        for(int i=0;i<iNames.size();i++){
            inputs.put(iNames.get(i), iVals.get(i));
        }
        
        if(inputs.isEmpty()){
            return -1;
        }
        
        double SSWithin = getSqrSum(gNodeInputs, inputs,dim);
        
        double SSTotal = 0;
        for(int i=0;i<dim;i++){
            ArrayList<Double> inputsOneDim = new ArrayList<Double>();
            for(Map.Entry<String,double[]> e : inputs.entrySet()){
                inputsOneDim.add(e.getValue()[i]);
            }
            
            double sumOneDim = 0;
            for(double d: inputsOneDim){
                sumOneDim += d;
            }
            double meanOneDim = sumOneDim/inputsOneDim.size();
            
            double sSumOneDim = 0;
            for(double d: inputsOneDim){
                sSumOneDim += (d-meanOneDim)*(d-meanOneDim);
            }
            
            SSTotal +=sSumOneDim;
        }
        
        return (SSTotal - SSWithin)/SSTotal;
    }

    private static double getSqrSum(Map<String, String> gNodeInputs, Map<String, double[]> inputs, int dim) {
        //for each dimension
        double sSum = 0;
        for(int i=0;i<dim;i++){
            double sSumOneDim = 0;
            for(Map.Entry<String,String> e : gNodeInputs.entrySet()){
                
                if(e.getValue()==null || e.getValue().isEmpty()){
                    continue;
                }
                
                //inputs in the given GNode
                String[] iNamesOfGnode = e.getValue().split(Constants.INPUT_TOKENIZER);
                
                double sumVal = 0.0;
                ArrayList<Double> iValsOneDimOfGNode = new ArrayList<Double>();
                //calculate the mean value for the inputs in the GNode
                for(String s : iNamesOfGnode){
                    iValsOneDimOfGNode.add(inputs.get(s)[i]);
                    sumVal += inputs.get(s)[i];
                }
                double meanOneDimOneGNode = sumVal/iNamesOfGnode.length;
                
                double sSumOneDimOfGNode = 0.0;
                for(double d : iValsOneDimOfGNode){
                    sSumOneDimOfGNode += (d-meanOneDimOneGNode)*(d-meanOneDimOneGNode);
                }
                
                sSumOneDim += sSumOneDimOfGNode;
            }
            sSum += sSumOneDim;
        }
        return sSum;
    }
}
