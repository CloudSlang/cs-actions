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

import io.cloudslang.content.abbyy.entities.AbbyyRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public abstract class AbbyyRequestValidatorTest<R extends AbbyyRequest> {

    AbbyyRequestValidator<R> sut;
    @Rule
    protected ExpectedException exception = ExpectedException.none();

    protected abstract AbbyyRequestValidator<R> newSutInstance() throws Exception;

    @Before
    public void setUp() throws Exception {
        this.sut = newSutInstance();
    }

    @Test
    public void validate_requestIsNull_IllegalArgumentException() {
        //Arrange
        final R request = null;
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.validate(request);
    }
}
