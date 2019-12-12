/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.amazon.services;

import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
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

    private static final String EXPECTED_SIGNATURE = "f0c1f1706f0378773c039781a41807b53d135bca4a0e1d5c824f9e14ae514078";

    @Test
    public void testSignature() throws SignatureException, MalformedURLException {
        AuthorizationHeader authorizationHeader = new AmazonSignatureService().signRequestHeaders(getWrapper(), null, null);

        assertEquals(EXPECTED_SIGNATURE, authorizationHeader.getSignature());
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("Authorization:AWS4-HMAC-SHA256 " +
                "Credential=AKIAIOSFODNN7EXAMPLE/20130524/us-east-1/s3/aws4_request, " +
                "SignedHeaders=host;range;x-amz-content-sha256;x-amz-date, " +
                "Signature=f0c1f1706f0378773c039781a41807b53d135bca4a0e1d5c824f9e14ae514078"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("x-amz-content-sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("x-amz-date:20130524T000000Z"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("host:s3.amazonaws.com"));
        assertTrue(authorizationHeader.getAuthorizationHeader().contains("range:bytes=0-9"));
    }

    private CommonInputs getCommonInputs() throws MalformedURLException {
        return new CommonInputs.Builder()
                .withEndpoint("https://s3.amazonaws.com", "s3", "")
                .withIdentity("AKIAIOSFODNN7EXAMPLE")
                .withCredential("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                .withApiService(API_SERVICE)
                .withRequestUri(REQUEST_URI)
                .withHttpClientMethod("GET")
                .withRequestPayload("")
                .withHeaders(HEADERS)
                .withQueryParams("")
                .build();
    }

    private InputsWrapper getWrapper() throws MalformedURLException {
        return new InputsWrapper.Builder()
                .withCommonInputs(getCommonInputs())
                .withApiService(API_SERVICE)
                .withRequestUri(REQUEST_URI)
                .withHttpVerb("GET")
                .withRequestPayload("")
                .withDate(DATE)
                .withHeaders(HEADERS)
                .withQueryParams("")
                .withSecurityToken(null)
                .build();
    }
}
