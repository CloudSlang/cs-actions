package io.cloudslang.content.office365.services;

import com.azure.core.credential.TokenCredential;
import com.google.gson.JsonParser;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.httpcore.HttpClients;
import com.microsoft.graph.models.FileAttachment;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.tasks.LargeFileUploadResult;
import com.microsoft.graph.tasks.LargeFileUploadTask;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.AddAttachmentInputs;
import okhttp3.*;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.office365.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.PopulateAttachmentBody.populateCreateUploadSessionBody;

public class UploadBigFileImpl {

    public static String createUploadSession(@NotNull final AddAttachmentInputs addAttachmentInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();

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

        UploadSession uploadSession = new UploadSession();
        uploadSession.uploadUrl = getUploadUrl(new HttpClientService().execute(httpClientInputs));
        return uploadFileChunk(addAttachmentInputs, uploadSession);
    }

    @NotNull
    private static String addBigAttachmentUrl(@NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(createUploadSessionMessagePath(messageId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getUploadUrl(Map<String, String> createSessionResult) {
        if (Integer.parseInt(createSessionResult.get(STATUS_CODE)) >= 200 && Integer.parseInt(createSessionResult.get(STATUS_CODE)) < 300)
            return new JsonParser().parse(createSessionResult.get(RETURN_RESULT)).getAsJsonObject().get(UPLOAD_SESSION_URL).getAsString();
        else
            throw new RuntimeException(createSessionResult.get(EXCEPTION));
    }

    private static String uploadFileChunk(@NotNull final AddAttachmentInputs addAttachmentInputs, UploadSession uploadSession) throws Exception {

        TokenCredential tokenCredential = new AuthTokenCredential(addAttachmentInputs.getCommonInputs().getAuthToken());
        final TokenCredentialAuthProvider tokenCredAuthProvider = new TokenCredentialAuthProvider(Arrays.asList(DEFAULT_SCOPE), tokenCredential);
        final OkHttpClient.Builder httpClientBuilder = HttpClients.createDefault(tokenCredAuthProvider).newBuilder();

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
        if (!addAttachmentInputs.getCommonInputs().getProxyHost().isEmpty())
            httpClientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(addAttachmentInputs.getCommonInputs().getProxyHost(), Integer.parseInt(addAttachmentInputs.getCommonInputs().getProxyPort()))));

        if (proxyAuthenticator != null)
            httpClientBuilder.proxyAuthenticator(proxyAuthenticator);

        final GraphServiceClient<Request> graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredAuthProvider)
                .httpClient(httpClientBuilder.build())
                .buildClient();

        InputStream fileStream = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()));

        //Used for logging the upload of chunks
//        IProgressCallback callback = new IProgressCallback() {
//            @Override
//            // Called after each slice of the file is uploaded
//            public void progress(final long current, final long max) {
//                System.out.println(
//                        String.format("Uploaded %d bytes of %d total bytes", current, max)
//                );
//            }
//        };

        LargeFileUploadTask<FileAttachment> uploadTask =
                new LargeFileUploadTask<>
                        (uploadSession, graphClient, fileStream, Files.size(Paths.get(addAttachmentInputs.getFilePath())), FileAttachment.class);
// Do the upload
        LargeFileUploadResult<FileAttachment> result = uploadTask.upload(0, null, null);
        return result.location;
    }
}
