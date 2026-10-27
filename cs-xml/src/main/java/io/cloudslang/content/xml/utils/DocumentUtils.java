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



package io.cloudslang.content.xml.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moldovas on 7/6/2016.
 */
public class DocumentUtils {
    /**
     * @return a new instance of DocumentBuilder
     * @throws ParserConfigurationException
     */
    public static DocumentBuilder createDocumentBuilder(String features) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        XmlUtils.setFeatures(factory, features);
        factory.setNamespaceAware(false);
        return factory.newDocumentBuilder();
    }

    /**
     * Returns a String representation for the XML Document.
     *
     * @param xmlDocument the XML Document
     * @return String representation for the XML Document
     * @throws IOException
     */
    public static String documentToString(Document xmlDocument) throws IOException {
        String encoding = (xmlDocument.getXmlEncoding() == null) ? "UTF-8" : xmlDocument.getXmlEncoding();
        try (StringWriter out = new StringWriter()) {
            Document documentCopy = (Document) xmlDocument.cloneNode(true);
            formatElement(documentCopy.getDocumentElement(), 0);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.transform(new DOMSource(documentCopy), new StreamResult(out));
            return out.toString();
        } catch (TransformerException e) {
            throw new IOException("Unable to serialize XML document", e);
        }
    }

    /**
     * Serializes an element without a declaration or trailing newline, using JDK XML formatting.
     *
     * @param element the element to serialize
     * @param prettyPrint whether to indent element-only content with two spaces and LF line endings
     * @return the serialized element; empty tags and character references use JDK formatting
     * @throws TransformerException if the element cannot be serialized
     */
    public static String elementToString(Element element, boolean prettyPrint) throws TransformerException {
        Element elementCopy = (Element) element.cloneNode(true);
        if (prettyPrint) {
            formatElement(elementCopy, 0);
        }
        Transformer transformer = TransformerFactory.newDefaultInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(elementCopy), new StreamResult(writer));
        return writer.toString().replace("\r\n", "\n");
    }

    private static void formatElement(Element element, int depth) {
        Document document = element.getOwnerDocument();
        NodeList childNodes = element.getChildNodes();
        List<Node> children = new ArrayList<>(childNodes.getLength());
        boolean hasElementChild = false;
        boolean hasNonWhitespaceText = false;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            children.add(child);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasElementChild = true;
            } else if ((child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE)
                    && !child.getTextContent().trim().isEmpty()) {
                hasNonWhitespaceText = true;
            }
        }

        if (hasElementChild && !hasNonWhitespaceText) {
            for (Node child : children) {
                if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                    element.removeChild(child);
                }
            }
            children.clear();
            childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                children.add(childNodes.item(i));
            }
            for (Node child : children) {
                element.insertBefore(document.createTextNode("\n" + "  ".repeat(depth + 1)), child);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    formatElement((Element) child, depth + 1);
                }
            }
            element.appendChild(document.createTextNode("\n" + "  ".repeat(depth)));
        } else {
            for (Node child : children) {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    formatElement((Element) child, depth + 1);
                }
            }
        }
    }
}
