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

package io.cloudslang.content.xml.services.impl;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.DocumentUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;

/**
 * Created by moldovas on 7/8/2016.
 */
public class MoveOperationServiceImpl implements OperationService {
    /**
     * Moves the Nodes of an XML (provided as String or file) from a give XPath to their new location provided by the second  XPath.
     *
     * @param inputs inputs
     * @return String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    @Override
    public String execute(EditXmlInputs inputs) throws Exception {
        Document doc = XmlUtils.createDocument(inputs.getXml(), inputs.getFilePath(), inputs.getParsingFeatures());
        NamespaceContext ctx = XmlUtils.getNamespaceContext(inputs.getXml(), inputs.getFilePath());
        NodeList nodeListToMove = XmlUtils.readNode(doc, inputs.getXpath1(), ctx);
        NodeList nodeListWhereToMove = XmlUtils.readNode(doc, inputs.getXpath2(), ctx);
        Node nodeToMove;
        Node nodeWhereToMove;

        if (!inputs.getXpath2().contains(inputs.getXpath1())) {
            // remove nodes
            for (int i = 0; i < nodeListToMove.getLength(); i++) {
                nodeToMove = nodeListToMove.item(i);
                if (nodeToMove != doc.getDocumentElement()) {
                    nodeToMove.getParentNode().removeChild(nodeToMove);
                }
            }
            //add nodes to designated location
            for (int i = 0; i < nodeListWhereToMove.getLength(); i++) {
                nodeWhereToMove = nodeListWhereToMove.item(i);
                for (int j = 0; j < nodeListToMove.getLength(); j++) {
                    nodeToMove = nodeListToMove.item(j);
                    nodeToMove = doc.importNode(nodeToMove, true);
                    nodeWhereToMove.appendChild(nodeToMove);
                }
            }
        }
        return DocumentUtils.documentToString(doc);
    }
}
