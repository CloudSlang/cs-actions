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

import io.cloudslang.content.constants.ReturnCodes;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_PREFIXES;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_URIS;
import static org.junit.Assert.assertEquals;

/**
 * Created by ursan on 8/4/2016.
 */
public class ConvertXmlToJsonTest {
    private final static String XML =
            "<root xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:ui=\"urn:x-hp:2012:software:eve:uibinding\" id=\"Page1\">\n" +
                    "<ui:position><x>1</x><y>2</y></ui:position>\n" +
                    "<f:properties>\n" +
                    "<f:property><key1>value1</key1></f:property>\n" +
                    "<f:property><key2>value2</key2></f:property>\n" +
                    "</f:properties>\n" +
                    "<details>\n" +
                    "<item id=\"1\"><type>size</type><height>10</height><width>10</width></item>\n" +
                    "<item id=\"2\"><type>color</type><name>blue</name></item>\n" +
                    "</details>\n" +
                    "</root>\n";
    private final static String JSON = "{\n" +
            "  \"root\": {\n" +
            "    \"@id\": \"Page1\",\n" +
            "    \"ui:position\": {\n" +
            "      \"x\": \"1\",\n" +
            "      \"y\": \"2\"\n" +
            "    },\n" +
            "    \"f:properties\": {\n" +
            "      \"f:property\": [\n" +
            "        {\n" +
            "          \"key1\": \"value1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"key2\": \"value2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"details\": {\n" +
            "      \"item\": [\n" +
            "        {\n" +
            "          \"@id\": \"1\",\n" +
            "          \"type\": \"size\",\n" +
            "          \"height\": \"10\",\n" +
            "          \"width\": \"10\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"@id\": \"2\",\n" +
            "          \"type\": \"color\",\n" +
            "          \"name\": \"blue\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
    private final static String JSON_NO_PRETTY_NO_ROOT_NO_ATTRIBUTES =
            "{\"ui:position\":{\"x\":\"1\",\"y\":\"2\"},\"f:properties\":{\"f:property\":[{\"key1\":\"value1\"},{\"key2\":\"value2\"}]},\"details\":{\"item\":[{\"type\":\"size\",\"height\":\"10\",\"width\":\"10\"},{\"type\":\"color\",\"name\":\"blue\"}]}}";
    private final static String XML_WITH_TEXT = "<root xmlns:f=\"http://java.sun.com/jsf/core\" xmlns:ui=\"urn:x-hp:2012:software:eve:uibinding\" id=\"Page1\">\n" +
            "<td id=\"1\">Apples</td>\n" +
            "<ui:position><x>1</x><y>2</y><td id=\"1\">Apples</td></ui:position>\n" +
            "<f:properties>\n" +
            "<f:property><key1>value1</key1></f:property>\n" +
            "<f:property><key2>value2</key2></f:property>\n" +
            "</f:properties>\n" +
            "<details>\n" +
            "<item id=\"1\"><type>size</type><height>10</height><width>10</width></item>\n" +
            "<item id=\"2\"><type>color</type><name>blue</name></item>\n" +
            "</details>\n" +
            "<character>here are some special characters!@#$%^*(\")_+:.,?/'\\|}{~`</character>\n" +
            "</root>";
    private final static String JSON_WITH_TEXT = "{\n" +
            "  \"root\": {\n" +
            "    \"@id\": \"Page1\",\n" +
            "    \"td\": {\n" +
            "      \"@id\": \"1\",\n" +
            "      \"_text\": \"Apples\"\n" +
            "    },\n" +
            "    \"ui:position\": {\n" +
            "      \"x\": \"1\",\n" +
            "      \"y\": \"2\",\n" +
            "      \"td\": {\n" +
            "        \"@id\": \"1\",\n" +
            "        \"_text\": \"Apples\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"f:properties\": {\n" +
            "      \"f:property\": [\n" +
            "        {\n" +
            "          \"key1\": \"value1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"key2\": \"value2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"details\": {\n" +
            "      \"item\": [\n" +
            "        {\n" +
            "          \"@id\": \"1\",\n" +
            "          \"type\": \"size\",\n" +
            "          \"height\": \"10\",\n" +
            "          \"width\": \"10\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"@id\": \"2\",\n" +
            "          \"type\": \"color\",\n" +
            "          \"name\": \"blue\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"character\": \"here are some special characters!@#$%^*(\\\")_+:.,?/'\\\\|}{~`\"\n" +
            "  }\n" +
            "}";
    private ConvertXmlToJson convertXmlToJson;

    @Before
    public void setUp() throws Exception {
        convertXmlToJson = new ConvertXmlToJson();
    }

    @Test
    public void testConvertXmlToJsonWithDefaultValues() {
        Map<String, String> result = convertXmlToJson.execute(XML, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(JSON, result.get(RETURN_RESULT));
        assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get(NAMESPACES_URIS));
        assertEquals("f,ui", result.get(NAMESPACES_PREFIXES));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testConvertXmlToJsonWithDefaultValuesSpecified() {
        Map<String, String> result = convertXmlToJson.execute(XML, "_text", TRUE, TRUE, TRUE, EMPTY);

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(JSON, result.get(RETURN_RESULT));
        assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get(NAMESPACES_URIS));
        assertEquals("f,ui", result.get(NAMESPACES_PREFIXES));
    }


    @Test
    public void testConvertXmlToJsonWithInvalidBooleanValues() {
        Map<String, String> result = convertXmlToJson.execute(XML, "_text", "abc", "abc", "abc", EMPTY);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("abc is not a valid value for Boolean", result.get(RETURN_RESULT));
        assertEquals(EMPTY, result.get(NAMESPACES_URIS));
        assertEquals(EMPTY, result.get(NAMESPACES_PREFIXES));
    }

    @Test
    public void testConvertXmlToJsonWithNooRootNoPrettyPrintNoAttributes() {
        Map<String, String> result = convertXmlToJson.execute(XML, "+text", FALSE, FALSE, FALSE, EMPTY);

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(JSON_NO_PRETTY_NO_ROOT_NO_ATTRIBUTES, result.get(RETURN_RESULT));
        assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get(NAMESPACES_URIS));
        assertEquals("f,ui", result.get(NAMESPACES_PREFIXES));
    }

    @Test
    public void testConvertXmlToJsonWithInvalidXml() {
        Map<String, String> result = convertXmlToJson.execute(XML + "abc", "+text", FALSE, FALSE, FALSE, EMPTY);

        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertEquals("Error on line 12: Content is not allowed in trailing section.", result.get(RETURN_RESULT));
        assertEquals(EMPTY, result.get(NAMESPACES_URIS));
        assertEquals(EMPTY, result.get(NAMESPACES_PREFIXES));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testConvertXmlToJsonWithTextElements() {
        Map<String, String> result = convertXmlToJson.execute(XML_WITH_TEXT, "_text", TRUE, TRUE, TRUE, EMPTY);

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(JSON_WITH_TEXT, result.get(RETURN_RESULT));
        assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get(NAMESPACES_URIS));
        assertEquals("f,ui", result.get(NAMESPACES_PREFIXES));
    }

    @Test
    public void testConvertXmlToJsonSimpleTag() {
        final Map<String, String> result = convertXmlToJson.execute("<ip>1.2.3.4</ip>", EMPTY, TRUE, FALSE, FALSE, EMPTY);

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("{\"ip\":\"1.2.3.4\"}", result.get(RETURN_RESULT));
        assertEquals(EMPTY, result.get(NAMESPACES_URIS));
        assertEquals(EMPTY, result.get(NAMESPACES_PREFIXES));
    }

}