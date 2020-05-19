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
package io.cloudslang.content.json.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.entities.MergeArraysInput;
import io.cloudslang.content.json.services.MergeArraysService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;


public class MergeArraysAction {

    private final MergeArraysService service = new MergeArraysService();

    /**
     * This operation merge the contents of two JSON arrays. This operation does not modify either of the input arrays.
     * The result is the contents or array1 and array2, merged into a single array. The merge operation add into the result
     * the first array and then the second array.
     *
     * @param array1 The string representation of a JSON array object.
     *               Arrays in JSON are comma separated lists of objects, enclosed in square brackets [ ].
     *               Examples: [1,2,3] or ["one","two","three"] or [{"one":1, "two":2}, 3, "four"]
     * @param array2 The string representation of a JSON array object.
     *               Arrays in JSON are comma separated lists of objects, enclosed in square brackets [ ].
     *               Examples: [1,2,3] or ["one","two","three"] or [{"one":1, "two":2}, 3, "four"]
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the string representation of the new JSON array with the contents
     * of array1 and array2.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Merge Arrays Action",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY1, required = true) String array1,
                                       @Param(value = Constants.InputNames.ARRAY2, required = true) String array2) {
        try {
            MergeArraysInput input = new MergeArraysInput.Builder()
                    .array1(array1)
                    .array2(array2)
                    .build();
            return this.service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
