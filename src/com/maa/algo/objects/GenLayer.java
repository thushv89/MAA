/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import com.maa.algo.utils.AlgoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class GenLayer extends Layer implements Serializable{
    
    private Map<String, GNode> map;
    
    public GenLayer(Map<String,GNode> map){
        this.map = map;
    }
    
    public GenLayer(){
        map = new HashMap<String, GNode>();
    }
    
    public void addNode(GNode node){
        getMap().put(AlgoUtils.generateIndexString(node.getLc(), node.getId()), node);
    }
    
    public void addNodes(ArrayList<GNode> nodes){
        for(GNode n : nodes){
            addNode(n);
        }
    }
    
    public void removeNode(GNode node){
        getMap().remove(AlgoUtils.generateIndexString(node.getLc(), node.getId()));
    }

    /**
     * @return the map
     */
    public Map<String, GNode> getMap() {
        return map;
    }
    
    public Map<String, GNode> getCopyMap() {
        return new HashMap<String, GNode>(map);
    }
}
