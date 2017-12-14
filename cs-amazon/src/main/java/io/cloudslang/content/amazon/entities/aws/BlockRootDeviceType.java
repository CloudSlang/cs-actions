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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by TusaM
 * 11/1/2016.
 */
public enum BlockRootDeviceType {
    EBS("ebs"),
    INSTANCE_STORE("instance-store");

    private final String value;

    BlockRootDeviceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (BlockRootDeviceType member : BlockRootDeviceType.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }

        throw new RuntimeException("Unrecognized block root device type value: [" + input + "]. Valid values are: ebs, instance-store.");
    }
}
