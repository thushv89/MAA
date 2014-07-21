/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *
 * @author Thushan Ganegedara
 */
public class FileReader {
    
    public ArrayList<String> readLines(String fileName) {

        ArrayList<String> lines = new ArrayList<String>();
        
        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            File iFile = new File(fileName);
            BufferedReader input = new BufferedReader(new java.io.FileReader(iFile));
            try {
                String line = null; //not declared within while loop

                while ((line = input.readLine()) != null) {
                    String text = line;
                    if (text != null && text.length() > 0) {
                        lines.add(text);
                    }
                }
            } catch (IOException ex) {

            }

            input.close();

        } catch (IOException ex) {
            
        }
        
        return lines;
    }
}
