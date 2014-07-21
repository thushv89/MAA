/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.pref;

/**
 *
 * @author Thush
 */
public class StringPreference extends Preference{
    private String value;
    
    public StringPreference(String name, String value){
        super.setName(name);
        this.value=value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
}
