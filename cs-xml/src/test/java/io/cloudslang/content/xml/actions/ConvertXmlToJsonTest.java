package io.cloudslang.content.xml.actions;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Created by ursan on 8/4/2016.
 */
public class ConvertXmlToJsonTest {
    private ConvertXmlToJson convertXmlToJson;
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
    @Before
    public void setUp() throws Exception {
        convertXmlToJson = new ConvertXmlToJson();
    }

    @Test
    public void testConvertXmlToJsonWithDefaultValues() {
        Map<String, String> result = convertXmlToJson.execute(XML, "", "", "", "", "");

        Assert.assertEquals("0", result.get("returnCode"));
        Assert.assertEquals(JSON, result.get("returnResult"));
        Assert.assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get("namespacesUris"));
        Assert.assertEquals("f,ui", result.get("namespacesPrefixes"));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testConvertXmlToJsonWithDefaultValuesSpecified() {
        Map<String, String> result = convertXmlToJson.execute(XML, "_text", "true", "true", "true", "");

        Assert.assertEquals("0", result.get("returnCode"));
        Assert.assertEquals(JSON, result.get("returnResult"));
        Assert.assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get("namespacesUris"));
        Assert.assertEquals("f,ui", result.get("namespacesPrefixes"));
    }


    @Test
    public void testConvertXmlToJsonWithInvalidBooleanValues() {
        Map<String, String> result = convertXmlToJson.execute(XML, "_text", "abc", "abc", "abc", "");

        Assert.assertEquals("-1", result.get("returnCode"));
        Assert.assertEquals("abc is not a valid value for Boolean", result.get("returnResult"));
        Assert.assertEquals("", result.get("namespacesUris"));
        Assert.assertEquals("", result.get("namespacesPrefixes"));
    }

    @Test
    public void testConvertXmlToJsonWithNooRootNoPrettyPrintNoAttributes() {
        Map<String, String> result = convertXmlToJson.execute(XML, "+text", "false", "false", "false", "");

        Assert.assertEquals("0", result.get("returnCode"));
        Assert.assertEquals(JSON_NO_PRETTY_NO_ROOT_NO_ATTRIBUTES, result.get("returnResult"));
        Assert.assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get("namespacesUris"));
        Assert.assertEquals("f,ui", result.get("namespacesPrefixes"));
    }

    @Test
    public void testConvertXmlToJsonWithInvalidXml() {
        Map<String, String> result = convertXmlToJson.execute(XML + "abc", "+text", "false", "false", "false", "");

        Assert.assertEquals("-1", result.get("returnCode"));
        Assert.assertEquals("Error on line 12: Content is not allowed in trailing section.", result.get("returnResult"));
        Assert.assertEquals("", result.get("namespacesUris"));
        Assert.assertEquals("", result.get("namespacesPrefixes"));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testConvertXmlToJsonWithTextElements() {
        Map<String, String> result = convertXmlToJson.execute(XML_WITH_TEXT, "_text", "true", "true", "true", "");

        Assert.assertEquals("0", result.get("returnCode"));
        Assert.assertEquals(JSON_WITH_TEXT, result.get("returnResult"));
        Assert.assertEquals("http://java.sun.com/jsf/core,urn:x-hp:2012:software:eve:uibinding", result.get("namespacesUris"));
        Assert.assertEquals("f,ui", result.get("namespacesPrefixes"));
    }

}