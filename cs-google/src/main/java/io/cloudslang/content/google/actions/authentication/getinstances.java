package io.cloudslang.content.google.actions.authentication;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.google.actions.authentication.Inputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class getinstances {
    @Action(name = "List Instances",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = PROJECT_ID, required = true) String projectId,
                                           @Param(value = ZONE) String zone,
                                           @Param(value = FILTER) String filter,
                                           @Param(value = ORDER_BY) String orderBy,
                                           @Param(value = ACCESS_TOKEN, required = true) String accessToken,
                                           @Param(value = PROXY_HOST) String proxyHost,
                                           @Param(value = PROXY_PORT) String proxyPort,
                                           @Param(value = PROXY_USERNAME, required = true) String proxyUsername,
                                           @Param(value = PROXY_PASSWORD) String proxyPassword) {

        proxyHost = defaultIfEmpty(proxyHost, "web-proxy.us.softwaregrp.net");
        proxyPort = defaultIfEmpty(proxyPort, "8080");
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);

        try {
            final Map<String, String> result = GoogleComputeImpl.listinstance(GoogleCreateVMInputs.builder()
                    .accessToken(accessToken)
                    .orderBy(orderBy)
                    .filter(filter)
                    .zone(zone)
                    .projectId(projectId)
                    .proxyHost(proxyHost)
                    .proxyPort(proxyPort)
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword).build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = (result);
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);

        }
    }
}
