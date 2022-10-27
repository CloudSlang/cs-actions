package io.cloudslang.content.office365.services;

import com.google.gson.JsonParser;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.office365.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
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

        uploadLargeFile(getUploadUrl(new HttpClientService().execute(httpClientInputs)), addAttachmentInputs);
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

    @NotNull
    public static void uploadLargeFile(@NotNull String uploadUrl, @NotNull final AddAttachmentInputs addAttachmentInputs) {
        int byteSize = 0;
        try (InputStream in = Files.newInputStream(Paths.get(addAttachmentInputs.getFilePath()))) {
            final byte[] buffer = new byte[1000000];
            int dataRead = in.read(buffer);
            while (dataRead > -1) {
                StringBuilder contentRange = new StringBuilder();
                contentRange.append(BYTES)
                        .append(byteSize)
                        .append(MINUS)
                        .append(byteSize + dataRead - 1)
                        .append(PATH_SEPARATOR)
                        .append(Files.size(Paths.get(addAttachmentInputs.getFilePath())));
                uploadFileChunk(uploadUrl, addAttachmentInputs, buffer, contentRange.toString(), String.valueOf(dataRead));
                byteSize = byteSize + dataRead;
                dataRead = in.read(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void uploadFileChunk(@NotNull String uploadUrl, @NotNull final AddAttachmentInputs addAttachmentInputs, byte[] fileChunk, String contentRange, String contentLength) throws IOException {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setUrl(uploadUrl);
        httpClientInputs.setMethod(METHOD_PUT);

        setCommonHttpInputs(httpClientInputs, addAttachmentInputs.getCommonInputs());

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(OCTET_STREAM);
        httpClientInputs.setResponseCharacterSet(addAttachmentInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getBigFileHeaders(contentLength, contentRange));
        httpClientInputs.setBody(populateUploadChunkBody(fileChunk));

        new HttpClientService().execute(httpClientInputs);
    }


}
