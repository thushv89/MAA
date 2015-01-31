package com.maa.algo.gsom;


import java.util.ArrayList;
import java.util.Map;
import com.maa.algo.listeners.TaskListener;
import com.maa.algo.objects.LNode;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.LogMessages;
import com.maa.algo.utils.AlgoUtils;

public class GSOMSmoothner {

    AlgoParameters algoParams;
    public GSOMSmoothner(AlgoParameters algoParams) {
        this.algoParams = algoParams;
        //GSOMConstants.MAX_NEIGHBORHOOD_RADIUS = GSOMConstants.MAX_NEIGHBORHOOD_RADIUS/2;
    }

    public Map<String, LNode> smoothGSOM(Map<String, LNode> map, ArrayList<double[]> inputs) {
        double origStartLR = algoParams.getSTART_LEARNING_RATE();
        double origNR = algoParams.getMAX_NEIGHBORHOOD_RADIUS();
        
        if(algoParams.getSTART_LEARNING_RATE()>0){
            algoParams.setSTART_LEARNING_RATE((Math.log10(algoParams.getSTART_LEARNING_RATE())/5)+0.25);
        }
        
        if(algoParams.getMAX_NEIGHBORHOOD_RADIUS()>0){
            algoParams.setMAX_NEIGHBORHOOD_RADIUS((Math.log(algoParams.getMAX_NEIGHBORHOOD_RADIUS())*2)+1);
        }
        
        for (int iter = 0; iter < algoParams.getMAX_ITERATIONS(); iter++) {
            double learningRate = AlgoUtils.getLearningRate(iter, map.size(),algoParams.getMAX_ITERATIONS(),algoParams.getSTART_LEARNING_RATE(),algoParams.getMAX_NEIGHBORHOOD_RADIUS());
            double radius = AlgoUtils.getRadius(iter, AlgoUtils.getTimeConst(algoParams.getMAX_ITERATIONS(),algoParams.getMAX_NEIGHBORHOOD_RADIUS()),algoParams.getDIMENSIONS());
            for (double[] singleInput : inputs) {
                if (singleInput.length == algoParams.getDIMENSIONS()) {
                    smoothSingleIterSingleInput(map, iter, singleInput, learningRate, radius);
                } else {
                    //dimension mismatch
                }
            }
        }
        
        //set original Start Learning Rate and Neighborhood Radius values
        algoParams.setSTART_LEARNING_RATE(origStartLR);
        algoParams.setMAX_NEIGHBORHOOD_RADIUS(origNR);
        return map;
    }

    private void smoothSingleIterSingleInput(Map<String, LNode> map, int iter, double[] input, double learningRate, double radius) {
        LNode winner = AlgoUtils.selectLWinner(map, input,algoParams.getDIMENSIONS(),algoParams.getATTR_WEIGHTS(),algoParams.getDistType());

        String leftNode = AlgoUtils.generateIndexString(winner.getX() - 1, winner.getY());
        String rightNode = AlgoUtils.generateIndexString(winner.getX() + 1, winner.getY());
        String topNode = AlgoUtils.generateIndexString(winner.getX(), winner.getY() + 1);
        String bottomNode = AlgoUtils.generateIndexString(winner.getX(), winner.getY() - 1);

        if (map.containsKey(leftNode)) {
            map.put(leftNode, AlgoUtils.adjustNeighbourWeight(map.get(leftNode), winner, input, radius, learningRate, algoParams.getDIMENSIONS(),
                    true, algoParams.getRANGE_GRANULARITY(), algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(),algoParams.getTHRESH_MAX_BOUND()));
        } else if (map.containsKey(rightNode)) {
            map.put(rightNode, AlgoUtils.adjustNeighbourWeight(map.get(rightNode), winner, input, radius, learningRate, algoParams.getDIMENSIONS(),
                    true, algoParams.getRANGE_GRANULARITY(), algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(),algoParams.getTHRESH_MAX_BOUND()));
        } else if (map.containsKey(topNode)) {
            map.put(topNode, AlgoUtils.adjustNeighbourWeight(map.get(topNode), winner, input, radius, learningRate, algoParams.getDIMENSIONS(),
                    true, algoParams.getRANGE_GRANULARITY(), algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(),algoParams.getTHRESH_MAX_BOUND()));
        } else if (map.containsKey(bottomNode)) {
            map.put(bottomNode, AlgoUtils.adjustNeighbourWeight(map.get(bottomNode), winner, input, radius, learningRate, algoParams.getDIMENSIONS(),
                    true, algoParams.getRANGE_GRANULARITY(), algoParams.getMIN_BOUNDS(), algoParams.getMAX_BOUNDS(),algoParams.getTHRESH_MAX_BOUND()));
        }
    }
}
