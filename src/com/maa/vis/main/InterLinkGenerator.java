/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.vis.main;

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
    ArrayList<Map<String, String>> allGLayerInputs;
    int LCOffset;

    public ArrayList<String> getCommon(String link) {
        ArrayList<String> commonIn = new ArrayList<String>();

        int maxLC = gLayerNodes.size() + LCOffset;

        String[] allIn = link.split(Constants.NODE_TOKENIZER);
        String[] inStrInit = allIn[0].split(Constants.I_J_TOKENIZER);
        int lcInit = Integer.parseInt(inStrInit[0]);

        if (allGLayerInputs.get(lcInit - LCOffset).get(allIn[0]) == null) {
            return commonIn;
        }

        String[] inputsInit = allGLayerInputs.get(lcInit - LCOffset).get(allIn[0]).split(Constants.INPUT_TOKENIZER);
        commonIn.addAll(Arrays.asList(inputsInit));

        for (int i = 1; i < allIn.length; i++) {
            String[] inStr = allIn[i].split(Constants.I_J_TOKENIZER);

            int lc = Integer.parseInt(inStr[0]);

            String inputs = "";

            for (int j = maxLC - 1; j >= lc; j--) {
                if (allGLayerInputs.get(j - LCOffset).containsKey(allIn[i])) {
                    inputs = allGLayerInputs.get(j - LCOffset).get(allIn[i]);
                    break;
                }
            }

            String[] inputTokens = inputs.split(Constants.INPUT_TOKENIZER);

            List<String> in2List = Arrays.asList(inputTokens);

            commonIn.retainAll(in2List);
        }
        return commonIn;
    }

    private ArrayList<String> findAdjacentLCLinks(ArrayList<String> links, int nxtLvl, int minCount) {

        //if we have reached end of all GLayers, return the links we have
        if (nxtLvl - LCOffset > gLayerNodes.size() - 1) {
            return links;
        }

        //new nodes to which we compare end points of existing links
        ArrayList<String> newNodes = gLayerNodes.get(nxtLvl - LCOffset);

        //if there are no new nodes stop and return links
        if (newNodes == null || newNodes.isEmpty()) {
            return links;
        }

        Iterator<String> linksIter = links.iterator();
        ArrayList<String> tempLinks = new ArrayList<String>();  //to keep the new links temporarily

        //until the links arraylist have links, get the next
        while (linksIter.hasNext()) {
            String s1 = linksIter.next();
            
            //if the the number of common elements in teh link are less than minCount, remove them
            if (getCommon(s1).size() < minCount) {
                linksIter.remove();
            }

            //for each node ID (s2) in new nodes
            for (String s2 : newNodes) {
                //create a new link with s1+s2;
                String newLink = s1 + Constants.NODE_TOKENIZER + s2;

                int newNodeLC = Integer.parseInt(s2.split(Constants.I_J_TOKENIZER)[0]);

                //if the newlink has commone elments more than minCount and newNode is from above or equal to nxtLvl
                //add new link to a temp Array
                //if this is the case we do not need to the s1 link any longer, remove it
                if (getCommon(newLink).size() >= minCount && newNodeLC >= nxtLvl) {
                    if(links.contains(s1)){
                        linksIter.remove();
                    }
                    tempLinks.add(newLink);
                }

            }
        }
        links.addAll(tempLinks);

        return findAdjacentLCLinks(links, nxtLvl + 1, minCount);
    }

    public void setData(ArrayList<ArrayList<String>> gLayerNodes, ArrayList<Map<String, String>> allGLayerInputs, int offset) {
        this.allGLayerInputs = allGLayerInputs;
        this.LCOffset = offset;
        this.gLayerNodes = gLayerNodes;
    }

    public ArrayList<String> getFullLinks(int minLength, int minCount) {

        ArrayList<String> longLinks = new ArrayList<String>();
        for (int i = 0; i <= gLayerNodes.size() - minLength; i++) {
            longLinks.addAll(findAdjacentLCLinks(new ArrayList<String>(gLayerNodes.get(i)), i + 1 + LCOffset, minCount));

            Iterator<String> iterLongLinks = longLinks.iterator();
            while (iterLongLinks.hasNext()) {
                String s = iterLongLinks.next();
                if (s.split(Constants.NODE_TOKENIZER).length < minLength) {
                    iterLongLinks.remove();
                }

            }
        }
        
        return longLinks;
    }


    public ArrayList<Integer> getIntLinkStrength(String link, ArrayList<Map<String, String>> allGNodeInputs, int startLCOffset) {
        String[] linkNodes = link.split(Constants.NODE_TOKENIZER);
        int maxInputs = 0;
        for (String s : linkNodes) {
            String[] sTokens = s.split(Constants.I_J_TOKENIZER);
            int idx = Integer.parseInt(sTokens[0]) - startLCOffset;

            if (allGNodeInputs.get(idx).get(s).split(Constants.INPUT_TOKENIZER).length > maxInputs) {
                maxInputs = allGNodeInputs.get(idx).get(s).split(Constants.INPUT_TOKENIZER).length;
            }
        }

        if (maxInputs == 0) {
            maxInputs = 1;
        }

        ArrayList<Integer> percentages = new ArrayList<>();

        for (int i = 0; i < linkNodes.length - 1; i++) {
            String s1 = linkNodes[i];
            String s2 = linkNodes[i + 1];
            String tempLink = s1 + Constants.NODE_TOKENIZER + s2;

            int comIn = getCommon(tempLink).size();

            Integer perc = comIn * 100 / maxInputs;
            percentages.add(perc);
        }

        return percentages;
    }
    
    public int getIntLinkAvgStrength(String link, ArrayList<Map<String, String>> allGNodeInputs, int startLCOffset) {
        ArrayList<Integer> strengths = getIntLinkStrength(link, allGNodeInputs, startLCOffset);
        int avg = 0;
        if(strengths!=null){
            for(int val : strengths){
                avg += val;
            }
            if(!strengths.isEmpty()){
                avg = avg/strengths.size();
            }
        }
        return avg;
    }
    
}
