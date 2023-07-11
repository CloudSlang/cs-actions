/*
 * Copyright 2020-2023 Open Text
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
import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.inputs.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({ProcessTextFieldInputValidator.class})
public class ProcessTextFieldInputValidatorTest extends AbbyyInputValidatorTest<ProcessTextFieldInput> {

    @Test
    public void validate_languagesIsNull_nullReturned() {
        //Arrange
        ProcessTextFieldInput request = mockAbbyyRequest();
        when(request.getLanguages()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_languagesContainsBlankLanguage_ValidationException() {
        //Arrange
        ProcessTextFieldInput request = mockAbbyyRequest();
        when(request.getLanguages()).thenReturn(Arrays.asList("asd", " "));
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_placeholdersCountIsNegative_ValidationException() {
        //Arrange
        ProcessTextFieldInput request = mockAbbyyRequest();
        when(request.getPlaceholdersCount()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_descriptionIsEmpty_nullReturned() {
        //Arrange
        ProcessTextFieldInput request = mockAbbyyRequest();
        when(request.getDescription()).thenReturn(StringUtils.EMPTY);
        //Act
        ValidationException ex = this.sut.validate(request);
        //Assert
        assertNull(ex);
    }


    @Test
    public void validate_descriptionSizeExceeded_ValidationException() {
        //Arrange
        ProcessTextFieldInput request = mockAbbyyRequest();

        String description = StringUtils.repeat("*", Limits.MAX_SIZE_OF_DESCR + 1);
        when(request.getDescription()).thenReturn(description);

        //Act
        ValidationException ex = this.sut.validate(request);

        //Assert
        assertNotNull(ex);
    }


    @Override
    AbbyyInputValidator<ProcessTextFieldInput> newSutInstance() {
        return new ProcessTextFieldInputValidator();
    }


    @Override
    ProcessTextFieldInput mockAbbyyRequest() {
        ProcessTextFieldInput requestMock = mock(ProcessTextFieldInput.class);

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getApplicationId()).thenReturn("dummy");
        when(requestMock.getPassword()).thenReturn("dummy");
        when(requestMock.getProxyPort()).thenReturn((short) 20);
        when(requestMock.getDestinationFile()).thenReturn(null);
        Path sourceFileMock = Paths.get(StringUtils.EMPTY);
        PowerMockito.when(Files.exists(sourceFileMock)).thenReturn(true);
        PowerMockito.when(Files.isRegularFile(sourceFileMock)).thenReturn(true);
        when(requestMock.getSourceFile()).thenReturn(sourceFileMock);
        when(requestMock.getConnectTimeout()).thenReturn(0);
        when(requestMock.getSocketTimeout()).thenReturn(0);
        when(requestMock.getConnectionsMaxPerRoute()).thenReturn(20);
        when(requestMock.getConnectionsMaxTotal()).thenReturn(20);

        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("language"));
        when(requestMock.getDescription()).thenReturn("asd");

        return requestMock;
    }
}
