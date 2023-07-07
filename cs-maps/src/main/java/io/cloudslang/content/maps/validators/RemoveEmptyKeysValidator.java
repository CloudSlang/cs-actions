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

package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.entities.RemoveEmptyKeysInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

public class RemoveEmptyKeysValidator {

    public ValidationException validate(@NotNull RemoveEmptyKeysInput input) {
        try {
            FieldValidator.validatePairDelimiter(input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getEntryDelimiter(), input.getPairDelimiter());
            FieldValidator.validateElementWrapper(input.getElementWrapper(), input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateMap(input.getMap(), input.getMapStart(), input.getMapEnd());
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }
}
