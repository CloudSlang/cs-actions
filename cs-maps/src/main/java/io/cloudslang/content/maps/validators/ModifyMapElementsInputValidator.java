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

import io.cloudslang.content.maps.entities.ModifyMapElementsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

public class ModifyMapElementsInputValidator {

    public ValidationException validate(@NotNull ModifyMapElementsInput input) {
        try {
            FieldValidator.validateMap(input.getMap(), input.getMapStart(), input.getMapEnd());
            FieldValidator.validateMethod(input.getMethod());
            FieldValidator.validateElements(input.getElements());
            FieldValidator.validatePairDelimiter(input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getEntryDelimiter(), input.getPairDelimiter());
            FieldValidator.validateElementWrapper(input.getElementWrapper(), input.getPairDelimiter(), input.getEntryDelimiter());
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }
}
