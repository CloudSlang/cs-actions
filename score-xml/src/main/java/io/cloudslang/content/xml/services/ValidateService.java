package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class ValidateService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) {
        Map<String, String> result = new HashMap<>();

        try {
            String xmlDocument;
            String xsdDocument;
            if (Constants.XML_PATH.equalsIgnoreCase(commonInputs.getXmlDocumentSource())) {
                Document doc = XmlUtils.createDocumentFromFile(commonInputs.getXmlDocument(), commonInputs.getSecureProcessing());
                xmlDocument = getStringValueOfDocument(doc);
            } else if (Constants.XML_URL.equalsIgnoreCase(commonInputs.getXmlDocumentSource())) {
                xmlDocument = XmlUtils.createXmlDocumentFromUrl(commonInputs);
                xmlDocument = xmlDocument.replaceAll("<!DOCTYPE[^>]*>\n", "");
            } else {
                xmlDocument = commonInputs.getXmlDocument();
            }

            if (Constants.XSD_PATH.equalsIgnoreCase(customInputs.getXsdDocumentSource())) {
                Document doc = XmlUtils.createDocumentFromFile(customInputs.getXsdDocument(), commonInputs.getSecureProcessing());
                xsdDocument = getStringValueOfDocument(doc);
            } else {
                xsdDocument = customInputs.getXsdDocument();
            }
            XmlUtils.parseXML(xmlDocument, commonInputs.getSecureProcessing());
            result.put(Constants.OutputNames.RETURN_RESULT, Constants.SuccessMessages.PARSING_SUCCESS);

            if (xsdDocument != null && StringUtils.isNotBlank(xsdDocument)) {
                validateAgainstXsd(xmlDocument, xsdDocument);
                result.put(Constants.OutputNames.RETURN_RESULT, Constants.SuccessMessages.VALIDATION_SUCCESS);
            }

            result.put(Constants.OutputNames.RESULT_TEXT, Constants.SUCCESS);

        } catch (SAXParseException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, Constants.ErrorMessages.PARSING_ERROR + e.getMessage());
        } catch (SAXException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, Constants.ErrorMessages.VALIDATION_FAILURE + e.getMessage());
        } catch (Exception e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        }

        return result;
    }

    private static String getStringValueOfDocument(Document doc) throws TransformerException {
        String xmlDocument;
        StringWriter writer = XmlUtils.getStringWriter(doc);
        xmlDocument = writer.toString();
        return xmlDocument;
    }

//    private String readFromFile(String path) throws IOException {
//        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append(System.lineSeparator());
//                line = br.readLine();
//            }
//            return sb.toString();
//        }
//    }

    private static void validateAgainstXsd(String xmlDocument, String xsdDocument) throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(xsdDocument)));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xmlDocument)));
    }
}
