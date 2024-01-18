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

package io.cloudslang.content.filesystem.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.filesystem.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
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
import static io.cloudslang.content.filesystem.utils.Descriptions.Common.EXCEPTION_DESCRIPTION;
import static io.cloudslang.content.filesystem.utils.Descriptions.Common.RETURN_CODE_DESCRIPTION;
import static io.cloudslang.content.filesystem.utils.Descriptions.GetSize.*;

public class GetSizeAction {

    private final GetSizeService service = new GetSizeService();

    /**
     * Compares a file's size to a given threshold.
     *
     * @param source    The file to read. It must be an absolute path.
     * @param threshold The threshold to compare the file size to (in bytes).
     * @return a map with following entries:
     * size: The file's size in bytes.
     * return_result: The result of the comparison between the file's size and the threshold. Otherwise it will contain
     *                the exception message.
     * return_code: 0 if the operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if the operation failed. Empty otherwise.
     * @result FAILURE         The operation failed.
     * @result LESS_THAN       The file's size is smaller than the threshold.
     * @result EQUAL_TO        The file's size is the same as the threshold.
     * @result GREATER_THAN    The file's size is the greater than the threshold.
     */

    @Action(name = "Get Size",
            outputs = {
                    @Output(value = OutputNames.SIZE, description = SIZE_DESCRIPTION),
                    @Output(value = OutputNames.RETURN_RESULT, description = GET_SIZE_RETURN_RESULT_DESCRIPTION),
                    @Output(value = OutputNames.RETURN_CODE, description = RETURN_CODE_DESCRIPTION),
                    @Output(value = OutputNames.EXCEPTION, description = EXCEPTION_DESCRIPTION)
            },
            responses = {
                    @Response(text = LESS_THAN, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_LESS,
                            responseType = ResponseType.RESOLVED, description = LESS_THAN_DESCRIPTION),
                    @Response(text = EQUAL_TO, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL,
                            responseType = ResponseType.RESOLVED, description = EQUAL_TO_DESCRIPTION),
                    @Response(text = GREATER_THAN, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_GREATER,
                            responseType = ResponseType.RESOLVED, description = GREATER_THAN_DESCRIPTION),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true, description = FAILURE_DESCRIPTION)
            })
    public Map<String, String> execute(@Param(value = InputNames.SOURCE, required = true, description = SOURCE_DESCRIPTION) String source,
                                       @Param(value = InputNames.THRESHOLD, required = true, description = THRESHOLD_DESCRIPTION) String threshold) {

        Map<String, String> result;

        try {
            GetSizeInputs input = new GetSizeInputs.Builder()
                    .source(source)
                    .threshold(threshold)
                    .build();

            result = service.execute(input);

            result.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
            result.put(OutputNames.RETURN_RESULT, result.get(OutputNames.RETURN_RESULT));
            result.put(OutputNames.SIZE, result.get(OutputNames.SIZE));
            return result;
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
