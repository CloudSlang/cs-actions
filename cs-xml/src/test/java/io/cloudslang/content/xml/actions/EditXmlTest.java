package io.cloudslang.content.xml.actions;

import org.apache.commons.io.FileUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by moldovas on 6/22/2016.
 */
public class EditXmlTest {

    private static final String EMPTY_STRING = "";
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
    private static String STRING_XML = "";
    private static String insertTextResponse = "";
    private static String appendTextResponse = "";
    private static final String nameSpaceResult1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"Server Group 1\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref><eve:test><t:test1>alabala</t:test1></eve:test></eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String nameSpaceResult2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"Server Group 1\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref>newText</eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String nameSpaceResult3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><t:Definitions id=\"1\" name=\"Provision\"targetNamespace=\"urn:example:InfrastructureTopology\"xmlns:eve=\"urn:x-hp:2012:software:eve:realized-types\"xmlns:t=\"http://docs.oasis-open.org/tosca/ns/2011/12\"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://docs.oasis-open.org/tosca/ns/2011/12/tosca.xsd\"><t:Import importType=\"http://docs.oasis-open.org/tosca/ns/2011/12\"location=\"EveRealizedTypes.xml\" namespace=\"urn:x-hp:2012:software:eve:realized-types\"/><t:ServiceTemplate id=\"service-1\" name=\"Provision\"><t:TopologyTemplate id=\"topology-1\"><t:NodeTemplate id=\"T1-ServerGroup0001\" name=\"newAttr\" type=\"eve:realized_server_group_type\"><t:Properties><eve:realized_server_group modelClass=\"urn:x-hp:2009:software:data_model:type:ci_collection\"><eve:resource_pool_ref>http://127.0.0.1:21071/1/resource_pool_list/67</eve:resource_pool_ref><eve:last_modified_time>2013-11-25T14:05:54.803Z</eve:last_modified_time><eve:name>Server Group 1</eve:name><eve:global_id>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:global_id><eve:self>http://172.29.0.202:21051/1/infrastructure_topology_list/1/realized_server_group_list/T1-ServerGroup0001</eve:self></eve:realized_server_group></t:Properties></t:NodeTemplate></t:TopologyTemplate></t:ServiceTemplate></t:Definitions>";
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";
    private static final String EXCEPTION = "exception";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String RETURN_CODE_FAILURE = "-1";
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private EditXml editXml;
    private Map<String, String> result;
    private String fullPath;

    @Before
    public void beforeTest() throws URISyntaxException, IOException {
        editXml = new EditXml();
        URI resource = getClass().getResource("/editxmlres/xmlString.xml").toURI();
        STRING_XML = FileUtils.readFileToString(new File(resource));
        URI resource2 = getClass().getResource("/editxmlres/editXMLinsertTextResponse.xml").toURI();
        insertTextResponse = FileUtils.readFileToString(new File(resource2), "UTF-8");
        URI resource3 = getClass().getResource("/editxmlres/appendTextResponse.xml").toURI();
        appendTextResponse = FileUtils.readFileToString(new File(resource3));
        fullPath = this.getClass().getResource("/editxmlres/xmlFile.xml").getPath();
    }

    @Test
    public void testDeleteFromFile() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, DELETE, "/Employees/Employee/firstname|/Employees/Employee/age",
                EMPTY_STRING, EMPTY_STRING, ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLdelete.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteFromString() throws Exception {
        result = editXml.xPathReplaceNode(STRING_XML, EMPTY_STRING, DELETE, "/Employees/Employee/firstname|/Employees/Employee/age", EMPTY_STRING, EMPTY_STRING, ELEM, EMPTY_STRING, "");
        Assert.assertEquals (getResponseFromFile("/editxmlres/editXMLdelete.xml"), result.get(RETURN_RESULT));
        Assert.assertEquals (RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        Assert.assertEquals (result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, DELETE, "/Employees/Employee", EMPTY_STRING, EMPTY_STRING, ATTR, "emplid", "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLdeletAttribute.xml"), result.get(RETURN_RESULT));
        Assert.assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testDeleteText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, DELETE, "/Employees/Employee/firstname", EMPTY_STRING, EMPTY_STRING, TEXT, "", "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLdeleteText.xml"),result.get(RETURN_RESULT));
        Assert.assertEquals(RETURN_CODE_SUCCESS,result.get(RETURN_CODE));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, RENAME, "/Employees/Employee/age", EMPTY_STRING, "newNameElem", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLRenameElem.xml"), result.get(RETURN_RESULT));
        Assert.assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testRenameAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, RENAME, "/Employees/Employee", EMPTY_STRING, "newNameAttribute", ATTR, "emplid", "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLRenameAttribute.xml"), result.get(RETURN_RESULT));
        Assert.assertEquals (RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals (result.get(EXCEPTION) ,null);
    }

    @Test
    public void testInsertText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY_STRING, " new text", TEXT, EMPTY_STRING, "");
        Assert.assertEquals (getResponseFromFile("/editxmlres/editXMLInsertText1.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals (RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals (result.get(EXCEPTION), null);
    }

    @Test
    public void testInsetText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees/Employee", EMPTY_STRING, " new text", TEXT, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLinsertTextResponse.xml"), result.get(RETURN_RESULT));
        Assert.assertEquals(RETURN_CODE_SUCCESS, result.get(RETURN_CODE));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertAttribute() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees/Employee", EMPTY_STRING, " new text", ATTR, "newAttr", "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees/Employee/firstname", EMPTY_STRING, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLInsertElement.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees/Employee", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLInsertElement1.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testInsertElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, INSERT, "/Employees", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees/Employee/firstname", EMPTY_STRING,
                " new text", TEXT, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLAppendText.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendText1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees/Employee", EMPTY_STRING, " new text", TEXT, EMPTY_STRING, "");
        Assert.assertEquals(appendTextResponse, (result.get(RETURN_RESULT)));
        Assert.assertEquals(appendTextResponse, (result.get(RETURN_RESULT)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendAttribute() throws Exception {
        //NOTE append attribute and insert attribute work in the same way!!!
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees/Employee", EMPTY_STRING, " new text", ATTR, "newAttr", "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLInsertAttribute.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees/Employee/email", EMPTY_STRING, "<a id=\"test id \"><b name =\"testName\"><c test=\"testCV\">newAttributeValue</c></b></a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLAppendElement.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees/Employee", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLAppendElement1.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testAppendElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, APPEND, "/Employees", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/xmlFile.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, SUBNODE, "/Employees", EMPTY_STRING, "<a id=\"testID\">newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, SUBNODE, "/Employees/Employee", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode1.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testSubNodeElem2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, SUBNODE, "/Employees/Employee/firstname", EMPTY_STRING, "<a>newAttributeValue</a>", ELEM, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLSubNode2.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee[3]", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLMove.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testMove2() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, fullPath, MOVE, "/Employees/Employee/firstname", "/Employees/Employee", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(getResponseFromFile("/editxmlres/editXMLMove2.xml"), (result.get(RETURN_RESULT)));
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        Assert.assertEquals(result.get(EXCEPTION), null);
    }

    @Test
    public void testValidate1() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, EMPTY_STRING, RENAME, "/", EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("Supplied parameters: file path and xml is missing when one is required", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate2() throws Exception {
        result = editXml.xPathReplaceNode("asd", "asd", RENAME, "/", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, result.get(RETURN_CODE));
        Assert.assertEquals("Supplied parameters: file path and xml when only one is required", result.get(EXCEPTION));
    }

    @Test
    public void testValidate3() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("action input is required.", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate4() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", MOVE, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("xpath1 input is required.", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate5() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", MOVE, "/test/test", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("xpath2 input is required for action 'move' ", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate6() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", DELETE, "/test/test", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("type input is required for action 'delete'", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate7() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", DELETE, "/test/test", EMPTY_STRING, EMPTY_STRING, "wrong type", EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("Invalid type. Only supported : elem, attr, text", (result.get(EXCEPTION)));
    }

    @Test
    public void testValidate8() throws Exception {
        result = editXml.xPathReplaceNode(EMPTY_STRING, "asd", DELETE, "/test/test", EMPTY_STRING, EMPTY_STRING, "attr", EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_FAILURE, (result.get(RETURN_CODE)));
        Assert.assertEquals("name input is required for type 'attr' ", (result.get(EXCEPTION)));
    }

    @Test
    public void testUpdate() {
        result = editXml.xPathReplaceNode(EMPTY_STRING, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY_STRING, "<eve:test><t:test1>alabala</t:test1></eve:test>", "elem", EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY_STRING).replace("\n", EMPTY_STRING).replace("returnResult=", EMPTY_STRING).replace("  ", "");
        Assert.assertEquals(nameSpaceResult1, (resultFormatted));
    }

    @Test
    public void testUpdateText() {
        result = editXml.xPathReplaceNode(EMPTY_STRING, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate/t:Properties/eve:realized_server_group/eve:resource_pool_ref",
                EMPTY_STRING, "newText", "text", EMPTY_STRING, "");
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY_STRING).replace("\n", EMPTY_STRING).replace("returnResult=", EMPTY_STRING).replace("  ", "");
        Assert.assertEquals(nameSpaceResult2, (resultFormatted));
    }

    @Test
    public void testUpdateAttr() {
        result = editXml.xPathReplaceNode(EMPTY_STRING, getClass().getResource("/editxmlres/nameSpace.xml").getPath(), UPDATE, "/t:Definitions/t:ServiceTemplate/t:TopologyTemplate/t:NodeTemplate",
                EMPTY_STRING, "newAttr", "attr", "name", "");
        Assert.assertEquals(RETURN_CODE_SUCCESS, (result.get(RETURN_CODE)));
        String resultFormatted = result.get(RETURN_RESULT).replace("\t", EMPTY_STRING).replace("\n", EMPTY_STRING).replace("returnResult=", EMPTY_STRING).replace("  ", "");
        Assert.assertEquals(nameSpaceResult3, (resultFormatted));
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
