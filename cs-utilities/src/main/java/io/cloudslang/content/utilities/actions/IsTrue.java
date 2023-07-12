/*
 * Copyright 2022-2023 Open Text
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




package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.util.Descriptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Inputs.BOOL_VALUE;
import static io.cloudslang.content.utilities.util.Descriptions.ConvertBytesToFile.RETURN_CODE_DESC;
import static io.cloudslang.content.utilities.util.Descriptions.IsTrueDescriptions.BOOL_VALUE_DESC;

public class IsTrue {

    /**
     * Checks if boolean is true or false. Used for flow control.
     *
     * @param boolValue: Boolean value to check.
     */

    @Action(name = "Is True",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = Descriptions.RandomPasswordGeneratorDescriptions.SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = Descriptions.RandomPasswordGeneratorDescriptions.FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = BOOL_VALUE, description = BOOL_VALUE_DESC) String boolValue) {

        List<String> valid = Arrays.asList("True", "true", "'True'", "'true'");
        Map<String, String> result = new HashMap();

        if (valid.contains(boolValue)) {
            result.put("returnCode", "0");
            return result;
        } else {
            result.put("returnCode", "-1");
            return result;
        }
    }
}

