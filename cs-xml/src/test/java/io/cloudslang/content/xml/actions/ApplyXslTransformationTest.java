/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.xml.actions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Created by moldovas on 09/21/2016.
 */
public class ApplyXslTransformationTest {
    private ApplyXslTransformation applyXslTransformation;
    private String xml;
    private String xsl;
    private String invalidXml;
    private String resultHtml;

    @Before
    public void setUp() throws Exception {
        applyXslTransformation = new ApplyXslTransformation();
        xml = join(readLines(ClassLoader.getSystemResourceAsStream("applyxslres/testTransform.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        xsl = join(readLines(ClassLoader.getSystemResourceAsStream("applyxslres/xslTemplate.xsl"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        invalidXml = join(readLines(ClassLoader.getSystemResourceAsStream("applyxslres/invalid.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        resultHtml = join(readLines(ClassLoader.getSystemResourceAsStream("applyxslres/result.html"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
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
        assertEquals(result.get(RETURN_RESULT), resultHtml);
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
        assertEquals(result.get(RETURN_RESULT), resultHtml);
    }

    @Test
    public void applyXslTransformationInvalidXml() {
        Map<String, String> result = applyXslTransformation.applyXslTransformation(
                invalidXml,
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
