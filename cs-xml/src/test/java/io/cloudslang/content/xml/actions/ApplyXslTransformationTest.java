/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by moldovas on 09/21/2016.
 */
public class ApplyXslTransformationTest {
    private ApplyXslTransformation applyXslTransformation;
    private String xml;
    private String xsl;

    @Before
    public void setUp() throws Exception {
        applyXslTransformation = new ApplyXslTransformation();
        URI xmlResource = getClass().getResource("/applyxslres/testTransform.xml").toURI();
        xml = FileUtils.readFileToString(new File(xmlResource));
        URI xslResource = getClass().getResource("/applyxslres/xslTemplate.xsl").toURI();
        xsl = FileUtils.readFileToString(new File(xslResource));
    }

    @After
    public void tearDown() {
        applyXslTransformation = null;
        xml = null;
    }

    @Test
    public void applyXslTransformationSuccess() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<map>\n" +
                        "    <entry key=\"key1\" value=\"value1\" />\n" +
                        "    <entry key=\"key2\" />\n" +
                        "</map>",
                "<?xml version=\"1.0\"?>\n" +
                        "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" +
                        "  <xsl:output method=\"html\" indent=\"yes\"/>\n" +
                        "\n" +
                        "<xsl:template match=\"map\">\n" +
                        "<HTML>\n" +
                        "<HEAD>\n" +
                        "<TITLE>Map</TITLE>\n" +
                        "</HEAD>\n" +
                        "<BODY>\n" +
                        "    <xsl:apply-templates/>\n" +
                        "</BODY>\n" +
                        "</HTML>\n" +
                        "</xsl:template>\n" +
                        "\n" +
                        "\n" +
                        "<xsl:template match=\"entry\">\n" +
                        "    <xsl:value-of select=\"@key\"/>=<xsl:value-of select=\"@value\"/>\n" +
                        "    <br></br>\n" +
                        "</xsl:template>\n" +
                        "\n" +
                        "\n" +
                        "</xsl:stylesheet>",
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(result.get(RETURN_CODE), SUCCESS);
        assertEquals(result.get(RETURN_RESULT), "<HTML>\n" +
                "<HEAD>\n" +
                "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<TITLE>Map</TITLE>\n" +
                "</HEAD>\n" +
                "<BODY>\n" +
                "    key1=value1<br>\n" +
                "    key2=<br>\n" +
                "\n" +
                "</BODY>\n" +
                "</HTML>\n");
    }

    @Test
    public void applyXslTransformationXmlFile() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                xml,
                xsl,
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));

    }

    @Test
    public void applyXslTransformationXmlStringAndXslFile() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<map>\n" +
                        "    <entry key=\"key1\" value=\"value1\" />\n" +
                        "    <entry key=\"key2\" />\n" +
                        "</map>",
                xsl,
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
    }

    @Test
    public void applyXslTransformationXmlFileAndXslString() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                xml,
                "<?xml version=\"1.0\"?>\n" +
                        "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n" +
                        "    <xsl:output method=\"html\" indent=\"yes\"/>\n" +
                        "\n" +
                        "    <xsl:template match=\"map\">\n" +
                        "        <HTML>\n" +
                        "            <HEAD>\n" +
                        "                <TITLE>Map</TITLE>\n" +
                        "            </HEAD>\n" +
                        "            <BODY>\n" +
                        "                <xsl:apply-templates/>\n" +
                        "            </BODY>\n" +
                        "        </HTML>\n" +
                        "    </xsl:template>\n" +
                        "\n" +
                        "\n" +
                        "    <xsl:template match=\"entry\">\n" +
                        "        <xsl:value-of select=\"@key\"/>=<xsl:value-of select=\"@value\"/>\n" +
                        "        <br></br>\n" +
                        "    </xsl:template>\n" +
                        "\n" +
                        "\n" +
                        "</xsl:stylesheet>",
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
    }

    @Test
    public void applyXslTransformationAllFileFieldsWithResult() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                xml,
                xsl,
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(RETURN_RESULT), "<HTML>\n" +
                "<HEAD>\n" +
                "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<TITLE>Map</TITLE>\n" +
                "</HEAD>\n" +
                "<BODY>\n" +
                "    key1=value1<br>\n" +
                "    key2=<br>\n" +
                "\n" +
                "</BODY>\n" +
                "</HTML>\n");
    }

    @Test
    public void applyXslTransformationInvalidXml() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                "<?xml version=\"1.0\"?>\n" +
                        "<catalog>\n" +
                        "   <book id=\"bk101\">\n" +
                        "      <author>Gambardella, Matthew</author>\n" +
                        "      <title>XML Developer's Guide</title>\n" +
                        "      <genre>Computer</genre>\n" +
                        "      <price>44.95</price>\n" +
                        "      <publish_date>2000-10-01</publish_date>\n" +
                        "      <description>An in-depth look at creating applications \n" +
                        "      with XML.</description>\n" +
                        "   </book>",
                xsl,
                "",
                ""
        );
        assertNotNull(result);
        assertNotNull(result.get(RETURN_RESULT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(result.get(RETURN_RESULT), "XML document structures must start and end within the same entity.");
    }
}
