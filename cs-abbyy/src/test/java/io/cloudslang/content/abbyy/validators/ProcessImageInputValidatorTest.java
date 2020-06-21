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
import io.cloudslang.content.abbyy.entities.inputs.ProcessImageInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.others.TextType;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({String.class})
public class ProcessImageInputValidatorTest extends AbbyyInputValidatorTest<ProcessImageInput> {

    @Test
    public void validate_destinationFileIsNull_nullReturned() {
        //Arrange
        ProcessImageInput abbyyRequestMock = mockAbbyyRequest();
        PowerMockito.when(abbyyRequestMock.getDestinationFile()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_destinationFolderDoesNotExist_ValidationException() {
        //Arrange
        ProcessImageInput abbyyRequestMock = mockAbbyyRequest();

        File destinationFileMock = mock(File.class);
        PowerMockito.when(destinationFileMock.exists()).thenReturn(false);
        PowerMockito.when(abbyyRequestMock.getDestinationFile()).thenReturn(destinationFileMock);

        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_destinationFolderIsNotDirectory_ValidationException() {
        //Arrange
        ProcessImageInput abbyyRequestMock = mockAbbyyRequest();

        File destinationFolderMock = mock(File.class);
        when(destinationFolderMock.exists()).thenReturn(true);
        when(destinationFolderMock.isDirectory()).thenReturn(false);
        when(abbyyRequestMock.getDestinationFile()).thenReturn(destinationFolderMock);

        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_languagesIsNull_nullReturned() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getLanguages()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_languagesContainsBlankLanguage_ValidationException() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getLanguages()).thenReturn(Arrays.asList("asd", " "));
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_textTypesIsNull_nullReturned() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getTextTypes()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_textTypesContainsNullValue_nullReturned() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getTextTypes()).thenReturn(Arrays.asList(TextType.OCR_A, null));
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_exportFormatsIsEmpty_ValidationException() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getExportFormats()).thenReturn(new ArrayList<ExportFormat>());
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_exportFormatsHasMoreElementsThanAllowed_ValidationException() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();

        List<ExportFormat> exportFormatsMock = new ArrayList<>();
        exportFormatsMock = mock(exportFormatsMock.getClass());
        when(exportFormatsMock.size()).thenReturn(Limits.MAX_NR_OF_EXPORT_FORMATS + 1);

        when(request.getExportFormats()).thenReturn(exportFormatsMock);

        //Act
        ValidationException ex = this.sut.validate(request);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_exportFormatsHasDuplicates_ValidationException() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();

        List<ExportFormat> exportFormats = Arrays.asList(ExportFormat.XML, ExportFormat.XML, ExportFormat.TXT);
        when(request.getExportFormats()).thenReturn(exportFormats);

        //Act
        ValidationException ex = this.sut.validate(request);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_descriptionIsEmpty_nullReturned() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();
        when(request.getDescription()).thenReturn(StringUtils.EMPTY);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }

    @Test
    public void validate_descriptionSizeExceeded_ValidationException() {
        //Arrange
        ProcessImageInput request = mockAbbyyRequest();

        String description = StringUtils.repeat("*", Limits.MAX_SIZE_OF_DESCR + 1);
        when(request.getDescription()).thenReturn(description);

        //Act
        ValidationException ex = this.sut.validate(request);

        //Assert
        assertNotNull(ex);
    }


    @Override
    AbbyyInputValidator<ProcessImageInput> newSutInstance() {
        return new ProcessImageInputValidator();
    }


    @Override
    ProcessImageInput mockAbbyyRequest() {
        ProcessImageInput requestMock = mock(ProcessImageInput.class);

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getApplicationId()).thenReturn("dummy");
        when(requestMock.getPassword()).thenReturn("dummy");
        when(requestMock.getProxyPort()).thenReturn((short) 20);
        when(requestMock.getDestinationFile()).thenReturn(null);
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(requestMock.getSourceFile()).thenReturn(sourceFileMock);
        when(requestMock.getConnectTimeout()).thenReturn(0);
        when(requestMock.getSocketTimeout()).thenReturn(0);
        when(requestMock.getConnectionsMaxPerRoute()).thenReturn(20);
        when(requestMock.getConnectionsMaxTotal()).thenReturn(20);

        when(requestMock.getExportFormats()).thenReturn(Collections.singletonList(ExportFormat.XML));
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("language"));
        when(requestMock.getTextTypes()).thenReturn(Collections.singletonList(TextType.OCR_A));
        when(requestMock.getDescription()).thenReturn("asd");

        return requestMock;
    }
}
