/*
 * Copyright 2026-2027 Open Text
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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Map;

import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by moldovas on 6/22/2016.
 */
public class EditXmlTest {

    private static final String EMPTY = "";
    private static final String DELETE = "delete";
    private static final String RENAME = "rename";
    private static final String INSERT = "insert";
    private static final String APPEND = "append";
    private static final String SUBNODE = "subnode";
    private static final String MOVE = "move";
    private static final String UPDATE = "update";
    private static final String ELEM = "elem";
    private static final String ATTR = "attr";
    private static final String TEXT = "text";
    private static final String nameSpaceResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"Server Group 1\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref><eve:test><t:test1>alabala</t:test1></eve:test></eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String nameSpaceResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"Server Group 1\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref>newText</eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String nameSpaceResult3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"newAttr\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref>http://127.0.0.1:21071/1/resource_pool_list/67</eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final String EXCEPTION = "exception";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String RETURN_CODE_FAILURE = "-1";
    private static String STRING_XML = "";
    private static String insertTextResponse = "";
    private static String appendTextResponse = "";
    private EditXml editXml;
    private Map<String, String> result;
    private String fullPath;

    @BeforeEach
    public void beforeTest() throws Exception {
        editXml = new EditXml();
        STRING_XML = join(readLines(ClassLoader.getSystemResourceAsStream("editxmlres/xmlString.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        insertTextResponse = join(readLines(ClassLoader.getSystemResourceAsStream("editxmlres/editXMLinsertTextResponse.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        appendTextResponse = join(readLines(ClassLoader.getSystemResourceAsStream("editxmlres/appendTextResponse.xml"), Charset.forName("UTF-8")), IOUtils.LINE_SEPARATOR);
        fullPath = this.getClass().getResource("/editxmlres/xmlFile.xml").getPath();
    }

    @Test
    public void testDeleteFromFile() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, DELETE, "/Employees/Employee/firstname|/Employees/Employee/age",
                EMPTY, EMPTY, ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLdelete.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteFromString() throws Exception {
        result = editXml.xPathReplaceNode(STRING_XML, EMPTY, DELETE, "/Employees/Employee/firstname|/Employees/Employee/age", EMPTY, EMPTY, ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLdelete.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, DELETE, "/Employees/Employee", EMPTY, EMPTY, ATTR, "emplid", "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLdeleteAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, DELETE, "/Employees/Employee/firstname", EMPTY, EMPTY, TEXT, "", "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLdeleteText.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, RENAME, "/Employees/Employee/age", EMPTY, "newNameElem", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLRenameElem.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, RENAME, "/Employees/Employee", EMPTY, "newNameAttribute", ATTR, "emplid", "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLRenameAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY, " new text", TEXT, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLInsertText1.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsetText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, " new text", TEXT, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLinsertTextResponse.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, " new text", ATTR, "newAttr", "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLInsertElement.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLInsertElement1.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee/firstname", EMPTY,
                " new text", TEXT, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLAppendText.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, " new text", TEXT, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/appendTextResponse.xml"), result.get(RETURN_RESULT));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendAttribute() throws Exception {
        //NOTE append attribute and insert attribute work in the same way!!!
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, " new text", ATTR, "newAttr", "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee/email", EMPTY, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLAppendElement.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLAppendElement1.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees", EMPTY, "<a id=\"testID\">newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLSubNode.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLSubNode1.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees/Employee/firstname", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLSubNode2.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee[3]", EMPTY, EMPTY, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLMove.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee", EMPTY, EMPTY, EMPTY, "");
        assertXmlEquals(getResponseFromFile("/editxmlres/editXMLMove2.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testValidate1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, EMPTY, RENAME, "/", EMPTY,
                EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("Supplied parameters: file path and xml is missing when one is required", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate2() throws Exception {
        result = editXml.xPathReplaceNode("asd", "asd", RENAME, "/", EMPTY, EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, result.get(RETURN_CODE));
        assertEquals("Supplied parameters: file path and xml when only one is required", result.get(EXCEPTION));
    }

    @Test
    public void testValidate3() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("action input is required.", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate4() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", MOVE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("xpath1 input is required.", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate5() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", MOVE, "/test/test", EMPTY, EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("xpath2 input is required for action 'move' ", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate6() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", DELETE, "/test/test", EMPTY, EMPTY, EMPTY, EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("type input is required for action 'delete'", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate7() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", DELETE, "/test/test", EMPTY, EMPTY, "wrong type", EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("Invalid type. Only supported : elem, attr, text", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate8() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, "asd", DELETE, "/test/test", EMPTY, EMPTY, "attr", EMPTY, "");
        assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        assertEquals("name input is required for type 'attr' ", (result.get(EXCEPTION)));
    }

    @Test
    public void testUpdate() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY, "<eve:test><t:test1>alabala</t:test1></eve:test>", "elem", EMPTY, "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertXmlEquals(nameSpaceResult1, result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY, "newText", "text", EMPTY, "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertXmlEquals(nameSpaceResult2, result.get(RETURN_RESULT));
    }

    @Test
    public void testUpdateAttr() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate",
                EMPTY, "newAttr", "attr", "name", "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertXmlEquals(nameSpaceResult3, result.get(RETURN_RESULT));
    }

    private String getResponseFromFile(String file) throws IOException {
        InputStream inputXML = getClass().getResourceAsStream(file);
        if (inputXML == null) {
            throw new IOException("XML test resource not found: " + file);
        }
        try (inputXML) {
            return IOUtils.toString(inputXML, Charset.forName("UTF-8"));
        }
    }

    private void assertXmlEquals(String expectedXml, String actualXml) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        // These legacy expected strings were compacted by removing spaces between adjacent attributes.
        expectedXml = expectedXml.replaceAll("\"(?=[A-Za-z_:][A-Za-z0-9_.:-]*=)", "\" ");
        Document expected = builder.parse(new InputSource(new StringReader(expectedXml)));
        Document actual = builder.parse(new InputSource(new StringReader(actualXml)));
        removeFormattingWhitespace(expected);
        removeFormattingWhitespace(actual);
        assertTrue(expected.isEqualNode(actual), "XML documents differ");
    }

    private void removeFormattingWhitespace(Node node) {
        NodeList children = node.getChildNodes();
        boolean hasElementChild = false;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasElementChild = true;
            }
        }

        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (hasElementChild
                    && child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                node.removeChild(child);
            } else {
                removeFormattingWhitespace(child);
            }
        }
    }
}
