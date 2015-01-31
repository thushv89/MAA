/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.ikasl.links;

import java.io.Serializable;

/**
 *
 * @author Thush
 */
public class GlobalLink implements Serializable{

    private String link;
    private boolean isMergedLeftOver;

    public GlobalLink(String link, boolean ismleftover) {
        this.link = link;
        this.isMergedLeftOver = ismleftover;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the isMergedLeftOver
     */
    public boolean isIsMergedLeftOver() {
        return isMergedLeftOver;
    }

    /**
     * @param isMergedLeftOver the isMergedLeftOver to set
     */
    public void setIsMergedLeftOver(boolean isMergedLeftOver) {
        this.isMergedLeftOver = isMergedLeftOver;
    }
}
