/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
public class BlockDeviceMappingStatusTest {

    private static final String ATTACHING = "attaching";
    private static final String ATTACHED = "attached";
    private static final String DETACHING = "detaching";
    private static final String DETACHED = "detached";
    private static final String WRONG_VALUE = "wrongValue";

    @Test
    public void blockDeviceMappingStatusTest() {
        assertEquals(ATTACHING, BlockDeviceMappingStatus.getValue(ATTACHING));
        assertEquals(ATTACHED, BlockDeviceMappingStatus.getValue(ATTACHED));
        assertEquals(DETACHING, BlockDeviceMappingStatus.getValue(DETACHING));
        assertEquals(DETACHED, BlockDeviceMappingStatus.getValue(DETACHED));

        assertEquals(NOT_RELEVANT, BlockDeviceMappingStatus.getValue(EMPTY));
    }

    @Test(expected = RuntimeException.class)
    public void blockDeviceMappingStatusTestException() {
        VolumeAttachmentStatus.getValue(WRONG_VALUE);
    }
}
