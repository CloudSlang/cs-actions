/*
 * Copyright 2022-2024 Open Text
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




package io.cloudslang.content.httpclient.entities;

public class Constants {
    public static final String ZERO = "0";

    public static final String URL = "url";
    public static final String TEXT = "text";
    public static final String CHARACTER_SET = "characterSet";
    public static final String NTLM_AUTH = "NTLM";
    public static final String BASIC_AUTH = "Basic";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String CHANGEIT = "changeit";
    public static final String TLSv12 = "TLSv1.2";
    public static final String TLSv13 = "TLSv1.3";
    public static final String RESPONSE_CHARACTER_SET = "ISO-8859-1";
    public static final String NEW_LINE = "\n";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_VALUE = "%s is not a valid value for executionTimeout. The value must be a positive number.";
    public static final String EXCEPTION_NEGATIVE_VALUE = "%s is not a valid timeout value. The value must be bigger or equal to 0.";
}
