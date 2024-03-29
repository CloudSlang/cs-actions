/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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

/**
 * Created by TusaM
 * 5/30/2017.
 */
public enum ApiVersion {
    NONE(""),
    ONE("/1"),
    TWO("/2");

    private final String value;

    ApiVersion(String value) {
        this.value = value;
    }

    public static String getApiVersion(String key) {
        for (ApiVersion version : values()) {
            if (version.name().equalsIgnoreCase(key)) {
                return version.getValue();
            }
        }

        return TWO.getValue();
    }

    private String getValue() {
        return value;
    }
}