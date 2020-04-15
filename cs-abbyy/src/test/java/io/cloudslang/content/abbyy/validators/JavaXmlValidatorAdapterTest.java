/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;

@PrepareForTest({SchemaFactory.class, JavaxXmlValidatorAdapter.class})
public class JavaXmlValidatorAdapterTest extends AbbyyResultValidatorTest {

    @Mock
    private Validator validatorMock;

    @Override
    protected AbbyyResultValidator newSutInstance() throws Exception {
        Schema schemaMock = PowerMockito.mock(Schema.class);
        PowerMockito.when(schemaMock.newValidator()).thenReturn(validatorMock);
        SchemaFactory factoryMock = PowerMockito.mock(SchemaFactory.class);
        PowerMockito.when(factoryMock.newSchema(any(Source.class))).thenReturn(schemaMock);
        PowerMockito.mockStatic(SchemaFactory.class);
        PowerMockito.when(SchemaFactory.class, "newInstance", anyString()).thenReturn(factoryMock);

        return new JavaxXmlValidatorAdapter(MiscConstants.ABBYY_XML_RESULT_XSD_SCHEMA_PATH);
    }

    @Test
    public void constructor_xsdSchemaPathIsNull_IllegalArgumentException() throws SAXException {
        //Arrange
        final String xsdSchemaPath = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        new JavaxXmlValidatorAdapter(xsdSchemaPath);
    }

    @Test
    public void validate_resultIsValid_nullReturned() throws Exception {
        //Arrange
        final String resultToBeValidated = "dummy";
        //Act
        AbbyySdkException result = this.sut.validate(resultToBeValidated);
        //Assert
        assertNull(result);
    }


    @Test
    public void validate_resultIsInvalid_AbbyySdkExceptionReturned() throws Exception {
        //Arrange
        final String resultToBeValidated = "dummy";
        doThrow(SAXException.class).when(this.validatorMock).validate(any(Source.class));
        //Act
        AbbyySdkException result = this.sut.validate(resultToBeValidated);
        //Assert
        assertNotNull(result);
    }
}
