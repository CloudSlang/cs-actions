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
            validateMap(input.getMap1(), input.getMap1Start(),input.getMap1End());
            validatePairDelimiter(input.getMap1PairDelimiter(), input.getMap1EntryDelimiter());
            validateEntryDelimiter(input.getMap1PairDelimiter(),input.getMap1EntryDelimiter());
            validateMapStart(input.getMap1Start());
            validateMapEnd(input.getMap1End());

            validateMap(input.getMap2(), input.getMap2Start(),input.getMap2End());
            validatePairDelimiter(input.getMap2PairDelimiter(), input.getMap2EntryDelimiter());
            validateEntryDelimiter(input.getMap2PairDelimiter(),input.getMap2EntryDelimiter());
            validateMapStart(input.getMap2Start());
            validateMapEnd(input.getMap2End());

            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateMap(String map, String mapStart, String mapEnd) throws ValidationException {
        if (StringUtils.isBlank(map)) {
            throw new ValidationException(ExceptionsMsgs.NO_MAP_PROVIDED);
        }

        if (!map.startsWith(mapStart)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_START_WITH_MAP_START);
        }

        if (!map.endsWith(mapEnd)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_END_WITH_MAP_END);
        }

        if (map.length() < mapStart.length() + mapEnd.length()) {
            throw new ValidationException(ExceptionsMsgs.INVALID_MAP);
        }
    }

    private void validatePairDelimiter(String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_PAIR_DELIMITER);
        }

        if(pairDelimiter.contains(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }

    private void validateEntryDelimiter(String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_ENTRY_DELIMITER);
        }

        if(entryDelimiter.equals(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT);
        }
    }

    private void validateMapStart(String mapStart) throws ValidationException {
        if (mapStart == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_START);
        }
    }
    
    private void validateMapEnd(String mapEnd) throws ValidationException {
        if (mapEnd == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_END);
        }
    }
}
