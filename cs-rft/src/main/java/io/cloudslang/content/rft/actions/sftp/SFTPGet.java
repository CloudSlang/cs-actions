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
package io.cloudslang.content.rft.actions.sftp;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.rft.entities.sftp.SFTPCommonInputs;
import io.cloudslang.content.rft.entities.sftp.SFTPConnection;
import io.cloudslang.content.rft.entities.sftp.SFTPGetInputs;
import io.cloudslang.content.rft.services.SFTPService;
import io.cloudslang.content.rft.utils.SFTPOperation;
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
import static io.cloudslang.content.rft.utils.Descriptions.SFTPDescriptions.*;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.*;
import static io.cloudslang.content.rft.utils.InputsValidation.verifyInputsSFTP;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class SFTPGet {
    @Action(name = "SFTP Get Operation",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = PARAM_HOST, description = PARAM_HOST_DESC) String host,
                                       @Param(value = PARAM_PORT, description = PARAM_PORT_DESC) String port,
                                       @Param(value = PARAM_USERNAME, description = PARAM_USERNAME_DESC) String username,
                                       @Param(value = PARAM_PASSWORD, description = PARAM_PASSWORD_DESC) String password,
                                       @Param(value = PARAM_PRIVATE_KEY, description = PARAM_PRIVATE_KEY_DESC) String privateKey,
                                       @Param(value = PARAM_REMOTE_FILE, description = PARAM_REMOTE_FILE_DESC) String remoteFile,
                                       @Param(value = PARAM_LOCAL_LOCATION, description = PARAM_LOCAL_LOCATION_DESC) String localLocation,
                                       @Param(value = SSH_SESSIONS_DEFAULT_ID, description = PARAM_GLOBAL_SESSION_DESC) GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject,
                                       @Param(value = PARAM_CHARACTER_SET, description = PARAM_CHARACTER_SET_DESC) String characterSet,
                                       @Param(value = PARAM_CLOSE_SESSION, description = PARAM_CLOSE_SESSION_DESC) String closeSession) {

        host = defaultIfEmpty(host, EMPTY);
        port = defaultIfEmpty(port, String.valueOf(DEFAULT_PORT));
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        privateKey = defaultIfEmpty(privateKey, EMPTY);
        remoteFile = defaultIfEmpty(remoteFile, EMPTY);
        localLocation = defaultIfEmpty(localLocation, EMPTY);
        characterSet = defaultIfEmpty(characterSet, CHARACTER_SET_UTF8);
        closeSession = defaultIfEmpty(closeSession, BOOLEAN_TRUE);

        final List<String> exceptionMessages = verifyInputsSFTP(host, port, username, password, privateKey, characterSet, closeSession, SFTPOperation.GET, remoteFile, localLocation);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        SFTPGetInputs sftpGetInputs = SFTPGetInputs.builder()
                .remoteFile(remoteFile)
                .localLocation(localLocation)
                .sftpCommonInputs(SFTPCommonInputs.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .privateKey(privateKey)
                        .characterSet(characterSet)
                        .closeSession(closeSession)
                        .globalSessionObject(globalSessionObject)
                        .build())
                .build();

        return new SFTPService().execute(sftpGetInputs,SFTPOperation.GET);

    }
}
