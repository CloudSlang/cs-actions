/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.json.actions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by nane on 2/9/2016.
 */
public class RemoveEmptyElementTest {

    private static final String EXCEPTION = "exception";
    private static final String RETURN_RESULT = "returnResult";
    private static final String RETURN_CODE = "returnCode";


    private RemoveEmptyElementAction actionUnderTest;
    private Map<String, String> returnResult;

    @Before
    public void setUp() {
        actionUnderTest = new RemoveEmptyElementAction();
    }

    @After
    public void tearDown() {
        actionUnderTest = null;
        returnResult = null;
    }

    @Test
    public void givenValidJsonInputThenReturnSuccess() {
        String expectedResultString = "{\"expected\":\"value\"}";
        String jsonInput = "{\"removed1\":\"\", \"removed2\":[], \"removed3\":null, \"expected\":\"value\"} ";
        returnResult = actionUnderTest.removeEmptyElements(jsonInput);

        assertEquals("0", returnResult.get(RETURN_CODE));
        assertEquals(expectedResultString, returnResult.get(RETURN_RESULT));
    }

    @Test
    public void givenInvalidJsonThenReturnFailure() {
        String invalidJsonInput = "{\"removed1\":\"\", \"removed2\":[] \"removed3\":null \"expected\":\"value\"} ";
        returnResult = actionUnderTest.removeEmptyElements(invalidJsonInput);

        assertEquals("-1", returnResult.get(RETURN_CODE));
        assertNotNull(returnResult.get(RETURN_RESULT));
        assertNotNull(returnResult.get(EXCEPTION));
    }

}
