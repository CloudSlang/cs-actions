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

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Mihai Tusa
 * 4/8/2017.
 */
public enum SuffixUri {
    GET_BUCKET_STATISTICS("GetBucketStatistics", "/stats"),
    GET_DESIGN_DOCS_INFO("GetDesignDocsInfo", "/ddocs"),
    REBALANCING_NODES("RebalancingNodes", "/rebalance"),
    REMOTE_CLUSTERS("GetDestinationClusterReference", "/remoteClusters");

    private final String key;
    private final String value;

    SuffixUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getSuffixUriValue(String key) {
        for (SuffixUri suffixUri : values()) {
            if (suffixUri.getKey().equalsIgnoreCase(key)) {
                return suffixUri.getSuffixUriValue();
            }
        }

        return EMPTY;
    }

    private String getKey() {
        return key;
    }

    private String getSuffixUriValue() {
        return value;
    }
}