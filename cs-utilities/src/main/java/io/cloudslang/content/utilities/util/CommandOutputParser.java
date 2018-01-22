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
package io.cloudslang.content.utilities.util;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;

public class CommandOutputParser {

    public static String extractValue(String output, String key) {
        String value = EMPTY_STRING;
        int startIndex = output.indexOf(key);

        if (startIndex >= 0) {
            value = output.substring(startIndex + key.length());
        }
        return value;
    }

    public static String extractValue(String output, String key, String endTag) {
        String value = EMPTY_STRING;
        int startIndex = output.indexOf(key);
        int valueStartIndex = startIndex + key.length();

        if (startIndex >= 0) {
            value = output.substring(valueStartIndex, output.indexOf(endTag, valueStartIndex));
        }

        return value;
    }
}
