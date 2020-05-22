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
import io.cloudslang.content.json.entities.AddObjectToArrayInput;
import io.cloudslang.content.json.services.AddObjectToArrayService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class AddObjectToArrayAction {

    private final AddObjectToArrayService service = new AddObjectToArrayService();


    /**
     * Insert an object into a JSON array, optionally specifying the position at which to insert the new object.
     * This Operation differs from the 'Add to Array' operation in that this one will try and validate the new element as
     * a JSON object, whereas 'Add to Array' will just wrap the element in quotes and treat it as a string. This means
     * that the element passed into this operation must be a valid JSON object, or the operation will fail.
     *
     * @param array   The JavaScript array that will be added to.
     *                The operation will return a failure if the array is not a JavaScript array.
     * @param element The element to insert into the array. As noted above, this must be a valid JSON object or
     *                the operation will fail. It can be any valid JSON object; such as a number, string, an array,
     *                or a name/value pair list. Numbers can be entered as-is, strings
     *                must be wrapped in quotes, and objects must be formatted correctly.
     * @param index   The index (-n < i < n) in the array in which the value will be inserted.
     *                If an index is not specified, then the element will be appended to the end of the array.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The element of the array at specified index.
     * If the operation failed, this field will contain an error message.
     * <br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     * <br><b>exception</b> - The exception message if the operation goes to failure.
     */
    @Action(name = "Add Object to Array",
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
    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array,
                                       @Param(value = Constants.InputNames.ELEMENT, required = true) String element,
                                       @Param(value = Constants.InputNames.INDEX) String index) {
        try {
            AddObjectToArrayInput input = new AddObjectToArrayInput.Builder()
                    .array(array)
                    .element(element)
                    .index(index)
                    .build();
            return this.service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
