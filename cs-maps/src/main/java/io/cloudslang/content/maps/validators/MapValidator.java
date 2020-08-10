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
package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.constants.Delimiters;
import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class MapValidator {
    public ValidationException validate(@Nullable String map) {
        if(StringUtils.isBlank(map)) {
            return new ValidationException(ExceptionsMsgs.NO_MAP_PROVIDED);
        }

        final String keyRegex = "\\s*(" + Delimiters.QUOTE_DELIM + "[^" + Delimiters.KEY_VALUE_PAIR_DELIM +
                Delimiters.MAP_ENTRY_DELIM + Delimiters.QUOTE_DELIM + "]*" + Delimiters.QUOTE_DELIM + "|null)\\s*";
        final String valueRegex = "\\s*" + Delimiters.QUOTE_DELIM + "[^" + Delimiters.KEY_VALUE_PAIR_DELIM +
                Delimiters.MAP_ENTRY_DELIM + Delimiters.QUOTE_DELIM + "]*" + Delimiters.QUOTE_DELIM + "\\s*";
        final String entryRegex = keyRegex + Delimiters.KEY_VALUE_PAIR_DELIM + valueRegex;
        final String mapRegex = "\\s*\\" + Delimiters.MAP_START + "(" + entryRegex + "(" + Delimiters.MAP_ENTRY_DELIM +
                entryRegex + ")*|\\s*)" + Delimiters.MAP_END + "\\s*";

        if (!map.matches(mapRegex)) {
            return new ValidationException(ExceptionsMsgs.MAP_HAS_INVALID_FORMAT);
        }

        return null;
    }
}
