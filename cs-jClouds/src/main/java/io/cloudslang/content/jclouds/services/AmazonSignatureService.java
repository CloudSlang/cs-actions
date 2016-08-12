package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.aws.HttpClientMethods;
import io.cloudslang.content.jclouds.entities.constants.Constants;
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
    private static final String T_REGEX_STRING = "T";
    private static final String X_AMZ_DATE = "X-Amz-Date";
    private static final String X_AMZ_SECURITY_TOKEN = "X-Amz-Security-Token";

    private AWSSignatureV4 awsSignatureV4 = new AWSSignatureV4();

    public AuthorizationHeader signRequestHeaders(String requestEndpoint, String accessKeyId, String secretAccessKey,
                                                  String apiService, String requestUri, String requestHttpMethod,
                                                  String requestPayload, String securityToken, String date,
                                                  Map<String, String> requestHeaders, Map<String, String> queryParams)
            throws SignatureException, MalformedURLException {
        AWSSignatureHelper signatureUtils = new AWSSignatureHelper();
        String amazonDate = StringUtils.isBlank(date) ? signatureUtils.getAmazonDateString(new Date()) : date;

        requestEndpoint = getRequestEndpoint(requestEndpoint);
        requestHeaders = getRequestHeadersMap(requestHeaders, requestEndpoint, securityToken, amazonDate);

        String dateStamp = amazonDate.split(T_REGEX_STRING)[0];
        String region = signatureUtils.getAmazonRegion(requestEndpoint);
        apiService = InputsUtil.getDefaultStringInput(apiService, Constants.Apis.AMAZON_EC2_API);
        String credentialScope = signatureUtils.getAmazonCredentialScope(dateStamp, region, apiService);
        String amzCredential = accessKeyId + Constants.Miscellaneous.SCOPE_SEPARATOR + credentialScope;

        requestHttpMethod = HttpClientMethods.getValue(requestHttpMethod);
        requestUri = InputsUtil.getDefaultStringInput(requestUri, Constants.Miscellaneous.SCOPE_SEPARATOR);
        requestPayload = InputsUtil.getDefaultStringInput(requestPayload, Constants.Miscellaneous.EMPTY);
        String signedHeadersString = signatureUtils.getSignedHeadersString(requestHeaders);

        String canonicalRequest = awsSignatureV4.getCanonicalRequest(requestHttpMethod, requestUri,
                signatureUtils.canonicalizedQueryString(queryParams), signatureUtils.canonicalizedHeadersString(requestHeaders),
                signedHeadersString, requestPayload);

        String stringToSign = awsSignatureV4.getStringToSign(amazonDate, credentialScope, canonicalRequest);
        byte[] key = awsSignatureV4.getDerivedSigningKey(secretAccessKey, dateStamp, region, apiService);
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

    private Map<String, String> getRequestHeadersMap(Map<String, String> requestHeaders, String requestEndpoint,
                                                     String securityToken, String amazonDate) {
        requestHeaders = requestHeaders == null ? new HashMap<String, String>() : requestHeaders;
        if(!(requestHeaders.containsKey(X_AMZ_DATE.toLowerCase()))){
            requestHeaders.put(X_AMZ_DATE, amazonDate);
        }

        if (!(requestHeaders.containsKey(HOST.toLowerCase()) || requestHeaders.containsKey(HOST))) {
            requestHeaders.put(HOST.toLowerCase(), requestEndpoint); // At least the host header must be signed.
        }
        if (StringUtils.isNotBlank(securityToken)) {
            requestHeaders.put(X_AMZ_SECURITY_TOKEN, securityToken);
        }

        return requestHeaders;
    }
}