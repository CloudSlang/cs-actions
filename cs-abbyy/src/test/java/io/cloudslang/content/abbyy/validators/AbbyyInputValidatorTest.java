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

import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.cloudslang.content.abbyy.constants.ExceptionMsgs.SOURCE_FILE_DOES_NOT_EXIST;
import static io.cloudslang.content.abbyy.constants.ExceptionMsgs.SOURCE_FILE_IS_NOT_REGULAR_FILE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbbyyInputValidatorTest<R extends AbbyyInput> {

    AbbyyInputValidator<R> sut;
    @TempDir
    Path tempDir;
    Path validSourceFile;


    @BeforeEach
    public void setUp() throws IOException {
        this.sut = newSutInstance();
        this.validSourceFile = Files.createTempFile(tempDir, "source", ".tmp");
    }


    @Test
    public void validate_locationIdIsNull_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getLocationId()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_applicationIdIsBlank_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getApplicationId()).thenReturn(" ");
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_passwordIsBlank_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getPassword()).thenReturn(" ");
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_proxyPortIsNegativeAndNotMinusOne_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getProxyPort()).thenReturn((short) -2);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_proxyPortIsMinusOne_nullReturned() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getProxyPort()).thenReturn((short) -1);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_connectTimeoutIsNegative_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getConnectTimeout()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_socketTimeoutIsNegative_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getSocketTimeout()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_connectionsMaxPerRouteIsNegative_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getConnectionsMaxPerRoute()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_connectionsMaxTotalIsNegative_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getConnectionsMaxTotal()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_destinationFileIsNull_nullReturned() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getDestinationFile()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_sourceFileIsNull_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();
        when(abbyyRequestMock.getSourceFile()).thenReturn(null);

        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);

        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_sourceFileDoesNotExist_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();

        when(abbyyRequestMock.getSourceFile()).thenReturn(tempDir.resolve("missing-source"));

        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);

        //Assert
        assertEquals(SOURCE_FILE_DOES_NOT_EXIST, ex.getMessage());
    }


    @Test
    public void validate_sourceFileIsNotFile_ValidationException() {
        //Arrange
        R abbyyRequestMock = mockAbbyyRequest();

        when(abbyyRequestMock.getSourceFile()).thenReturn(tempDir);

        //Act
        ValidationException ex = this.sut.validate(abbyyRequestMock);

        //Assert
        assertEquals(SOURCE_FILE_IS_NOT_REGULAR_FILE, ex.getMessage());
    }


    @Test
    public void validate_validRequest_nullReturned() {
        //Arrange
        R request = mockAbbyyRequest();
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }


    abstract AbbyyInputValidator<R> newSutInstance();

    abstract R mockAbbyyRequest();
}
