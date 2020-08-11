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
package io.cloudslang.content.maps.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.services.AddKeyService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;


public class AddKeyAction {

    private final AddKeyService service = new AddKeyService();


    /**
     * Adds a key to a map. If the given key already exists in the map then its value will be overwritten.
     * <p>
     * Examples:
     * 1. For an SQL like map ---
     * map=|A|1||B|2|, key=B, value=3, pair_delimiter=|, entry_delimiter=||, map_start=|, map_end=| => |A|1||B|3|
     * 2. For a JSON like map ---
     * map={'A':'1','B':'2'}, key=B, value=3, pair_delimiter=':', entry_delimiter=',', map_start={', map_end='} => {'A':'1','B':'3'}
     *
     * @param map            The map to add a key to.
     *                       Example: {a:1,b:2,c:3,d:4}, <John|1||George|2>, Apples=3;Oranges=2
     * @param key            Optional - The key to add.
     *                       Valid values: Any string that does not contain or is equal to value of pairDelimiter or entryDelimiter.
     * @param value          Optional - The value to map to the added key.
     *                       Valid values: Any string that does not contain or is equal to value of pairDelimiter or entryDelimiter.
     * @param pairDelimiter  The separator to use for splitting key-value pairs into key, respectively value.
     *                       Valid values: Any value.
     * @param entryDelimiter The separator to use for splitting the map into entries.
     *                       Valid values: Any value.
     * @param mapStart       A sequence of 0 or more characters that marks the beginning of the map.
     *                       Valid values: Any value.
     * @param mapEnd         A sequence of 0 or more characters that marks the end of the map.
     *                       Valid values: Any value.
     * @return a map with following entries:
     * return_result: The map with the added key if operation succeeded. Otherwise it will contain the message of the exception.
     * return_code: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */
    @Action(name = "Add Key",
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
    public Map<String, String> execute(@Param(value = InputNames.MAP, required = true) String map,
                                       @Param(value = InputNames.KEY) String key,
                                       @Param(value = InputNames.VALUE) String value,
                                       @Param(value = InputNames.PAIR_DELIMITER, required = true) String pairDelimiter,
                                       @Param(value = InputNames.ENTRY_DELIMITER, required = true) String entryDelimiter,
                                       @Param(value = InputNames.MAP_START) String mapStart,
                                       @Param(value = InputNames.MAP_END) String mapEnd) {
        try {
            AddKeyInput input = new AddKeyInput.Builder()
                    .map(map)
                    .key(key)
                    .value(value)
                    .pairDelimiter(pairDelimiter)
                    .entryDelimiter(entryDelimiter)
                    .mapStart(mapStart)
                    .mapEnd(mapEnd)
                    .build();
            return service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
