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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PdfResultValidator.class})
public class PdfResultValidatorTest extends AbbyyResultValidatorTest {

    @Mock
    private AbbyyApi abbyyApiMock;


    @Test
    public void validateBeforeDownload_resultSizeIsTooBig_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInput), eq(url), any(ExportFormat.class))).thenReturn(Limits.MAX_SIZE_OF_PDF_FILE + 1);

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInput, url);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateBeforeDownload_resultIsNotPdf_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInput), eq(url), any(ExportFormat.class)))
                .thenReturn(Limits.MAX_SIZE_OF_PDF_FILE - 1);
        when(this.abbyyApiMock.getResultChunk(eq(abbyyInput), eq(url), any(ExportFormat.class), anyInt(), anyInt()))
                .thenReturn("1234");

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInput, url);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateBeforeDownload_resultValid_NullReturned() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String url = "url";

        when(this.abbyyApiMock.getResultSize(eq(abbyyInput), eq(url), any(ExportFormat.class)))
                .thenReturn(Limits.MAX_SIZE_OF_PDF_FILE - 1);
        when(this.abbyyApiMock.getResultChunk(eq(abbyyInput), eq(url), any(ExportFormat.class), anyInt(), anyInt()))
                .thenReturn("%PDF");

        //Act
        ValidationException ex = this.sut.validateBeforeDownload(abbyyInput, url);

        //Assert
        assertNull(ex);
    }


    @Test
    public void validateAfterDownload_resultSizeIsTooBig_ValidationException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String targetPath = "dummy";

        PowerMockito.mockStatic(Paths.class);
        PowerMockito.when(Paths.get(anyString())).thenReturn(null);
        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.size(any(Path.class))).thenReturn(Limits.MAX_SIZE_OF_PDF_FILE + 1);

        //Act
        ValidationException ex = this.sut.validateAfterDownload(abbyyInput, targetPath);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validateAfterDownload_resultIsValid_nullReturned() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String result = "result";

        PowerMockito.mockStatic(Paths.class);
        PowerMockito.when(Paths.get(anyString())).thenReturn(null);
        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.size(any(Path.class))).thenReturn(Limits.MAX_SIZE_OF_PDF_FILE - 1);

        //Act
        ValidationException ex = sut.validateAfterDownload(abbyyInput, result);

        //Assert
        assertNull(ex);
    }


    @Override
    AbbyyResultValidator newSutInstance() {
        return new PdfResultValidator(abbyyApiMock);
    }
}
