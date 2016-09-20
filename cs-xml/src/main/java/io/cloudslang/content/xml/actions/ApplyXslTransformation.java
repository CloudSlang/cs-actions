package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.xml.entities.inputs.ApplyXslTransformationInputs;
import io.cloudslang.content.xml.services.ApplyXslTransformationService;
import io.cloudslang.content.xml.utils.Constants;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;

/**
 * Created by moldovas on 9/7/2016.
 */
public class ApplyXslTransformation {
    /**
     *
     * @param xmlDocument The location of the XML document to transform. Can be a local file path, an HTTP URL,
     *                    or the actual xml to transform, as constant. This is optional as some stylesheets do not need
     *                    an XML document and can create output based on runtime parameters.
     * @param xslTemplate The location of the XSL stylesheet to use. Can be a local file path,
     *                    an HTTP URL or the actual template as constant.
     * @param outputFile The local file to write the output of the transformation. If an output file is not specified
     *                   the output of the transformation will be returned as returnResult.
     * @param parsingFeatures The list of XML parsing features separated by new line (CRLF).
     *                        The feature name - value must be separated by empty space.
     *                        Setting specific features this field could be used to avoid XML security issues like
     *                        "XML Entity Expansion injection" and "XML External Entity injection".
     *                        To avoid aforementioned security issues we strongly recommend to set this input to the following values:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     *                        When the "http://apache.org/xml/features/disallow-doctype-decl" feature is set to "true"
     *                        the parser will throw a FATAL ERROR if the incoming document contains a DOCTYPE declaration.
     *                        When the "http://xml.org/sax/features/external-general-entities" feature is set to "false"
     *                        the parser will not include external general entities.
     *                        When the "http://xml.org/sax/features/external-parameter-entities" feature is set to "false"
     *                        the parser will not include external parameter entities or the external DTD subset.
     *                        If any of the validations fails, the operation will fail with an error message describing the problem.
     *                        Default value:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     * @return The output of the transformation, if no output file is specified.
     */
    @Action(name = "Apply XSL Transformation",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> applyXslTransformation(
            @Param(value = Constants.Inputs.XML_DOCUMENT) String xmlDocument,
            @Param(value = Constants.Inputs.XSL_TEMPLATE, required = true) String xslTemplate,
            @Param(value = Constants.Inputs.OUTPUT_FILE) String outputFile,
            @Param(value = Constants.Inputs.FEATURES) String parsingFeatures) {

        try {
            final ApplyXslTransformationInputs applyXslTransformationInputs = new ApplyXslTransformationInputs.ApplyXslTransformationBuilder()
                    .withXmlDocument(xmlDocument)
                    .withXslTemplate(xslTemplate)
                    .withOutputFile(outputFile)
                    .withParsingFeatures(parsingFeatures)
                    .build();

            return new ApplyXslTransformationService().execute(applyXslTransformationInputs);
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e);
        }
    }

    public static void main(String[] args) {
        ApplyXslTransformation action = new ApplyXslTransformation();
        Map<String, String> resultMap = action.applyXslTransformation(
                "",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
                        "    <xsl:param name=\"firstName\">OO</xsl:param>\n" +
                        "    <xsl:param name=\"lastName\">User</xsl:param>\n" +
                        "    <xsl:param name=\"color\">#000000</xsl:param>\n" +
                        "    <xsl:template match=\"/\">\n" +
                        "        <html>\n" +
                        "            <body>\n" +
                        "                <h2>\n" +
                        "                    <font color=\"{$color}\">\n" +
                        "                    Hello <xsl:value-of select=\"$firstName\"/><xsl:text> </xsl:text><xsl:value-of select=\"$lastName\"/>!\n" +
                        "                    </font>\n" +
                        "                </h2>\n" +
                        "            </body>\t\t\n" +
                        "        </html>\n" +
                        "    </xsl:template>\n" +
                        "</xsl:stylesheet>",
//                "c:\\Users\\moldovas\\output.html"
                "",
                ""
        );

        for (Map.Entry entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
