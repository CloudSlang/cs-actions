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
 * Created by TusaM
 * 4/28/2017.
 */
public enum ViewsUri {
    GET_DESIGN_DOC_INFO("GetDesignDocsInfo", "/buckets");

    private final String key;
    private final String value;

    ViewsUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getViewsUriValue(String key) {
        for (ViewsUri uri : values()) {
            if (uri.getKey().equalsIgnoreCase(key)) {
                return uri.getViewsUriValue();
            }
        }

        return EMPTY;
    }

    private String getKey() {
        return key;
    }

    private String getViewsUriValue() {
        return value;
    }
}