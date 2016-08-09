package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.AuthorizationHeader;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.junit.Test;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AmazonSignatureServiceTest {
    @Test
    public void testNormalSignature() throws SignatureException {
        String expectedSignature = "f0e8bdb87c964420e857bd35b5d6ed310bd44f0170aba48dd91039c6036bdb41";
        String expectedAuthorizationHeader = "AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/s3/aws4_request," +
                "SignedHeaders=host;range;x-amz-content-sha256;x-amz-date," +
                "Signature=f0e8bdb87c964420e857bd35b5d6ed310bd44f0170aba48dd91039c6036bdb41";

        String headers = "Range: bytes=0-9\r\n" +
                "x-amz-content-sha256: e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\r\n" +
                "x-amz-date: 20130524T000000Z";
        String requestUri = "/test.txt";

        Map<String, String> headersMap = InputsUtil.getHeadersMap(headers);

        AmazonSignatureService amazonSignatureService = new AmazonSignatureService();

        AuthorizationHeader authorizationHeader = amazonSignatureService
                .computeSignatureAuthorization(null, requestUri, null, new HashMap<String, String>(), headersMap, "s3",
                        "examplebucket.s3.amazonaws.com", "AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",
                        "20130524T000000Z", false);

        assertEquals(expectedSignature, authorizationHeader.getSignature());
        assertEquals(expectedAuthorizationHeader, authorizationHeader.getAuthorizationHeader());
    }
}