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
 * 10/24/2016.
 */
public enum Attribute {
    INSTANCE_TYPE("instanceType"),
    KERNEL("kernel"),
    RAMDISK("ramdisk"),
    USER_DATA("userData"),
    DISABLE_API_TERMINATION("disableApiTermination"),
    INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR("instanceInitiatedShutdownBehavior"),
    ROOT_DEVICE_NAME("rootDeviceName"),
    BLOCK_DEVICE_MAPPING("blockDeviceMapping"),
    PRODUCT_CODES("productCodes"),
    SOURCE_DEST_CHECK("sourceDestCheck"),
    GROUP_SET("groupSet"),
    EBS_OPTIMIZED("ebsOptimized"),
    SRIOV_NET_SUPPORT("sriovNetSupport"),
    ENA_SUPPORT("enaSupport");

    private final String value;

    Attribute(String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }

    public static String getAttribute(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (Attribute attribute : Attribute.values()) {
            if (attribute.getValue().equalsIgnoreCase(input)) {
                return attribute.getValue();
            }
        }

        throw new IllegalArgumentException("Invalid attribute value: [" + input + "]. Valid values: instanceType | kernel | " +
                "ramdisk | userData | disableApiTermination | instanceInitiatedShutdownBehavior | rootDeviceName | " +
                "blockDeviceMapping | productCodes | sourceDestCheck | groupSet | ebsOptimized | sriovNetSupport | enaSupport");
    }
}
