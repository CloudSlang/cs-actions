/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.Base64EncoderInputs;
import io.cloudslang.content.utilities.services.base64coder.Base64EncoderToStringImpl;
import io.cloudslang.content.utilities.util.Constants;
import io.cloudslang.content.utilities.util.Descriptions;
import io.cloudslang.content.utilities.util.Inputs;
import io.cloudslang.content.utilities.util.InputsValidation;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

public class Base64Encoder {
    /**
     * This encodes in base64 a value read from a file.
     *
     * @param filePath - the path were the desired file is to be encoded.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * returnResult - The encoded value.
     * returnCode - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception - the exception message if the operation fails.
     */

    @Action(name = Descriptions.EncodeFileToStringOutput.BASE64_ENCODER_FROM_FILE,
            description = Descriptions.EncodeFileToStringOutput.BASE64_ENCODER_FROM_FILE_DESC,
            outputs = {
                    @Output(value = OutputNames.EXCEPTION, description = Descriptions.EncodeFileToStringOutput.EXCEPTION_DESC),
                    @Output(value = OutputNames.RETURN_CODE, description = Descriptions.EncodeFileToStringOutput.RETURN_CODE_DESC),
                    @Output(value = OutputNames.RETURN_RESULT, description = Descriptions.EncodeFileToStringOutput.RETURN_RESULT_DESC),
                    @Output(value = Constants.RETURN_VALUE, description = Descriptions.EncodeFileToStringOutput.RETURN_VALUE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = Descriptions.EncodeFileToStringOutput.FAILURE_DESC),
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = Descriptions.EncodeFileToStringOutput.SUCCESS_DESC)
            })
    public Map<String, String> execute
    (@Param(value = Inputs.Base64CoderInputs.FILE_PATH, description = Descriptions.EncodeFileToStringOutput.FILE_PATH_DESC, required = true) String filePath) {

        final List<String> exceptionMessages = InputsValidation.verifyBase64EncoderInputs(filePath);

        if (!exceptionMessages.isEmpty()) {
            final Map<String, String> result = OutputUtilities.getFailureResultsMap(StringUtilities.join(exceptionMessages, Constants.NEW_LINE));
            result.put(Constants.RETURN_VALUE, Constants.ENCODE_EXCEPTION_MESSAGE);
            return result;
        }

        try {
            Base64EncoderInputs base64EncoderInputs = new Base64EncoderInputs.Base64EncoderInputsBuilder()
                    .with(builder -> builder.filePath = filePath)
                    .buildInputs();
            final String resultResult = Base64EncoderToStringImpl.displayEncodedBytes(base64EncoderInputs);
            final Map<String, String> result = OutputUtilities.getSuccessResultsMap(Constants.ENCODE_RETURN_VALUE + resultResult);
            result.put(Constants.RETURN_VALUE, resultResult);
            return result;
        } catch (Exception exception) {
            final Map<String, String> result = OutputUtilities.getFailureResultsMap(exception);
            result.put(Constants.RETURN_VALUE, Constants.ENCODE_NO_FILE_EXCEPTION);
            return result;
        }
    }
}
