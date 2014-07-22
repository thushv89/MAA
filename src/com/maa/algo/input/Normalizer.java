package com.maa.algo.input;

import com.maa.algo.utils.AlgoParameters;
import com.maa.algo.utils.ArrayHelper;
import java.awt.Color;
import java.util.ArrayList;

public abstract class Normalizer {

    ArrayList<String> strForWeights;
    ArrayList<double[]> weights;
    AlgoParameters algoParams;
    
    public Normalizer(AlgoParameters algoParams) {
        this.algoParams = algoParams;
        strForWeights = new ArrayList<String>();
        weights = new ArrayList<double[]>();
    }


    public static ArrayList<double[]> normalizeWithBounds(ArrayList<double[]> inputs, double[] minBound, double[] maxBound,int dims) {

        for (int i = 0; i < dims; i++) {
            for (int j = 0; j < inputs.size(); j++) {
                double[] inArr = inputs.get(j);

                //do this if there's some value other than 0 is in column
                if (maxBound[i] - minBound[i] > 0) {
                    inArr[i] = (inArr[i] - minBound[i]) / (maxBound[i] - minBound[i]);
                    if (inArr[i] > 1) {
                        inArr[i] = 1;
                    } else if (inArr[i] < 0) {
                        inArr[i] = 0;
                    }
                    inputs.set(j, inArr);
                }
            }
        }
        return inputs;
    }

    protected boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    protected Color getColor(String color) {
        Color clr = null;

        if (color.equalsIgnoreCase("red")) {
            clr = Color.red;
        } else if (color.equalsIgnoreCase("green")) {
            clr = Color.green;
        } else if (color.equalsIgnoreCase("blue")) {
            clr = Color.blue;
        } else if (color.equalsIgnoreCase("black")) {
            clr = Color.black;
        } else if (color.equalsIgnoreCase("white")) {
            clr = Color.white;
        } else if (color.equalsIgnoreCase("orange")) {
            clr = Color.orange;
        } else if (color.equalsIgnoreCase("gold")) {
            clr = new Color(255, 215, 0);
        } else if (color.equalsIgnoreCase("brown")) {
            clr = new Color(165, 42, 42);
        }

        return clr;
    }

    public ArrayList<String> getStrForWeights() {
        return strForWeights;
    }

    public ArrayList<double[]> getWeights() {
        return weights;
    }

}
