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

package io.cloudslang.content.utils;

import org.junit.Test;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by moldovas on 7/13/2016.
 */
public class ListProcessorTest {
    @Test
    public void arrayElementsAreNull() throws Exception {
        assertTrue(ListProcessor.arrayElementsAreNull(new String[3]));
        String[] arrayOfWords = {"a","b","c"};
        assertFalse(ListProcessor.arrayElementsAreNull(arrayOfWords));
    }

    @Test
    public void getEmptyUncontainedArray() throws Exception {
        String[] subArray = {"e"};
        String[] containerArray = {"d","e","f"};
        String[] result = ListProcessor.getUncontainedArray(subArray,containerArray, true);
        List<String> listResult =  Arrays.asList(result);
        assertEquals(listResult, Collections.emptyList());
    }

    @Test
    public void getPopulatedUncontainedArray() throws Exception {
        String[] subArray = {"a","e"};
        String[] containerArray = {"d","e","f"};
        String[] result = ListProcessor.getUncontainedArray(subArray,containerArray, true);
        List<String> listResult =  Arrays.asList(result);
        assertEquals(listResult, Collections.singletonList("a"));
    }

    @Test
    public void elementsAreEqual() throws Exception {
        String ignoreCaseTrue = "true";
        String ignoreCaseFalse = "false";
        assertTrue(ListProcessor.elementsAreEqual("A", "a", Boolean.parseBoolean(ignoreCaseTrue)));
        assertFalse(ListProcessor.elementsAreEqual("A", "a", Boolean.parseBoolean(ignoreCaseFalse)));

    }

}