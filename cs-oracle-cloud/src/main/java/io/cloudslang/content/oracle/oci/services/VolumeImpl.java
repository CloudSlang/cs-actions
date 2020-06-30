package io.cloudslang.content.oracle.oci.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIAttachVolumeInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIDetachVolumeAttachmentInputs;
import io.cloudslang.content.oracle.oci.services.models.volumes.AttachVolumeRequestBody;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class VolumeImpl {

    @NotNull
    private static SignerImpl.RequestSigner getRequestSigner(OCICommonInputs ociCommonInputs) {
        String apiKey = (ociCommonInputs.getTenancyOcid() + FORWARD_SLASH
                + ociCommonInputs.getUserOcid() + FORWARD_SLASH
                + ociCommonInputs.getFingerPrint());
        PrivateKey privateKeyData = SignerImpl.loadPrivateKey(ociCommonInputs.getPrivateKeyData(),
                ociCommonInputs.getPrivateKeyFile());
        return new SignerImpl.RequestSigner(apiKey, privateKeyData);
    }

    @NotNull
    public static Map<String, String> attachVolume(@NotNull final OCIAttachVolumeInputs ociAttachVolumeInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(attachVolumeUrl(ociAttachVolumeInputs.getCommonInputs().getRegion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setBody(attachVolumeRequestBody(ociAttachVolumeInputs));
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociAttachVolumeInputs.getCommonInputs()).signRequest(uri, POST, httpClientInputs.getBody());
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociAttachVolumeInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> detachVolume(@NotNull final OCIDetachVolumeAttachmentInputs
                                                           ociDetachVolumeAttachmentInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(detachVolumeUrl(ociDetachVolumeAttachmentInputs.getCommonInputs().getRegion(),
                ociDetachVolumeAttachmentInputs.getVolumeAttachmentId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociDetachVolumeAttachmentInputs.getCommonInputs())
                .signRequest(uri, DELETE, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociDetachVolumeAttachmentInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static String attachVolumeUrl(@NotNull final String region) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(attachVolumePath());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String detachVolumeUrl(@NotNull final String region, @NotNull final String volumeAttachmentId)
            throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(detachVolumePath(volumeAttachmentId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String attachVolumePath() {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(VOLUME_ATTACHMENTS);
        return pathString.toString();
    }

    @NotNull
    public static String detachVolumePath(@NotNull final String volumeAttachmentId) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(VOLUME_ATTACHMENTS)
                .append(volumeAttachmentId);
        return pathString.toString();
    }

    private static String attachVolumeRequestBody(@NotNull final OCIAttachVolumeInputs ociAttachVolumeInputs) {
        String requestBody = EMPTY;
        ObjectMapper attachVolumeMapper = new ObjectMapper();
        AttachVolumeRequestBody attachVolumeRequestBody = new AttachVolumeRequestBody();

        attachVolumeRequestBody.setDevice(ociAttachVolumeInputs.getDeviceName());
        attachVolumeRequestBody.setDisplayName(ociAttachVolumeInputs.getDisplayName());
        attachVolumeRequestBody.setInstanceId(ociAttachVolumeInputs.getCommonInputs().getInstanceId());
        attachVolumeRequestBody.setVolumeId(ociAttachVolumeInputs.getCommonInputs().getVolumeId());
        attachVolumeRequestBody.setType(ociAttachVolumeInputs.getVolumeType());
        attachVolumeRequestBody.setIsReadOnly(Boolean.parseBoolean(ociAttachVolumeInputs.getIsReadOnly()));
        attachVolumeRequestBody.setIsShareable(Boolean.parseBoolean(ociAttachVolumeInputs.getIsShareable()));
        try {
            requestBody = attachVolumeMapper.writeValueAsString(attachVolumeRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
