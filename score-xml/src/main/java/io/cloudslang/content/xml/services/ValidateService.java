package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class ValidateService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs){
        Map<String, String> result = new HashMap<>();

        try {
            String xmlDocument = commonInputs.getXmlDocument();
            String xsdDocument = customInputs.getXsdDocument();

            XmlUtils.parseXML(xmlDocument, commonInputs.getSecureProcessing());
            result.put(Constants.OutputNames.RETURN_RESULT, "Parsing successful.");

            if(xsdDocument != null && !xsdDocument.isEmpty()) {
                validateAgainstXsd(xmlDocument, customInputs.getXsdDocument());
                result.put(Constants.OutputNames.RETURN_RESULT, "XML is valid.");
            }

            result.put(Constants.OutputNames.RESULT_TEXT, Constants.SUCCESS);

        } catch (SAXParseException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "Parsing error: " + e.getMessage());
        } catch (SAXException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "Validation failed: " + e.getMessage());
        } catch (Exception e){
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        }

        return result;
    }

    private static void validateAgainstXsd(String xmlDocument, String xsdDocument) throws Exception{
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(xsdDocument)));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xmlDocument)));
    }
}
