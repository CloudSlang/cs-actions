package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.services.AuthorizationTokenImpl.getToken;
import static io.cloudslang.content.utils.AuthorizationInputNames.EXPIRY;
import static io.cloudslang.content.utils.AuthorizationInputNames.IDENTIFIER;
import static io.cloudslang.content.utils.AuthorizationInputNames.PRIMARY_OR_SECONDARY_KEY;
import static io.cloudslang.content.utils.InputsValidation.verifyRequiredInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * Created by victor on 27.09.2016.
 */
public class GetAuthorizationToken {

    @Action(name = "Get the authorization token for Azure",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = IDENTIFIER, required = true) final String identifier,
                                       @Param(value = PRIMARY_OR_SECONDARY_KEY, required = true) final String primaryOrSecondaryKey,
                                       @Param(value = EXPIRY, required = true) final String expiry) {

        final List<String> exceptionMessages = verifyRequiredInputs(identifier, primaryOrSecondaryKey, expiry);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(exceptionMessages.toString());
        }
        try {
            return getSuccessResultsMap(getToken(identifier.trim(), primaryOrSecondaryKey.trim(), expiry.trim()));
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }

    public static void main(String[] args) {
        System.out.println(new GetAuthorizationToken().execute("53dd860e1b72ff0467030003", "pXeTVcmdbU9XxH6fPcPlq8Y9D9G3Cdo5Eh2nMSgKj/DWqeSFFXDdmpz5Trv+L2hQNM+nGa704Rf8Z22W9O1jdQ==", "1"));
    }
}
