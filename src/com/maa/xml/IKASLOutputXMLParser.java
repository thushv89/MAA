/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.algo.enums.VisGNodeType;
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

    ArrayList<ReducedNode> allNodes = new ArrayList<>();
    HashMap<String, String> nodeInputsMap = new HashMap<>();

    public ArrayList<ReducedNode> parseXML(String loc) {

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
        NodeList nList = doc.getElementsByTagName(PreferenceNames.NODE_TAG);

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String idStr = eElement.getAttribute(PreferenceNames.NODE_ID_TAG);
                String[] idTokens = idStr.split(Tokenizers.I_J_TOKENIZER);
                int[] id = new int[]{Integer.parseInt(idTokens[0]), Integer.parseInt(idTokens[1])};

                String pIDStr = eElement.getAttribute(PreferenceNames.NODE_PARENT_ID_TAG);

                String inputs = eElement.getNodeValue();

                ReducedNode rn = new ReducedNode(id, pIDStr);
                allNodes.add(rn);
                
                nodeInputsMap.put(idStr, inputs);
            }
        }
        return allNodes;
    }
    
    public HashMap<String,String> getNodeInputsMap(){
        return nodeInputsMap;
    }
}
