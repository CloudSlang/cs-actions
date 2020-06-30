package io.cloudslang.content.oracle.oci.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIDetachVolumeAttachmentInputs;
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
        PrivateKey privateKeyData = SignerImpl.loadPrivateKey(ociCommonInputs.getPrivateKeyData(), ociCommonInputs.getPrivateKeyFile());
        return new SignerImpl.RequestSigner(apiKey, privateKeyData);
    }

    @NotNull
    public static Map<String, String> detachVolume(@NotNull final OCIDetachVolumeAttachmentInputs ociDetachVolumeAttachmentInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(detachVolumeUrl(ociDetachVolumeAttachmentInputs.getCommonInputs().getRegion(), ociDetachVolumeAttachmentInputs.getVolumeAttachmentId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociDetachVolumeAttachmentInputs.getCommonInputs()).signRequest(uri, DELETE, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociDetachVolumeAttachmentInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static String detachVolumeUrl(@NotNull final String region, @NotNull final String volumeAttachmentId) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(detachVolumePath(volumeAttachmentId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String detachVolumePath(@NotNull final String volumeAttachmentId) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(VOLUME_ATTACHMENTS)
                .append(volumeAttachmentId);
        return pathString.toString();
    }
}
