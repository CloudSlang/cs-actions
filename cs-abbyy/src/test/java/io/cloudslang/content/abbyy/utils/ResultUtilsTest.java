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
package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.constants.ReturnCodes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class ResultUtilsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void createNewEmptyMap_Invoked_AllOutputsArePresent() {
        //Act
        Map<String, String> map = ResultUtils.createNewEmptyMap();
        //Assert
        assertTrue(map.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertTrue(map.containsKey(OutputNames.TXT_RESULT));
        assertTrue(map.containsKey(OutputNames.XML_RESULT));
        assertTrue(map.containsKey(OutputNames.PDF_URL));
        assertTrue(map.containsKey(OutputNames.TASK_ID));
        assertTrue(map.containsKey(OutputNames.CREDITS));
        assertTrue(map.containsKey(OutputNames.STATUS_CODE));
        assertTrue(map.containsKey(io.cloudslang.content.constants.OutputNames.EXCEPTION));
        assertTrue(map.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
        assertTrue(map.containsKey(OutputNames.TIMED_OUT));
    }

    @Test
    public void fromException_exceptionIsNotAbbyySdkException_exceptionIsPresentInOutput() {
        //Arrange
        final String exMsg = "msg";
        final Exception ex = new Exception(exMsg);

        //Act
        Map<String, String> result = ResultUtils.fromException(ex);

        //Assert
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertTrue(result.containsKey(OutputNames.TXT_RESULT));
        assertTrue(result.containsKey(OutputNames.XML_RESULT));
        assertTrue(result.containsKey(OutputNames.PDF_URL));
        assertTrue(result.containsKey(OutputNames.TASK_ID));
        assertTrue(result.containsKey(OutputNames.CREDITS));
        assertTrue(result.containsKey(OutputNames.STATUS_CODE));
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.EXCEPTION));
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
        assertTrue(result.containsKey(OutputNames.TIMED_OUT));

        assertEquals(exMsg, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertTrue(result.get(io.cloudslang.content.constants.OutputNames.EXCEPTION).contains(exMsg));
        assertEquals(ReturnCodes.FAILURE, result.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
    }


    @Test
    public void fromException_exceptionIsAbbyySdkException_exceptionAndResultIsPresentInOutput() {
        //Arrange
        final String exMsg = "msg";
        final Map<String, String> exMap = new HashMap<>();
        exMap.put(OutputNames.TIMED_OUT, "tru");
        final Exception ex = new AbbyySdkException(exMsg, exMap);

        //Act
        Map<String, String> result = ResultUtils.fromException(ex);

        //Assert
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertTrue(result.containsKey(OutputNames.TXT_RESULT));
        assertTrue(result.containsKey(OutputNames.XML_RESULT));
        assertTrue(result.containsKey(OutputNames.PDF_URL));
        assertTrue(result.containsKey(OutputNames.TASK_ID));
        assertTrue(result.containsKey(OutputNames.CREDITS));
        assertTrue(result.containsKey(OutputNames.STATUS_CODE));
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.EXCEPTION));
        assertTrue(result.containsKey(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
        assertTrue(result.containsKey(OutputNames.TIMED_OUT));

        assertEquals(exMsg, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertTrue(result.get(io.cloudslang.content.constants.OutputNames.EXCEPTION).contains(exMsg));
        assertEquals(ReturnCodes.FAILURE, result.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));

        assertEquals(exMap.get(OutputNames.TIMED_OUT), result.get(OutputNames.TIMED_OUT));
    }
}
