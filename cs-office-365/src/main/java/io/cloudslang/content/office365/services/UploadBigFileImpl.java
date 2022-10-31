package io.cloudslang.content.office365.services;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.models.File;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.tasks.IProgressCallback;
import com.microsoft.graph.tasks.LargeFileUploadTask;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.AddAttachmentInputs;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
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
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.CLIENT_ID;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.CLIENT_SECRET;
import static io.cloudslang.content.office365.utils.PopulateAttachmentBody.populateCreateUploadSessionBody;
import static io.cloudslang.content.office365.utils.PopulateAttachmentBody.populateUploadChunkBody;

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

//        uploadLargeFile(getUploadUrl(new HttpClientService().execute(httpClientInputs)), addAttachmentInputs);

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        UploadSession uploadSession = new UploadSession();
        uploadSession.uploadUrl = getUploadUrl(new HttpClientService().execute(httpClientInputs));
//                = gson.fromJson(new HttpClientService().execute(httpClientInputs).get(RETURN_RESULT), UploadSession.class);
        uploadFileChunk(addAttachmentInputs, uploadSession);
    }

    @NotNull
    private static String addBigAttachmentUrl(@NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(createUploadSessionMessagePath(messageId));

        return uriBuilder.build().toURL().toString();
    }
//
    @NotNull
    private static String getUploadUrl(Map<String, String> createSessionResult) {
        if(Integer.parseInt(createSessionResult.get(STATUS_CODE)) >= 200 && Integer.parseInt(createSessionResult.get(STATUS_CODE)) < 300)
            return new JsonParser().parse(createSessionResult.get(RETURN_RESULT)).getAsJsonObject().get(UPLOAD_SESSION_URL).getAsString();
        else
            throw new RuntimeException(createSessionResult.get(EXCEPTION));
    }
//
//    @NotNull
//    public static void uploadLargeFile(@NotNull String uploadUrl, @NotNull final AddAttachmentInputs addAttachmentInputs) {
//        int byteSize = 0;
//        try (InputStream in = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()))) {
//            final byte[] buffer = new byte[1000000];
//            int dataRead = in.read(buffer);
//            while (dataRead > -1) {
//                StringBuilder contentRange = new StringBuilder();
//                contentRange.append(BYTES)
//                        .append(byteSize)
//                        .append(MINUS)
//                        .append(byteSize + dataRead - 1)
//                        .append(PATH_SEPARATOR)
//                        .append(Files.size(Paths.get(addAttachmentInputs.getFilePath())));
//                uploadFileChunk(uploadUrl, addAttachmentInputs, buffer, contentRange.toString(), String.valueOf(dataRead));
//                byteSize = byteSize + dataRead;
//                dataRead = in.read(buffer);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private static void uploadFileChunk(@NotNull String uploadUrl, @NotNull final AddAttachmentInputs addAttachmentInputs, byte[] fileChunk, String contentRange, String contentLength) throws IOException {

    private static void uploadFileChunk(@NotNull final AddAttachmentInputs addAttachmentInputs, UploadSession uploadSession) throws IOException {



        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId("c49005ae-7d31-4135-b32d-23081ebe6011")
                .clientSecret("OvyJ5u/s4BfpGw8rpvnqbJNxXOT/k2lGjD6GxID/cX0=")
                .tenantId("b4ee84e5-2507-4e8f-a5c1-fe663f3d2f80")
                .build();

        List<String> scope = new ArrayList<>();
        scope.add("https://graph.microsoft.com/.default");

        final TokenCredentialAuthProvider tokenCredAuthProvider =
                new TokenCredentialAuthProvider(scope, clientSecretCredential);

        final GraphServiceClient graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredAuthProvider)
                .buildClient();


//        final Message draftMessage = new Message();
//        draftMessage.subject = "Large attachment";
//
//        final Message savedDraft = graphClient
//                .me()
//                .messages()
//                .buildRequest()
//                .post(draftMessage);

// Get an input stream for the file
        InputStream fileStream = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()));

//        final AttachmentItem largeAttachment = new AttachmentItem();
//        largeAttachment.attachmentType = AttachmentType.FILE;
//        largeAttachment.name = "largefile.gif";
//        largeAttachment.size = Files.size(Paths.get(addAttachmentInputs.getFilePath()));

//        final AttachmentCreateUploadSessionParameterSet upParams =
//                AttachmentCreateUploadSessionParameterSet.newBuilder()
//                        .withAttachmentItem(largeAttachment)
//                        .build();

//        final UploadSession uploadSession = graphClient
//                .me()
//                .messages(addAttachmentInputs.getMessageId())
//                .attachments()
//                .createUploadSession(upParams)
//                .buildRequest()
//                .post();

// Create a callback used by the upload provider
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
