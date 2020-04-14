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
package io.cloudslang.content.abby.entities;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class RegionTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void fromString_nullStr_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        Region.fromString(str);
    }


    @Test
    public void fromString_invalidLength_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        Region.fromString(str);
    }


    @Test
    public void fromString_coordinateIsNotNumber_NumberFormatException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1,asd";
        //Assert
        exception.expect(NumberFormatException.class);
        //Act
        Region.fromString(str);
    }


    @Test
    public void fromString_coordinateIsInvalidNumber_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "-1,-1,-1,-2";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        Region.fromString(str);
    }


    @Test
    public void fromString_illegalCoordinateCombination_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "2,-1,1,-1";
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        Region.fromString(str);
    }


    @Test
    public void fromString_validCoordinates_IllegalArgumentException() throws Exception {
        //Arrange
        final String str = "0,1,2,3";
        //Act
        Region result = Region.fromString(str);
        //Assert
        assertEquals(0, result.getLeft());
        assertEquals(1, result.getTop());
        assertEquals(2, result.getRight());
        assertEquals(3, result.getBottom());
    }
}
