/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.models;

import com.maa.pref.Preference;
import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class ParamModel {
    
    private ArrayList<Preference> prefs;
    
    public ParamModel(){
        prefs = new ArrayList<>();
    }

    /**
     * @return the prefs
     */
    public ArrayList<Preference> getPrefs() {
        return prefs;
    }

    /**
     * @param prefs the prefs to set
     */
    public void setPrefs(ArrayList<Preference> prefs) {
        this.prefs = prefs;
    }
    
    
}
