/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.enums.ExecFrequency;
import com.maa.models.BasicParamModel;
import com.maa.models.ParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BasicXMLWriter extends XMLWriter {

    @Override
    public void writeXML(ParamModel pModel, String location) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            // Create top-level Data element
            Element basicParamElement = doc.createElement("BasicParameters");
            doc.appendChild(basicParamElement);

            BasicParamModel bPModel;
            if (pModel instanceof BasicParamModel) {
                bPModel = (BasicParamModel) pModel;
            } else {
                return;
            }

            ExecFrequency execFreq = bPModel.getFreq();
            String execFreqString= execFreq.name();
            int numStreams = bPModel.getNumStreams();
            
            String streamIDs = ""; 
            for(String s : bPModel.getStreamIDs()){
                streamIDs=streamIDs+s+",";
            }

            basicParamElement.setAttribute(PreferenceNames.EXEC_FREQUENCY, execFreqString);
            basicParamElement.setAttribute(PreferenceNames.NUM_STREAMS, numStreams+"");
            basicParamElement.setAttribute(PreferenceNames.STREAM_IDS,streamIDs);


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //Enable indentation in the xml file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(location));
            transformer.transform(source, result);

        } catch (TransformerException ex) {
            
        } catch (ParserConfigurationException ex) {
            
        }
    }
}
