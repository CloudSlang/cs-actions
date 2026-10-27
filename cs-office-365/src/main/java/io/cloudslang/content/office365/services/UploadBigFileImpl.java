/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.office365.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.graph.core.models.UploadResult;
import com.microsoft.graph.core.requests.BaseGraphRequestAdapter;
import com.microsoft.graph.core.tasks.LargeFileUploadTask;
import com.microsoft.graph.models.FileAttachment;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.kiota.authentication.AnonymousAuthenticationProvider;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.AddAttachmentInputs;
import io.cloudslang.content.office365.entities.Office365HttpClientInputs;
import okhttp3.*;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.office365.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.PopulateAttachmentBody.populateCreateUploadSessionBody;

public class UploadBigFileImpl {

    public static String createUploadSession(@NotNull final AddAttachmentInputs addAttachmentInputs) throws Exception {

        final Office365HttpClientInputs httpClientInputs = new Office365HttpClientInputs();

        httpClientInputs.setUrl(addBigAttachmentUrl(addAttachmentInputs.getMessageId()));

        setCommonHttpInputs(httpClientInputs, addAttachmentInputs.getCommonInputs());

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setResponseCharacterSet(addAttachmentInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(addAttachmentInputs.getCommonInputs().getAuthToken()));
        httpClientInputs.setBody(populateCreateUploadSessionBody(
                addAttachmentInputs.getFilePath(),
                addAttachmentInputs.getContentName(),
                addAttachmentInputs.getContentBytes(),
                Files.size(Paths.get(addAttachmentInputs.getFilePath()))));

        UploadSession uploadSession = getUploadSession(HttpClientService.execute(httpClientInputs.build()));
        return uploadFileChunk(addAttachmentInputs, uploadSession);
    }

    @NotNull
    private static String addBigAttachmentUrl(@NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(createUploadSessionMessagePath(messageId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    static UploadSession getUploadSession(Map<String, String> createSessionResult) {
        int statusCode = Integer.parseInt(createSessionResult.get(STATUS_CODE));
        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException(createSessionResult.get(EXCEPTION));
        }

        JsonObject sessionResponse = JsonParser.parseString(createSessionResult.get(RETURN_RESULT)).getAsJsonObject();
        UploadSession uploadSession = new UploadSession();
        uploadSession.setUploadUrl(sessionResponse.get(UPLOAD_SESSION_URL).getAsString());

        JsonArray nextExpectedRanges = sessionResponse.getAsJsonArray("nextExpectedRanges");
        if (nextExpectedRanges == null || nextExpectedRanges.isEmpty()) {
            throw new IllegalArgumentException("The upload session response does not contain nextExpectedRanges");
        }
        List<String> ranges = new ArrayList<>(nextExpectedRanges.size());
        for (JsonElement range : nextExpectedRanges) {
            ranges.add(range.getAsString());
        }
        uploadSession.setNextExpectedRanges(ranges);

        JsonElement expirationDateTime = sessionResponse.get("expirationDateTime");
        if (expirationDateTime != null && !expirationDateTime.isJsonNull()) {
            uploadSession.setExpirationDateTime(OffsetDateTime.parse(expirationDateTime.getAsString()));
        }
        return uploadSession;
    }

    private static String uploadFileChunk(@NotNull final AddAttachmentInputs addAttachmentInputs, UploadSession uploadSession) throws Exception {

        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }

                }
        };

        Authenticator proxyAuthenticator = null;
        if (!addAttachmentInputs.getCommonInputs().getProxyUsername().isEmpty()) {
            proxyAuthenticator = new Authenticator() {
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credential = Credentials.basic(addAttachmentInputs.getCommonInputs().getProxyUsername(), addAttachmentInputs.getCommonInputs().getProxyPassword());
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
        }

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance(TLS);
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create a ssl socket factory with our all-trusting manager
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier(new NoopHostnameVerifier());

        if (!addAttachmentInputs.getCommonInputs().getProxyHost().isEmpty())
            httpClientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(addAttachmentInputs.getCommonInputs().getProxyHost(), Integer.parseInt(addAttachmentInputs.getCommonInputs().getProxyPort()))));

        if (proxyAuthenticator != null)
            httpClientBuilder.proxyAuthenticator(proxyAuthenticator);

        final BaseGraphRequestAdapter requestAdapter = new BaseGraphRequestAdapter(
                new AnonymousAuthenticationProvider(),
                uploadSession.getUploadUrl(),
                httpClientBuilder.build());

        try (InputStream fileStream = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()))) {
            LargeFileUploadTask<FileAttachment> uploadTask = new LargeFileUploadTask<>(
                    requestAdapter,
                    uploadSession,
                    fileStream,
                    Files.size(Paths.get(addAttachmentInputs.getFilePath())),
                    FileAttachment::createFromDiscriminatorValue);
            UploadResult<FileAttachment> result = uploadTask.upload();
            if (result.location == null) {
                throw new IllegalStateException("The large attachment upload completed without a location");
            }
            return result.location.toString();
        }
    }
}
