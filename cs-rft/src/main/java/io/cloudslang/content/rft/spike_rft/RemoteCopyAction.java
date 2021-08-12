package io.cloudslang.content.rft.spike_rft;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.rft.spike_rft.Constants.*;
import static io.cloudslang.content.rft.spike_rft.InputsValidation.*;
import static io.cloudslang.content.rft.utils.Descriptions.FTPDescriptions.*;

public class RemoteCopyAction {

    @Action(name = "Remote Copy Action",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = INPUT_SRC_HOST, description = PARAM_HOSTNAME_DESC) String sourceHost,
                                       @Param(value = INPUT_SRC_PORT, description = PARAM_PORT_DESC) String sourcePort,
                                       @Param(value = INPUT_SRC_USERNAME, description = PARAM_LOCAL_FILE_DESC) String sourceUsername,
                                       @Param(value = INPUT_SRC_PASSWORD, description = PARAM_REMOTE_FILE_DESC) String sourcePassword,
                                       @Param(value = INPUT_SRC_PRIVATE_KEY_FILE, description = PARAM_USER_DESC) String sourcePrivateKeyFile,
                                       @Param(value = INPUT_SRC_PATH, description = PARAM_PASSWORD_DESC) String sourcePath,
                                       @Param(value = INPUT_SRC_PROTOCOL, description = PARAM_TYPE_DESC) String sourceProtocol,
                                       @Param(value = INPUT_SRC_TIMEOUT, description = PARAM_PASSIVE_DESC) String sourceTimeout,
                                       @Param(value = INPUT_SRC_CHARACTER_SET, description = PARAM_CHARACTER_SET_DESC) String sourceCharacterSet,
                                       @Param(value = INPUT_DEST_HOST, description = PARAM_HOSTNAME_DESC) String destinationHost,
                                       @Param(value = INPUT_DEST_PORT, description = PARAM_PORT_DESC) String destinationPort,
                                       @Param(value = INPUT_DEST_USERNAME, description = PARAM_LOCAL_FILE_DESC) String destinationUsername,
                                       @Param(value = INPUT_DEST_PASSWORD, description = PARAM_REMOTE_FILE_DESC) String destinationPassword,
                                       @Param(value = INPUT_DEST_PRIVATE_KEY_FILE, description = PARAM_USER_DESC) String destinationPrivateKeyFile,
                                       @Param(value = INPUT_DEST_PATH, description = PARAM_PASSWORD_DESC) String destinationPath,
                                       @Param(value = INPUT_DEST_PROTOCOL, description = PARAM_TYPE_DESC) String destinationProtocol,
                                       @Param(value = INPUT_DEST_TIMEOUT, description = PARAM_PASSIVE_DESC) String destinationTimeout,
                                       @Param(value = INPUT_DEST_CHARACTERSET, description = PARAM_CHARACTER_SET_DESC) String destinationCharacterSet,
                                       @Param(value = INPUT_FILE_TYPE, description = PARAM_CHARACTER_SET_DESC) String fileType,
                                       @Param(value = INPUT_PASSIVE, description = PARAM_CHARACTER_SET_DESC) String passive) throws Exception {

        Map<String, String> result = null;

        validateProtocol(sourceProtocol);
        validateProtocol(destinationProtocol);
        ICopier src = CopierFactory.getExecutor(sourceProtocol);
        src.setProtocol(sourceProtocol);
        ICopier dest = CopierFactory.getExecutor(destinationProtocol);
        dest.setProtocol(destinationProtocol);

        io.cloudslang.content.rft.spike_rft.InputsValidation.checkOptions(sourceProtocol, sourceHost, fileType);
        checkOptions(destinationProtocol, destinationHost, fileType);

        setCredentials(src, sourceHost, sourceProtocol, sourceUsername, sourcePassword, sourcePrivateKeyFile);
        setCredentials(dest, destinationHost, destinationPort, destinationUsername, destinationPassword, destinationPrivateKeyFile);

        setAndValidateCharacterSet(src, sourceCharacterSet, INPUT_SRC_CHARACTER_SET);
        setAndValidateCharacterSet(dest, destinationCharacterSet, INPUT_DEST_CHARACTERSET);

        setCustomArgumentForFTP(src, dest, sourceProtocol, destinationProtocol, fileType, Boolean.parseBoolean(passive));

        setTimeout(src, sourceTimeout);
        setTimeout(dest, destinationTimeout);

        src.copyTo(dest, sourcePath, destinationPath);
        result.put("returnResult", "Copy completed successfully");
        result.put("returnCode", "0");

        return result;
    }

}