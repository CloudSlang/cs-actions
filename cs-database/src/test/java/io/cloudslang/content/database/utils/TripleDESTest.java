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

package io.cloudslang.content.database.utils;

import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 1/29/17.
 */
public class TripleDESTest {
    @Test
    public void md5Hash() throws Exception {
        assertArrayEquals(TripleDES.md5Hash("ana123"),
                new byte[]{83, -112, 72, -99, -93, -105, 28, -69, -51, 34, -63, 89, -43, 77, 36, -38, 83, -112, 72, -99, -93, -105, 28, -69});
    }

    @Test
    public void encryptPassword() throws Exception {
        assertEquals(TripleDES.encryptPassword("ana123!@#"), "BsPbYGM8+FqTH7yl3wYsqg==");
    }

    @Test
    public void encryptString() throws Exception {
        assertArrayEquals(TripleDES.encryptString("ana123!@#".getBytes()),
                new byte[]{6, -61, -37, 96, 99, 60, -8, 90, -109, 31, -68, -91, -33, 6, 44, -86});
    }

}