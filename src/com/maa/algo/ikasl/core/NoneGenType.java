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
public class NoneGenType implements IKASLGenType {

    @Override
    public double[] generalize(Node hit, List<Node> neigh1, List<Node> neigh2,AlgoParameters algoParam) {
        double[] weights;
        
        weights = hit.getWeights();

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
