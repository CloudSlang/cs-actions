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
package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.utils.Constants.*;

public class StringMethod {

    public static String execute(String list, String method, String value, boolean stripWhitespaces, String delimiter) {

        switch (method) {
            case TO_UPPERCASE:
                if (stripWhitespaces)
                    return list.replaceAll(WHITESPACES, EMPTY_STRING).toUpperCase();
                else
                    return list.toUpperCase();
            case TO_LOWERCASE:
                if (stripWhitespaces)
                    return list.replaceAll(WHITESPACES, EMPTY_STRING).toLowerCase();
                else
                    return list.toUpperCase();
            case ADD_PREFIX:
            case ADD_SUFIX:
                if (stripWhitespaces)
                    return iterateList(value.replaceAll(WHITESPACES, EMPTY_STRING), list.replaceAll(WHITESPACES, EMPTY_STRING), delimiter, method);
                else
                    return iterateList(value, list, delimiter, method);
            default:
                return list;
        }
    }

    private static String iterateList(String value, String list, String delimiter, String method) {
        String[] listString;
        if (!StringUtils.isEmpty(delimiter)) {
            listString = list.split(delimiter);
        } else {
            listString = new String[]{list};
        }
        StringBuilder newString = new StringBuilder();
        for (String aListString : listString) {
            if (method.equals(ADD_PREFIX))
                newString.append(value).append(aListString).append(delimiter);
            else if (method.equals(ADD_SUFIX))
                newString.append(aListString).append(value).append(delimiter);
        }
        if (newString.length() > 0)
            newString.deleteCharAt(newString.length() - 1);
        return newString.toString();
    }

}
