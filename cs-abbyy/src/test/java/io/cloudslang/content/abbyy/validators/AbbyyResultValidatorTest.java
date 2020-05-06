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
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public abstract class AbbyyResultValidatorTest {

    AbbyyResultValidator sut;
    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() {
        this.sut = newSutInstance();
    }


    @Test
    public void validateBeforeDownload_noValidationError_nullReturned() throws Exception {
        //Arrange
        final AbbyyRequest abbyyInitialRequest = mock(AbbyyRequest.class);
        final String url = "url";

        final AbbyyResultValidator sut = mock(AbbyyResultValidator.class);
        when(sut.validateBeforeDownload(eq(abbyyInitialRequest), eq(url))).thenCallRealMethod();

        //Act
        ValidationException ex = sut.validateBeforeDownload(abbyyInitialRequest, url);

        //Assert
        assertNull(ex);
    }


    @Test
    public void validateAfterDownload_noValidationError_nullReturned() throws Exception {
        //Arrange
        final String result = "result";

        final AbbyyResultValidator sut = mock(AbbyyResultValidator.class);
        when(sut.validateAfterDownload(eq(result))).thenCallRealMethod();

        //Act
        ValidationException ex = sut.validateAfterDownload(result);

        //Assert
        assertNull(ex);
    }


    abstract AbbyyResultValidator newSutInstance();
}
