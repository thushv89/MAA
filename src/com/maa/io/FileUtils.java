/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.io;

import com.maa.models.DataParamModel;
import com.maa.utils.ImportantFileNames;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class FileUtils {

    public static void cleanConfigDir() {
        File configDir = new File(ImportantFileNames.CONFIG_DIRNAME);
        for (File f : configDir.listFiles()) {
            f.delete();
        }
    }

    public static boolean allConfigFilesExist() {
        String configHomeDir = ImportantFileNames.CONFIG_DIRNAME;

        File bParamFile = new File(configHomeDir + File.separator + ImportantFileNames.BASIC_CONFIG_FILENAME);
        File dParamFile = new File(configHomeDir + File.separator + ImportantFileNames.DATA_CONFIG_FILENAME);
        File aParamFile = new File(configHomeDir + File.separator + ImportantFileNames.ALGO_CONFIG_FILENAME);

        if (bParamFile.exists() && dParamFile.exists() && aParamFile.exists()) {
            return true;
        }

        return false;
    }

    public static void setUpConfigDir() {
        File configDir = new File(ImportantFileNames.CONFIG_DIRNAME);
        if (!configDir.exists()) {
            configDir.mkdir();
        }
    }

    public static void setUpDataDir(ArrayList<String> streams) {
        File dataDir = new File(ImportantFileNames.DATA_DIRNAME);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        for(String s : streams){
            File streamDir = new File(ImportantFileNames.DATA_DIRNAME + File.separator + s);
            if(!streamDir.exists()){
                streamDir.mkdir();
            }
        }
    }

    public static void createDirs(ArrayList<String> dirNames, String parent) {
        for (String s : dirNames) {
            File f = new File(parent + File.separator + s);
            if (!f.exists()) {
                f.mkdir();
            }
        }
    }
}
