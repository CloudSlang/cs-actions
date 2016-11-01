package io.cloudslang.content.amazon.services;

import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.services.helpers.AwsSignatureHelper;
import io.cloudslang.content.amazon.services.helpers.AwsSignatureV4;
import io.cloudslang.content.amazon.utils.InputsUtil;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HEADER_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.LINE_SEPARATOR;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.SCOPE_SEPARATOR;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AmazonSignatureService {
    private static final String AWS4_SIGNING_ALGORITHM = "AWS4-HMAC-SHA256";
    private static final String AUTHORIZATION = "Authorization";
    private static final String HOST = "Host";
    private static final String PROTOCOL_AND_HOST_SEPARATOR = "://";
    private static final String T_REGEX_STRING = "T";
    private static final String X_AMZ_DATE = "X-Amz-Date";
    private static final String X_AMZ_SECURITY_TOKEN = "X-Amz-Security-Token";
    private static final String AMAZON_HOSTNAME = "amazonaws.com";

    private AwsSignatureV4 awsSignatureV4 = new AwsSignatureV4();

    public AuthorizationHeader signRequestHeaders(InputsWrapper wrapper, Map<String, String> headersMap,
                                                  Map<String, String> queryParamsMap) throws SignatureException, MalformedURLException {
        AwsSignatureHelper signatureUtils = new AwsSignatureHelper();
        String amazonDate = isBlank(wrapper.getDate()) ? signatureUtils.getAmazonDateString(new Date()) : wrapper.getDate();
        String dateStamp = amazonDate.split(T_REGEX_STRING)[0];

        String requestEndpoint = getRequestEndpoint(wrapper.getCommonInputs().getEndpoint());
        String region = signatureUtils.getAmazonRegion(requestEndpoint);

        String apiService = InputsUtil.getDefaultStringInput(wrapper.getApiService(), AMAZON_EC2_API);

        String credentialScope = signatureUtils.getAmazonCredentialScope(dateStamp, region, apiService);
        String amzCredential = wrapper.getCommonInputs().getIdentity() + SCOPE_SEPARATOR + credentialScope;

        Map<String, String> requestHeaders = getRequestHeadersMap(headersMap, wrapper.getHeaders(), requestEndpoint,
                wrapper.getSecurityToken(), amazonDate);
        String signedHeadersString = signatureUtils.getSignedHeadersString(requestHeaders);

        queryParamsMap = getPopulatedQueryParamsMap(queryParamsMap, wrapper);

        String canonicalRequest = awsSignatureV4.getCanonicalRequest(wrapper.getHttpVerb(), wrapper.getRequestUri(),
                signatureUtils.canonicalizedQueryString(queryParamsMap), signatureUtils.canonicalizedHeadersString(requestHeaders),
                signedHeadersString, wrapper.getRequestPayload());

        String stringToSign = awsSignatureV4.getStringToSign(amazonDate, credentialScope, canonicalRequest);
        byte[] key = awsSignatureV4.getDerivedSigningKey(wrapper.getCommonInputs().getCredential(), dateStamp, region, apiService);
        String signature = awsSignatureV4.getSignature(stringToSign, key);

        String authorizationHeader = AWS4_SIGNING_ALGORITHM + " Credential=" + amzCredential + ", SignedHeaders=" + signedHeadersString + ", Signature=" + signature;
        requestHeaders.put(AUTHORIZATION, authorizationHeader);

        return new AuthorizationHeader(InputsUtil.getHeadersOrParamsString(requestHeaders, COLON, LINE_SEPARATOR, false), signature);
    }

    private Map<String, String> getPopulatedQueryParamsMap(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        return isBlank(wrapper.getQueryParams()) ? getInitializedMap(queryParamsMap) :
                InputsUtil.getHeadersOrQueryParamsMap(getInitializedMap(queryParamsMap), wrapper.getQueryParams(), AMPERSAND, EQUAL, false);
    }

    private Map<String, String> getRequestHeadersMap(Map<String, String> headersMap, String headers, String requestEndpoint,
                                                     String securityToken, String amazonDate) {
        headersMap = isBlank(headers) ? getInitializedMap(headersMap) :
                InputsUtil.getHeadersOrQueryParamsMap(getInitializedMap(headersMap), headers, HEADER_DELIMITER, COLON, true);

        if (!(headersMap.containsKey(HOST.toLowerCase()) || headersMap.containsKey(HOST))) {
            requestEndpoint = requestEndpoint.contains(PROTOCOL_AND_HOST_SEPARATOR) ?
                    requestEndpoint.substring(requestEndpoint.indexOf(PROTOCOL_AND_HOST_SEPARATOR) + 3) :
                    requestEndpoint;

            headersMap.put(HOST.toLowerCase(), requestEndpoint); // At least the host header must be signed.
        }

        InputsUtil.setOptionalMapEntry(headersMap, X_AMZ_DATE, amazonDate,
                !(headersMap.containsKey(X_AMZ_DATE.toLowerCase()) || headersMap.containsKey(X_AMZ_DATE)));
        InputsUtil.setOptionalMapEntry(headersMap, X_AMZ_SECURITY_TOKEN, securityToken, isNotBlank(securityToken));

        return headersMap;
    }

    private String getRequestEndpoint(String requestEndpoint) throws MalformedURLException {
        requestEndpoint = InputsUtil.getDefaultStringInput(requestEndpoint, AMAZON_EC2_API + DOT + AMAZON_HOSTNAME);
        if (!requestEndpoint.contains(AMAZON_HOSTNAME)) {
            requestEndpoint = InputsUtil.getEndpointFromUrl(requestEndpoint);
        }

        return requestEndpoint;
    }

    @NotNull
    private Map<String, String> getInitializedMap(Map<String, String> inputMap) {
        return (inputMap == null || inputMap.isEmpty()) ? new HashMap<String, String>() : inputMap;
    }
}