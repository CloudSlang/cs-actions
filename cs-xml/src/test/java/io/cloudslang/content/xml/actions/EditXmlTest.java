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

import org.apache.commons.io.IOUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang3.StringUtils.join;
import static org.junit.Assert.assertEquals;

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
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private EditXml editXml;
    private Map<String, String> result;
    private String fullPath;

    @Before
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
        assertEquals(getResponseFromFile("/editxmlres/editXMLdelete.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteFromString() throws Exception {
        result = editXml.xPathReplaceNode(STRING_XML, EMPTY, DELETE, "/Employees/Employee/firstname|/Employees/Employee/age", EMPTY, EMPTY, ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLdelete.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, DELETE, "/Employees/Employee", EMPTY, EMPTY, ATTR, "emplid", "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLdeleteAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, DELETE, "/Employees/Employee/firstname", EMPTY, EMPTY, TEXT, "", "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLdeleteText.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, RENAME, "/Employees/Employee/age", EMPTY, "newNameElem", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLRenameElem.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, RENAME, "/Employees/Employee", EMPTY, "newNameAttribute", ATTR, "emplid", "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLRenameAttribute.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY, " new text", TEXT, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLInsertText1.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsetText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, " new text", TEXT, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLinsertTextResponse.xml"), result.get(RETURN_RESULT));
        assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, " new text", ATTR, "newAttr", "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLInsertElement.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLInsertElement1.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, INSERT, "/Employees", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee/firstname", EMPTY,
                " new text", TEXT, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLAppendText.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, " new text", TEXT, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/appendTextResponse.xml"), (result.get(RETURN_RESULT)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendAttribute() throws Exception {
        //NOTE append attribute and insert attribute work in the same way!!!
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, " new text", ATTR, "newAttr", "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee/email", EMPTY, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLAppendElement.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLAppendElement1.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, APPEND, "/Employees", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees", EMPTY, "<a id=\"testID\">newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees/Employee", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode1.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, SUBNODE, "/Employees/Employee/firstname", EMPTY, "<a>newAttributeValue</a>", ELEM, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode2.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee[3]", EMPTY, EMPTY, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLMove.xml"), (result.get(RETURN_RESULT)));
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee", EMPTY, EMPTY, EMPTY, "");
        assertEquals(getResponseFromFile("/editxmlres/editXMLMove2.xml"), (result.get(RETURN_RESULT)));
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
    public void testUpdate() {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY, "<eve:test><t:test1>alabala</t:test1></eve:test>", "elem", EMPTY, "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY).replace("\n", EMPTY).replace("returnResult=", EMPTY).replace("  ", "");
        assertEquals(nameSpaceResult1, (resultFormatted));
    }

    @Test
    public void testUpdateText() {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY, "newText", "text", EMPTY, "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY).replace("\n", EMPTY).replace("returnResult=", EMPTY).replace("  ", "");
        assertEquals(nameSpaceResult2, (resultFormatted));
    }

    @Test
    public void testUpdateAttr() {
        result = editXml.xPathReplaceNode(EMPTY, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate",
                EMPTY, "newAttr", "attr", "name", "");
        assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY).replace("\n", EMPTY).replace("returnResult=", EMPTY).replace("  ", "");
        assertEquals(nameSpaceResult3, (resultFormatted));
    }

    private String getResponseFromFile(String file) throws Exception {
        String filePath = this.getClass().getResource(file).getPath();
        Document xmlDocument;
        InputStream inputXML;
        inputXML = new FileInputStream(new File(filePath));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        xmlDocument = builder.parse(inputXML);
        OutputFormat format = new OutputFormat(xmlDocument);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(xmlDocument);
        return out.toString();

    }
}
