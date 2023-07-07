/*
 * Copyright 2020-2023 Open Text
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

package io.cloudslang.content.maps.utils;

public class StringMethods {

    public static String execute(String object, String method, String value, boolean stripWhitespaces) {

        switch (method.toLowerCase()) {
            case "to_uppercase":
                return object.toUpperCase();
            case "to_lowercase":
                return object.toLowerCase();
            case "add_prefix":
                if(stripWhitespaces)
                    return (value + object).replaceAll("\\s+","");
                else
                    return value + object;
            case "add_suffix":
                if(stripWhitespaces)
                    return (object + value).replaceAll("\\s+","");
                else
                    return object + value;
            default:
                return object.replaceAll("\\s+","");
        }
    }
}
