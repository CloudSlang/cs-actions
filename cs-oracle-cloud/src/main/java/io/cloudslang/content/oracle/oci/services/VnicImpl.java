package io.cloudslang.content.oracle.oci.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIAttachVnicInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIVnicAttachmentInputs;
import io.cloudslang.content.oracle.oci.services.models.instances.AttachVnicRequestBody;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.getQueryParams;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class VnicImpl {

    private static String attachVnicRequestBody(@NotNull final OCIAttachVnicInputs ociAttachVnicInputs) {
        String requestBody = EMPTY;
        ObjectMapper attachVnicMapper = new ObjectMapper();
        AttachVnicRequestBody attachVnicRequestBody = new AttachVnicRequestBody();
        AttachVnicRequestBody.CreateVnicDetails createVnicDetails = attachVnicRequestBody.new CreateVnicDetails();
        if (!isEmpty(ociAttachVnicInputs.getAssignPublicIp())) {
            createVnicDetails.setAssignPublicIp(Boolean.parseBoolean(ociAttachVnicInputs.getAssignPublicIp()));
        }
        if (!isEmpty(ociAttachVnicInputs.getSkipSourceDestCheck())) {
            createVnicDetails.setSkipSourceDestCheck(stringToBoolean(ociAttachVnicInputs.getSkipSourceDestCheck()));
        }
        createVnicDetails.setPrivateIp(ociAttachVnicInputs.getPrivateIp());
        createVnicDetails.setDisplayName(ociAttachVnicInputs.getVnicDisplayName());
        createVnicDetails.setHostnameLabel(ociAttachVnicInputs.getHostnameLabel());
        createVnicDetails.setSubnetId(ociAttachVnicInputs.getSubnetId());

        if (!isEmpty(ociAttachVnicInputs.getNsgIds()))
            createVnicDetails.setNsgIds(ociAttachVnicInputs.getNsgIds().split(","));
        attachVnicRequestBody.setDisplayName(ociAttachVnicInputs.getVnicAttachmentDisplayName());
        attachVnicRequestBody.setInstanceId(ociAttachVnicInputs.getCommonInputs().getInstanceId());
        if (!isEmpty(ociAttachVnicInputs.getNicIndex())) {
            attachVnicRequestBody.setNicIndex(ociAttachVnicInputs.getNicIndex());
        }
        JSONParser jsonParser = new JSONParser();

        try {
            createVnicDetails.setDefinedTags(stringToJSON(jsonParser, ociAttachVnicInputs.getDefinedTags()));
            createVnicDetails.setFreeformTags(stringToJSON(jsonParser, ociAttachVnicInputs.getFreeformTags()));
            attachVnicRequestBody.setCreateVnicDetails(createVnicDetails);
            requestBody = attachVnicMapper.writeValueAsString(attachVnicRequestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

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
    public static Map<String, String> getVnicAttachmentDetails(@NotNull final OCIVnicAttachmentInputs ociVnicAttachmentInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(vnicUrl(ociVnicAttachmentInputs.getCommonInputs().getRegion(), ociVnicAttachmentInputs.getVnicAttachmentId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociVnicAttachmentInputs.getCommonInputs()).signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociVnicAttachmentInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> attachVnic(@NotNull final OCIAttachVnicInputs ociAttachVnicInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listVnicAttachmentsUrl(ociAttachVnicInputs.getCommonInputs().getRegion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setBody(attachVnicRequestBody(ociAttachVnicInputs));
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociAttachVnicInputs.getCommonInputs()).signRequest(uri, POST, httpClientInputs.getBody());
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociAttachVnicInputs.getCommonInputs());
        System.out.println(httpClientInputs.getBody());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> detachVnic(@NotNull final OCIVnicAttachmentInputs ociDetachVnicAttachmentInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(vnicUrl(ociDetachVnicAttachmentInputs.getCommonInputs().getRegion(), ociDetachVnicAttachmentInputs.getVnicAttachmentId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociDetachVnicAttachmentInputs.getCommonInputs()).signRequest(uri, DELETE, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociDetachVnicAttachmentInputs.getCommonInputs());
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
                .append(VNIC_ATTACHMENTS);
        return pathString.toString();
    }

    @NotNull
    public static String vnicUrl(@NotNull final String region, @NotNull final String vnicOCID) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(vnicPath(vnicOCID));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String vnicPath(@NotNull final String vnicOCID) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(VNIC_ATTACHMENTS)
                .append(vnicOCID);
        return pathString.toString();
    }

    private static boolean stringToBoolean(String property) {
        if (!isEmpty(property))
            return Boolean.parseBoolean(property);
        return false;
    }

    private static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!isEmpty(property))
            return (JSONObject) jsonParser.parse(property);
        return new JSONObject();
    }
}
