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
package io.cloudslang.content.abbyy.services;

import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.TimeoutException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.http.AbbyyApi;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.validators.AbbyyInputValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Thread.class})
public abstract class AbbyyServiceTest<R extends AbbyyInput> {

    AbbyyService<R> sut;
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    AbbyyInputValidator<R> requestValidatorMock;
    @Mock
    AbbyyApi abbyyApiMock;


    @Before
    public void setUp() {
        this.sut = newSutInstance();
        PowerMockito.mockStatic(Thread.class);
    }


    @Test
    public void execute_requestIsInvalid_ValidationException() throws Exception {
        //Arrange
        R request = mockAbbyyRequest();
        ValidationException expectedEx = new ValidationException("msg");
        when(this.requestValidatorMock.validate(eq(request))).thenReturn(expectedEx);

        try {
            //Act
            this.sut.execute(request);

            //Assert
            fail();
        } catch (ValidationException ex) {
            assertEquals(expectedEx, ex);
        }
    }


    @Test
    public void execute_notEnoughCredits_AbbyySdkException() throws Exception {
        //Arrange
        R request = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getTaskStatus()).thenReturn(AbbyyResponse.TaskStatus.NOT_ENOUGH_CREDITS);
        when(this.abbyyApiMock.request(eq(request))).thenReturn(responseMock);

        //Assert
        this.exception.expect(AbbyySdkException.class);

        //Act
        this.sut.execute(request);
    }


    @Test
    public void execute_timeout_TimeoutException() throws Exception {
        //Arrange
        R request = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getTaskStatus())
                .thenReturn(AbbyyResponse.TaskStatus.QUEUED)
                .thenReturn(AbbyyResponse.TaskStatus.IN_PROGRESS);
        when(this.abbyyApiMock.request(eq(request))).thenReturn(responseMock);

        when(this.abbyyApiMock.getTaskStatus(eq(request), anyString())).thenReturn(responseMock);

        try {
            //Act
            this.sut.execute(request);

            //Assert
            fail();
        } catch (TimeoutException ex) {
            assertEquals(String.valueOf(true), ex.getResultsMap().get(OutputNames.TIMED_OUT));
        }
    }


    @Test
    public void execute_processingFailed_AbbyySdkException() throws Exception {
        //Arrange
        final String expecterErr = "This is the reason";

        R request = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getTaskStatus())
                .thenReturn(AbbyyResponse.TaskStatus.QUEUED)
                .thenReturn(AbbyyResponse.TaskStatus.PROCESSING_FAILED);
        when(responseMock.getErrorMessage()).thenReturn(expecterErr);
        when(this.abbyyApiMock.request(eq(request))).thenReturn(responseMock);

        when(this.abbyyApiMock.getTaskStatus(eq(request), anyString())).thenReturn(responseMock);

        try {
            //Act
            this.sut.execute(request);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(expecterErr));
        }
    }


    AbbyyResponse mockAbbyyResponse() {
        AbbyyResponse responseMock = mock(AbbyyResponse.class);

        when(responseMock.getTaskId()).thenReturn("taskid");
        when(responseMock.getTaskStatus())
                .thenReturn(AbbyyResponse.TaskStatus.QUEUED)
                .thenReturn(AbbyyResponse.TaskStatus.COMPLETED);
        when(responseMock.getErrorMessage()).thenReturn("error");
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("url"));

        return responseMock;
    }


    abstract AbbyyService<R> newSutInstance();

    abstract R mockAbbyyRequest();
}
