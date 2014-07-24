/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maa.xml;

import com.maa.models.AlgoParamModel;
import com.maa.models.ParamModel;
import com.maa.utils.ImportantFileNames;
import com.maa.utils.PreferenceNames;
import java.io.File;
import java.util.ArrayList;
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
public class AlgoXMLWriter extends XMLWriter {

    @Override
    public void writeXML(ParamModel pModel, String location) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            // Create top-level Data element
            Element algoParamElement = doc.createElement("AlgoParameters");
            doc.appendChild(algoParamElement);

            AlgoParamModel aPModel;
            if (pModel instanceof AlgoParamModel) {
                aPModel = (AlgoParamModel) pModel;
            } else {
                return;
            }

            double sf = aPModel.getSpreadFactor();
            int nr = aPModel.getNeighRad();
            double lr = aPModel.getLearningRate();
            int iter = aPModel.getIterations();
            int ht = aPModel.getHitThreshold();
            String aTypeStr = aPModel.getAggrType().name();

            algoParamElement.setAttribute(PreferenceNames.SPREAD_FACTOR, sf + "");
            algoParamElement.setAttribute(PreferenceNames.NEIGH_RAD, nr + "");
            algoParamElement.setAttribute(PreferenceNames.LEARN_RATE, lr + "");
            algoParamElement.setAttribute(PreferenceNames.ITERATIONS, iter + "");
            algoParamElement.setAttribute(PreferenceNames.HIT_THRESHOLD, ht + "");
            algoParamElement.setAttribute(PreferenceNames.AGGREGATION_TYPE, aTypeStr);

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

    public void writeMultipleXML(ArrayList<String> ids, ArrayList<AlgoParamModel> pModels, String location) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            // Create top-level Data element
            Element algoParamElement = doc.createElement(PreferenceNames.ALGO_ELEMENT_NAME);
            doc.appendChild(algoParamElement);

            for (int i = 0; i < ids.size(); i++) {
                Element streamElement = doc.createElement(PreferenceNames.STREAM_ELEMENT_NAME);
                streamElement.setAttribute(PreferenceNames.ID_TAG, ids.get(i));
                AlgoParamModel aPModel;
                
                aPModel = (AlgoParamModel) pModels.get(i);
                

                double sf = aPModel.getSpreadFactor();
                int nr = aPModel.getNeighRad();
                double lr = aPModel.getLearningRate();
                int iter = aPModel.getIterations();
                int ht = aPModel.getHitThreshold();
                int dim = aPModel.getDimensions();
                String aTypeStr = aPModel.getAggrType().name();

                streamElement.setAttribute(PreferenceNames.DIMENSIONS_NAME, dim + "");
                streamElement.setAttribute(PreferenceNames.SPREAD_FACTOR, sf + "");
                streamElement.setAttribute(PreferenceNames.NEIGH_RAD, nr + "");
                streamElement.setAttribute(PreferenceNames.LEARN_RATE, lr + "");
                streamElement.setAttribute(PreferenceNames.ITERATIONS, iter + "");
                streamElement.setAttribute(PreferenceNames.HIT_THRESHOLD, ht + "");
                streamElement.setAttribute(PreferenceNames.AGGREGATION_TYPE, aTypeStr);
                
                algoParamElement.appendChild(streamElement);
            }



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
