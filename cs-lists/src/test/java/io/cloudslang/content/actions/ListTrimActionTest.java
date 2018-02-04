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

package io.cloudslang.content.actions;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListTrimActionTest {

    @Test
    public void testTrimList() throws IOException {
        String listString = "1A,2B,3C,4D,5E,6F,7G,8H,9I,10J";
        String listInteger = "1,2,3,4,5,6,7,8,9,10";

        Map<String, String> result = new ListTrimAction().trimList(listString, ",", "20");
        assertEquals("success", result.get("response"));
        assertEquals("1A,2B,3C,4D,5E,6F,7G,8H", result.get("result"));

        Map<String, String> result2 = new ListTrimAction().trimList(listInteger, ",", "20");
        assertEquals("success", result2.get("response"));
        assertEquals("2,3,4,5,6,7,8,9", result2.get("result"));
    }
}
