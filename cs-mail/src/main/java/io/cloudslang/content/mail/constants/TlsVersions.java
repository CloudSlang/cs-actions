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
package io.cloudslang.content.mail.constants;

import java.lang.reflect.Field;

public final class TlsVersions {
    public static final String SSLv3 = "SSLv3";
    public static final String TLSv1_0 = "TLSv1";
    public static final String TLSv1_1 = "TLSv1.1";
    public static final String TLSv1_2 = "TLSv1.2";

    public static boolean validate(String tlsVersion) throws IllegalAccessException {
        Field[] possibleVersions = TlsVersions.class.getFields();
        for(Field possibleVersion : possibleVersions) {
            if(possibleVersion.get(null) != null && possibleVersion.get(null).toString().equals(tlsVersion)) {
                return true;
            }
        }
        return false;
    }
}
