/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.pref;

/**
 *
 * @author Thush
 */
public abstract class Preference {
    private String name;

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
