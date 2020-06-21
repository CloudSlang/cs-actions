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

import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class AbbyyResponseValidatorImplTest extends AbbyyResponseValidatorTest {


    @Test
    public void validate_responseHasEmptyTaskId_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getTaskId()).thenReturn(StringUtils.EMPTY);
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_responseHasNegativeNrOfCredits_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getCredits()).thenReturn(-1);
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_taskStatusIsNull_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getTaskStatus()).thenReturn(null);
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_resultUrlsMissing_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getResultUrls()).thenReturn(Collections.<String>emptyList());
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_resultUrlIsBlank_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getResultUrls()).thenReturn(Arrays.asList("asd", " "));
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_responseHasNegativeEstimatedProcessingTime_ValidationException() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        when(response.getEstimatedProcessingTime()).thenReturn(-1L);
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNotNull(ex);
    }


    @Test
    public void validate_responseIsValid_NullReturned() {
        //Arrange
        final AbbyyResponse response = mockAbbyyResponse();
        //Act
        ValidationException ex = this.sut.validate(response);
        //Assert
        assertNull(ex);
    }


    private AbbyyResponse mockAbbyyResponse() {
        final AbbyyResponse response = mock(AbbyyResponse.class);

        when(response.getTaskId()).thenReturn("taskId");
        when(response.getCredits()).thenReturn(1);
        when(response.getTaskStatus()).thenReturn(AbbyyResponse.TaskStatus.COMPLETED);
        when(response.getResultUrls()).thenReturn(Collections.singletonList("url"));
        when(response.getEstimatedProcessingTime()).thenReturn(1L);

        return response;
    }


    @Override
    AbbyyResponseValidator newSutInstance() {
        return new AbbyyResponseValidatorImpl();
    }
}
