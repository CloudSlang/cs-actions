/*
 * Copyright 2020-2024 Open Text
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


package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.constants.XsdSchemas;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.xml.sax.SAXException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
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

        String resultMock = StringUtils.repeat("a", (int) Limits.MAX_SIZE_OF_XML_FILE + 1);

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, resultMock);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateAfterDownload_resultXmlIsInvalid_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String xml = "<document";

        when(abbyyInput.getResponseCharacterSet()).thenReturn(StandardCharsets.UTF_8.displayName());

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, xml);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateAfterDownload_resultXmlIsValid_nullReturned() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String xml = "<document xmlns=\"http://www.abbyy.com/FineReader_xml/FineReader10-schema-v1.xml\" version=\"1.0\" producer=\"test\"/>";

        when(abbyyInput.getResponseCharacterSet()).thenReturn(StandardCharsets.UTF_8.displayName());

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
