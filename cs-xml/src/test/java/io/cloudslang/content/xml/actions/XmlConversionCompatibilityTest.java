/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
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

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_PREFIXES;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_URIS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlConversionCompatibilityTest {
    @Test
    void preservesPrettyPrintingAndDeclaration() {
        Map<String, String> result = toXml("{\"root\":{\"child\":\"  hello   world  \",\"empty\":\"\"}}",
                true, true, "", "", "");
        assertSuccess(result, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root>\n  <child>hello   world</child>\n  <empty/>\n</root>\n");
    }

    @Test
    void preservesCompactWhitespaceAndEscaping() {
        assertSuccess(toXml("{\"root\":{\"@value\":\"a\\tb\\nc\\rd\\\"&<>\",\"child\":\"  a   b\\n c & < >  \"}}",
                false, false, "", "", ""),
                "<root value=\"a&#9;b&#10;c&#13;d&quot;&amp;&lt;&gt;\"><child>a b c &amp; &lt; &gt;</child></root>");
    }

    @Test
    void preservesEmptyCollectionsAndNullValues() {
        assertSuccess(toXml("{\"root\":{\"nothing\":null,\"items\":[null,1,true,{\"x\":\"y\"}],"
                        + "\"empty\":{},\"emptyArray\":[]}}", false, false, "", "", ""),
                "<root><items><item>1</item><item>true</item><item><x>y</x></item></items><empty/><emptyArray/></root>");
    }

    @Test
    void preservesMultipleFragmentFormatting() {
        assertSuccess(toXml("{\"first\":\"one\",\"second\":{\"child\":\"two\"}}", true, false, "", "", ""),
                "<first>one</first>\n<second>\n  <child>two</child>\n</second>");
    }

    @Test
    void preservesDefaultNamespaces() {
        Map<String, String> result = toJson("<root xmlns=\"urn:default\"><child>value</child></root>",
                true, true, "");
        assertSuccess(result, "{\"root\":{\"child\":\"value\"}}");
        assertEquals("", result.get(NAMESPACES_PREFIXES));
        assertEquals("", result.get(NAMESPACES_URIS));
    }

    @Test
    void preservesUnknownPrefixBehavior() {
        assertSuccess(toXml("{\"missing:root\":{\"missing:child\":\"value\"}}", false, false, "", "", ""),
                "<root><child>value</child></root>");
    }

    @Test
    void rejectsInvalidXmlNamesAndCharacters() {
        assertEquals(FAILURE, toXml("{\"bad name\":\"value\"}", false, false, "", "", "").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("{\"root\":\"\\u0001\"}", false, false, "", "", "").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("{\"root\":{\"@p:name\":\"value\"}}", false, false, "", "", "").get(RETURN_CODE));
    }

    @Test
    void preservesInputAttributeAndNamespaceOrder() {
        Map<String, String> result = toJson("<root xmlns:z=\"urn:z\" xmlns:a=\"urn:a\" z:last=\"1\" first=\"2\">"
                + "<child b=\"3\" a=\"4\">text</child></root>", true, true, "");
        assertSuccess(result, "{\"root\":{\"@last\":\"1\",\"@first\":\"2\",\"child\":{\"@b\":\"3\","
                + "\"@a\":\"4\",\"_text\":\"text\"}}}");
        assertEquals("z,a", result.get(NAMESPACES_PREFIXES));
        assertEquals("urn:z,urn:a", result.get(NAMESPACES_URIS));
    }

    @Test
    void preservesOwnNamespaceExclusionAndRepeatedNamespaces() {
        Map<String, String> result = toJson("<p:root xmlns:p=\"urn:p\" xmlns:q=\"urn:q\">"
                + "<q:child xmlns:r=\"urn:r\">one</q:child><q:child xmlns:r=\"urn:r\">two</q:child></p:root>",
                true, true, "");
        assertSuccess(result, "{\"root\":{\"q:child\":[{\"_text\":\"one\"},{\"_text\":\"two\"}]}}");
        assertEquals("q,r,r", result.get(NAMESPACES_PREFIXES));
        assertEquals("urn:q,urn:r,urn:r", result.get(NAMESPACES_URIS));
    }

    @Test
    void preservesMixedTextAndCdata() {
        assertSuccess(toJson("<root>before<![CDATA[ & middle]]><child>inside</child>after</root>", true, true, ""),
                "{\"root\":{\"child\":\"inside\",\"_text\":\"before & middleafter\"}}");
    }

    @Test
    void preservesPrimitiveAndAttributeOptions() {
        assertSuccess(toJson("<root><child a=\"1\">value</child><empty/></root>", false, false, ""),
                "{\"child\":{\"_text\":\"value\"},\"empty\":\"\"}");
        assertSuccess(toJson("<p:root xmlns:p=\"urn:p\">value</p:root>", false, false, ""),
                "{\"p:root\":\"value\"}");
    }

    @Test
    void preservesMultilineAndNonAlphanumericTextFiltering() {
        assertSuccess(toJson("<root><child a=\"1\">line1\nline2</child><punct a=\"1\">!!!</punct></root>",
                true, true, ""), "{\"root\":{\"child\":{\"@a\":\"1\"},\"punct\":{\"@a\":\"1\"}}}");
    }

    @Test
    void preservesArrayGroupingOrder() {
        assertSuccess(toJson("<root><first>x</first><item>one</item><second>y</second><item>two</item></root>",
                true, true, ""),
                "{\"root\":{\"item\":[{\"_text\":\"one\"},{\"_text\":\"two\"}],\"first\":\"x\",\"second\":\"y\"}}");
    }

    @Test
    void preservesParserFeaturesAndFailureOutputs() {
        Map<String, String> result = toJson("<!DOCTYPE root [<!ENTITY value \"expanded\">]><root>&value;</root>",
                true, true, "http://apache.org/xml/features/disallow-doctype-decl false");
        assertSuccess(result, "{\"root\":{\"_text\":\"expanded\"}}");
        Map<String, String> rejected = toJson("<!DOCTYPE root><root/>", true, true,
                "http://apache.org/xml/features/disallow-doctype-decl true");
        assertEquals(FAILURE, rejected.get(RETURN_CODE));
        assertTrue(rejected.get(RETURN_RESULT).contains("DOCTYPE"));
        assertEquals("", rejected.get(NAMESPACES_PREFIXES));
        assertEquals("", rejected.get(NAMESPACES_URIS));
        assertEquals(FAILURE, toJson("<root/>", true, true, "invalid-feature true").get(RETURN_CODE));
    }

    @Test
    void excludesNamespaceDeclarationsWhenNamespacePrefixReportingIsEnabled() {
        Map<String, String> result = toJson("<root xmlns:p=\"urn:p\" p:id=\"value\"><child>text</child></root>",
                true, true, "http://xml.org/sax/features/namespace-prefixes true");
        assertSuccess(result, "{\"root\":{\"@id\":\"value\",\"child\":\"text\"}}");
        assertEquals("p", result.get(NAMESPACES_PREFIXES));
        assertEquals("urn:p", result.get(NAMESPACES_URIS));
    }

    @Test
    void preservesUnicodeAndWhitespaceInAttributeValues() throws Exception {
        String value = "a\tb\nc\rd & < > \" \u00e9 \ud83d\ude00";
        for (boolean pretty : new boolean[]{false, true}) {
            Map<String, String> result = toXml("{\"root\":{\"@value\":\"a\\tb\\nc\\rd & < > \\\" \u00e9 \ud83d\ude00\","
                    + "\"child\":\"\u00e9 \ud83d\ude00\"}}", pretty, true, "", "", "");
            assertEquals(SUCCESS, result.get(RETURN_CODE), result.get(RETURN_RESULT));
            Document document = parse(result.get(RETURN_RESULT));
            assertEquals(value, document.getDocumentElement().getAttribute("value"));
            assertEquals("\u00e9 \ud83d\ude00", document.getElementsByTagName("child").item(0).getTextContent());
        }
    }

    @Test
    void preservesDeclaredNamespacesAndCustomArrayNames() throws Exception {
        Map<String, String> result = new ConvertJsonToXml().execute(
                "{\"p:entries\":[1,2],\"empty\":{}}", "true", "true", "p:root", "item",
                "p", "urn:items", "p:entries", "p:entry", ",");
        assertEquals(SUCCESS, result.get(RETURN_CODE), result.get(RETURN_RESULT));
        Document document = parse(result.get(RETURN_RESULT));
        assertEquals("urn:items", document.getDocumentElement().getNamespaceURI());
        assertEquals(2, document.getElementsByTagNameNS("urn:items", "entry").getLength());
        assertEquals("1", document.getElementsByTagNameNS("urn:items", "entry").item(0).getTextContent());
        assertEquals(1, document.getElementsByTagName("empty").getLength());
    }

    @Test
    void rejectsInvalidNamespaceBindingsEvenForEmptyInput() {
        assertEquals(FAILURE, toXml("", false, false, "", "bad prefix", "urn:p").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("", false, false, "", "xml", "urn:wrong").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("", false, false, "", "xmlns", "urn:p").get(RETURN_CODE));
    }

    @Test
    void preservesMissingAndMultipleDocumentRootFailures() {
        assertEquals(FAILURE, toXml("{\"a\":1,\"b\":2}", false, true, "", "", "").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("[]", false, true, "", "", "").get(RETURN_CODE));
        assertEquals(FAILURE, toXml("{}", false, true, "", "", "").get(RETURN_CODE));
    }

    private Document parse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private Map<String, String> toXml(String json, boolean pretty, boolean declaration, String root,
                                      String prefixes, String uris) {
        return new ConvertJsonToXml().execute(json, Boolean.toString(pretty), Boolean.toString(declaration),
                root, "item", prefixes, uris, "", "", ",");
    }

    private Map<String, String> toJson(String xml, boolean root, boolean attributes, String features) {
        return new ConvertXmlToJson().execute(xml, "_text", Boolean.toString(root), Boolean.toString(attributes),
                "false", features);
    }

    private void assertSuccess(Map<String, String> result, String expected) {
        assertEquals(SUCCESS, result.get(RETURN_CODE), result.get(RETURN_RESULT));
        assertEquals(expected, result.get(RETURN_RESULT),
                () -> "Actual output: " + result.get(RETURN_RESULT).replace("\r", "\\r").replace("\n", "\\n"));
    }
}
