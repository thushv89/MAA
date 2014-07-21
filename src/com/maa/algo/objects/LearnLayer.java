/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thushan Ganegedara
 */
public class LearnLayer extends Layer{
    Map<String,Map<String,LNode>> maps;
    
    public LearnLayer(){
        maps = new HashMap<String, Map<String, LNode>>();
    }
    
    public void addMap(String parentID, Map<String,LNode> map){
        maps.put(parentID, map);
    }
    
    public int getMapCount(){
        return maps.size();
    }
    
    public Map<String,Map<String,LNode>> getMap(){
        return maps;
    }
    
    public Map<String,Map<String,LNode>> getCopyMap(){
        return new HashMap<String,Map<String,LNode>>(maps);
    }
    
    public Map<String,LNode> getSingleMapWithParent(String id){
        return maps.get(id);
    }
    
     public Map<String,LNode> getSingleMapCopyWithParent(String id){
        return new HashMap<String,LNode>(maps.get(id));
    }
}
