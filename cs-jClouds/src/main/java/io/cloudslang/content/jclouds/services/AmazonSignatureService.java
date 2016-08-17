package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.helpers.AWSSignatureHelper;
import io.cloudslang.content.jclouds.services.helpers.AWSSignatureV4;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private AWSSignatureV4 awsSignatureV4 = new AWSSignatureV4();

    public AuthorizationHeader signRequestHeaders(AWSInputsWrapper wrapper, Map<String, String> headersMap,
                                                  Map<String, String> queryParamsMap) throws SignatureException, MalformedURLException {
        AWSSignatureHelper signatureUtils = new AWSSignatureHelper();
        String amazonDate = StringUtils.isBlank(wrapper.getDate()) ? signatureUtils.getAmazonDateString(new Date()) : wrapper.getDate();
        String dateStamp = amazonDate.split(T_REGEX_STRING)[0];

        String requestEndpoint = getRequestEndpoint(wrapper.getCommonInputs().getEndpoint());
        String region = signatureUtils.getAmazonRegion(requestEndpoint);

        String apiService = InputsUtil.getDefaultStringInput(wrapper.getApiService(), Constants.Apis.AMAZON_EC2_API);

        String credentialScope = signatureUtils.getAmazonCredentialScope(dateStamp, region, apiService);
        String amzCredential = wrapper.getCommonInputs().getIdentity() + Constants.Miscellaneous.SCOPE_SEPARATOR + credentialScope;

        Map<String, String> requestHeaders = getRequestHeadersMap(headersMap, wrapper.getHeaders(), requestEndpoint,
                wrapper.getSecurityToken(), amazonDate);
        String signedHeadersString = signatureUtils.getSignedHeadersString(requestHeaders);

        if (queryParamsMap == null || queryParamsMap.isEmpty()) {
            queryParamsMap = new HashMap<>();
        }

        queryParamsMap = StringUtils.isBlank(wrapper.getQueryParams()) ?
                queryParamsMap : InputsUtil.getQueryParamsMap(queryParamsMap, wrapper.getQueryParams());

        String canonicalRequest = awsSignatureV4.getCanonicalRequest(wrapper.getHttpVerb(), wrapper.getRequestUri(),
                signatureUtils.canonicalizedQueryString(queryParamsMap), signatureUtils.canonicalizedHeadersString(requestHeaders),
                signedHeadersString, wrapper.getRequestPayload());

        String stringToSign = awsSignatureV4.getStringToSign(amazonDate, credentialScope, canonicalRequest);
        byte[] key = awsSignatureV4.getDerivedSigningKey(wrapper.getCommonInputs().getCredential(), dateStamp, region, apiService);
        String signature = awsSignatureV4.getSignature(stringToSign, key);

        String authorizationHeader = AWS4_SIGNING_ALGORITHM + " Credential=" + amzCredential +
                ", SignedHeaders=" + signedHeadersString + ", Signature=" + signature;
        requestHeaders.put(AUTHORIZATION, authorizationHeader);

        return new AuthorizationHeader(getSignedRequestHeadersString(requestHeaders), signature);
    }

    private String getSignedRequestHeadersString(Map<String, String> requestHeaders) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            sb.append(entry.getKey());
            sb.append(Constants.Miscellaneous.COLON);
            sb.append(entry.getValue());
            sb.append(Constants.Miscellaneous.LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String getRequestEndpoint(String requestEndpoint) throws MalformedURLException {
        requestEndpoint = InputsUtil.getDefaultStringInput(requestEndpoint, Constants.Apis.AMAZON_EC2_API +
                Constants.Miscellaneous.DOT + Constants.AWSParams.AMAZON_HOSTNAME);
        if (!requestEndpoint.contains(Constants.AWSParams.AMAZON_HOSTNAME)) {
            requestEndpoint = InputsUtil.getEndpointFromUrl(requestEndpoint);
        }

        return requestEndpoint;
    }

    private Map<String, String> getRequestHeadersMap(Map<String, String> headersMap, String headers, String requestEndpoint,
                                                     String securityToken, String amazonDate) {
        if (headersMap == null || headersMap.isEmpty()) {
            headersMap = new HashMap<>();
        }

        if (StringUtils.isNotBlank(headers)) {
            headersMap = InputsUtil.getHeadersMap(headersMap, headers);
        }

        if (!(headersMap.containsKey(HOST.toLowerCase()) || headersMap.containsKey(HOST))) {
            if (requestEndpoint.contains(PROTOCOL_AND_HOST_SEPARATOR)) {
                requestEndpoint = requestEndpoint.substring(requestEndpoint.indexOf(PROTOCOL_AND_HOST_SEPARATOR) + 3);
            }
            headersMap.put(HOST.toLowerCase(), requestEndpoint); // At least the host header must be signed.
        }

        if (!(headersMap.containsKey(X_AMZ_DATE.toLowerCase()) || headersMap.containsKey(X_AMZ_DATE))) {
            headersMap.put(X_AMZ_DATE, amazonDate);
        }


        if (StringUtils.isNotBlank(securityToken)) {
            headersMap.put(X_AMZ_SECURITY_TOKEN, securityToken);
        }

        return headersMap;
    }
}