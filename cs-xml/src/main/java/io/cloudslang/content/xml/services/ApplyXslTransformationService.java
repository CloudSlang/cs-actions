package io.cloudslang.content.xml.services;

import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.xml.entities.inputs.ApplyXslTransformationInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;


/**
 * Created by moldovas on 9/7/2016.
 */

public class ApplyXslTransformationService {
    public final Map<String, String> execute(final ApplyXslTransformationInputs applyXslTransformationInputs)
            throws Exception {
        final Templates template = getTemplate(applyXslTransformationInputs);
        final Transformer xmlTransformer = template.newTransformer();

        final Source source = getSourceStream(applyXslTransformationInputs);
        final Result result = getResultStream(applyXslTransformationInputs);

        xmlTransformer.transform(source, result);
        if (StringUtilities.isEmpty(applyXslTransformationInputs.getOutputFile())) {
            return OutputUtilities.getSuccessResultsMap(((StreamResult) result).getWriter().toString());
        }
        return OutputUtilities.getSuccessResultsMap("Result was written in the output file: " + applyXslTransformationInputs.getOutputFile());
    }

    private Result getResultStream(ApplyXslTransformationInputs applyXslTransformationInputs) throws FileNotFoundException {
        String outputFile = applyXslTransformationInputs.getOutputFile();
        if (StringUtilities.isEmpty(outputFile)) {
            StringWriter stringWriter = new StringWriter();
            return new StreamResult(stringWriter);
        }
        return new StreamResult(new FileOutputStream(outputFile));
    }

    private Source getSourceStream(ApplyXslTransformationInputs applyXslTransformationInputs) throws Exception {
        String xmlDocument = applyXslTransformationInputs.getXmlDocument();
        if (StringUtilities.isNotEmpty(xmlDocument)) {
            return readSource(xmlDocument, applyXslTransformationInputs.getParsingFeatures());
        } else {
            return new StreamSource();
        }
    }

    /**
     * Reads the xml content from a file, URL or string.
     * @param xmlDocument xml document as String, path or URL
     * @return the resulting xml after validation
     * @throws Exception in case something went wrong
     */
    private Source readSource(String xmlDocument, String features) throws Exception {
        if (xmlDocument.startsWith(Constants.Inputs.HTTP_PREFIX_STRING) || xmlDocument.startsWith(Constants.Inputs.HTTPS_PREFIX_STRING)) {
            URL xmlUrl = new URL(xmlDocument);
            InputStream xmlStream = xmlUrl.openStream();
            XmlUtils.parseXmlInputStream(xmlStream, features);
            return new StreamSource(xmlStream);
        } else {
            if (new File(xmlDocument).exists()) {
                XmlUtils.parseXmlFile(xmlDocument, features);
                return new StreamSource(new FileInputStream(xmlDocument));
            } else {
                XmlUtils.parseXmlString(xmlDocument, features);
                return new StreamSource(new StringReader(xmlDocument));
            }
        }
    }

    private Templates getTemplate(ApplyXslTransformationInputs applyXslTransformationInputs) throws Exception {
        final TransformerFactory factory = TransformerFactory.newInstance();
        return factory.newTemplates(readSource(applyXslTransformationInputs.getXslTemplate(), applyXslTransformationInputs.getParsingFeatures()));
    }
}
