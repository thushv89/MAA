package com.maa.algo.utils;

import com.maa.algo.enums.DistanceType;
import com.maa.algo.objects.GNode;
import java.util.Map;

import com.maa.algo.objects.LNode;
import com.maa.algo.objects.Node;
import java.util.ArrayList;
import java.util.Collections;

public class Utils {

    public static double[] generateRandomArray(int dimensions) {
        double[] arr = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            arr[i] = Math.random();
        }
        return arr;
    }

    public static String generateIndexString(int i, int j) {
        return i + Constants.I_J_TOKENIZER + j;
    }

    public static String generateNodeSequence(String prevSeq, int x, int y) {
        if (prevSeq == null || prevSeq.length() == 0) {
            return x + "," + y;
        } else {
            return prevSeq + ":" + x + "," + y;
        }
    }

    public static double getLearningRate(int iter, int nodeCount, int maxIter, double lr, double nr) {
        double minPhi = 0.95;
        //if 3.8 used for a node count < 4 learning rate becomes negative
        if (nodeCount < 4) {
            return lr * Math.exp(-(double) iter / maxIter) * (1 - minPhi);
        } else {
            return lr * Math.exp(-(double) iter / maxIter) * (1 - (3.8 / nodeCount));
        }
    }

    public static double getTimeConst(int maxIter, double nr) {
        return (double) maxIter / Math.log(nr);
    }

    //get the node with the minimal ED to the input (winner)
    public static LNode selectLWinner(Map<String, LNode> nodeMap, double[] input, int dim, double[] weights, DistanceType dType) {
        LNode winner = null;
        double currDist = Double.MAX_VALUE;
        double minDist = Double.MAX_VALUE;
        for (Map.Entry<String, LNode> entry : nodeMap.entrySet()) {
            currDist = Utils.calcDist(input, entry.getValue().getWeights(), dim, weights, dType);
            if (currDist < minDist) {
                winner = entry.getValue();
                minDist = currDist;
            }

        }
        return winner;
    }

    public static double[] getUnitVector(int dimensions) {
        double[] unitVec = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            unitVec[i] = 1;
        }
        return unitVec;
    }

    public static double[] getZeroVector(int dimensions) {
        double[] zeroVec = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            zeroVec[i] = 0;
        }
        return zeroVec;
    }

    public static double[] getUniformVector(double value, int dimensions) {
        double[] unifVec = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            unifVec[i] = value;
        }
        return unifVec;
    }

    public static GNode selectGWinner(Map<String, GNode> nodeMap, double[] input, int dim, double[] weights, DistanceType dType) {
        GNode winner = null;
        double currDist = Double.MAX_VALUE;
        double minDist = Double.MAX_VALUE;
        for (Map.Entry<String, GNode> entry : nodeMap.entrySet()) {
            currDist = Utils.calcDist(input, entry.getValue().getWeights(), dim, weights, dType);
            if (currDist < minDist) {
                winner = entry.getValue();
                minDist = currDist;
            }

        }
        return winner;
    }

    public static LNode adjustNeighbourWeight(LNode node, LNode winner, double[] input, double radius, double learningRate, int dim) {
        double nodeDistSqr = Math.pow(winner.getX() - node.getX(), 2) + Math.pow(winner.getY() - node.getY(), 2);
        double radiusSqr = radius * radius;

        //if node is within the radius
        if (nodeDistSqr < radiusSqr) {
            double influence = Math.exp(-(double) nodeDistSqr / (2 * radiusSqr));
            node.adjustWeights(input, influence, learningRate,dim);
        }
        return node;
    }

    public static double getRadius(int iter, double timeConst, int dim) {
        return dim * Math.exp(-(double) iter / timeConst);
    }

    public static double calcDist(double[] in1, double[] in2, int dimensions, double[] weights, DistanceType dType) {
        double dist = 0.0;
        if (dType == DistanceType.EUCLIDEAN) {
            for (int i = 0; i < dimensions; i++) {
                dist += (in1[i] - in2[i])*(in1[i] - in2[i]) * weights[i];
            }

            return Math.sqrt(dist);
        }else if (dType == DistanceType.MANHATTAN){
            for (int i = 0; i < dimensions; i++) {
                dist += Math.abs(in1[i] - in2[i]) * weights[i];
            }

            return dist;
        }else if(dType == DistanceType.FRACT_HALF){
            for (int i = 0; i < dimensions; i++) {
                dist += Math.sqrt(Math.abs(in1[i] - in2[i])) * weights[i];
            }

            return dist*dist;
        }
        return -Double.MAX_VALUE;
    }

    public static int[] getMinMaxMapCoord(Map<String, Node> map) {
        ArrayList<Integer> allX = new ArrayList<Integer>();
        ArrayList<Integer> allY = new ArrayList<Integer>();
        int[] result = new int[4];
        for (String s : map.keySet()) {
            String[] tokens = s.split(",");
            allX.add(Integer.parseInt(tokens[0]));
            allY.add(Integer.parseInt(tokens[1]));
        }

        result[0] = Collections.min(allX);
        result[1] = Collections.min(allY);
        result[2] = Collections.max(allX);
        result[3] = Collections.max(allY);

        return result;
    }
}
