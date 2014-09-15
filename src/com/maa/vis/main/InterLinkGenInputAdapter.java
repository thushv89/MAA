/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.vis.main;

import com.maa.utils.Tokenizers;
import com.maa.vis.objects.ReducedNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thush
 */
public class InterLinkGenInputAdapter {
    
    private static ArrayList<ArrayList<String>> gNodes;
    private static ArrayList<Map<String, String>> allGNodeInputs;
    
    public static void adaptInputs(ArrayList<ArrayList<ReducedNode>> allNodes, int startIdx) {
        gNodes = new ArrayList<>();
        allGNodeInputs = new ArrayList<>();
        
        for (int i = startIdx; i < allNodes.size(); i++) {
            ArrayList<ReducedNode> layer = allNodes.get(i);
            ArrayList<String> currLayerGnodes = new ArrayList<>();
            Map<String, String> currGNodeInputs = new HashMap<>();

            for (ReducedNode gn : layer) {
                currLayerGnodes.add(gn.getId()[0] + Tokenizers.I_J_TOKENIZER + gn.getId()[1]);
                if (gn.getInputs() != null && !gn.getInputs().isEmpty()) {
                    String inputs = gn.getInputs().get(0);
                    for (String s : gn.getInputs()) {
                        inputs += Tokenizers.INPUT_TOKENIZER + s;
                    }
                    currGNodeInputs.put(gn.getId()[0] + Tokenizers.I_J_TOKENIZER + gn.getId()[1], inputs);
                }
            }
            gNodes.add(currLayerGnodes);
            allGNodeInputs.add(currGNodeInputs);
        }
    }
    
    public static ArrayList<ArrayList<String>> getNodes(){
        return gNodes;
    }
    
    public static ArrayList<Map<String, String>> getNodeInputMap(){
        return allGNodeInputs;
    }
}
