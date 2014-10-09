/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.algo.objects;

import com.maa.algo.enums.Behavior;
import com.maa.algo.utils.Utils;
import java.io.Serializable;
import org.omg.CORBA.TRANSIENT;

/**
 *
 * @author Thush
 */
public class GNode extends Node implements Serializable{
    private int lc;
    private int id;
    //prevHitVal is a value used to keep the number of hits got by feeding the previous input seq
    //e.g. For a node in G2 we feed I2
    private int prevHitVal; 
    
    private String parentID;
    
    
    public GNode(){        
    }
       
    public GNode(int lc, int id,double[] weights,String parentID){
        super(weights);
        this.lc = lc;
        this.id = id;
        this.parentID = parentID;
    }

    public Behavior getBehaviorType(double[] weights){
        return null;
    }
    
    /**
     * @return the lc
     */
    public int getLc() {
        return lc;
    }

    /**
     * @param lc the lc to set
     */
    public void setLc(int lc) {
        this.lc = lc;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the prevHitVal
     */
    public int getPrevHitVal() {
        return prevHitVal;
    }

    /**
     * @param prevHitVal the prevHitVal to set
     */
    public void setPrevHitVal(int prevHitVal) {
        this.prevHitVal = prevHitVal;
    }
    
    public void increasePrevHitVal(){
        this.prevHitVal++;
    }

    /**
     * @return the parentID
     */
    public String getParentID() {
        return parentID;
    }

    /**
     * @param parentID the parentID to set
     */
    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
 
    @Override
    public boolean equals(Object o){
        if(Utils.generateIndexString(((GNode)o).getLc(),((GNode)o).getId()).equals(
                Utils.generateIndexString(lc, id))){
            return true;
        }
        return false;
    }
}
