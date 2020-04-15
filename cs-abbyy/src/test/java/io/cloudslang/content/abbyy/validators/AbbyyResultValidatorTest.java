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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
public abstract class AbbyyResultValidatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    AbbyyResultValidator sut;


    @Before
    public void setUp() throws Exception {
        this.sut = this.newSutInstance();
    }


    protected abstract AbbyyResultValidator newSutInstance() throws Exception;


    @Test
    public void validate_resultIsNull_IllegalArgumentException() throws IOException {
        //Arrange
        final String result = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.validate(result);
    }
}
