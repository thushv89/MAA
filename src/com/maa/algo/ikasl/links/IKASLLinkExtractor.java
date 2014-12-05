/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.links;

import com.maa.algo.utils.Constants;
import com.maa.utils.DefaultValues;
import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class IKASLLinkExtractor {

    ArrayList<String> allGlobalLinks;   //need to get all links including MergedLeftOvers, because we're ignoring the merged list
    HashMap<String, String> mergeList;
    ArrayList<ArrayList<ReducedNode>> allNodes;

    public IKASLLinkExtractor(ArrayList<String> allGlobalLinks, HashMap<String, String> mergeList, ArrayList<ArrayList<ReducedNode>> allNodes) {
        this.allGlobalLinks = allGlobalLinks;
        this.mergeList = mergeList;
        this.allNodes = allNodes;
    }

    public HashMap<String, ArrayList<String>> performFiltering() {
        ArrayList<String> filtLinks = extractBestLinksFromAllGlobalLinks();
        HashMap<String, ArrayList<String>> filtLinkMap = extractFilteredLinksWithDevThresh(filtLinks);
        return filtLinkMap;
    }

    private ArrayList<String> extractBestLinksFromAllGlobalLinks() {
        ArrayList<String> extractedLinks = new ArrayList<>();

        for (int i = 0; i < allGlobalLinks.size(); i++) {
            String link = allGlobalLinks.get(i);
            String linkPar = getPartialLinkExceedThresh(link);
            if (linkPar != null && !linkPar.isEmpty() && !extractedLinks.contains(linkPar)) {
                extractedLinks.add(linkPar);
            }
        }

        /*
         * What's the point in adding MergedList elements? 
         * 
        for (Map.Entry<String, String> e : mergeList.entrySet()) {
            String link = e.getValue();
            String linkPar = getPartialLinkExceedThresh(link);
            if (linkPar != null && !extractedLinks.contains(linkPar)) {
                extractedLinks.add(linkPar);
            }
        }*/

        return extractedLinks;
    }

    private HashMap<String, ArrayList<String>> extractFilteredLinksWithDevThresh(ArrayList<String> currLinks) {
        HashMap<String, ArrayList<String>> linkMap = new HashMap<>();
        for (String link : currLinks) {
            String reqLink = "";
            ArrayList<String> reqCommon = new ArrayList<>();
            String[] linkTokens = link.split(Constants.NODE_TOKENIZER);
            for (int i = linkTokens.length - 1; i >= DefaultValues.MIN_EXT_COMMON_THRESH - 1; i--) {
                String parLink = linkTokens[0];
                for (int j = 1; j <= i; j++) {
                    parLink += Constants.NODE_TOKENIZER + linkTokens[j];
                }
                if(parLink.startsWith(";")){
                    System.out.println("");
                }
                ArrayList<String> common = CheckDevThreshSingleLink(parLink);
                if (common != null) {
                    reqLink = parLink;
                    reqCommon.addAll(common);
                    break;
                }
            }
            if (reqLink != null && !reqLink.isEmpty()) {
                linkMap.put(reqLink, reqCommon);
            }
        }
        return linkMap;
    }

    private String getPartialLinkExceedThresh(String link) {
        String[] linkTokens = link.split(Constants.NODE_TOKENIZER);
        String newLink = "";
        boolean firstNodeEncountered = false;
        for (int j = 0; j < linkTokens.length; j++) {

            if (linkTokens[j].contains("M")) {
                String withoutM = linkTokens[j].substring(1);
                if (!firstNodeEncountered) {
                    newLink = withoutM;
                    firstNodeEncountered = true;
                } else {
                    continue;
                }
            } else {
                //if link does not contain Mx,x
                if (!linkTokens[j].equals(Constants.EMPTY_NODE)) {
                    if (!firstNodeEncountered) {
                        newLink = linkTokens[j];
                        firstNodeEncountered = true;
                    } else {
                        newLink += Constants.NODE_TOKENIZER + linkTokens[j];
                    }
                }
            }
            
        }

        if (newLink.split(Constants.NODE_TOKENIZER).length >= DefaultValues.MIN_EXT_LINK_LENGTH) {
            return newLink;
        } else {
            return null;
        }
    }

    private ArrayList<String> CheckDevThreshSingleLink(String link) {
        if(link.isEmpty()){
            return null;
        }
        
        ArrayList<String> common = new ArrayList<>();

        String[] lTokens = link.split(Constants.NODE_TOKENIZER);
        for (int i = 0; i < lTokens.length; i++) {
            String s = lTokens[i];
            
            int lc = Integer.parseInt(s.split(Constants.I_J_TOKENIZER)[0]);
            int id = Integer.parseInt(s.split(Constants.I_J_TOKENIZER)[1]);
            for (int j = 0; j < allNodes.get(lc).size(); j++) {
                ReducedNode rn = allNodes.get(lc).get(j);
                if (lc == rn.getId()[0] && id == rn.getId()[1]) {
                    if (i == 0) {
                        common.addAll(rn.getInputs());
                        break;
                    } else {
                        common.retainAll(rn.getInputs());
                        break;
                    }
                }
            }

            if (common.size() < DefaultValues.MIN_EXT_COMMON_THRESH) {
                return null;
            }
        }
        return common;
    }
}
