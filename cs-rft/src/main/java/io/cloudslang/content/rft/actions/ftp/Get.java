/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.actions.ftp;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.rft.entities.FTPInputs;
import io.cloudslang.content.rft.services.FTPService;
import io.cloudslang.content.rft.utils.FTPOperation;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Descriptions.CommonInputsDescriptions.PASSIVE_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.CommonInputsDescriptions.TYPE_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.FTPDescriptions.*;
import static io.cloudslang.content.rft.utils.Inputs.FTPInputs.*;
import static io.cloudslang.content.rft.utils.InputsValidation.verifyInputsFTP;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class Get {
    @Action(name = "FTP Get Operation",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = FTP_REPLY_CODE, description = FTP_REPLY_CODE_DESC),
                    @Output(value = FTP_SESSION_LOG, description = FTP_SESSION_LOG_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST_NAME, description = HOSTNAME_DESC) String hostName,
                                       @Param(value = PORT, description = PORT_DESC) String port,
                                       @Param(value = LOCAL_FILE, description = LOCAL_FILE_DESC) String localFile,
                                       @Param(value = REMOTE_FILE, description = REMOTE_FILE_DESC) String remoteFile,
                                       @Param(value = USER, description = USER_DESC) String user,
                                       @Param(value = PASSWORD, description = PASSWORD_DESC) String password,
                                       @Param(value = TYPE, description = TYPE_DESC) String type,
                                       @Param(value = PASSIVE, description = PASSIVE_DESC) String passive,
                                       @Param(value = CHARACTER_SET, description = CHARACTER_SET_DESC) String characterSet) {

        hostName = defaultIfEmpty(hostName, EMPTY);
        port = defaultIfEmpty(port, PORT_21);
        localFile = defaultIfEmpty(localFile, EMPTY);
        remoteFile = defaultIfEmpty(remoteFile, EMPTY);
        user = defaultIfEmpty(user, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        type = defaultIfEmpty(type, BINARY_FILE_TYPE);
        passive = defaultIfEmpty(passive, BOOLEAN_FALSE);
        passive = passive.toLowerCase();
        characterSet = defaultIfEmpty(characterSet, CHARACTER_SET_LATIN1);


        final List<String> exceptionMessages = verifyInputsFTP(hostName, port, localFile, remoteFile, user, password, type, passive, characterSet);

        if (!exceptionMessages.isEmpty()) {
            Map<String, String> result = getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
            result.put(FTP_REPLY_CODE, "501");                                    //REPLY CODE = 501 SYNTAX ERROR IN PARAMETERS OR ARGUMENTS
            result.put(FTP_SESSION_LOG, "");
            return result;
        }

        return new FTPService().ftpOperation(FTPInputs.builder()
                .hostname(hostName)
                .port(port)
                .localFile(localFile)
                .remoteFile(remoteFile)
                .user(user)
                .password(password)
                .type(type)
                .passive(passive)
                .characterSet(characterSet)
                .build(), FTPOperation.GET);
    }
}
