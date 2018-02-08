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

package io.cloudslang.content.couchbase.entities.couchbase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public enum NodesUri {
    FAIL_OVER("FailOverNode", "/failOver"),
    GRACEFUL_FAIL_OVER("GracefulFailOverNode", "/startGracefulFailover"),
    SET_RECOVERY_TYPE("SetRecoveryType", "/setRecoveryType");

    private final String key;
    private final String value;

    private static final Map<String, String> NODES_URI_MAP = new HashMap<>();

    static {
        stream(values())
                .forEach(nodeUri -> NODES_URI_MAP.put(nodeUri.getKey(), nodeUri.getNodesUriValue()));
    }

    NodesUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getNodesUriValue(String input) {
        return Optional
                .ofNullable(NODES_URI_MAP.get(input))
                .orElse(EMPTY);
    }

    private String getKey() {
        return key;
    }

    private String getNodesUriValue() {
        return value;
    }
}