package io.cloudslang.content.rft.actions.sftp;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.rft.entities.sftp.SFTPCommandInputs;
import io.cloudslang.content.rft.entities.sftp.SFTPCommonInputs;
import io.cloudslang.content.rft.entities.sftp.SFTPConnection;
import io.cloudslang.content.rft.services.SFTPService;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.Inputs;
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
import static io.cloudslang.content.rft.utils.Constants.SSH_SESSIONS_DEFAULT_ID;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.*;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.EXCEPTION_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.FAILURE_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.RETURN_CODE_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.RETURN_RESULT_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPCommandDescriptions.SUCCESS_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPDescriptions.*;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.*;
import static io.cloudslang.content.rft.utils.Constants.*;

import static io.cloudslang.content.rft.utils.InputsValidation.verifyInputsSFTPCommand;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class SFTPCommand {
    @Action(name = SFTP_COMMAND,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOST, description = HOST_NAME, required = true) String host,
                                       @Param(value = PORT, description = PORT_DESC) String port,
                                       @Param(value = USERNAME, description = USERNAME_DESC, required = true) String username,
                                       @Param(value = PASSWORD, description = PASSWORD_DESC, required = true, encrypted = true) String password,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, description = PROXY_PASSWORD_DESC, encrypted = true) String proxyPassword,
                                       @Param(value = PRIVATE_KEY, description = PRIVATE_KEY_DESC) String privateKey,
                                       @Param(value = COMMAND_TYPE, description = COMMAND_TYPE_DESC) String commandType,
                                       @Param(value = REMOTE_PATH, description = REMOTE_PATH_DESC, required = true) String remotePath,
                                       @Param(value = MODE, description = MODE_DESC, required = true) String mode,
                                       @Param(value = GID, description = GID_DESC, required = true) String gid,
                                       @Param(value = UID, description = UID_DESC, required = true) String uid,
                                       @Param(value = SSH_SESSIONS_DEFAULT_ID, description = GLOBAL_SESSION_DESC) GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject,
                                       @Param(value = CHARACTER_SET, description = CHARACTER_SET_DESC) String characterSet,
                                       @Param(value = CLOSE_SESSION, description = CLOSE_SESSION_DESC) String closeSession,
                                       @Param(value = Inputs.SFTPInputs.CONNECTION_TIMEOUT, description = CONNECTION_TIMEOUT_DESC) String connectionTimeout,
                                       @Param(value = Inputs.SFTPInputs.EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout) {

        host = defaultIfEmpty(host, EMPTY);
        port = defaultIfEmpty(port, String.valueOf(DEFAULT_PORT));
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, String.valueOf(DEFAULT_PROXY_PORT));
        privateKey = defaultIfEmpty(privateKey, EMPTY);
        remotePath = defaultIfEmpty(remotePath, EMPTY);
        characterSet = defaultIfEmpty(characterSet, CHARACTER_SET_UTF8);
        closeSession = defaultIfEmpty(closeSession, BOOLEAN_TRUE);
        connectionTimeout = defaultIfEmpty(connectionTimeout, Constants.CONNECTION_TIMEOUT);
        executionTimeout = defaultIfEmpty(executionTimeout, Constants.EXECUTION_TIMEOUT);

        final List<String> exceptionMessages = verifyInputsSFTPCommand(remotePath, mode, host, port, username, password,
                proxyPort, characterSet, closeSession, connectionTimeout, executionTimeout);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        SFTPCommandInputs sftpCommandInputs = SFTPCommandInputs.builder()
                .remotePath(remotePath)
                .mode(mode)
                .commandType(commandType)
                .gid(gid)
                .uid(uid)
                .sftpCommonInputs(SFTPCommonInputs.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .proxyHost(proxyHost)
                        .proxyPort(proxyPort)
                        .proxyUsername(proxyUsername)
                        .proxyPassword(proxyPassword)
                        .connectionTimeout(connectionTimeout)
                        .executionTimeout(executionTimeout)
                        .privateKey(privateKey)
                        .characterSet(characterSet)
                        .closeSession(closeSession)
                        .globalSessionObject(globalSessionObject)
                        .build())
                .build();

        return new SFTPService().execute(sftpCommandInputs, SFTPOperation.COMMAND);
    }
}
