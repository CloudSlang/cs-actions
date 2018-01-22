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

package io.cloudslang.content.dropbox.entities.dropbox;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public enum DropboxApi {
    FILES("Files", "/files");

    private final String key;
    private final String value;

    DropboxApi(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getApi(String input) {
        for (DropboxApi api : values()) {
            if (api.getKey().equalsIgnoreCase(input)) {
                return api.getValue();
            }
        }

        return EMPTY;
    }

    private String getValue() {
        return value;
    }

    private String getKey() {
        return key;
    }
}