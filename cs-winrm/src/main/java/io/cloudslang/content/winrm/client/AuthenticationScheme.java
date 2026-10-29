/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.winrm.client;

import java.util.Locale;

public enum AuthenticationScheme {
    BASIC("Basic"),
    NTLM("NTLM"),
    KERBEROS("Kerberos");

    private final String value;

    AuthenticationScheme(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AuthenticationScheme fromString(final String value) {
        if (value != null) {
            for (AuthenticationScheme scheme : values()) {
                if (scheme.value.toLowerCase(Locale.ROOT).equals(value.trim().toLowerCase(Locale.ROOT))) {
                    return scheme;
                }
            }
        }
        throw new UnsupportedOperationException("No such authentication scheme " + value + "; options are [Basic, NTLM, Kerberos]");
    }
}
