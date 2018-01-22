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

package io.cloudslang.content.entities;

/**
 * Created by giloan on 5/3/2016.
 */
public enum InputDefaults {
    PROTOCOL("https"),
    PORT("5986"),
    TRUST_ALL_ROOTS("false"),
    MAX_ENVELOPE_SIZE("153600"),
    X_509_HOSTNAME_VERIFIER("strict"),
    WINRM_LOCALE("en-US"),
    OPERATION_TIMEOUT("60"),
    AUTH_TYPE("Basic");

    private String defaultValue;

    /**
     * Instantiates a new input.
     *
     * @param defaultValue the default value of the input.
     */
    InputDefaults(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     *
     * @return the default value.
     */
    public String getValue() {
        return defaultValue;
    }
}
