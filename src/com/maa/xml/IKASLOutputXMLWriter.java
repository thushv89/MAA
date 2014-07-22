/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.algo.utils.Constants;
import com.maa.models.AlgoParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import java.io.File;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Thush
 */
public class IKASLOutputXMLWriter {

    public void writeXML(String loc,Map<String,String> results, String timeFrame) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            // Create top-level Data element
            Element algoParamElement = doc.createElement("IKASLOutput");
            algoParamElement.setAttribute(PreferenceNames.TIME_FRAME_TAG, timeFrame);
            doc.appendChild(algoParamElement);

            for(Map.Entry<String,String> e : results.entrySet()){
                String id = e.getKey().split(Constants.NODE_TOKENIZER)[0];
                String pID = e.getKey().split(Constants.NODE_TOKENIZER)[1];
                Element nodeElement = doc.createElement("Node");
                nodeElement.setAttribute("ID", id);
                nodeElement.setAttribute("ParentID", pID);
                nodeElement.appendChild(doc.createTextNode(e.getValue()));
                algoParamElement.appendChild(nodeElement);
            }

            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(loc));
            transformer.transform(source, result);

        } catch (TransformerException ex) {
        } catch (ParserConfigurationException ex) {
        }
    }
}
