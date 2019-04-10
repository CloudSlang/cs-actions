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
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.base64decoder.Base64DecoderToFileInputs;
import io.cloudslang.content.utilities.services.base64decoder.Base64DecoderToFileImpl;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.util.base64decoder.Constants.*;
import static io.cloudslang.content.utilities.util.base64decoder.Descriptions.ConvertBytesToFile.*;
import static io.cloudslang.content.utilities.util.base64decoder.Inputs.ConvertBytesToFileInputs.CONTENT_BYTES;
import static io.cloudslang.content.utilities.util.base64decoder.Inputs.ConvertBytesToFileInputs.FILE_PATH;
import static io.cloudslang.content.utilities.util.base64decoder.InputsValidation.verifyBase64DecoderToFileInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class Base64DecoderToFile {
    @Action(name = BASE64_DECODER_TO_FILE,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = RETURN_PATH, description = RETURN_PATH_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = FILE_PATH, description = FILE_PATH_DESC, required = true) String filePath,
                                       @Param(value = CONTENT_BYTES, description = CONTENT_BYTES_DESC, required = true) String contentBytes) {

        final List<String> exceptionMessages = verifyBase64DecoderToFileInputs(filePath, contentBytes);
        if (!exceptionMessages.isEmpty()) {
            final Map<String, String> result = getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
            result.put(RETURN_PATH, EXCEPTION_MESSAGE);
            return result;
        }

        try {
            final Base64DecoderToFileInputs base64DecoderToFileInputs = Base64DecoderToFileInputs.builder()
                    .filePath(filePath)
                    .contentBytes(contentBytes)
                    .build();
            final String resultPath = Base64DecoderToFileImpl.writeBytesToFile(base64DecoderToFileInputs);
            final Map<String, String> result = getSuccessResultsMap(FILE_RETURN_MESSAGE + resultPath);
            result.put(RETURN_PATH, resultPath);

            return result;
        } catch (Exception exception) {
            final Map<String, String> result = getFailureResultsMap(exception);
            result.put(RETURN_PATH, EXCEPTION_MESSAGE);
            return result;
        }
    }
}

