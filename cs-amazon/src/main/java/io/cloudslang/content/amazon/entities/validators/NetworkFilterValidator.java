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

import io.cloudslang.content.amazon.entities.aws.NetworkInterfaceAttachmentStatus;
import io.cloudslang.content.amazon.entities.aws.NetworkInterfaceStatus;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.amazon.entities.aws.NetworkFilter.ATTACHMENT_STATUS;
import static io.cloudslang.content.amazon.entities.aws.NetworkFilter.STATUS;

/**
 * Created by Tirla Alin
 * 2/16/2017.
 */
public class NetworkFilterValidator implements FilterValidator {
    @Override
    @NotNull
    public String getFilterValue(@NotNull final String filterName, @NotNull final String filterValue) {
        switch (filterName) {
            case ATTACHMENT_STATUS: {
                return NetworkInterfaceAttachmentStatus.getValue(filterValue);
            }
            case STATUS: {
                return NetworkInterfaceStatus.getValue(filterValue);
            }
            default: {
                return filterValue;
            }
        }
    }
}
