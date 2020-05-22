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

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.AUTHORIZATION;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.QUERY;

public class SignerImpl {

    public static PrivateKey loadPrivateKey(String privateKeyFilename) {
        try (InputStream privateKeyStream = Files.newInputStream(Paths.get(privateKeyFilename))){
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
                    .put("get", ImmutableList.of("date", "(request-target)", "host"))
                    .put("head", ImmutableList.of("date", "(request-target)", "host"))
                    .put("delete", ImmutableList.of("date", "(request-target)", "host"))
                    .put("put", ImmutableList.of("date", "(request-target)", "host", "content-length", "content-type", "x-content-sha256"))
                    .put("post", ImmutableList.of("date", "(request-target)", "host", "content-length", "content-type", "x-content-sha256"))
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



        public Map<String,String> signRequest(URI uri, String method, String requestBody) {
            Map<String,String> myheaders = new HashMap<>();
Map<String,String> headers = new HashMap<>();
String date=DATE_FORMAT.format(new Date());
            headers.put("date", date);
            myheaders.put("date:", date);
            headers.put("host", uri.getHost());
            myheaders.put("host:", uri.getHost());
          if (method.equals("put") || method.equals("post")) {
                headers.put("content-type", "application/json");
                myheaders.put("content-type:", "application/json");

                if (requestBody!=null && !requestBody.isEmpty()) {
                    byte[] body =requestBody.getBytes();

                    headers.put("content-length", Integer.toString(body.length));
                    myheaders.put("content-length:", Integer.toString(body.length));


                    headers.put("x-content-sha256", calculateSHA256(body));
                    myheaders.put("x-content-sha256:", calculateSHA256(body));

                }
            }

            final String signature = this.calculateSignature(method, uri.getPath()+QUERY+uri.getQuery(), headers);
            myheaders.put(AUTHORIZATION,signature);
            return myheaders;
        }
        private String calculateSHA256(byte[] body) {
            byte[] hash = Hashing.sha256().hashBytes(body).asBytes();
            return Base64.getEncoder().encodeToString(hash);
        }

        private String calculateSignature(String method, String path, Map<String, String> headers) {
            Signer signer = this.signers.get(method);
            if (signer == null) {
                throw new RuntimeException("Don't know how to sign method " + method);
            }
            try {
                return signer.sign(method, path, headers).toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate signature", e);
            }
        }


    }
}
