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
import io.cloudslang.content.json.entities.GetArraySublistInput;
import io.cloudslang.content.json.services.GetArraySublistService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.StringUtils;
import io.cloudslang.content.json.validators.GetArraySublistValidator;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.List;
import java.util.Map;

public class GetArraySublistAction {

    private final GetArraySublistService service = new GetArraySublistService();


    /**
     * Get a JSON array which is the subset of an array. The subset can be defined by a start index (fromIndex) alone,
     * in which case the resulting array will contain elements from the input 'array' from the 'fromIndex' to the end of
     * the input array.
     *
     * The optional 'toIndex' parameter can be supplied, in which case the sublist returned by this operation will
     * contain the elements in array starting with 'fromIndex', and include everything up to, but not including, the
     * element at 'toIndex'.
     *
     * It is also possible to specify 'fromIndex' and 'toIndex' in either the "from the left" positive format or in the
     * "from the right" negative format, and even specify them each in a different format in the same call to this
     * operation. However, sublist is always create from left to right from the original array, and therefore
     * 'fromIndex' must allayws be to the left of 'toIndex' in the parent array. Any combination of 'fromIndex' and
     * 'toIndex' which violates this rule (including the case where fromIndex=toIndex) will return an error.
     *
     * Example:
     * With the input array: [{"one":1,"two":2}, "3", "four"]
     *
     * fromIndex=0  and toIndex=1                  returns [{"one":1,"two":2}]
     * fromIndex=0  and toIndex=3                  returns [{"one":1,"two":2}, "3", "four"]
     *
     * @param array String representation of a JSON array. Arrays in JSON are comma seperated lists of objects,
     *              enclosed in square brackets ( [ ] ).
     *              Examples: [1,2,3] or ["one","two","three"] or [{"one":1, "two":2}, 3, "four"]
     * @param fromIndex The index of the element in 'array' which will be the 1st element in the sublist array.
     *                  Valid values: Integer between (-1*n) and (n-1) for an array with n elements.
     * @param toIndex Optional. If this input is supplied, the sub-list will contain the elements from the parent array,
     *                starting at the position fromIndex, up to, but not including, the element at toIndex. In other
     *                words, the element in the parent array at toIndex is one past the last element of the sub-list
     *                returned by this operation. If this input is not supplied, the sub-array will contain the elements
     *                of 'array' beginning with fromIndex to the end of the array.
     *                Valid values: Integer between (-1*n) and (n-1) for an array with n elements.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> -  The new JSON array which is a subset of the input array.
     * If the operation failed, this field will contain an error message.
     * <br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     * <br><b>exception</b> - The exception message if the operation goes to failure.
     */

    @Action(name = "Get Array Sublist",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.ARRAY, required = true) String array,
            @Param(value = Constants.InputNames.FROM_INDEX, required = true) String fromIndex,
            @Param(value = Constants.InputNames.TO_INDEX) String toIndex) {

        if (StringUtils.isEmpty(toIndex))
            toIndex = null;

        List<RuntimeException> exceptionList = GetArraySublistValidator.validate(array, fromIndex, toIndex);
        if (exceptionList.size() > 0)
            return OutputUtilities.getFailureResultsMap(exceptionList.get(0));
        else {
            try {
                GetArraySublistInput input = new GetArraySublistInput.Builder()
                        .array(array)
                        .fromIndex(fromIndex)
                        .toIndex(toIndex)
                        .build();
                return this.service.execute(input);
            } catch (Exception ex) {
                return OutputUtilities.getFailureResultsMap(ex);
            }
        }
    }
}
