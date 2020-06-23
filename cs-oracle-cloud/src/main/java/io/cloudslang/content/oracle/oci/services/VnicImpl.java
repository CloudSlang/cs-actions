package io.cloudslang.content.oracle.oci.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.getQueryParams;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class VnicImpl {

    @NotNull
    private static SignerImpl.RequestSigner getRequestSigner(OCICommonInputs ociCommonInputs) {
        String apiKey = (ociCommonInputs.getTenancyOcid() + FORWARD_SLASH
                + ociCommonInputs.getUserOcid() + FORWARD_SLASH
                + ociCommonInputs.getFingerPrint());
        PrivateKey privateKeyData = SignerImpl.loadPrivateKey(ociCommonInputs.getPrivateKeyData(), ociCommonInputs.getPrivateKeyFile());
        return new SignerImpl.RequestSigner(apiKey, privateKeyData);
    }

    @NotNull
    public static Map<String, String> listVnicAttachments(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {


        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listVnicAttachmentsUrl(ociCommonInputs.getRegion()));
        httpClientInputs.setQueryParams(getQueryParams(ociCommonInputs.getAvailabilityDomain(), ociCommonInputs.getCompartmentOcid(), ociCommonInputs.getInstanceId(), ociCommonInputs.getPage(), ociCommonInputs.getLimit(), ociCommonInputs.getVnicId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl() + QUERY + httpClientInputs.getQueryParams());
        Map<String, String> headers = getRequestSigner(ociCommonInputs).signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> getVnicDetails(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getVnicDetailsUrl(ociCommonInputs.getRegion(), ociCommonInputs.getVnicId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociCommonInputs).signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static String getVnicDetailsUrl(@NotNull final String region, @NotNull final String vnicId) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(getVnicDetailsPath(vnicId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVnicDetailsPath(@NotNull final String vnicId) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(GET_VNIC)
                .append(vnicId);
        return pathString.toString();
    }

    @NotNull
    public static String listVnicAttachmentsUrl(@NotNull final String region) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(listVnicAttachmentsPath());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String listVnicAttachmentsPath() {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_VNIC_ATTACHMENTS);
        return pathString.toString();
    }
}
