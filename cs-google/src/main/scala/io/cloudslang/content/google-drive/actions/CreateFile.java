/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.google-drive.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.google-drive.entities.CreateFileInputs;
import io.cloudslang.content.google-drive.entities.GoogleDriveCommonInputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.google-drive.services.GoogleDriveServiceImpl.createFile;
import static io.cloudslang.content.google-drive.utils.Constants.*;
import static io.cloudslang.content.google-drive.utils.Descriptions.Common.*;
import static io.cloudslang.content.google-drive.utils.Descriptions.CreateFile.*;
import static io.cloudslang.content.google-drive.utils.Descriptions.GetAuthorizationToken.FAILURE_DESC;
import static io.cloudslang.content.google-drive.utils.Descriptions.GetAuthorizationToken.SUCCESS_DESC;
import static io.cloudslang.content.google-drive.utils.Descriptions.GetFile.STATUS_CODE_DESC;
import static io.cloudslang.content.google-drive.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.google-drive.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.google-drive.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.google-drive.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.google-drive.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.google-drive.utils.Inputs.CreateFile.*;
import static io.cloudslang.content.google-drive.utils.Inputs.CreateFile.BODY;
import static io.cloudslang.content.google-drive.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.google-drive.utils.InputsValidation.verifyCreateFileInputs;
import static io.cloudslang.content.google-drive.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateFile {
    @Action(name = "Creates a new file.",
            outputs = {
                    @Output(value = RETURN_RESULT, description = CREATE_MESSAGE_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = DOCUMENT, description = DOCUMENT_DESC),
                    @Output(value = EXCEPTION, description = CREATE_MESSAGE_EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
}