/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.utils;

import com.maa.algo.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class PreAnomUtil {

    public static void updatePreAnomalyData(ArrayList<String> preAnomLinkList, HashMap<String, ArrayList<String>> preAnomDevIDMap,
            ArrayList<String> normalLinkList, HashMap<String, ArrayList<String>> normalLinkDevIDMap, boolean getRecentNoAnom, boolean getRecentNoSameAnom) {
        System.out.println("Pre Anom Links");

        for (Map.Entry<String, ArrayList<String>> e : CurrentJobState.FILT_LINKS.entrySet()) {
            String link = e.getKey();
            String[] linkTokens = link.split(Constants.NODE_TOKENIZER);
            for (int i = DefaultValues.PAST_NODES_PRE_ANOMALY - 1; i < linkTokens.length; i++) {
                String newLink = "";
                if (CurrentJobState.ALL_ANOM_GNODES.contains(linkTokens[i])) {
                    newLink = linkTokens[i];
                    String anomToken = linkTokens[i];
                    String anomType = getAnomType(anomToken);
                    for (int j = i - 1; j > i - DefaultValues.PAST_NODES_PRE_ANOMALY; j--) {
                        String prevAnomType = getAnomType(linkTokens[j]);
                        if (getRecentNoAnom && CurrentJobState.ALL_ANOM_GNODES.contains(linkTokens[j])) {
                            newLink = null;
                            break;
                        }
                        if (!getRecentNoAnom && getRecentNoSameAnom && prevAnomType != null
                                && (prevAnomType.equals(anomType) || prevAnomType.contains(anomType) || anomType.contains(prevAnomType))) {
                            newLink = null;
                            break;
                        }
                        newLink = linkTokens[j] + Constants.NODE_TOKENIZER + newLink;
                    }

                    if (newLink != null && !newLink.isEmpty() && !preAnomLinkList.contains(newLink)) {
                        preAnomLinkList.add(newLink);
                        preAnomDevIDMap.put(newLink, e.getValue());
                        System.out.println(newLink + " [" + anomType + "]" + " ,Devices: " + e.getValue().size());
                    }
                }



                if (normalLinkList.size() < DefaultValues.MAX_NORMAL_LINKS) {
                    String newNormLink = "";
                    if (CurrentJobState.ALL_NORM_GNODES.contains(linkTokens[i])) {
                        newNormLink = linkTokens[i];
                        for (int j = i - 1; j > i - DefaultValues.PAST_NODES_PRE_ANOMALY; j--) {
                            if (!CurrentJobState.ALL_NORM_GNODES.contains(linkTokens[j])) {
                                newNormLink = null;
                                break;
                            }
                            newNormLink = linkTokens[j] + Constants.NODE_TOKENIZER + newNormLink;
                        }
                    }

                    if (newNormLink != null && !newNormLink.isEmpty()) {
                        normalLinkList.add(newNormLink);
                        normalLinkDevIDMap.put(newNormLink, e.getValue());
                    }
                }

            }
        }
    }

    public static String getAnomType(String node) {
        String key = null;
        int idx = 0;
        for (Map.Entry<String, ArrayList<String>> e : CurrentJobState.ANOM_GNODES_BY_TYPE.entrySet()) {
            if (e.getValue().contains(node)) {
                if (idx == 0) {
                    key = e.getKey();
                } else {
                    key += "," + e.getKey();
                }
                idx++;
            }

        }

        return key;
    }

    public static void mapAnomsToPreAnomLinks(ArrayList<String> preAnomLinkList, HashMap<String, ArrayList<String>> preAnomLinkMap) {
        for (String link : preAnomLinkList) {
            String[] linkTokens = link.split(Constants.NODE_TOKENIZER);
            String lastToken = linkTokens[linkTokens.length - 1];
            for (Map.Entry<String, ArrayList<String>> e : CurrentJobState.ANOM_GNODES_BY_TYPE.entrySet()) {
                if (e.getValue().contains(lastToken)) {
                    ArrayList<String> val = preAnomLinkMap.get(e.getKey());
                    if (val == null) {
                        val = new ArrayList<>();
                    }
                    if (!val.contains(link)) {
                        val.add(link);
                        preAnomLinkMap.put(e.getKey(), val);
                    }

                }
            }

        }
    }
}
