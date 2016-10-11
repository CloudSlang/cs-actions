package io.cloudslang.content.azure.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.microsoft.aad.adal4j.AuthenticationResult;
import io.cloudslang.content.azure.services.AuthorizationTokenImpl;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.AUTHORITY;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.CLIENT_ID;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.RESOURCE;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.USERNAME;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_AUTHORITY;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_RESOURCE;
import static io.cloudslang.content.azure.utils.Constants.NEW_LINE;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyRequiredInputs;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * Created by victor on 27.09.2016.
 */
public class GetAuthToken {

    /**
     * @param username
     * @param password
     * @param clientId
     * @param authority
     * @param resource
     * @return
     */
    @Action(name = "Get the authorization token for Azure",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = USERNAME, required = true) final String username,
                                       @Param(value = PASSWORD, required = true, encrypted = true) final String password,
                                       @Param(value = CLIENT_ID, required = true) final String clientId,
                                       @Param(value = AUTHORITY) final String authority,
                                       @Param(value = RESOURCE) final String resource) {
        final String checkedAuthority = StringUtilities.defaultIfEmpty(authority, DEFAULT_AUTHORITY);
        final String checkedResource = StringUtilities.defaultIfEmpty(resource, DEFAULT_RESOURCE);
        final List<String> exceptionMessages = verifyRequiredInputs(username, password, clientId);

        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final AuthenticationResult result = AuthorizationTokenImpl.getToken(username, password, checkedAuthority, clientId, checkedResource);
            return getSuccessResultsMap(result.getAccessToken());
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }

}
