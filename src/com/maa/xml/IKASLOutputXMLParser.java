/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.algo.enums.NodeHitType;
import com.maa.enums.ExecFrequency;
import com.maa.models.DataParamModel;
import com.maa.models.ParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import com.maa.utils.Tokenizers;
import com.maa.vis.objects.ReducedNode;
import com.maa.vis.objects.VisGNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
public class IKASLOutputXMLParser {

    String tf;
    
    public ArrayList<ReducedNode> parseXML(String loc) {

        ArrayList<ReducedNode> allNodes = new ArrayList<>();
    
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
        
        NodeList nList2 = doc.getElementsByTagName(PreferenceNames.IKASL_OUTPUT_TAG);
        Node n = nList2.item(0);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element ele = (Element) n;
                tf = ele.getAttribute(PreferenceNames.TIME_FRAME_TAG);
        }
        
        NodeList nList = doc.getElementsByTagName(PreferenceNames.NODE_TAG);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String idStr = eElement.getAttribute(PreferenceNames.NODE_ID_TAG);
                String[] idTokens = idStr.split(Tokenizers.I_J_TOKENIZER);
                int[] id = new int[]{Integer.parseInt(idTokens[0]), Integer.parseInt(idTokens[1])};

                String pIDStr = eElement.getAttribute(PreferenceNames.NODE_PARENT_ID_TAG);
                
                String[] weightStr = eElement.getAttribute(PreferenceNames.WEIGHTS_TAG).split(Tokenizers.INPUT_TOKENIZER);
                double[] weights = new double[weightStr.length];
                for(int i=0;i<weightStr.length && !weightStr[i].isEmpty();i++){
                    weights[i] = Double.parseDouble(weightStr[i]);
                }

                String inputStr = eElement.getFirstChild().getNodeValue();
                ArrayList<String> inputs = new ArrayList<>(Arrays.asList(inputStr.split(Tokenizers.INPUT_TOKENIZER)));
                ReducedNode rn = new ReducedNode(id, pIDStr,weights,inputs);
                allNodes.add(rn);
                
            }
        }
        return allNodes;
    }
    
    
    public String getTimeFram(){
        return tf;
    }
}
