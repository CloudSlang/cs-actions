package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.junit.Test;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AmazonSignatureServiceTest {
    @Test
    public void testNormalSignature() throws SignatureException, MalformedURLException {
        String expectedSignature = "492846040557217d2b58535a4bc1cde9ea7084609f80e90ca4b33a9bb8205a93";
        String expectedAuthorizationHeader = "x-amz-content-sha256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\n" +
                "Authorization:AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/ec2/aws4_request, SignedHeaders=host;range;x-amz-content-sha256;x-amz-date;x-amz-date, Signature=492846040557217d2b58535a4bc1cde9ea7084609f80e90ca4b33a9bb8205a93\n" +
                "X-Amz-Date:20130524T000000Z\n" +
                "x-amz-date: 20130524T000000Z\n" +
                "host:examplebucket.s3.amazonaws.com\n" +
                "range: bytes=0-9\n";

        String headers = "Range: bytes=0-9\r\n" +
                "x-amz-content-sha256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\r\n" +
                "x-amz-date: 20130524T000000Z";
        String requestUri = "/test.txt";

        Map<String, String> headersMap = InputsUtil.getHeadersMap(headers);
        Map<String, String> queryParamsMap = InputsUtil.getHeadersMap("");

        AmazonSignatureService amazonSignatureService = new AmazonSignatureService();

        AuthorizationHeader authorizationHeader = amazonSignatureService
                .signRequestHeaders("examplebucket.s3.amazonaws.com", "AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",
                        "", requestUri, null, null, null, "20130524T000000Z", headersMap, queryParamsMap);

        assertEquals(expectedSignature, authorizationHeader.getSignature());
        assertEquals(expectedAuthorizationHeader, authorizationHeader.getAuthorizationHeader());
    }
}