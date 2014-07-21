/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.models.ParamModel;

/**
 *
 * @author Thush
 */
public abstract class XMLWriter {
    
    public abstract void writeXML(ParamModel pModel, String location);
}
