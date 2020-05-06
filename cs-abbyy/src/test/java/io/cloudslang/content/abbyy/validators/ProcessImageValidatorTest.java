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
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.entities.ProcessImageInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessImageValidatorTest extends AbbyyRequestValidatorTest<ProcessImageInput> {

    @Mock
    private ProcessImageInput requestMock;


    @Override
    protected AbbyyRequestValidator<ProcessImageInput> newSutInstance() throws Exception {
        return new ProcessImageValidator();
    }


    @Test
    public void validate_destFileDoesNotExist_ValidationExceptionReturned() {
        //Arrange
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.exists()).thenReturn(false);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);
        //Act
        ValidationException ex = this.sut.validate(this.requestMock);
        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.DESTINATION_FOLDER_DOES_NOT_EXIST, ex.getMessage());
    }


    @Test
    public void validate_destFileIsNotDirectory_ValidationExceptionReturned() {
        //Arrange
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.exists()).thenReturn(true);
        when(destinationFileMock.isDirectory()).thenReturn(false);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);
        //Act
        ValidationException ex = this.sut.validate(this.requestMock);
        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.DESTINATION_FOLDER_IS_NOT_FOLDER, ex.getMessage());
    }


    @Test
    public void validate_sourceFileIsNull_ValidationExceptionReturned() {
        //Arrange
        when(this.requestMock.getSourceFile()).thenReturn(null);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertTrue(ex.getMessage().contains("null"));
    }


    @Test
    public void validate_sourceFileDoesNotExist_ValidationExceptionReturned() {
        //Arrange
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(false);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.SOURCE_FILE_DOES_NOT_EXIST, ex.getMessage());
    }


    @Test
    public void validate_sourceFileIsNotFile_ValidationExceptionReturned() {
        //Arrange
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(false);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.SOURCE_FILE_IS_NOT_FILE, ex.getMessage());
    }


    @Test
    public void validate_tooManyExportFormats_ValidationExceptionReturned() {
        //Arrange
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

        List<ExportFormat> exportFormatsMock = (List<ExportFormat>) mock(List.class);
        when(exportFormatsMock.size()).thenReturn(4);
        when(this.requestMock.getExportFormats()).thenReturn(exportFormatsMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.TOO_MANY_EXPORT_FORMATS, ex.getMessage());
    }


    @Test
    public void validate_validRequest_nullReturned() {
        //Arrange

        File destinationFileMock = mock(File.class);
        when(destinationFileMock.exists()).thenReturn(true);
        when(destinationFileMock.isDirectory()).thenReturn(true);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);

        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

        List<ExportFormat> exportFormatsMock = (List<ExportFormat>) mock(List.class);
        when(exportFormatsMock.size()).thenReturn(3);
        when(this.requestMock.getExportFormats()).thenReturn(exportFormatsMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNull(ex);
    }
}
