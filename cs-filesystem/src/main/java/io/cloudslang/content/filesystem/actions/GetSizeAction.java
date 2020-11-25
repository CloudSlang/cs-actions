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
package io.cloudslang.content.filesystem.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.filesystem.constants.Constants;
import io.cloudslang.content.filesystem.constants.InputNames;
import io.cloudslang.content.filesystem.entities.GetSizeInputs;
import io.cloudslang.content.filesystem.services.GetSizeService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.filesystem.constants.ResultsName.*;

public class GetSizeAction {

    private final GetSizeService service = new GetSizeService();

    /**
     * Compares a files size to a given threshold.
     *
     * @param source           The file to read. It must be an absolute path.
     * @param threshold        The threshold to compare the file size to (in bytes).
     * @result LESS_THAN       File's size is smaller than the threshold.
     * @result EQUAL_TO        File's size is the same as the threshold.
     * @result GREATER_THAN    File's size is the greater than the threshold.
     * @return a map with following entries:
     * size: The file's size in bytes.
     * return_result: The file's size in bytes if operation succeeded. Otherwise it will contain the message of the exception.
     * return_code: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */

    @Action(name = "Get Size",
            outputs = {
                    @Output(io.cloudslang.content.filesystem.constants.Constants.SIZE),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_CODE),
                    @Output(io.cloudslang.content.constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = LESS_THAN, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_LESS,
                            responseType = ResponseType.RESOLVED),
                    @Response(text = EQUAL_TO, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL,
                            responseType = ResponseType.RESOLVED),
                    @Response(text = GREATER_THAN, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_GREATER,
                            responseType = ResponseType.RESOLVED),
                    @Response(text = io.cloudslang.content.constants.ResponseNames.FAILURE,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL,
                            isOnFail = true, isDefault = true)
            })
    public Map<String, String> execute(@Param(value = InputNames.SOURCE, required = true) String source,
                                       @Param(value = InputNames.THRESHOLD, required = true) String threshold) {

        Map<String, String> result;

        try {
            GetSizeInputs input = new GetSizeInputs.Builder()
                    .source(source)
                    .threshold(threshold)
                    .build();

            result = service.execute(input);

            result.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
            result.put(OutputNames.RETURN_RESULT, result.get(Constants.RETURN_RESULT));
            result.put(Constants.SIZE, result.get(Constants.SIZE));
            return result;
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
