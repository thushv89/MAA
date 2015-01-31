/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.links;

import com.maa.algo.objects.GNode;
import com.maa.algo.objects.GenLayer;
import com.maa.algo.utils.Constants;
import com.maa.algo.utils.AlgoUtils;
import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class IKASLGlobalLinkUpdater implements Serializable{

    private ArrayList<GlobalLink> allGlobalIntersectLinks;
    private HashMap<String, Integer> nodeChildCountMap;
    private HashMap<String, String> mergedList;

    public IKASLGlobalLinkUpdater() {
        allGlobalIntersectLinks = new ArrayList<>();
        nodeChildCountMap = new HashMap<>();
        mergedList = new HashMap<>();

    }

    public void updateGlobalLinkList(GenLayer currGLayer, int lc) {

        if (lc > 0) {
            updateChildCountMap(currGLayer);
            System.out.println("Updated Child Count");
            //for the LC = 1 we've to add both parentID and nodeID
            //but we do not need to explicitly do that here, as that part is handled in
            // addOrConcatGlobalLink
            for (Map.Entry<String, GNode> e : currGLayer.getMap().entrySet()) {
                String gID = AlgoUtils.generateIndexString(e.getValue().getLc(), e.getValue().getId());
                String pID = e.getValue().getParentID();
                // if node has atleast 1 parent
                if (pID != null && !pID.isEmpty() && !pID.equals(Constants.INIT_PARENT)) {
                    String newPID = pID;
                    for (Map.Entry<String, String> e1 : mergedList.entrySet()) {
                        if (e1.getValue().endsWith(pID)) {
                            newPID = e1.getKey();
                            break;
                        }
                    }
                    checkAndAddLinkToGlobalLinks(newPID, gID);
                }
            }

        }

        int ixx = 0;

    }

    //need to know how many links for a given pID has been created so far
    //update 
    private void checkAndAddLinkToGlobalLinks(String pID, String gID) {
        if (pID.contains(Constants.PARENT_TOKENIZER)) {
            //if there are multiple parents, we've to find strings which has all these parents
            //and merge them
            //n11,n21,n31
            //          \
            //            n41   ====> (n11+n12;n21+"-",n31+n32,n41)
            //          /
            //n12,-,n32

            //first create links for each pID,gID combinations without worrying about merging
            for (String s : pID.split(Constants.PARENT_TOKENIZER)) {
                createLinksforSingleParent(s, gID);
            }

            System.out.println("Merging Process Started for " + pID + Constants.NODE_TOKENIZER + gID);

            //now perform merging
            //ArrayList<String[]> ordPossibleMergeCand = new ArrayList<>();
            ArrayList<String[]> possibleMergeCand = new ArrayList<>();
            ArrayList<Integer> candIdx = new ArrayList<>();

            //todo: order possibleMergeCand so that strings belonging to same sub-merged components are near
            for (int i = 0; i < allGlobalIntersectLinks.size(); i++) {
                GlobalLink gl = allGlobalIntersectLinks.get(i);
                if (gl.getLink().endsWith(gID)) {
                    possibleMergeCand.add(gl.getLink().split(Constants.NODE_TOKENIZER));
                    candIdx.add(i);
                }
            }

            /*
             * DO NOT Remove the old links (before merge) Because they come in handy
             * if we have to merge some links which already has merged members in it
             * instead we set the isMergedLeftOver variable to true
             */
            for (int i : candIdx) {
                GlobalLink gl = allGlobalIntersectLinks.get(i);
                gl.setIsMergedLeftOver(true);
                allGlobalIntersectLinks.set(i, gl);
                System.out.println(gl.getLink() + " is set as MergedLeftOver");
            }

            /*ArrayList<Integer> finishedIdx = new ArrayList<>();
             for (int i = tempPossibleMergeCand.get(0).length - 1; i >= 0 && finishedIdx.size() < tempPossibleMergeCand.size(); i--) {
             for (int j = 0; j < tempPossibleMergeCand.size() && !finishedIdx.contains(j); j++) {
             boolean hadSimilar = false; //this variable check whether the tempPossible.get(j) had atleast 1 similar tempPossible.get(k)
             for (int k = j + 1; k < tempPossibleMergeCand.size() && !finishedIdx.contains(k); k++) {
             if (tempPossibleMergeCand.get(j)[i].equals(tempPossibleMergeCand.get(k)[i])) {
             ordPossibleMergeCand.add(tempPossibleMergeCand.get(k));
             finishedIdx.add(k);
             hadSimilar = true;
             }
             }
             if (hadSimilar) {
             ordPossibleMergeCand.add(tempPossibleMergeCand.get(j));
             finishedIdx.add(j);
             }
             }
             }
             for (int i = 0; i < tempPossibleMergeCand.size(); i++) {
             if (!finishedIdx.contains(i)) {
             ordPossibleMergeCand.add(tempPossibleMergeCand.get(i));
             }
             }*/
            System.out.println("Number of links to be merged " + possibleMergeCand.size());



            /*
             * n13,---,---,n42
             *                \
             * n11,n21,n31      n51 ====> (M1+n13;M1+--;M1+--;M1+n42;n51)
             *           \    /
             *             n41   
             *           /
             * n12,---,n32
             * 
             * <--- M1 ----->
             */

            String fullLink = "";
            for (int i = 0; i < possibleMergeCand.get(0).length; i++) {
                String newLinkComp = "";
                for (int j = 0; j < possibleMergeCand.size(); j++) {

                    //These links in possibleMergeCand does not have PARENT_TOKENIZER they have MERGE_TOKENIZER
                    if (i == possibleMergeCand.get(0).length - 1) {
                        newLinkComp += possibleMergeCand.get(j)[i];
                        break;
                    }
                    if (j == possibleMergeCand.size() - 1) {
                        newLinkComp += possibleMergeCand.get(j)[i];
                    } else {
                        newLinkComp += possibleMergeCand.get(j)[i] + Constants.MERGED_TOKENIZER;
                    }

                }

                if (i == possibleMergeCand.get(0).length - 1) {
                    fullLink += newLinkComp;
                } else {
                    fullLink += newLinkComp + Constants.NODE_TOKENIZER;
                }
            }




            String[] allLinkComp = fullLink.split(Constants.NODE_TOKENIZER);
            String newKey = "M" + allLinkComp[allLinkComp.length - 1];
            mergedList.put(newKey, fullLink);
            String substLink = "";
            for (int i = 0; i < allLinkComp.length; i++) {
                if (i == allLinkComp.length - 1) {
                    substLink += newKey;
                } else {
                    substLink += newKey + Constants.NODE_TOKENIZER;
                }
            }
            allGlobalIntersectLinks.add(new GlobalLink(substLink, false));
            System.out.println("Merge Result: " + newKey + " -> " + fullLink);

        } else {
            createLinksforSingleParent(pID, gID);
        }

    }

    private String checkIsInMergedListAndGetKey(String gID) {
        for (Map.Entry<String, String> e : mergedList.entrySet()) {
            if (e.getValue().contains(gID)) {
                return e.getKey();
            }
        }
        return null;
    }

    private void createLinksforSingleParent(String pID, String gID) {
        System.out.println("Creating Link for PID: " + pID + " and GID: " + gID);

        String origPID = pID;
        if (mergedList.get(pID) != null) {
            String[] tempPID = mergedList.get(pID).split(Constants.NODE_TOKENIZER);
            origPID = tempPID[tempPID.length - 1];
        }

        if (nodeChildCountMap.get(origPID) > 1) {
            //get how many links are already there and what is the exact full link
            int createdForPID = 0;  //number of children the PID already has links for
            String linkForPID = ""; //exact link which has PID
            for (GlobalLink gl : allGlobalIntersectLinks) {
                String s = gl.getLink();
                if (!gl.isIsMergedLeftOver() && s.contains(pID)) {
                    linkForPID = s;
                    createdForPID++;
                }
            }

            //if we already have some links with that pID included
            if (!linkForPID.isEmpty()) {
                //modify linkForPID because think of a situation the string with PID has been concatenated with a child
                //then we've to get only the part upto pID, not the whole string
                String[] linkTokens = linkForPID.split(Constants.NODE_TOKENIZER);
                String modLinkForPID = "";
                for (int i=linkTokens.length-1;i>=0;i--) {
                    if (linkTokens[i].equals(pID)) {
                        for(int j=0;j<=i;j++){
                            if(j==i){
                                modLinkForPID += linkTokens[j];
                            } else {
                                modLinkForPID += linkTokens[j] + Constants.NODE_TOKENIZER;
                            }
                        }
                        break;
                    }
                }

                //create new links by taking the current number of links aleady created
                //and number of links needed in total
                for (int i = createdForPID; i < nodeChildCountMap.get(origPID); i++) {
                    allGlobalIntersectLinks.add(new GlobalLink(modLinkForPID, false));
                }
                if ((nodeChildCountMap.get(origPID) - createdForPID) > 0) {
                    System.out.println("Replicating " + modLinkForPID + " " + (nodeChildCountMap.get(origPID) - createdForPID + 1) + " times");
                }

                addOrConcatGlobalLink(pID, gID);
            } else {
                addOrConcatGlobalLink(pID, gID);
            }

        } //value of nodeChildMap(key) = 1 means that, this parent nodes has not been
        //seen before, therefore, it will not be in the GlobalLinkList
        else {
            addOrConcatGlobalLink(pID, gID);
        }
    }

    private void addOrConcatGlobalLink(String pID, String gID) {
        
        //get only the numbers in the strig according to what we get as PID
        //take substring(1) if pID is Mx,y
        //take full string if pID is x,y
        int pLC; 
        if(pID.startsWith("M")){
            pLC = Integer.parseInt(pID.substring(1).split(Constants.I_J_TOKENIZER)[0]);
        } else {
            pLC = Integer.parseInt(pID.split(Constants.I_J_TOKENIZER)[0]);
        }
        int gLC = Integer.parseInt(gID.split(Constants.I_J_TOKENIZER)[0]);

        String intermediateEmptySpace = "";
        for (int i = pLC + 1; i < gLC; i++) {
            intermediateEmptySpace += Constants.NODE_TOKENIZER + Constants.EMPTY_NODE;
        }



        for (GlobalLink gl : allGlobalIntersectLinks) {
            String s = gl.getLink();
            String[] sTokens = s.split(Constants.NODE_TOKENIZER);

            //if string contains pID and pID is the last element of the string
            if (!gl.isIsMergedLeftOver() && s.contains(pID) && sTokens[sTokens.length - 1].equals(pID)) {
                s += intermediateEmptySpace + Constants.NODE_TOKENIZER + gID;
                gl.setLink(s);
                System.out.println("Existing link updated to " + s);
                return;
            }

        }

        //if we couldn't find any string with pID
        String beforeParentEmptySpace = "";
        for (int i = 0; i < pLC; i++) {
            beforeParentEmptySpace += Constants.EMPTY_NODE + Constants.NODE_TOKENIZER;
        }

        allGlobalIntersectLinks.add(new GlobalLink(beforeParentEmptySpace + pID + intermediateEmptySpace + Constants.NODE_TOKENIZER + gID, false));
        System.out.println("Add new link: " + beforeParentEmptySpace + pID + intermediateEmptySpace + Constants.NODE_TOKENIZER + gID);

    }
    //is not taking care of multi-parent IDs (solved)

    private void updateChildCountMap(GenLayer currGLayer) {
        for (Map.Entry<String, GNode> e : currGLayer.getMap().entrySet()) {
            String pID = e.getValue().getParentID();
            if (pID.equals(Constants.INIT_PARENT)) {
                continue;
            }

            if (!pID.contains(Constants.PARENT_TOKENIZER)) {
                if (nodeChildCountMap.containsKey(pID)) {
                    int val = nodeChildCountMap.get(pID);
                    val++;
                    nodeChildCountMap.put(pID, val);
                } else {
                    nodeChildCountMap.put(pID, 1);
                }
            } else {
                String[] pIDTokens = pID.split(Constants.PARENT_TOKENIZER);
                for (String p : pIDTokens) {
                    if (nodeChildCountMap.containsKey(p)) {
                        int val = nodeChildCountMap.get(p);
                        val++;
                        nodeChildCountMap.put(p, val);
                    } else {
                        nodeChildCountMap.put(p, 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> e : nodeChildCountMap.entrySet()) {
            System.out.print(e.getKey() + " (" + e.getValue() + ") ");
        }
        System.out.println("");
    }

    public ArrayList<String> getAllLinksWithoutMergedLeftOvers() {
        ArrayList<String> links = new ArrayList<>();
        for (GlobalLink gl : allGlobalIntersectLinks) {
            if (!gl.isIsMergedLeftOver()) {
                links.add(gl.getLink());
            }
        }
        return links;
    }
    
    public ArrayList<String> getAllLinksWithMergedLeftOvers() {
        ArrayList<String> links = new ArrayList<>();
        for (GlobalLink gl : allGlobalIntersectLinks) {
            
                links.add(gl.getLink());
            
        }
        return links;
    }

    public HashMap<String, String> getMergedLinks() {
        return mergedList;
    }
}
