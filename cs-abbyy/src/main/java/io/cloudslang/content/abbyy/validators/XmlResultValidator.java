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
import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.io.StringReader;

public class XmlResultValidator extends AbbyyResultValidator {

    private static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String FORBID_EXTERNAL_ENTITY = "http://xml.org/sax/features/external-general-entities";
    private static final String FORBID_EXTERNAL_PARAMETERS = "http://xml.org/sax/features/external-parameter-entities";

    private final String xsdSchemaPath;
    private final AbbyyApi abbyyApi;


    public XmlResultValidator(@NotNull AbbyyApi abbyyApi, @NotNull String xsdSchemaPath) {
        this.abbyyApi = abbyyApi;
        this.xsdSchemaPath = xsdSchemaPath;
    }


    @Override
    void validateBefore(@NotNull AbbyyInput abbyyInitialRequest, @NotNull String url) throws Exception {
        if (this.abbyyApi.getResultSize(abbyyInitialRequest, url, ExportFormat.XML) > Limits.MAX_SIZE_OF_RESULT) {
            throw new ValidationException(String.format(ExceptionMsgs.MAX_SIZE_OF_RESULT_EXCEEDED, ExportFormat.XML));
        }
    }


    @Override
    void validateAfter(@NotNull String xml) throws Exception {
        Validator xmlValidator = newXmlValidator();

        xml = xml.trim().replaceFirst("^([\\W]+)<", "<");

        try {
            StringReader xmlReader = new StringReader(xml);
            xmlValidator.validate(new StreamSource(xmlReader));
        } catch (SAXException ex) {
            throw new ValidationException(ExceptionMsgs.RESPONSE_VALIDATION_ERROR + ex.getMessage());
        }
    }


    private Validator newXmlValidator() throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream xsdStream = getClass().getResourceAsStream(xsdSchemaPath);
        Schema schema = factory.newSchema(new StreamSource(xsdStream));
        return schema.newValidator();
    }
}
