/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.oci.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.Hashing;
import org.tomitribe.auth.signatures.PEM;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class SignerImpl {

    public static PrivateKey loadPrivateKey(String privateKeyData, String privateKeyFile) {
        try {
            InputStream privateKeyStream;
            if(!(privateKeyData).isEmpty()) {
                privateKeyStream = new ByteArrayInputStream(privateKeyData.getBytes());
            }else{
                privateKeyStream = Files.newInputStream(Paths.get(privateKeyFile));
            }
            return PEM.readPrivateKey(privateKeyStream);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Invalid format for private key");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load private key");
        }
    }

    public static class RequestSigner {
        private static final SimpleDateFormat DATE_FORMAT;
        private static final String SIGNATURE_ALGORITHM = "rsa-sha256";
        private static final Map<String, List<String>> REQUIRED_HEADERS;

        static {
            DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
            REQUIRED_HEADERS = ImmutableMap.<String, List<String>>builder()
                    .put(GET, ImmutableList.of(DATE, REQUEST_TARGET, HOST))
                    .put(HEAD, ImmutableList.of(DATE, REQUEST_TARGET, HOST))
                    .put(DELETE, ImmutableList.of(DATE, REQUEST_TARGET, HOST))
                    .put(PUT, ImmutableList.of(DATE, REQUEST_TARGET, HOST, CONTENT_LENGTH, CONTENT_TYPE, X_CONTENT_SHA256))
                    .put(POST, ImmutableList.of(DATE, REQUEST_TARGET, HOST, CONTENT_LENGTH, CONTENT_TYPE, X_CONTENT_SHA256))
                    .build();
        }

        private final Map<String, Signer> signers;

        public RequestSigner(String apiKey, Key privateKey) {
            this.signers = REQUIRED_HEADERS
                    .entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> buildSigner(apiKey, privateKey, entry.getKey())));
        }

        protected Signer buildSigner(String apiKey, Key privateKey, String method) {
            final Signature signature = new Signature(
                    apiKey, SIGNATURE_ALGORITHM, null, REQUIRED_HEADERS.get(method.toLowerCase()));
            return new Signer(privateKey, signature);
        }


        public Map<String, String> signRequest(URI uri, String method, String requestBody) {
            Map<String, String> myheaders = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            String date = DATE_FORMAT.format(new Date());
            headers.put(DATE, date);
            myheaders.put(DATE + COLON, date);
            headers.put(HOST, uri.getHost());
            myheaders.put(HOST + COLON, uri.getHost());
            if (method.equals(PUT) || method.equals(POST)) {
                headers.put(CONTENT_TYPE, APPLICATION_JSON);
                myheaders.put(CONTENT_TYPE + COLON, APPLICATION_JSON);

                if (!isEmpty(requestBody)) {
                    byte[] body = requestBody.getBytes();

                    headers.put(CONTENT_LENGTH, Integer.toString(body.length));
//                    myheaders.put(CONTENT_LENGTH + COLON, Integer.toString(body.length));

                    headers.put(X_CONTENT_SHA256, calculateSHA256(body));
                    myheaders.put(X_CONTENT_SHA256 + COLON, calculateSHA256(body));

                }
            }
            String path;
            if (!isEmpty(uri.getQuery())) {
                path = uri.getPath() + QUERY + uri.getQuery();
            } else {
                path = uri.getPath();
            }

            final String signature = this.calculateSignature(method, path, headers);
            myheaders.put(AUTHORIZATION, signature);
            return myheaders;
        }

        private String calculateSHA256(byte[] body) {
            byte[] hash = Hashing.sha256().hashBytes(body).asBytes();
            return Base64.getEncoder().encodeToString(hash);
        }

        private String calculateSignature(String method, String path, Map<String, String> headers) {
            Signer signer = this.signers.get(method);
            if (signer == null) {
                throw new RuntimeException("failed to sign with the give method" + method);
            }
            try {
                return signer.sign(method, path, headers).toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate signature", e);
            }
        }


    }
}
