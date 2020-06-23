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


import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.*;
import io.cloudslang.content.oracle.oci.services.models.instances.CreateInstanceRequestBody;
import io.cloudslang.content.oracle.oci.services.models.instances.UpdateInstanceRequestBody;
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
import static io.cloudslang.content.oracle.oci.utils.Constants.GetInstanceDefaultCredentialsConstants.GET_INSTANCE_DEFAULT_CREDENTIALS_OPERATION_PATH;
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InstanceImpl {


    private static String createInstanceRequestBody(@NotNull final OCICreateInstanceInputs createInstancesInputs) {
        String requestBody = EMPTY;
        ObjectMapper createInstanceMapper = new ObjectMapper();
        CreateInstanceRequestBody createInstanceRequestBody = new CreateInstanceRequestBody();
        CreateInstanceRequestBody.Metadata metadata = createInstanceRequestBody.new Metadata();
        CreateInstanceRequestBody.AgentConfig agentConfig = createInstanceRequestBody.new AgentConfig();

        CreateInstanceRequestBody.ShapeConfig shapeConfig = createInstanceRequestBody.new ShapeConfig();
        if (!isEmpty(createInstancesInputs.getOcpus()))
            shapeConfig.setOcpus(Integer.parseInt(createInstancesInputs.getOcpus()));

        if (!isEmpty(createInstancesInputs.getNetworkType())) {
            CreateInstanceRequestBody.LaunchOptions launchOptions = createInstanceRequestBody.new LaunchOptions();
            launchOptions.setBootVolumeType(createInstancesInputs.getBootVolumeType());
            if (!isEmpty(createInstancesInputs.getIsConsistentVolumeNamingEnabled()))
                launchOptions.setIsConsistentVolumeNamingEnabled(stringToBoolean(createInstancesInputs.getIsConsistentVolumeNamingEnabled()));
            launchOptions.setFirmware(createInstancesInputs.getFirmware());
            launchOptions.setNetworkType(createInstancesInputs.getNetworkType());
            launchOptions.setRemoteDataVolumeType(createInstancesInputs.getRemoteDataVolumeType());
            createInstanceRequestBody.setLaunchOptions(launchOptions);
        }
        metadata.setSshAuthorizedKeys(createInstancesInputs.getSSHAuthorizedKeys());
        metadata.setUserData(createInstancesInputs.getUserdata());
        createInstanceRequestBody.setMetadata(metadata);
        JSONParser jsonParser = new JSONParser();

        createInstanceRequestBody.setAvailabilityDomain(createInstancesInputs.getCommonInputs().getAvailabilityDomain());
        createInstanceRequestBody.setCompartmentId(createInstancesInputs.getCommonInputs().getCompartmentOcid());
        createInstanceRequestBody.setShape(createInstancesInputs.getShape());
        createInstanceRequestBody.setDedicatedVmHostId(createInstancesInputs.getDedicatedVmHostId());
        CreateInstanceRequestBody.SourceDetails sourceDetails = createInstanceRequestBody.new SourceDetails();
        createInstanceRequestBody.setDisplayName(createInstancesInputs.getDisplayName());
        createInstanceRequestBody.setFaultDomain(createInstancesInputs.getFaultDomain());
        createInstanceRequestBody.setIpxeScript(createInstancesInputs.getIpxeScript());
        createInstanceRequestBody.setIsPvEncryptionInTransitEnabled(stringToBoolean(createInstancesInputs.getIsPvEncryptionInTransitEnabled()));
        createInstanceRequestBody.setLaunchMode(createInstancesInputs.getLaunchMode());
        agentConfig.setIsManagementDisabled(stringToBoolean(createInstancesInputs.getIsManagementDisabled()));
        agentConfig.setIsMonitoringDisabled(stringToBoolean(createInstancesInputs.getIsMonitoringDisabled()));
        CreateInstanceRequestBody.CreateVnicDetails createVnicDetails = createInstanceRequestBody.new CreateVnicDetails();
        if (!isEmpty(createInstancesInputs.getAssignPublicIp())) {
            createVnicDetails.setAssignPublicIp(Boolean.parseBoolean(createInstancesInputs.getAssignPublicIp()));
        }

        createVnicDetails.setSkipSourceDestCheck(stringToBoolean(createInstancesInputs.getSkipSourceDestCheck()));
        createVnicDetails.setPrivateIp(createInstancesInputs.getPrivateIp());
        createVnicDetails.setDisplayName(createInstancesInputs.getVnicDisplayName());
        createVnicDetails.setHostnameLabel(createInstancesInputs.getHostnameLabel());
        createVnicDetails.setSubnetId(createInstancesInputs.getSubnetId());
        if (isEmpty(createInstancesInputs.getSourceType())) {
            if (!isEmpty(createInstancesInputs.getImageId())) {
                sourceDetails.setSourceType(IMAGE);
            } else {
                sourceDetails.setSourceType(BOOT_VOLUME);
            }
        } else {
            sourceDetails.setSourceType(createInstancesInputs.getSourceType());
        }

        if (!isEmpty(createInstancesInputs.getNetworkSecurityGroupIds()))
            createVnicDetails.setNsgIds(createInstancesInputs.getNetworkSecurityGroupIds().split(","));

        if (!isEmpty(createInstancesInputs.getBootVolumeId())) {
            sourceDetails.setBootVolumeId(createInstancesInputs.getBootVolumeId());
        } else {
            sourceDetails.setImageId(createInstancesInputs.getImageId());
            sourceDetails.setKmsKeyId(createInstancesInputs.getKmsKeyId());
            if (!isEmpty(createInstancesInputs.getBootVolumeSizeInGBs())) {
                sourceDetails.setBootVolumeSizeInGBs(Integer.parseInt(createInstancesInputs.getBootVolumeSizeInGBs()));
            }
        }

        createInstanceRequestBody.setSourceDetails(sourceDetails);
        createInstanceRequestBody.setAgentConfig(agentConfig);
        try {
            createInstanceRequestBody.setDefinedTags(stringToJSON(jsonParser, createInstancesInputs.getDefinedTags()));
            createInstanceRequestBody.setFreeformTags(stringToJSON(jsonParser, createInstancesInputs.getFreeformTags()));
            createVnicDetails.setDefinedTags(stringToJSON(jsonParser, createInstancesInputs.getVnicDefinedTags()));
            createVnicDetails.setFreeformTags(stringToJSON(jsonParser, createInstancesInputs.getVnicFreeformTags()));
            createInstanceRequestBody.setCreateVnicDetails(createVnicDetails);

            createInstanceRequestBody.setExtendedMetadata(stringToJSON(jsonParser, createInstancesInputs.getExtendedMetadata()));
            requestBody = createInstanceMapper.writeValueAsString(createInstanceRequestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    private static String updateInstanceRequestBody(@NotNull final OCIUpdateInstanceInputs ociUpdateInstanceInputs) {
        String requestBody = EMPTY;
        ObjectMapper updateInstanceMapper = new ObjectMapper();
        UpdateInstanceRequestBody updateInstanceRequestBody = new UpdateInstanceRequestBody();
        UpdateInstanceRequestBody.AgentConfig agentConfig = updateInstanceRequestBody.new AgentConfig();

        UpdateInstanceRequestBody.ShapeConfig shapeConfig = updateInstanceRequestBody.new ShapeConfig();
        if (!isEmpty(ociUpdateInstanceInputs.getOcpus())) {
            shapeConfig.setOcpus(Integer.parseInt(ociUpdateInstanceInputs.getOcpus()));
            updateInstanceRequestBody.setShapeConfig(shapeConfig);
        }

        JSONParser jsonParser = new JSONParser();
        updateInstanceRequestBody.setShape(ociUpdateInstanceInputs.getShape());
        updateInstanceRequestBody.setDisplayName(ociUpdateInstanceInputs.getDisplayName());

        if (!isEmpty(ociUpdateInstanceInputs.getIsManagementDisabled()))
            agentConfig.setIsManagementDisabled(stringToBoolean(ociUpdateInstanceInputs.getIsManagementDisabled()));
        if (!isEmpty(ociUpdateInstanceInputs.getIsMonitoringDisabled()))
            agentConfig.setIsMonitoringDisabled(stringToBoolean(ociUpdateInstanceInputs.getIsMonitoringDisabled()));
        if (!isEmpty(ociUpdateInstanceInputs.getIsManagementDisabled()) || !isEmpty(ociUpdateInstanceInputs.getIsMonitoringDisabled()))
            updateInstanceRequestBody.setAgentConfig(agentConfig);


        try {
            updateInstanceRequestBody.setDefinedTags(stringToJSON(jsonParser, ociUpdateInstanceInputs.getDefinedTags()));
            updateInstanceRequestBody.setFreeformTags(stringToJSON(jsonParser, ociUpdateInstanceInputs.getFreeformTags()));
            requestBody = updateInstanceMapper.writeValueAsString(updateInstanceRequestBody);
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
    public static Map<String, String> createInstance(@NotNull final OCICreateInstanceInputs createInstancesInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(createInstancesInputs.getCommonInputs().getRegion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setBody(createInstanceRequestBody(createInstancesInputs));
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(createInstancesInputs.getCommonInputs()).signRequest(uri, POST, httpClientInputs.getBody());
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, createInstancesInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> updateInstance(@NotNull final OCIUpdateInstanceInputs ociUpdateInstanceInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDetailsUrl(ociUpdateInstanceInputs.getCommonInputs().getRegion(), ociUpdateInstanceInputs.getCommonInputs().getInstanceId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setBody(updateInstanceRequestBody(ociUpdateInstanceInputs));
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociUpdateInstanceInputs.getCommonInputs()).signRequest(uri, PUT, httpClientInputs.getBody());
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociUpdateInstanceInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }


    @NotNull
    public static Map<String, String> listInstances(@NotNull final OCIListInstanceInputs ociListInstanceInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(ociListInstanceInputs.getCommonInputs().getRegion()));
        httpClientInputs.setQueryParams(getQueryParams(ociListInstanceInputs.getCommonInputs().getAvailabilityDomain(), ociListInstanceInputs.getCommonInputs().getCompartmentOcid(), ociListInstanceInputs.getDisplayName(), ociListInstanceInputs.getCommonInputs().getLimit(), ociListInstanceInputs.getCommonInputs().getPage(), ociListInstanceInputs.getSortBy(), ociListInstanceInputs.getSortOrder(), ociListInstanceInputs.getLifecycleState()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl() + QUERY + httpClientInputs.getQueryParams());
        Map<String, String> headers = getRequestSigner(ociListInstanceInputs.getCommonInputs()).signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociListInstanceInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> terminateInstance(@NotNull final OCITerminateInstanceInputs ociTerminateInstanceInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDetailsUrl(ociTerminateInstanceInputs.getCommonInputs().getRegion(), ociTerminateInstanceInputs.getCommonInputs().getInstanceId()));
        httpClientInputs.setQueryParams(getQueryParamForTerminateInstance(ociTerminateInstanceInputs.getPreserveBootVolume()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        URI uri = URI.create(httpClientInputs.getUrl() + QUERY + httpClientInputs.getQueryParams());
        Map<String, String> headers = getRequestSigner(ociTerminateInstanceInputs.getCommonInputs()).signRequest(uri, DELETE, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociTerminateInstanceInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> instanceAction(@NotNull final OCIInstanceActionInputs ociInstanceActionInputs) throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDetailsUrl(ociInstanceActionInputs.getCommonInputs().getRegion(), ociInstanceActionInputs.getCommonInputs().getInstanceId()));
        httpClientInputs.setQueryParams(getQueryParamForInstanceAction(ociInstanceActionInputs.getActionName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        URI uri = URI.create(httpClientInputs.getUrl() + QUERY + httpClientInputs.getQueryParams());
        Map<String, String> headers = getRequestSigner(ociInstanceActionInputs.getCommonInputs()).signRequest(uri, POST, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociInstanceActionInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> getInstanceDetails(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDetailsUrl(ociCommonInputs.getRegion(), ociCommonInputs.getInstanceId()));
        return getStringStringMap(ociCommonInputs, httpClientInputs);

    }

    @NotNull
    private static Map<String, String> getStringStringMap(@NotNull OCICommonInputs ociCommonInputs, HttpClientInputs httpClientInputs) {
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = getRequestSigner(ociCommonInputs).signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getInstanceDefaultCredentials(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDefaultCredentialsUrl(ociCommonInputs.getRegion(), ociCommonInputs.getInstanceId()));
        return getStringStringMap(ociCommonInputs, httpClientInputs);

    }

    @NotNull
    private static String listInstancesUrl(@NotNull final String region) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(listInstancesPath());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String listInstancesPath() {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_INSTANCES);
        return pathString.toString();
    }

    @NotNull
    private static String getInstanceDetailsUrl(@NotNull final String region, @NotNull final String instanceId) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(getInstanceDetailsPath(instanceId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getInstanceDetailsPath(@NotNull final String instanceId) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_INSTANCES)
                .append(instanceId);
        return pathString.toString();
    }


    @NotNull
    private static String getInstanceDefaultCredentialsUrl(@NotNull final String region, @NotNull final String instanceId) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
        uriBuilder.setPath(getInstanceDefaultCredentialsPath(instanceId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getInstanceDefaultCredentialsPath(@NotNull final String instanceId) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_INSTANCES)
                .append(instanceId)
                .append(GET_INSTANCE_DEFAULT_CREDENTIALS_OPERATION_PATH);
        return pathString.toString();
    }

    private static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!isEmpty(property))
            return (JSONObject) jsonParser.parse(property);
        return new JSONObject();
    }

    private static boolean stringToBoolean(String property) {
        if (!isEmpty(property))
            return Boolean.parseBoolean(property);
        return false;
    }
}
