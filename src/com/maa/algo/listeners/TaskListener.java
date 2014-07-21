/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.listeners;

/**
 *
 * @author Thushan Ganegedara
 */
public interface TaskListener {
    
    public void logMessage(String msg);
    public void updateStatus(String msg);
    public void outputMsgBox(String msg);
}
