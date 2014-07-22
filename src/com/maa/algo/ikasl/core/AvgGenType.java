/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;


import com.maa.algo.objects.GNode;
import com.maa.algo.objects.Node;
import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.ArrayHelper;
import java.util.List;

/**
 *
 * @author Thush
 */
public class AvgGenType implements IKASLGenType {

    @Override
    public double[] generalize(Node hit, List<Node> neigh1, List<Node> neigh2,AlgoParameters algoParam) {
        int dimensions = algoParam.getDIMENSIONS();

        double[] weights;

        double[] totalNeigh1 = new double[dimensions];
        double[] totalNeigh2 = new double[dimensions];

        int countNeigh1 = 0;
        int countNeigh2 = 0;

        for (Node n1 : neigh1) {
            
                totalNeigh1 = ArrayHelper.add(totalNeigh1, n1.getWeights(), dimensions);
                countNeigh1++;
            
        }
        for (Node n2 : neigh2) {
            
                totalNeigh2 = ArrayHelper.add(totalNeigh2, n2.getWeights(), dimensions);
                countNeigh2++;
            
        }

        if(countNeigh1==0){countNeigh1=1;}
        if(countNeigh2==0){countNeigh2=1;}
        
        weights = ArrayHelper.multiplyArrayByConst(hit.getWeights(), 0.7);

        totalNeigh1 = ArrayHelper.multiplyArrayByConst(totalNeigh1, 1 / countNeigh1);
        totalNeigh1 = ArrayHelper.multiplyArrayByConst(totalNeigh1, 0.3);
        weights = ArrayHelper.add(weights, totalNeigh1, dimensions);

        for (double val : weights) {
            if (val > 1) {
                val = 1;
            } else if (val < 0) {
                val = 0;
            }
        }

        return weights;
    }
}
