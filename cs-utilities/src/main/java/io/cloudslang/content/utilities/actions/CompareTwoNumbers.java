/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.utilities.util;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OperationDescription.COMPARE_TWO_NUMBERS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.EXCEPTION_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.RETURN_CODE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.RETURN_RESULT_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Inputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.toBoolean;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class CompareTwoNumbers {

    /*
     * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
    /**
     * Created by moldovai on 8/21/2017.
     */

    /**
     * This operation checks if a string is blank or empty and if it's true a default value
     * will be assigned instead of the initial string.
     *
     * @param value1 The initial string.
     * @param value2 The default value used to replace the initial string.
     * @return a map containing the output of the operation. Keys present in the map are:
     * returnResult - This will contain the replaced string with the default value.
     * exception - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * returnCode - The returnCode of the operation: 0 for success, -1 for failure.
     */

    @Action(name = "Compare 2 numbers",
            description = COMPARE_TWO_NUMBERS_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public String execute(
            @Param(value = VALUE1, description = VALUE1_DESC) String value1,
            @Param(value = VALUE2, required = true, description = VALUE2_DESC) String value2) {

        try {
            int val1 = Integer.parseInt(value1);
            int val2 = Integer.parseInt(value2);

            final boolean comparison = val1 < val2;

            if (comparison) {
                return "First number is smaller";

            } else {
                return "Second number is smaller";
            }
        }catch
                (Exception e) {
            return "Exception";
        }


    }
}
