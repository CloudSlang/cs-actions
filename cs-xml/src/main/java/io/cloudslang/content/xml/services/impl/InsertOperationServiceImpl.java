/*
 * (c) Copyright 2022 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.xml.services.impl;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.DocumentUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by moldovas on 7/8/2016.
 */
public class InsertOperationServiceImpl implements OperationService {

    /**
     * Inserts in an XML (provided as String or file) a new element; attribute or text text at a given XPath.
     *
     * @param inputs inputs
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    @Override
    public String execute(EditXmlInputs inputs) throws Exception {
        Document doc = XmlUtils.createDocument(inputs.getXml(), inputs.getFilePath(), inputs.getParsingFeatures());
        NodeList nodeList = XmlUtils.readNode(doc, inputs.getXpath1(), XmlUtils.getNamespaceContext(inputs.getXml(), inputs.getFilePath()));
        Node childNode = null;
        Node node;
        Node parentNode;
        // create new Node to insert
        if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType())) {
            childNode = XmlUtils.stringToNode(inputs.getValue(), doc.getXmlEncoding(), inputs.getParsingFeatures());
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType())) {
                //check if provided xpath doesn't contain root node
                if (node != doc.getDocumentElement()) {
                    childNode = doc.importNode(childNode, true);
                    parentNode = node.getParentNode();
                    parentNode.insertBefore(childNode, node);
                }
            } else if (Constants.Inputs.TYPE_TEXT.equals(inputs.getType())) {
                node.setTextContent(inputs.getValue() + node.getTextContent());
            } else if (Constants.Inputs.TYPE_ATTR.equals(inputs.getType())) {
                ((Element) node).setAttribute(inputs.getName(), inputs.getValue());
            }
        }
        return DocumentUtils.documentToString(doc);
    }
}
