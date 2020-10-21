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

import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.entities.MergeMapsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class MergeMapsInputValidator {

    public ValidationException validate(@NotNull MergeMapsInput input) {
        try {
            FieldValidator.validatePairDelimiter(input.getMap1PairDelimiter(), input.getMap1EntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getMap1EntryDelimiter(), input.getMap1PairDelimiter());
            FieldValidator.validateMap(input.getMap1(), input.getMap1Start(), input.getMap1End());
            FieldValidator.validateElementWrapper(input.getMap1ElementWrapper(), input.getMap1PairDelimiter(), input.getMap1EntryDelimiter());

            FieldValidator.validatePairDelimiter(input.getMap2PairDelimiter(), input.getMap2EntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getMap2EntryDelimiter(), input.getMap2PairDelimiter());
            FieldValidator.validateMap(input.getMap2(), input.getMap2Start(), input.getMap2End());
            FieldValidator.validateElementWrapper(input.getMap2ElementWrapper(), input.getMap2PairDelimiter(), input.getMap2EntryDelimiter());

            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }
}
