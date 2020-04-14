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
package io.cloudslang.content.abby.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class BuilderUtilsTest {

    @Test
    public void buildDescription_descrIsNull_emptyStringReturned() {
        //Arrange
        final String descr = null;
        //Act
        String result = BuilderUtils.buildDescription(descr);
        //Assert
        assertEquals(StringUtils.EMPTY, result);
    }


    @Test
    public void buildDescription_descrHasMaxNrOfChars_descrIsNotTrimmed() {
        //Arrange
        final String descr = getDescription(255);
        //Act
        String result = BuilderUtils.buildDescription(descr);
        //Assert
        assertEquals(descr, result);
    }


    @Test
    public void buildDescription_descrHasMoreThanMaxNrOfChars_descrIsTrimmed() {
        //Arrange
        final String descr = getDescription(256);
        //Act
        String result = BuilderUtils.buildDescription(descr);
        //Assert
        assertEquals(255, result.length());
    }


    private String getDescription(int length) {
        StringBuilder descrBuilder = new StringBuilder();
        for(int i = 1; i <= length; i++) {
            descrBuilder.append("\n");
        }
        return descrBuilder.toString();
    }
}
