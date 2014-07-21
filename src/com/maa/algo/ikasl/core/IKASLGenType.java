/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.core;

import com.maa.algo.objects.GNode;
import com.maa.algo.objects.Node;
import java.util.List;

/**
 *
 * @author Thush
 */
public interface IKASLGenType {
    public double[] generalize(Node hit, List<Node> neigh1, List<Node> neigh2);
}
