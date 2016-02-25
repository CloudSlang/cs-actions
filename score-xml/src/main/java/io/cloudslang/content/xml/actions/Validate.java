package io.cloudslang.content.xml.actions;

import java.io.*;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.utils.Constants;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by markowis on 18/02/2016.
 */
public class Validate {

    @Action(name = "Validate",
            outputs = {
                @Output(Constants.OutputNames.RESULT_TEXT),
                @Output(Constants.OutputNames.RETURN_RESULT)},
            responses = {
                @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.InputNames.XSD_DOCUMENT, required = true) String xsdDocument) {

            Map<String, String> result = new HashMap<>();

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.parse(new InputSource(new StringReader(xmlDocument)));

                result.put(Constants.OutputNames.RETURN_RESULT, "Parsing successful.");

                if(xsdDocument != null) {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(xsdDocument)));
                    Validator validator = schema.newValidator();
                    validator.validate(new StreamSource(new StringReader(xmlDocument)));

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
}
