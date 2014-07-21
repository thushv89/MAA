/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.pref;

import java.util.ArrayList;

/**
 *
 * @author Thush
 */
public class StringListPreference extends Preference{
    
    private ArrayList<String> value;
    
    public StringListPreference(String name, ArrayList<String> value){
        super.setName(name);
        this.value=value;
    }

    /**
     * @return the value
     */
    public ArrayList<String> getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(ArrayList<String> value) {
        this.value = value;
    }
}
