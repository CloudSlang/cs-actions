/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.ssh.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AnsiEscapeUtils {

    private static final String ESCAPE_CHAR = "\\x1B";
    private static final String CONTROL_SEQUENCE_INTRODUCER= "\\x9B|\\x1B\\[";
    private static final String PARAMETER_BYTES = "0-9:;<=>?";
    private static final String INTERMEDIATE_BYTES = "!\"#$%&'()*+,\\-./";
    private static final String FINAL_BYTES = "@A-Za-z\\[\\\\\\]^_`{|}~";

    private AnsiEscapeUtils() {

    }


    public static String removeEscapeSequences(@Nullable String str) {
        if(str == null) {
            return null;
        }
        str = removeControlSequences(str);
        str = removeEscapeEqualChar(str);
        return str;
    }


    private static String removeControlSequences(@NotNull String str) {
        String regex = String.format("(%s)[%s]*[%s]*[%s]", CONTROL_SEQUENCE_INTRODUCER, PARAMETER_BYTES, INTERMEDIATE_BYTES, FINAL_BYTES);
        return str.replaceAll(regex, StringUtils.EMPTY);
    }

    private static String removeEscapeEqualChar(@NotNull String str) {
        String regex = ESCAPE_CHAR + "=";
        return str.replaceAll(regex, StringUtils.EMPTY);
    }
}
