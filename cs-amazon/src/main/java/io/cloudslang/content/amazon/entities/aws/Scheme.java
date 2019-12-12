/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
 * Created by TusaM
 * 11/10/2016.
 */
public enum Scheme {
    INTERNET_FACING("internet-facing"),
    INTERNAL("internal");

    private final String value;

    Scheme(String value) {
        this.value = value;
    }

    private String getSchemeValue() {
        return value;
    }

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return INTERNET_FACING.getSchemeValue();
        }

        for (Scheme scheme : Scheme.values()) {
            if (scheme.getSchemeValue().equalsIgnoreCase(input)) {
                return scheme.getSchemeValue();
            }
        }

        throw new RuntimeException("Invalid Amazon load balancer scheme value: [" + input + "]. Valid values: ec2, elasticloadbalancing, s3.");
    }
}
