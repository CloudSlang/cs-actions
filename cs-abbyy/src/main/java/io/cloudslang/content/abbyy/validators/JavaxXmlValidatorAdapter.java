/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class JavaxXmlValidatorAdapter implements AbbyyResultValidator {

    private Validator adaptee;

    public JavaxXmlValidatorAdapter(String xsdSchemaPath) throws SAXException {
        if(xsdSchemaPath == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "xsdSchemaPath"));
        }

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream xsdStream = getClass().getResourceAsStream(xsdSchemaPath);
        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        this.adaptee = schema.newValidator();
    }

    @Override
    public AbbyySdkException validate(String xml) throws IOException {
        if(xml == null) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_ARGUMENT, "xml"));
        }

        xml = xml.trim().replaceFirst("^([\\W]+)<","<");

        try {
            StringReader xmlReader = new StringReader(xml);
            this.adaptee.validate(new StreamSource(xmlReader));
            return null;
        } catch (SAXException ex) {
            return new AbbyySdkException(ex);
        }
    }
}
