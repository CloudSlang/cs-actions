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

package io.cloudslang.content.xml.utils;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

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
        OutputFormat format = new OutputFormat(xmlDocument);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        format.setEncoding(encoding);
        try (Writer out = new StringWriter()) {
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(xmlDocument);
            return out.toString();
        }
    }
}
