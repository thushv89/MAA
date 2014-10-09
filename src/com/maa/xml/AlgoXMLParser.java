/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.enums.AggregationType;
import com.maa.models.AlgoParamModel;
import com.maa.models.ParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Thush
 */
public class AlgoXMLParser extends XMLParser{

    @Override
    public ParamModel parseXML() {
        String loc = ImportantFileNames.CONFIG_DIRNAME+File.separator+ImportantFileNames.ALGO_CONFIG_FILENAME;
        
        File fXmlFile = new File(loc);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            try {
                doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
            } catch (SAXException ex) {
                
            } catch (IOException ex) {
                
            }
        } catch (ParserConfigurationException ex) {
            
        }

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(PreferenceNames.ALGO_ELEMENT_NAME);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                AggregationType aType = null;
                String aTypeStr = eElement.getAttribute(PreferenceNames.AGGREGATION_TYPE);
                if(aTypeStr.equals(AggregationType.FUZZY.name())){
                    aType = AggregationType.FUZZY;
                } else if(aTypeStr.equals(AggregationType.AVERAGE.name())){
                    aType = AggregationType.AVERAGE;
                } else if(aTypeStr.equals(AggregationType.NONE.name())){
                    aType = AggregationType.NONE;
                }
                
                String id = eElement.getAttribute(PreferenceNames.ID_TAG);
                int dimensions = Integer.parseInt(eElement.getAttribute(PreferenceNames.DIMENSIONS_NAME));
                double sf = Double.parseDouble(eElement.getAttribute(PreferenceNames.SPREAD_FACTOR));
                double lr = Double.parseDouble(eElement.getAttribute(PreferenceNames.LEARN_RATE));
                int nr = Integer.parseInt(eElement.getAttribute(PreferenceNames.NEIGH_RAD));
                int iter = Integer.parseInt(eElement.getAttribute(PreferenceNames.ITERATIONS));
                int ht = Integer.parseInt(eElement.getAttribute(PreferenceNames.HIT_THRESHOLD));
                
                return new AlgoParamModel(id,sf, nr, lr, iter, ht, aType,dimensions);
            }
        }
        return null;
    }
    
    public ArrayList<AlgoParamModel> parseMultiXML() {
        
        ArrayList<AlgoParamModel> aPModels = new ArrayList<AlgoParamModel>();
        
        String loc = ImportantFileNames.CONFIG_DIRNAME+File.separator+ImportantFileNames.ALGO_CONFIG_FILENAME;
        
        File fXmlFile = new File(loc);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            try {
                doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
            } catch (SAXException ex) {
                
            } catch (IOException ex) {
                
            }
        } catch (ParserConfigurationException ex) {
            
        }

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(PreferenceNames.STREAM_ELEMENT_NAME);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                AggregationType aType = null;
                String aTypeStr = eElement.getAttribute(PreferenceNames.AGGREGATION_TYPE);
                if(aTypeStr.equals(AggregationType.FUZZY.name())){
                    aType = AggregationType.FUZZY;
                } else if(aTypeStr.equals(AggregationType.AVERAGE.name())){
                    aType = AggregationType.AVERAGE;
                }
                
                String id = eElement.getAttribute(PreferenceNames.ID_TAG);
                int dimensions = Integer.parseInt(eElement.getAttribute(PreferenceNames.DIMENSIONS_NAME));
                double sf = Double.parseDouble(eElement.getAttribute(PreferenceNames.SPREAD_FACTOR));
                double lr = Double.parseDouble(eElement.getAttribute(PreferenceNames.LEARN_RATE));
                int nr = Integer.parseInt(eElement.getAttribute(PreferenceNames.NEIGH_RAD));
                int iter = Integer.parseInt(eElement.getAttribute(PreferenceNames.ITERATIONS));
                int ht = Integer.parseInt(eElement.getAttribute(PreferenceNames.HIT_THRESHOLD));
                
                aPModels.add(new AlgoParamModel(id,sf, nr, lr, iter, ht, aType,dimensions));
            }
        }
        return aPModels;
    }
}
