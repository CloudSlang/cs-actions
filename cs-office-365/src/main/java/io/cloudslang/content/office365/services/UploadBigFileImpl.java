package io.cloudslang.content.office365.services;

import com.azure.core.credential.TokenCredential;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.tasks.IProgressCallback;
import com.microsoft.graph.tasks.LargeFileUploadTask;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.AddAttachmentInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void createUploadSession(@NotNull final AddAttachmentInputs addAttachmentInputs) throws Exception {

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
        uploadFileChunk(addAttachmentInputs, uploadSession);
    }

    @NotNull
    private static String addBigAttachmentUrl(@NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(createUploadSessionMessagePath(messageId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getUploadUrl(Map<String, String> createSessionResult) {
        if(Integer.parseInt(createSessionResult.get(STATUS_CODE)) >= 200 && Integer.parseInt(createSessionResult.get(STATUS_CODE)) < 300)
            return new JsonParser().parse(createSessionResult.get(RETURN_RESULT)).getAsJsonObject().get(UPLOAD_SESSION_URL).getAsString();
        else
            throw new RuntimeException(createSessionResult.get(EXCEPTION));
    }
    private static void uploadFileChunk(@NotNull final AddAttachmentInputs addAttachmentInputs, UploadSession uploadSession) throws IOException {

        List<String> scope = new ArrayList<>();
        scope.add("https://graph.microsoft.com/.default");

        TokenCredential tokenCredential = new AuthTokenCredential(addAttachmentInputs.getCommonInputs().getAuthToken());
        final TokenCredentialAuthProvider tokenCredAuthProvider = new TokenCredentialAuthProvider(scope, tokenCredential);

        final GraphServiceClient graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredAuthProvider)
                .buildClient();

        InputStream fileStream = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()));

        IProgressCallback callback = new IProgressCallback() {
            @Override
            // Called after each slice of the file is uploaded
            public void progress(final long current, final long max) {
                System.out.println(
                        String.format("Uploaded %d bytes of %d total bytes", current, max)
                );
            }
        };

        LargeFileUploadTask<FileAttachment> uploadTask =
                new LargeFileUploadTask<FileAttachment>
                        (uploadSession, graphClient, fileStream, Files.size(Paths.get(addAttachmentInputs.getFilePath())), FileAttachment.class);

// Do the upload
        uploadTask.upload(0, null, callback);






//        final HttpClientInputs httpClientInputs = new HttpClientInputs();
//        OKHttpService example = new OKHttpService();
//
//        httpClientInputs.setUrl(uploadUrl);
//        httpClientInputs.setMethod(METHOD_PUT);
//
//        setCommonHttpInputs(httpClientInputs, addAttachmentInputs.getCommonInputs());
//
//        httpClientInputs.setAuthType(ANONYMOUS);
//        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
//        httpClientInputs.setKeystorePassword(CHANGEIT);
//        httpClientInputs.setContentType(OCTET_STREAM);
//        httpClientInputs.setResponseCharacterSet(addAttachmentInputs.getCommonInputs().getResponseCharacterSet());
//        httpClientInputs.setHeaders(getBigFileHeaders(contentLength, contentRange));
//        httpClientInputs.setBody(populateUploadChunkBody(fileChunk, contentLength));
//
//        new HttpClientService().execute(httpClientInputs);

//        String response = example.post(uploadUrl, populateUploadChunkBody(fileChunk), contentLength, contentRange);
    }


}
