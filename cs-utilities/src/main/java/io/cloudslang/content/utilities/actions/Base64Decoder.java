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
package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.base64decoder.Base64DecoderInputs;
import io.cloudslang.content.utilities.services.base64decoder.Base64DecoderImpl;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.util.base64decoder.Constants.FILE_RETURN_MESSAGE;
import static io.cloudslang.content.utilities.util.base64decoder.Constants.NEW_LINE;
import static io.cloudslang.content.utilities.util.base64decoder.Descriptions.ConvertBytesToFile.*;
import static io.cloudslang.content.utilities.util.base64decoder.Inputs.ConvertBytesToFileInputs.CONTENT_BYTES;
import static io.cloudslang.content.utilities.util.base64decoder.Inputs.ConvertBytesToFileInputs.FILE_PATH;
import static io.cloudslang.content.utilities.util.base64decoder.InputsValidation.verifyBase64DecoderInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class Base64Decoder {
    @Action(name = BASE64_DECODER_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = FILE_PATH, description = FILE_PATH_DESC, required = true) String filePath,
                                       @Param(value = CONTENT_BYTES, description = CONTENT_BYTES_DESC, required = true) String contentBytes) {

        final List<String> exceptionMessages = verifyBase64DecoderInputs(filePath, contentBytes);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Base64DecoderInputs base64DecoderInputs = Base64DecoderInputs.builder()
                    .filePath(filePath)
                    .contentBytes(contentBytes)
                    .build();
            final String resultPath = Base64DecoderImpl.writeBytesToFile(base64DecoderInputs);
            final Map<String, String> result = getSuccessResultsMap(FILE_RETURN_MESSAGE + resultPath);
            result.put(FILE_PATH, resultPath);

            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}

