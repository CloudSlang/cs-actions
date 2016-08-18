package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import org.junit.Test;

import java.net.MalformedURLException;
import java.security.SignatureException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AmazonSignatureServiceTest {
    private static final String API_SERVICE = "s3";
    private static final String REQUEST_URI = "/test.txt";
    private static final String DATE = "20130524T000000Z";
    private static final String HEADERS = "Range:bytes=0-9\r\n" +
            "x-amz-content-sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\r\n" +
            "x-amz-date:20130524T000000Z";

    private static final String EXPECTED_SIGNATURE = "f0e8bdb87c964420e857bd35b5d6ed310bd44f0170aba48dd91039c6036bdb41";

    @Test
    public void testSignature() throws SignatureException, MalformedURLException {
        AuthorizationHeader authorizationHeader = new AmazonSignatureService().signRequestHeaders(getWrapper(), null, null);

        assertEquals(EXPECTED_SIGNATURE, authorizationHeader.getSignature());
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("Authorization:AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/s3/aws4_request, SignedHeaders=host;range;x-amz-content-sha256;x-amz-date, Signature=f0e8bdb87c964420e857bd35b5d6ed310bd44f0170aba48dd91039c6036bdb41"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("x-amz-content-sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("x-amz-date:20130524T000000Z"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("host:examplebucket.s3.amazonaws.com"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("range:bytes=0-9"));
    }

    private CommonInputs getCommonInputs() throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withEndpoint("https://examplebucket.s3.amazonaws.com")
                .withIdentity("AKIAIOSFODNN7EXAMPLE")
                .withCredential("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                .build();
    }

    private AWSInputsWrapper getWrapper() throws MalformedURLException {
        return new AWSInputsWrapper.AWSInputsWrapperBuilder()
                .withCommonInputs(getCommonInputs())
                .withApiService(API_SERVICE)
                .withRequestUri(REQUEST_URI)
                .withHttpVerb(null)
                .withRequestPayload(null)
                .withDate(DATE)
                .withHeaders(HEADERS)
                .withQueryParams("")
                .withSecurityToken(null)
                .build();
    }
}