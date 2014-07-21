package com.maa.algo.gsom;

import com.maa.algo.objects.LNode;
import com.maa.algo.utils.Utils;
import java.util.HashMap;
import java.util.Map;

public class GCoordAdjuster {

	public Map<String,LNode> adjustMapCoords(Map<String,LNode> map){
		Map<String,LNode> newMap = new HashMap<String,LNode>();
		
		int minX=0;
		int minY=0;
		//find minimum x,y coordinates in the map
		for(LNode node: map.values()){
			if(node.getX()<minX){
				minX = node.getX();
			}
			if(node.getY()<minY){
				minY = node.getY();
			}
		}
		
		System.out.println("Min,Max: "+minX+","+minY);
		//adjust node coordinates by subtracting the minimum x,y from all nodes
                //using map.values here is OK because we're discarding the old map anyway
		for(LNode node: map.values()){
			
			//System.out.print("Node changed from "+ node.getX()+","+node.getY());
			node.setX(node.getX()+(-minX));
			node.setY(node.getY()+(-minY));
			//System.out.print(" to "+node.getX()+","+node.getY()+"\n");
			newMap.put(Utils.generateIndexString(node.getX(), node.getY()), node);
		}
		//discard the previous map
		
		return newMap;
	}
}
