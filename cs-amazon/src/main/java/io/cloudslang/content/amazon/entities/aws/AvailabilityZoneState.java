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

/**
 * Created by TusaM
 * 11/4/2016.
 */
public enum AvailabilityZoneState {
    AVAILABLE,
    INFORMATION,
    IMPAIRED,
    UNAVAILABLE;

    public static String getValue(String input) throws RuntimeException {
        for (AvailabilityZoneState member : AvailabilityZoneState.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid architecture value: [" + input + "]. Valid values: available, information, impaired, unavailable.");
    }
}
