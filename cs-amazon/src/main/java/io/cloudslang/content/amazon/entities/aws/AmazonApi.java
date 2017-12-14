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

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum AmazonApi {
    EC2("ec2"),
    ELASTIC_LOAD_BALANCING("elasticloadbalancing"),
    S3("s3");

    private final String value;

    AmazonApi(String value) {
        this.value = value;
    }

    public static String getApiValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return EC2.getValue();
        }

        for (AmazonApi api : AmazonApi.values()) {
            if (api.getValue().equalsIgnoreCase(input)) {
                return api.getValue();
            }
        }

        throw new RuntimeException("Invalid Amazon API service value: [" + input + "]. Valid values: ec2, elasticloadbalancing, s3.");
    }

    private String getValue() {
        return value;
    }
}
