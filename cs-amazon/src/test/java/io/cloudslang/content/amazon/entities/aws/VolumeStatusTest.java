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

package io.cloudslang.content.amazon.entities.aws;

import io.cloudslang.content.amazon.entities.aws.VolumeStatus;
import org.junit.Test;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public class VolumeStatusTest {

    private static final String RANDOM_STRING = "randomString";
    private static final String CREATING = "creating";
    private static final String AVAILABLE = "available";
    private static final String IN_USE = "in-use";
    private static final String DELETING = "deleting";
    private static final String DELETED = "deleted";
    private static final String ERROR = "error";

    @Test
    public void volumeStatusTest() {
        assertEquals(CREATING, VolumeStatus.getValue(CREATING));
        assertEquals(AVAILABLE, VolumeStatus.getValue(AVAILABLE));
        assertEquals(IN_USE, VolumeStatus.getValue(IN_USE));
        assertEquals(DELETING, VolumeStatus.getValue(DELETING));
        assertEquals(DELETED, VolumeStatus.getValue(DELETED));
        assertEquals(ERROR, VolumeStatus.getValue(ERROR));

        assertEquals(NOT_RELEVANT, VolumeStatus.getValue(EMPTY));
    }

    @Test(expected = RuntimeException.class)
    public void volumeStatusTestException() {
        VolumeStatus.getValue(RANDOM_STRING);
    }
}
