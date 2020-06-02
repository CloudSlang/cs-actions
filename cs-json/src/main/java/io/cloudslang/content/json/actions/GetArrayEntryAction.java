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
import io.cloudslang.content.json.entities.GetArrayEntryInput;
import io.cloudslang.content.json.services.GetArrayEntryService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

public class GetArrayEntryAction {

    private final GetArrayEntryService service = new GetArrayEntryService();


    /**
     * Gets the value of an element in a JSON array. If the value of the element is a simple type, i.e. a string or a
     * number,
     * it will be returned as-is. If it is a complex JSON object, i.e. '{"one":1}' it will be returned in JSON format.
     * <p>
     * When specifying the index to an array element in javascript it is possible to use the standard notation, where
     * the 1st
     * element from the left is index 0, and the right-most element is (n-1), in an array with n elements. However,
     * it is also possible to specify elements starting from the right side of the array using negative numbers, in
     * which case
     * the right-most element is referred to by index -1 and the left-most element is at position (-1 * n), again for
     * an array with n elements.
     *
     * @param array String representation of a JSON array. Arrays in JSON are comma separated lists of objects,
     *              enclosed in square brackets ( [ ] ).
     * @param index The index of the element to retrieve from the array.
     *              See the notes above for more information on using negative numbers to specify array elements.
     *              Valid values: Integer between (-1*n) and (n-1) for an array with n elements.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The element of the array at specified index.
     * If the operation failed, this field will contain an error message.
     * <br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     * <br><b>exception</b> - The exception message if the operation goes to failure.
     */
    @Action(name = "Get Array Entry",
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
            @Param(value = Constants.InputNames.INDEX, required = true) String index) {
        try {
            GetArrayEntryInput input = new GetArrayEntryInput.Builder()
                    .array(array)
                    .index(index)
                    .build();
            return this.service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
