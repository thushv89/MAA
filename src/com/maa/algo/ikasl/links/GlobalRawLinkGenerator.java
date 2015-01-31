/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.links;

import com.maa.algo.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Thush
 */
public class GlobalRawLinkGenerator {

    int maxMergeLinkDepth = 3;

    public GlobalRawLinkGenerator() {
    }

    public ArrayList<String> getRawLinkGenerator(ArrayList<String> allGlobalLinks, HashMap<String, String> mergeList) {
        ArrayList<String> rawLinks = new ArrayList<>(); //this array list will hold the expanded links

        //take links in global link list
        for (String lnk : allGlobalLinks) {
            // if link contains a "M" that means we've to expand the link (crate multiple entries for the link)
            if (lnk.contains("M")) {

                String[] lTokens = lnk.split(Constants.NODE_TOKENIZER);

                //if link is not empty
                if (lTokens.length != 0) {
                    ArrayList<String> expandedLinks = new ArrayList<>();
                    String linkPar = lTokens[lTokens.length - 1];   //this contains the link found so far
                    boolean mergeNodeEncountered = false;   //this variable becomes true once it encounters a node with "M"

                    // for link tokesn from previous to the last to (0th or Node with "M")
                    for (int i = lTokens.length - 2; i >= 0 && !mergeNodeEncountered; i--) {
                        String s = lTokens[i];

                        //if the node doesn't contain "M" the token can be added to linkPar without problem
                        if (!s.contains("M")) {
                            linkPar = s + Constants.NODE_TOKENIZER + linkPar;
                            mergeNodeEncountered = false;
                            //else some work needs to be done (create multiple entries for each branch in the merged link)
                        } else {
                            linkPar = s.substring(1) + Constants.NODE_TOKENIZER + linkPar;
                            String mLink = mergeList.get(s);
                            String[] mLTokens = mLink.split(Constants.NODE_TOKENIZER);
                            for (int j = 0; j < mLTokens.length - 1; j++) {
                                String mLS = mLTokens[j];
                                String[] mLSTokens = mLS.split("\\" + Constants.MERGED_TOKENIZER);
                                if (j == 0) {
                                    for (int k = 0; k < mLSTokens.length; k++) {
                                        expandedLinks.add(mLSTokens[k]);
                                    }
                                } else {
                                    for (int k = 0; k < mLSTokens.length; k++) {
                                        expandedLinks.set(k, expandedLinks.get(k) + Constants.NODE_TOKENIZER + mLSTokens[k]);
                                    }
                                }
                            }
                            mergeNodeEncountered = true;
                        }
                    }

                    for (int i = 0; i < expandedLinks.size(); i++) {
                        String newLink = expandedLinks.get(i) + Constants.NODE_TOKENIZER + linkPar;
                        if (!rawLinks.contains(newLink)) {
                            rawLinks.add(newLink);
                        }
                    }
                }
            } else {
                if (!rawLinks.contains(lnk)) {
                    rawLinks.add(lnk);
                }
            }


        }

        return rawLinks;
    }

    public ArrayList<String> getRawLinkGenerator2(ArrayList<String> allGlobalLinks, HashMap<String, String> mergeList) {
        ArrayList<String> rawLinks = new ArrayList<>(); //this array list will hold the expanded links

        //take links in global link list
        for (String lnk : allGlobalLinks) {
            // if link contains a "M" that means we've to expand the link (crate multiple entries for the link)
            if (lnk.contains("M")) {

                String[] lTokens = lnk.split(Constants.NODE_TOKENIZER);

                //if link is not empty
                if (lTokens.length != 0) {
                    ArrayList<String> expandedLinks = new ArrayList<>();
                    String linkPar = lTokens[lTokens.length - 1];   //this contains the link found so far

                    // for link tokesn from previous to the last to (0th or Node with "M")
                    int mergeDepth = 0;
                    boolean mergeNodeEncountered = false;   //this variable becomes true once it encounters a node with "M"
                    for (int i = lTokens.length - 2; i >= 0 && !mergeNodeEncountered; i--) {
                        String s = lTokens[i];

                        //if the node doesn't contain "M" the token can be added to linkPar without problem
                        if (!s.contains("M")) {
                            linkPar = s + Constants.NODE_TOKENIZER + linkPar;
                            mergeNodeEncountered = false;
                            //else some work needs to be done (create multiple entries for each branch in the merged link)
                        } else {
                            linkPar = s + Constants.NODE_TOKENIZER + linkPar;
                            expandedLinks.add(linkPar);
                            while (mergeDepth <= maxMergeLinkDepth && !noMergedLink(expandedLinks)) {
                                expandedLinks = expandMLinks(expandedLinks, mergeList, mergeDepth);
                                mergeDepth++;
                            }

                            mergeNodeEncountered = true;
                            //recursive method
                        }
                    }

                    for (int i = 0; i < expandedLinks.size(); i++) {
                        String newLink = expandedLinks.get(i) + Constants.NODE_TOKENIZER + linkPar;
                        if (!rawLinks.contains(newLink)) {
                            rawLinks.add(newLink);
                        }
                    }
                }
            } else {
                if (!rawLinks.contains(lnk)) {
                    rawLinks.add(lnk);
                }
            }


        }

        return rawLinks;
    }

    private boolean noMergedLink(ArrayList<String> links) {
        for (String l : links) {
            if (l.contains("M")) {
                return false;
            }
        }
        return true;
    }

    //links in expanded links need to be in the following format
    //e.g. (Mx1,y1),(x1,y1),(x2,y2),... 
    private ArrayList<String> expandMLinks(ArrayList<String> expandedLinks, HashMap<String, String> mergeList, int mergeExpCount) {
        if (mergeExpCount > maxMergeLinkDepth) {
            for (String link : expandedLinks) {
                if (link.startsWith("M")) {
                    link = link.substring(1);
                }
            }
            return expandedLinks;
        }

        ArrayList<String> copyExpLinks = new ArrayList<>(expandedLinks);
        for (String link : copyExpLinks) {
            if (link.startsWith("M")) {
                String[] lTokens = link.split(Constants.NODE_TOKENIZER);
                String mNode = lTokens[0];
                String mLink = mergeList.get(mNode);
                String linkPar = mNode.substring(1);
                for (int i = 1; i < lTokens.length; i++) {
                    linkPar += Constants.NODE_TOKENIZER + lTokens[i];
                }
                String[] mLTokens = mLink.split(Constants.NODE_TOKENIZER);
                expandedLinks.remove(link);

                ArrayList<String> tempLinks = new ArrayList<>();
                for (int j = mLTokens.length - 2; j >= 0; j--) {
                    String mLS = mLTokens[j];
                    String[] mLSTokens = mLS.split("\\" + Constants.MERGED_TOKENIZER);
                    if (j == mLTokens.length - 2) {
                        for (int k = 0; k < mLSTokens.length; k++) {
                            tempLinks.add(mLSTokens[k]);
                        }
                    } else {
                        for (int k = 0; k < mLSTokens.length; k++) {
                            if (tempLinks.get(k).startsWith("M")) {
                                continue;
                            }
                            tempLinks.set(k, mLSTokens[k] + Constants.NODE_TOKENIZER + tempLinks.get(k));
                        }
                    }
                }

                for (String tLink : tempLinks) {
                    expandedLinks.add(tLink + Constants.NODE_TOKENIZER + linkPar);
                }
            }
        }
        return expandedLinks;
    }
}
