/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.pref;

/**
 *
 * @author Thush
 */
public class IntPreference extends Preference{
    private int value;
    
    public IntPreference(String name, int value){
        super.setName(name);
        this.value=value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
}
