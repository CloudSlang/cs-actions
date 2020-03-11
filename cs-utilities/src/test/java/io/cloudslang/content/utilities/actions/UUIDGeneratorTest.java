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
/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */

package io.cloudslang.content.utilities.actions;


import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UUIDGeneratorTest {

    /**
     * Test for time based UUID
     */
    @Test
    public void testTimeBasedGenerator() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("", "1");
        String uuidInStr = result.get("returnResult");
        UUID uuid = UUID.fromString(uuidInStr);
        assertEquals(uuidInStr, uuid.toString());
    }

    /**
     * Test for random based UUID 4
     */
    @Test
    public void test4RandomBasedGenerator() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("", "4");
        String uuidInStr = result.get("returnResult");
        UUID uuid = UUID.fromString(uuidInStr);
        assertEquals(uuidInStr, uuid.toString());
    }

    /**
     * Test for random based UUID 5
     */
    @Test
    public void test5RandomBasedGenerator() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("", "5");
        String uuidInStr = result.get("returnResult");
        UUID uuid = UUID.fromString(uuidInStr);
        assertEquals(uuidInStr, uuid.toString());
    }

    /**
     * Test for name based UUID
     */
    @Test
    public void testNameBasedGenerator() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("abc", "3");
        String uuidInStr = result.get("returnResult");
        UUID uuid = UUID.fromString(uuidInStr);
        assertEquals("a9993e36-4706-516a-ba3e-25717850c26c", uuid.toString());
    }

    /**
     * Test for no value
     */
    @Test
    public void testNoValue() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("", "");
        String uuidInStr = result.get("returnResult");
        UUID uuid = UUID.fromString(uuidInStr);
        assertEquals(uuidInStr, uuid.toString());
    }

    /**
     * Test for invalid version
     */
    @Test
    public void testInvalidVersion() {
        UUIDGenerator uuidGenerator = new UUIDGenerator();
        Map<String, String> result = uuidGenerator.execute("", "15");
        String uuidInStr = result.get("returnResult");
        assertEquals("Invalid UUID version", uuidInStr);
    }

}


