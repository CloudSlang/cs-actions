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
import org.jetbrains.annotations.Nullable;

public class KeyValidator {
    public ValidationException validate(@Nullable String key) {
        if (key == null) {
            return null;
        }

        if (key.contains(Delimiters.KEY_VALUE_PAIR_DELIM) ||
                key.contains(Delimiters.MAP_ENTRY_DELIM) ||
                key.contains(Delimiters.QUOTE_DELIM)) {
            return new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_DELIMITER);
        }

        return null;
    }
}
