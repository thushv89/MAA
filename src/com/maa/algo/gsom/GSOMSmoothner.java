package com.maa.algo.gsom;


import java.util.ArrayList;
import java.util.Map;
import com.maa.algo.listeners.TaskListener;
import com.maa.algo.objects.LNode;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.LogMessages;
import com.maa.algo.utils.Utils;

public class GSOMSmoothner {


    private TaskListener tListener;
    
    public GSOMSmoothner() {
        
        //GSOMConstants.MAX_NEIGHBORHOOD_RADIUS = GSOMConstants.MAX_NEIGHBORHOOD_RADIUS/2;
    }

    public Map<String, LNode> smoothGSOM(Map<String, LNode> map, ArrayList<double[]> inputs) {
        double origStartLR = AlgoParameters.START_LEARNING_RATE;
        double origNR = AlgoParameters.MAX_NEIGHBORHOOD_RADIUS;
        
        if(AlgoParameters.START_LEARNING_RATE>0){
            AlgoParameters.START_LEARNING_RATE = (Math.log10(AlgoParameters.START_LEARNING_RATE)/5)+0.25;
        }
        
        if(AlgoParameters.MAX_NEIGHBORHOOD_RADIUS>0){
            AlgoParameters.MAX_NEIGHBORHOOD_RADIUS = (Math.log(AlgoParameters.MAX_NEIGHBORHOOD_RADIUS)*2)+1;
        }
        
        for (int iter = 0; iter < AlgoParameters.MAX_ITERATIONS; iter++) {
            double learningRate = Utils.getLearningRate(iter, map.size());
            double radius = Utils.getRadius(iter, Utils.getTimeConst());
            for (double[] singleInput : inputs) {
                if (singleInput.length == AlgoParameters.DIMENSIONS) {
                    smoothSingleIterSingleInput(map, iter, singleInput, learningRate, radius);
                } else {
                    tListener.logMessage(LogMessages.DIM_MISMATCH);
                }
            }
        }
        
        //set original Start Learning Rate and Neighborhood Radius values
        AlgoParameters.START_LEARNING_RATE = origStartLR;
        AlgoParameters.MAX_NEIGHBORHOOD_RADIUS = origNR;
        return map;
    }

    private void smoothSingleIterSingleInput(Map<String, LNode> map, int iter, double[] input, double learningRate, double radius) {
        LNode winner = Utils.selectLWinner(map, input);

        String leftNode = Utils.generateIndexString(winner.getX() - 1, winner.getY());
        String rightNode = Utils.generateIndexString(winner.getX() + 1, winner.getY());
        String topNode = Utils.generateIndexString(winner.getX(), winner.getY() + 1);
        String bottomNode = Utils.generateIndexString(winner.getX(), winner.getY() - 1);

        if (map.containsKey(leftNode)) {
            map.put(leftNode, Utils.adjustNeighbourWeight(map.get(leftNode), winner, input, radius, learningRate));
        } else if (map.containsKey(rightNode)) {
            map.put(rightNode, Utils.adjustNeighbourWeight(map.get(rightNode), winner, input, radius, learningRate));
        } else if (map.containsKey(topNode)) {
            map.put(topNode, Utils.adjustNeighbourWeight(map.get(topNode), winner, input, radius, learningRate));
        } else if (map.containsKey(bottomNode)) {
            map.put(bottomNode, Utils.adjustNeighbourWeight(map.get(bottomNode), winner, input, radius, learningRate));
        }
    }
}
