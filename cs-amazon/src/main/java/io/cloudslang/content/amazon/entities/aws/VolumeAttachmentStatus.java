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

import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public enum VolumeAttachmentStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static final String INVALID_ATTACHMENT_STATUS_VALUE_FORMAT = "Invalid attachment.status value: [%s]. Valid values: attaching, attached, detaching, detached.";

    public static String getValue(@NotNull final String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (final VolumeAttachmentStatus volumeAttachmentStatus : VolumeAttachmentStatus.values()) {
            if (volumeAttachmentStatus.name().equalsIgnoreCase(input)) {
                return volumeAttachmentStatus.name().toLowerCase();
            }
        }

        throw new RuntimeException(String.format(INVALID_ATTACHMENT_STATUS_VALUE_FORMAT, input));
    }
}
