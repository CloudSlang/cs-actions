package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Outputs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public static String getElementsString(Set<String> elements, String delimiter) {
        if (elements.size() > Constants.Values.START_INDEX) {
            int index = Constants.Values.START_INDEX;
            StringBuilder sb = new StringBuilder();
            for (String element : elements) {
                sb.append(element);
                if (index < elements.size() - Constants.Values.ONE) {
                    sb.append(delimiter);
                }
                index++;
            }
            return sb.toString();
        }
        return Constants.Miscellaneous.EMPTY;
    }

    public static Map<String, String> populateSignatureResultsMap(AuthorizationHeader authorizationHeader) {
        Map<String, String> signatureReturnResultMap = getResultsMap(authorizationHeader.getSignature());

        signatureReturnResultMap.put(Constants.AwsParams.SIGNATURE_RESULT, authorizationHeader.getSignature());
        signatureReturnResultMap.put(Constants.AwsParams.AUTHORIZATION_HEADER_RESULT, authorizationHeader.getAuthorizationHeader());

        return signatureReturnResultMap;
    }
}
