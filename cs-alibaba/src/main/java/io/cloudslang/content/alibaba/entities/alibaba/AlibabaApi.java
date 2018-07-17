/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.entities.alibaba;

import static org.apache.commons.lang3.StringUtils.isBlank;

public enum AlibabaApi {

        ECS("ecs");

        private final String value;

    AlibabaApi(String value) {
            this.value = value;
        }

        public static String getApiValue(String input) throws RuntimeException {
            if (isBlank(input)) {
                return ECS.getValue();
            }

            for (AlibabaApi api : AlibabaApi.values()) {
                if (api.getValue().equalsIgnoreCase(input)) {
                    return api.getValue();
                }
            }

            throw new RuntimeException("Invalid Alibaba API service value: [" + input + "]. Valid value: ecs.");
        }

        private String getValue() {
            return value;
        }

}
