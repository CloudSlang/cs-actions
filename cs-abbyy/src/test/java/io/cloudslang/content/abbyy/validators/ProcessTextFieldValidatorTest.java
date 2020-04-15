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

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.entities.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessTextFieldValidatorTest extends AbbyyRequestValidatorTest<ProcessTextFieldInput> {

    @Mock
    private ProcessTextFieldInput requestMock;


    @Override
    protected AbbyyRequestValidator<ProcessTextFieldInput> newSutInstance() throws Exception {
        return new ProcessTextFieldValidator();
    }


    @Test
    public void validate_destFileAlreadyExists_ValidationExceptionReturned() {
        //Arrange
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.exists()).thenReturn(true);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.DESTINATION_FILE_ALREADY_EXISTS, ex.getMessage());
    }


    @Test
    public void validate_destFileParentDoesNotExist_ValidationExceptionReturned() {
        //Arrange
        File destinationFolderMock = mock(File.class);
        when(destinationFolderMock.exists()).thenReturn(false);
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.exists()).thenReturn(false);
        when(destinationFileMock.getParentFile()).thenReturn(destinationFolderMock);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNotNull(ex);
        assertEquals(ExceptionMsgs.DESTINATION_FOLDER_DOES_NOT_EXIST, ex.getMessage());
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
    public void validate_validRequest_ValidationExceptionReturned() {
        //Arrange
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

        //Act
        ValidationException ex = this.sut.validate(this.requestMock);

        //Assert
        assertNull(ex);
    }

}
