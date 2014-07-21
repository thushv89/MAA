/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.pref;

/**
 *
 * @author Thush
 */
public class DoublePreference extends Preference {
    private double value;
    
    public DoublePreference(String name, double value){
        super.setName(name);
        this.value=value;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }
}
