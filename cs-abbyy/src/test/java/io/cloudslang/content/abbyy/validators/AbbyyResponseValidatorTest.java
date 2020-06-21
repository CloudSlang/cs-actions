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
public abstract class AbbyyResponseValidatorTest {

    AbbyyResponseValidator sut;
    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setUp() {
        this.sut = newSutInstance();
    }


    @Test
    public void validate_noValidationError_NullReturned() {
        //Arrange
        final AbbyyResponse response = mock(AbbyyResponse.class);
        final AbbyyResponseValidator sut = mock(AbbyyResponseValidator.class);
        when(sut.validate(eq(response))).thenCallRealMethod();
        //Act
        ValidationException ex = sut.validate(response);
        //Assert
        assertNull(ex);
    }


    abstract AbbyyResponseValidator newSutInstance();
}
