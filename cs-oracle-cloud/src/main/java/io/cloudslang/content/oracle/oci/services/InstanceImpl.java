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
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.oci.services.models.instances.CreateInstanceRequestBody;
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
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.getQueryParams;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class InstanceImpl {


    public static String createInstanceRequestBody(@NotNull final OCIInstanceInputs createInstancesInputs) {
        String requestBody = EMPTY;
        ObjectMapper createInstanceMapper = new ObjectMapper();
        CreateInstanceRequestBody createInstanceRequestBody = new CreateInstanceRequestBody();
        CreateInstanceRequestBody.Metadata metadata = createInstanceRequestBody.new Metadata();
        CreateInstanceRequestBody.AgentConfig agentConfig = createInstanceRequestBody.new AgentConfig();

        CreateInstanceRequestBody.ShapeConfig shapeConfig = createInstanceRequestBody.new ShapeConfig();
        if (!createInstancesInputs.getOcpus().isEmpty())
            shapeConfig.setOcpus(Integer.parseInt(createInstancesInputs.getOcpus()));

        if (!createInstancesInputs.getNetworkType().isEmpty()) {
            CreateInstanceRequestBody.LaunchOptions launchOptions = createInstanceRequestBody.new LaunchOptions();
            launchOptions.setBootVolumeType(createInstancesInputs.getBootVolumeType());
            if (!createInstancesInputs.getIsConsistentVolumeNamingEnabled().isEmpty())
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
        if(!createInstancesInputs.getAssignPublicIp().isEmpty()){
            createVnicDetails.setAssignPublicIp(Boolean.parseBoolean(createInstancesInputs.getAssignPublicIp()));
        }

        createVnicDetails.setSkipSourceDestCheck(stringToBoolean(createInstancesInputs.getSkipSourceDestCheck()));
        createVnicDetails.setPrivateIp(createInstancesInputs.getPrivateIp());
        createVnicDetails.setDisplayName(createInstancesInputs.getDisplayName());
        createVnicDetails.setHostnameLabel(createInstancesInputs.getHostnameLabel());
        createVnicDetails.setSubnetId(createInstancesInputs.getSubnetId());
        if (!createInstancesInputs.getNetworkSecurityGroupIds().isEmpty())
            createVnicDetails.setNsgIds(createInstancesInputs.getNetworkSecurityGroupIds().split(","));
        sourceDetails.setSourceType(createInstancesInputs.getSourceType());
        sourceDetails.setImageId(createInstancesInputs.getImageId());
        if (!createInstancesInputs.getBootVolumeSizeInGBs().isEmpty()) {
            sourceDetails.setBootVolumeSizeInGBs(Integer.parseInt(createInstancesInputs.getBootVolumeSizeInGBs()));
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

    @NotNull
    public static Map<String, String> createInstance(@NotNull final OCIInstanceInputs createInstancesInputs)
            throws Exception {

        String apiKey = (createInstancesInputs.getCommonInputs().getTenancyOcid() + FORWARD_SLASH
                + createInstancesInputs.getCommonInputs().getUserOcid() + FORWARD_SLASH
                + createInstancesInputs.getCommonInputs().getFingerPrint());
        SignerImpl signerImpl = new SignerImpl();
        PrivateKey privateKey = signerImpl.loadPrivateKey(createInstancesInputs.getCommonInputs().getPrivateKey());
        SignerImpl.RequestSigner signer = new SignerImpl.RequestSigner(apiKey, privateKey);
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(createInstancesInputs.getCommonInputs().getRegion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setBody(createInstanceRequestBody(createInstancesInputs));
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = signer.signRequest(uri, POST, httpClientInputs.getBody());
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, createInstancesInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);

    }


    @NotNull
    public static Map<String, String> listInstances(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        String apiKey = (ociCommonInputs.getTenancyOcid() + FORWARD_SLASH
                + ociCommonInputs.getUserOcid() + FORWARD_SLASH
                + ociCommonInputs.getFingerPrint());
        SignerImpl signerImpl = new SignerImpl();
        PrivateKey privateKey = signerImpl.loadPrivateKey(ociCommonInputs.getPrivateKey());
        SignerImpl.RequestSigner signer = new SignerImpl.RequestSigner(apiKey, privateKey);
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(ociCommonInputs.getRegion()));
        httpClientInputs.setQueryParams(getQueryParams(ociCommonInputs.getCompartmentOcid()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl() + QUERY + httpClientInputs.getQueryParams());
        Map<String, String> headers = signer.signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> getInstanceDetails(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        String apiKey = (ociCommonInputs.getTenancyOcid() + FORWARD_SLASH
                + ociCommonInputs.getUserOcid() + FORWARD_SLASH
                + ociCommonInputs.getFingerPrint());
        SignerImpl signerImpl = new SignerImpl();
        PrivateKey privateKey = signerImpl.loadPrivateKey(ociCommonInputs.getPrivateKey());
        SignerImpl.RequestSigner signer = new SignerImpl.RequestSigner(apiKey, privateKey);
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDetailsUrl(ociCommonInputs.getRegion(), ociCommonInputs.getInstanceId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = signer.signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> getInstanceDefaultCredentials(@NotNull final OCICommonInputs ociCommonInputs)
            throws Exception {

        String apiKey = (ociCommonInputs.getTenancyOcid() + FORWARD_SLASH
                + ociCommonInputs.getUserOcid() + FORWARD_SLASH
                + ociCommonInputs.getFingerPrint());
        SignerImpl signerImpl = new SignerImpl();
        PrivateKey privateKey = signerImpl.loadPrivateKey(ociCommonInputs.getPrivateKey());
        SignerImpl.RequestSigner signer = new SignerImpl.RequestSigner(apiKey, privateKey);
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getInstanceDefaultCredentialsUrl(ociCommonInputs.getRegion(), ociCommonInputs.getInstanceId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = signer.signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        HttpCommons.setCommonHttpInputs(httpClientInputs, ociCommonInputs);
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static String listInstancesUrl(@NotNull final String region) throws Exception {
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
    public static String getInstanceDetailsUrl(@NotNull final String region, @NotNull final String instanceId) throws Exception {
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
    public static String getInstanceDefaultCredentialsUrl(@NotNull final String region, @NotNull final String instanceId) throws Exception {
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

    public static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!property.isEmpty())
            return (JSONObject) jsonParser.parse(property);
        return null;
    }

    public static boolean stringToBoolean(String property) {
        if (!property.isEmpty())
            return Boolean.parseBoolean(property);
        return false;
    }
}
