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

import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.constants.XsdSchemas;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XmlResultValidator.class, SchemaFactory.class})
public class XmlResultValidatorTest extends AbbyyResultValidatorTest {

    @Mock
    private AbbyyApi abbyyApiMock;

    @Test
    public void validateBeforeDownload_resultSizeIsTooBig_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInput), eq(url), any(ExportFormat.class)))
                .thenReturn(Limits.MAX_SIZE_OF_XML_FILE + 1);

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInput, url);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateBeforeDownload_noValidationError_nullReturned() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String url = "url";

        //Act
        ValidationException ex = sut.validateBeforeDownload(abbyyInput, url);

        //Assert
        assertNull(ex);
    }


    @Test
    public void validateAfterDownload_resultSizeIsTooBig_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);

        String resultMock = PowerMockito.mock(String.class);
        when(resultMock.getBytes()).thenReturn(new byte[(int)Limits.MAX_SIZE_OF_XML_FILE + 1]);

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, resultMock);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateAfterDownload_resultXmlIsInvalid_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String xml = "";

        SchemaFactory schemaFactoryMock = mock(SchemaFactory.class);
        PowerMockito.mockStatic(SchemaFactory.class);
        PowerMockito.when(SchemaFactory.newInstance(anyString())).thenReturn(schemaFactoryMock);

        StreamSource streamSourceMock = mock(StreamSource.class);
        PowerMockito.whenNew(StreamSource.class).withAnyArguments().thenReturn(streamSourceMock);

        Schema schemaMock = mock(Schema.class);
        when(schemaFactoryMock.newSchema(any(StreamSource.class))).thenReturn(schemaMock);

        Validator validatorMock = mock(Validator.class);
        when(schemaMock.newValidator()).thenReturn(validatorMock);

        doThrow(SAXException.class).when(validatorMock).validate(any(Source.class));

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, xml);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateAfterDownload_resultXmlIsValid_nullReturned() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String xml = "";

        SchemaFactory schemaFactoryMock = mock(SchemaFactory.class);
        PowerMockito.mockStatic(SchemaFactory.class);
        PowerMockito.when(SchemaFactory.newInstance(anyString())).thenReturn(schemaFactoryMock);

        StreamSource streamSourceMock = mock(StreamSource.class);
        PowerMockito.whenNew(StreamSource.class).withAnyArguments().thenReturn(streamSourceMock);

        Schema schemaMock = mock(Schema.class);
        when(schemaFactoryMock.newSchema(any(StreamSource.class))).thenReturn(schemaMock);

        Validator validatorMock = mock(Validator.class);
        when(schemaMock.newValidator()).thenReturn(validatorMock);

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, xml);

        //Assert
        assertNull(ex);
    }


    @Override
    AbbyyResultValidator newSutInstance() {
        return new XmlResultValidator(abbyyApiMock, XsdSchemas.PROCESS_IMAGE);
    }
}
