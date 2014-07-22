/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class InputParser {

    private int dimensions;
    private ArrayList<double[]> weights;
    private ArrayList<String> strForWeights;
    private String timeFrame = null;

    public void parseInput(String fileName) {

        //clear the weights and strForWeights arraylists.
        //Otherwise data from previous learn cycle are present when getting data for current learn cycle
        weights = new ArrayList<double[]>();
        strForWeights = new ArrayList<String>();

        String tokenizer = ",";
        int tempDim = 0;
        boolean dimensionMismatch = false;
        boolean notNormalized = false;

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            File iFile = new File(fileName);
            BufferedReader input = new BufferedReader(new FileReader(iFile));
            try {
                String line = null; //not declared within while loop

                while ((line = input.readLine()) != null) {
                    String text = line;
                    if (text != null && text.length() > 0) {
                        text = text.toLowerCase().trim();
                        if (!text.contains(PreferenceNames.TIME_FRAME_TAG.trim().toLowerCase())) {
                            String[] tokens = text.split(tokenizer);

                            strForWeights.add(tokens[0]);
                            double[] weightArr = new double[tokens.length - 1];
                            for (int j = 1; j < tokens.length; j++) {
                                weightArr[j - 1] = Double.parseDouble(tokens[j]);
                                if (weightArr[j - 1] > 1 || weightArr[j - 1] < 0) {
                                    notNormalized = true;
                                }
                            }
                            weights.add(weightArr);

                            if (tempDim == 0) {
                                tempDim = weightArr.length;
                            }

                            if (weightArr.length != tempDim) {
                                dimensionMismatch = true;
                                break;
                            }
                        } else {
                            if (text.split(tokenizer) != null && text.split(tokenizer).length == 2) {
                                timeFrame = text.split(tokenizer)[1];
                            }
                        }
                    }
                }
            } catch (IOException ex) {
            }

            input.close();

            if (dimensionMismatch) {
            }

            if (notNormalized) {
            }

            dimensions = weights.get(0).length;

        } catch (IOException ex) {
        }
    }

    public ArrayList<double[]> getIWeights() {
        return weights;
    }

    public ArrayList<String> getINames() {
        return strForWeights;
    }
    
    public String getTimeFrame(){
        return timeFrame;
    }
}
