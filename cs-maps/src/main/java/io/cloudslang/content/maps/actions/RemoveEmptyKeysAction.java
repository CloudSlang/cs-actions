/*
 * Copyright 2020-2024 Open Text
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

package io.cloudslang.content.maps.actions;

import io.cloudslang.content.annotations.Action;
import io.cloudslang.content.annotations.Output;
import io.cloudslang.content.annotations.Param;
import io.cloudslang.content.annotations.Response;
import io.cloudslang.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.entities.RemoveEmptyKeysInput;
import io.cloudslang.content.maps.services.RemoveEmptyKeysService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

public class RemoveEmptyKeysAction {

    RemoveEmptyKeysService service = new RemoveEmptyKeysService();

    @Action(name = "Remove Empty Keys",
            outputs = {
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_CODE),
                    @Output(io.cloudslang.content.constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = io.cloudslang.content.constants.ResponseNames.SUCCESS,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = io.cloudslang.content.constants.ResponseNames.FAILURE,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL,
                            isOnFail = true, isDefault = true)
            })
    public Map<String, String> execute(@Param(value = InputNames.MAP) String map,
                                       @Param(value = InputNames.PAIR_DELIMITER, required = true) String pairDelimiter,
                                       @Param(value = InputNames.ENTRY_DELIMITER, required = true) String entryDelimiter,
                                       @Param(value = InputNames.MAP_START) String mapStart,
                                       @Param(value = InputNames.MAP_END) String mapEnd,
                                       @Param(value = InputNames.ELEMENT_WRAPPER) String elementWrapper,
                                       @Param(value = InputNames.STRIP_WHITESPACES) String stripWhitespaces) {
        try {
            RemoveEmptyKeysInput input = new RemoveEmptyKeysInput.Builder()
                    .map(map)
                    .pairDelimiter(pairDelimiter)
                    .entryDelimiter(entryDelimiter)
                    .mapStart(mapStart)
                    .mapEnd(mapEnd)
                    .elementWrapper(elementWrapper)
                    .stripWhitespaces(stripWhitespaces)
                    .build();
            return service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
