/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.ui;

/**
 *
 * @author Thush
 */
public class Line {
    
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    public Line(int sX, int sY, int eX, int eY){
        startX = sX;
        startY = sY;
        endX = eX;
        endY = eY;
    }

    /**
     * @return the startX
     */
    public int getStartX() {
        return startX;
    }

    /**
     * @param startX the startX to set
     */
    public void setStartX(int startX) {
        this.startX = startX;
    }

    /**
     * @return the startY
     */
    public int getStartY() {
        return startY;
    }

    /**
     * @param startY the startY to set
     */
    public void setStartY(int startY) {
        this.startY = startY;
    }

    /**
     * @return the endX
     */
    public int getEndX() {
        return endX;
    }

    /**
     * @param endX the endX to set
     */
    public void setEndX(int endX) {
        this.endX = endX;
    }

    /**
     * @return the endY
     */
    public int getEndY() {
        return endY;
    }

    /**
     * @param endY the endY to set
     */
    public void setEndY(int endY) {
        this.endY = endY;
    }
    
    
}
