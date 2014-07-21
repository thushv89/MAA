/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.enums.ExecFrequency;
import com.maa.models.DataParamModel;
import com.maa.models.ParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import java.io.File;
import java.io.IOException;
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
public class DataXMLParser extends XMLParser{

    @Override
    public ParamModel parseXML() {
        String loc = ImportantFileNames.CONFIG_DIRNAME+File.separator+ImportantFileNames.DATA_CONFIG_FILENAME;
        
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
        NodeList nList = doc.getElementsByTagName(PreferenceNames.DATA_ELEMENT_NAME);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                
                ExecFrequency execFreq = null;
                String homeDir = eElement.getAttribute(PreferenceNames.HOME_DIR);
                
                return new DataParamModel(homeDir);
            }
        }
        return null;
    }
    
}
