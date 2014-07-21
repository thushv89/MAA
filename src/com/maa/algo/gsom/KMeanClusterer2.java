/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.gsom;

import com.maa.algo.objects.GCluster;
import com.maa.algo.objects.LNode;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.Utils;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author Thush
 */
public class KMeanClusterer2 {


    private Map<String, LNode> map;
    private int bestClusterCount;
    private int[] minMax;
    private double proxWeight;
    private int distance;
    
    public KMeanClusterer2(Map<String, LNode> map,int distance,double weight) {
        this.map = map;
        minMax = getMaxMinFromMap(map);
        proxWeight = weight;
        distance = this.distance;
    }

    //get All posible cluster lists
    public ArrayList<ArrayList<GCluster>> getAllClusters() {
        int kMax = (int) Math.ceil(Math.sqrt(map.size()));
        ArrayList<ArrayList<GCluster>> kClusterList = new ArrayList<ArrayList<GCluster>>();
        int i = 2;
        do{
            kClusterList.add(getKClusters(i,distance));
            i++;
        }while(i <= kMax);
        return kClusterList;
    }

    
    //get cluster list with "k" number of clusters
    private ArrayList<GCluster> getKClusters(int k, int distance) {
        ArrayList<LNode> initCentroids = getHighestHitNeurons(k,distance);
        ArrayList<GCluster> clusters = new ArrayList<GCluster>();
        for (int i = 0; i<k && i<initCentroids.size(); i++){
            GCluster cluster = new GCluster(initCentroids.get(i).getWeights());
            cluster.setX(initCentroids.get(i).getX());
            cluster.setY(initCentroids.get(i).getY());
            clusters.add(cluster);
        }
        
        clusters = assignNeuronsToClusters(clusters);
        
        int count = 0;
        while(count <1000){
            for(int i=0;i<clusters.size();i++){
                clusters.get(i).CalculateAndAssignNewCentroid();
            }
            clusters = assignNeuronsToClusters(clusters);
            count++;
        }
        
        return clusters;
    }

    //get the center nodes of the "k" number of cluster
    private ArrayList<LNode> getHighestHitNeurons(int k, int distance) {
        ArrayList<LNode> nodeList = new ArrayList<LNode>();
        nodeList.addAll(map.values());
        ArrayList<double[]> highestKWeights = new ArrayList<double[]>();
        ArrayList<LNode> highestKNodes = new ArrayList<LNode>();
        Collections.sort(nodeList, new Comparator<LNode>() {

            @Override
            public int compare(LNode o1, LNode o2) {
                if (o1.getHitValue() > o2.getHitValue()) {
                    return 1;
                } else if (o1.getHitValue() < o2.getHitValue()) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });

        int i=nodeList.size() - 1;
        int count = 0;
        while(count<k && i>=0) {
            
            boolean isDistant = true;
            if (nodeList.get(i).getHitValue() <= 0) {
                break;
            }
            for (int j=0;j<highestKWeights.size() && highestKWeights.size()>0;j++){
                if(Math.abs(nodeList.get(i).getX()-highestKNodes.get(j).getX())< distance &&
                        Math.abs(nodeList.get(i).getY()-highestKNodes.get(j).getY())< distance){
                    isDistant = false;
                    break;
                }
            }
            
            if(isDistant){
                highestKNodes.add(nodeList.get(i));
                highestKWeights.add(nodeList.get(i).getWeights());
                count++;
            }
            i--;
        }

        return highestKNodes;
    }

    //find other neurons belonging to this cluster
    private ArrayList<GCluster> assignNeuronsToClusters(ArrayList<GCluster> clusters) {
        
        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).getcNodes().clear();
        }
        
        int idxOfAssignedCluster = 0;
        for (Entry<String, LNode> entry : map.entrySet()) {
            double minDistance = Double.MAX_VALUE;
            double distance;
            double proxdistance;
            //if (entry.getValue().getHitValue() > 0) {
                for (int i = 0; i < clusters.size(); i++) {
                    distance = Utils.calcDist(entry.getValue().getWeights(), clusters.get(i).getCentroidWeights(), 
                            AlgoParameters.DIMENSIONS, AlgoParameters.ATTR_WEIGHTS, AlgoParameters.dType);
                    proxdistance = (double)((entry.getValue().getX() - clusters.get(i).getX())*(entry.getValue().getX() - clusters.get(i).getX()))/(minMax[2]*minMax[2]) + 
                            (double)((entry.getValue().getY() - clusters.get(i).getY())*(entry.getValue().getY() - clusters.get(i).getY()))/(minMax[3]*minMax[3]);
                    proxdistance = Math.sqrt(proxdistance);
                    distance += (proxWeight*AlgoParameters.DIMENSIONS*AlgoParameters.SPREAD_FACTOR*AlgoParameters.SPREAD_FACTOR*proxdistance);
                    
                    if (distance < minDistance) {
                        idxOfAssignedCluster = i;
                        minDistance = distance;
                    }
                }

                clusters.get(idxOfAssignedCluster).addNeuron(entry.getValue());
            //}
        }

        return clusters;
    }

    private int[] getMaxMinFromMap(Map<String, LNode> map) {
        ArrayList<Integer> xVal = new ArrayList<Integer>();
        ArrayList<Integer> yVal = new ArrayList<Integer>();

        for (LNode g : map.values()) {
            xVal.add(g.getX());
            yVal.add(g.getY());
        }

        int[] minMax = new int[4];
        minMax[0] = Collections.min(xVal);
        minMax[1] = Collections.min(yVal);
        minMax[2] = Collections.max(xVal);
        minMax[3] = Collections.max(yVal);

        return minMax;
    }
    
    /*
    //check whether neuron clusters has converged
    //whether old == new
    private boolean CheckAllNeuronsInClusters(ArrayList<GCluster> clusters) {
    }*/

}
