/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.auxi;

import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.Utils;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class InterLinkGenerator {

    ArrayList<ArrayList<String>> links;
    ArrayList<ArrayList<String>> gLayerNodes;
    ArrayList<Map<String,String>> allGLayerInputs;
    

    public ArrayList<String> getCommon(String link){
        ArrayList<String> commonIn = new ArrayList<String>();
        
        int maxLC = gLayerNodes.size();
        
        String[] allIn = link.split(Constants.NODE_TOKENIZER);
        String[] inStrInit = allIn[0].split(Constants.I_J_TOKENIZER);
        int lcInit = Integer.parseInt(inStrInit[0]);
        
        if(allGLayerInputs.get(lcInit).get(allIn[0])==null){
            return commonIn;
        }
        
        String[] inputsInit = allGLayerInputs.get(lcInit).get(allIn[0]).split(Constants.INPUT_TOKENIZER);
        commonIn.addAll(Arrays.asList(inputsInit));
        
        for (int i = 1; i < allIn.length; i++) {
            String[] inStr = allIn[i].split(Constants.I_J_TOKENIZER);            

            int lc = Integer.parseInt(inStr[0]);

            String inputs = "";
            
            for (int j = maxLC - 1; j >= lc; j--) {
                if (allGLayerInputs.get(j).containsKey(allIn[i])) {
                    inputs = allGLayerInputs.get(j).get(allIn[i]);
                    break;
                }
            }

            String[] inputTokens = inputs.split(Constants.INPUT_TOKENIZER);

            List<String> in2List = Arrays.asList(inputTokens);

            commonIn.retainAll(in2List);
        }
        return commonIn;
    }
    
    
    private ArrayList<String> testMethod(ArrayList<String> links, int nxtLvl, int startLvl, int minCount){
        
        //if we have reached end of all GLayers, return the links we have
        if(nxtLvl > gLayerNodes.size()-1){
            return links;
        }
        
        ArrayList<String> newNodes = gLayerNodes.get(nxtLvl);
        
        if(newNodes==null || newNodes.isEmpty()){
            return links;
        }
        
        Iterator<String> linksIter = links.iterator();
        ArrayList<String> tempLinks = new ArrayList<String>();
        while(linksIter.hasNext()){
            String s1 = linksIter.next();
            
            if(nxtLvl == startLvl+1 && getCommon(s1).size()<minCount){
                linksIter.remove();
            }
            
            for(String s2 : newNodes){
                String newLink = s1+Constants.NODE_TOKENIZER+s2;
                //System.out.println(newLink);
                
                int newNodeLC = Integer.parseInt(s2.split(Constants.I_J_TOKENIZER)[0]);
                if(getCommon(newLink).size()>= minCount && links.contains(s1) && newNodeLC>=nxtLvl){
                    //System.out.println("Remove "+s1);
                    linksIter.remove();
                    tempLinks.add(newLink);
                }
            }
        }
        links.addAll(tempLinks);
        
        return testMethod(links, nxtLvl+1 , startLvl, minCount);
    }
    
    public ArrayList<String> getFullLinks3(ArrayList<ArrayList<String>> gLayerNodes, int minLength, 
            int minCount, ArrayList<Map<String, String>> allGLayerInputs){
        
        this.allGLayerInputs = allGLayerInputs;
        this.gLayerNodes = gLayerNodes;
        ArrayList<String> longLinks = new ArrayList<String>();
        for(int i=0;i<=gLayerNodes.size()-minLength;i++){
             longLinks.addAll(testMethod(new ArrayList<String>(gLayerNodes.get(i)), i+1, i,minCount));
             
             Iterator<String> iterLongLinks = longLinks.iterator();
             while(iterLongLinks.hasNext()){
                 String s = iterLongLinks.next();
                 if(s.split(Constants.NODE_TOKENIZER).length<minLength){
                     iterLongLinks.remove();
                 }
                 
             }
        }
        
        return longLinks;
    }
}
