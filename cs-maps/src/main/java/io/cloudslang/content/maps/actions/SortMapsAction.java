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
import io.cloudslang.content.maps.entities.SortMapsInput;
import io.cloudslang.content.maps.services.SortMapsService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;


public class SortMapsAction {
    /**
     * Sorts a map in ascending or descending order by keys or values.
     *
     * @param map              The map that will be sorted.
     *                         Example: {a:1,b:2,c:3,d:4}, Apples=3;Oranges=2
     *                         Default: {''}.
     *                         Valid values: Any string representing a valid map according to specified delimiters
     *                         (pair_delimiter, entry_delimiter, map_start, map_end).
     * @param sortBy           The map entries that will be sorted.
     *                         Valid values: key, value.
     * @param sortOrder        The order in which the selected entries will be sorted.
     *                         Valid values: asc (ascending), desc (descending).
     * @param pairDelimiter    The separator to use for splitting key-value pairs into key, respectively value.
     *                         Default value: ':'.
     *                         Valid values: Any value that does not contain or is equal to entry_delimiter.
     * @param entryDelimiter   The separator to use for splitting the map into entries.
     *                         Valid values: Any value.
     * @param mapStart         Optional - A sequence of 0 or more characters that marks the beginning of the map.
     *                         Valid values: Any value.A sequence of 0 or more characters that marks the beginning of the map.
     *                         Valid values: Any value.
     * @param mapEnd           Optional - A sequence of 0 or more characters that marks the end of the map.
     *                         Valid values: Any value.
     * @param elementWrapper   Optional - A sequence of 0 or more characters that marks the beginning and the end of a key or value.
     *                         Valid values: Any value that does not have common characters with pair_delimiter or entry_delimiter.
     * @param stripWhitespaces Optional - True if leading and trailing whitespaces should be removed from the keys and values of the map.
     *                         Default: false.
     *                         Valid values: true, false.
     * @return a map with following entries:
     * return_result: The sorted map.
     * return_code: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */

    private final SortMapsService service = new SortMapsService();

    @Action(name = "Sort Maps",
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
                                       @Param(value = InputNames.SORT_BY, required = true) String sortBy,
                                       @Param(value = InputNames.SORT_ORDER) String sortOrder,
                                       @Param(value = InputNames.PAIR_DELIMITER, required = true) String pairDelimiter,
                                       @Param(value = InputNames.ENTRY_DELIMITER, required = true) String entryDelimiter,
                                       @Param(value = InputNames.MAP_START) String mapStart,
                                       @Param(value = InputNames.MAP_END) String mapEnd,
                                       @Param(value = InputNames.ELEMENT_WRAPPER) String elementWrapper,
                                       @Param(value = InputNames.STRIP_WHITESPACES) String stripWhitespaces) {
        try {
            SortMapsInput input = new SortMapsInput.Builder()
                    .map(map)
                    .sortBy(sortBy)
                    .sortOrder(sortOrder)
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
