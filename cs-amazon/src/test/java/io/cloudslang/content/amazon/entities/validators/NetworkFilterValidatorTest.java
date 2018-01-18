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

package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.aws.NetworkFilter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tirla Alin
 * 2/16/2017.
 */
public class NetworkFilterValidatorTest {


    private static final String AVAILABLE = "available";
    private static final String IN_USE = "in-use";
    private static final String WRONG_VALUE = "WRONG";
    private static final String ATTACHING = "attaching";
    private static final String ATTACHED = "attached";
    private static final String DETACHING = "detaching";
    private static final String DETACHED = "detached";

    private NetworkFilterValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new NetworkFilterValidator();
    }

    @Test
    public void testGoodValues() {
        assertEquals(AVAILABLE, validator.getFilterValue(NetworkFilter.STATUS, AVAILABLE));
        assertEquals(IN_USE, validator.getFilterValue(NetworkFilter.STATUS, IN_USE));

        assertEquals(ATTACHED, validator.getFilterValue(NetworkFilter.ATTACHMENT_STATUS, ATTACHED));
        assertEquals(ATTACHING, validator.getFilterValue(NetworkFilter.ATTACHMENT_STATUS, ATTACHING));
        assertEquals(DETACHED, validator.getFilterValue(NetworkFilter.ATTACHMENT_STATUS, DETACHED));
        assertEquals(DETACHING, validator.getFilterValue(NetworkFilter.ATTACHMENT_STATUS, DETACHING));
    }

    @Test(expected = RuntimeException.class)
    public void testBadValues() {
        validator.getFilterValue(NetworkFilter.ATTACHMENT_STATUS, WRONG_VALUE);
    }
}
