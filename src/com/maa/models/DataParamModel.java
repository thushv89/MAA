/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.models;

/**
 *
 * @author Thush
 */
public class DataParamModel extends ParamModel{
    
    private String homeDir;
    
    public DataParamModel(String homeDir){
        this.homeDir=homeDir;
    }

    /**
     * @return the homeDir
     */
    public String getHomeDir() {
        return homeDir;
    }

    /**
     * @param homeDir the homeDir to set
     */
    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }
    
}
