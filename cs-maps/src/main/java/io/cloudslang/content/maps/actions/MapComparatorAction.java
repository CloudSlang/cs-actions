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
import io.cloudslang.content.maps.entities.MapComparatorInput;
import io.cloudslang.content.maps.services.MapComparatorService;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MapComparatorAction {

    private final MapComparatorService service = new MapComparatorService();


    /**
     * Get all values or a specific value from a map.
     *
     * @param map1       The map from where the values will be retrieved.
     *                   Example: {a:1,b:2,c:3,d:4}, Apples=3;Oranges=2
     *                   Default: {''}.
     *                   Valid values: Any string representing a valid map according to specified delimiters
     *                   (pair_delimiter, entry_delimiter, map_start, map_end).
     * @param map2       The separator to use for splitting key-value pairs into key, respectively value.
     *                   Default value: ':'.
     *                   Valid values: Any value that does not contain or is equal to entry_delimiter.
     * @param matchType  The separator to use for splitting the map into entries.
     *                   Default value: ','.
     *                   Valid values: Any value.
     * @param map1PairDelimiter  Optional - The separator to use for splitting first map's key-value pairs into key, respectively value.
     *                           Default value: ':'.
     *                           Valid values: Any value that does not contain or is equal to map1_entry_delimiter.
     * @param map1EntryDelimiter Optional - The separator to use for splitting the first map into entries.
     *                           Default value: ','.
     *                           Valid values: Any value.
     * @param map1Start          Optional - A sequence of 0 or more characters that marks the beginning of the first  map.
     *                           Default value: {'.
     *                           Valid values: Any value.
     * @param map1End            Optional - A sequence of 0 or more characters that marks the end of the first map.
     *                           Default value: '}.
     *                           Valid values: Any value.
     * @param map1ElementWrapper Optional - A sequence of 0 or more characters that marks the beginning and the end of a key or value of map1.
     *                           Valid values: Any value that does not have common characters with pair_delimiter or entry_delimiter.
     * @param map2PairDelimiter  Optional - The separator to use for splitting second map's key-value pairs into key, respectively value.
     *                           Default value: ':'.
     *                           Valid values: Any value that does not contain or is equal to map2_entry_delimiter.
     * @param map2EntryDelimiter Optional - The separator to use for splitting the second map into entries.
     *                           Default value: ','.
     *                           Valid values: Any value.
     * @param map2Start          Optional - A sequence of 0 or more characters that marks the beginning of the second map.
     *                           Default value: {'.
     *                           Valid values: Any value.
     * @param map2End            Optional - A sequence of 0 or more characters that marks the end of the second map.
     *                           Default value: '}.
     *                           Valid values: Any value.
     * @param map2ElementWrapper Optional - A sequence of 0 or more characters that marks the beginning and the end of a key or value of map2.
     *                           Valid values: Any value that does not have common characters with pair_delimiter or entry_delimiter.
     * @param ignoreCase Optional - A sequence of 0 or more characters that marks the beginning of the map.
     *                   Default value: {'.
     *                   Valid values: Any value.A sequence of 0 or more characters that marks the beginning of the map.
     *                   Valid values: Any value.
     * @return a map with following entries:
     * return_result: List of the values from the map or the value of the key in case of success or error message in case
     * of failure.
     * return_code: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */
    @Action(name = "Map Comparator",
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
    public @NotNull Map<String, String> execute(@Param(value = InputNames.MAP1, required = true) String map1,
                                                @Param(value = InputNames.MAP1_PAIR_DELIMITER, required = true) String map1PairDelimiter,
                                                @Param(value = InputNames.MAP1_ENTRY_DELIMITER, required = true) String map1EntryDelimiter,
                                                @Param(value = InputNames.MAP1_START) String map1Start,
                                                @Param(value = InputNames.MAP1_END) String map1End,
                                                @Param(value = InputNames.MAP1_ELEMENT_WRAPPER) String map1ElementWrapper,
                                                @Param(value = InputNames.MAP2, required = true) String map2,
                                                @Param(value = InputNames.MAP2_PAIR_DELIMITER, required = true) String map2PairDelimiter,
                                                @Param(value = InputNames.MAP2_ENTRY_DELIMITER, required = true) String map2EntryDelimiter,
                                                @Param(value = InputNames.MAP2_START) String map2Start,
                                                @Param(value = InputNames.MAP2_END) String map2End,
                                                @Param(value = InputNames.MAP2_ELEMENT_WRAPPER) String map2ElementWrapper,
                                                @Param(value = InputNames.MATCH_TYPE, required = true) String matchType,
                                                @Param(value = InputNames.STRIP_WHITESPACES) String stripWhitespaces,
                                                @Param(value = InputNames.IGNORE_CASE) String ignoreCase) {

        try {
            MapComparatorInput input = new MapComparatorInput.Builder()
                    .map1(map1)
                    .map1PairDelimiter(map1PairDelimiter)
                    .map1EntryDelimiter(map1EntryDelimiter)
                    .map1Start(map1Start)
                    .map1End(map1End)
                    .map1ElementWrapper(map1ElementWrapper)
                    .map2(map2)
                    .map2PairDelimiter(map2PairDelimiter)
                    .map2EntryDelimiter(map2EntryDelimiter)
                    .map2Start(map2Start)
                    .map2End(map2End)
                    .map2ElementWrapper(map2ElementWrapper)
                    .ignoreCase(ignoreCase)
                    .stripWhitespaces(stripWhitespaces)
                    .matchType(matchType)
                    .build();
            return service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);

        }
    }
}