package io.cloudslang.content.amazon.utils;

import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.constants.Outputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AUTHORIZATION_HEADER_RESULT;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SIGNATURE_RESULT;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class OutputsUtil {
    private OutputsUtil() {
    }

    public static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }

    public static Map<String, String> populateSignatureResultsMap(AuthorizationHeader authorizationHeader) {
        Map<String, String> signatureReturnResultMap = getResultsMap(authorizationHeader.getSignature());

        signatureReturnResultMap.put(SIGNATURE_RESULT, authorizationHeader.getSignature());
        signatureReturnResultMap.put(AUTHORIZATION_HEADER_RESULT, authorizationHeader.getAuthorizationHeader());

        return signatureReturnResultMap;
    }
}
