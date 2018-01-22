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

import org.junit.Test;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public class VolumeAttachmentStatusTest {

    private static final String ATTACHING = "attaching";
    private static final String ATTACHED = "attached";
    private static final String DETACHING = "detaching";
    private static final String DETACHED = "detached";
    private static final String WRONG_VALUE = "wrongValue";

    @Test
    public void volumeAttachmentStatusTest() {
        assertEquals(ATTACHING, VolumeAttachmentStatus.getValue(ATTACHING));
        assertEquals(ATTACHED, VolumeAttachmentStatus.getValue(ATTACHED));
        assertEquals(DETACHING, VolumeAttachmentStatus.getValue(DETACHING));
        assertEquals(DETACHED, VolumeAttachmentStatus.getValue(DETACHED));

        assertEquals(NOT_RELEVANT, VolumeAttachmentStatus.getValue(EMPTY));
    }

    @Test(expected = RuntimeException.class)
    public void volumeAttachmentStatusTestException() {
        VolumeAttachmentStatus.getValue(WRONG_VALUE);
    }
}
