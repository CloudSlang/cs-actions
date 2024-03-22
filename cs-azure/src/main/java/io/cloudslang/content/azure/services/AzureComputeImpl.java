/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.azure.services;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.azure.entities.AzureComputeCommonInputs;
import io.cloudslang.content.azure.entities.AzureCreateVMInputs;
import io.cloudslang.content.azure.entities.models.compute.CreateVMRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.cloudslang.content.azure.utils.Constants.Common.ANONYMOUS;
import static io.cloudslang.content.azure.utils.Constants.Common.CONTENT_TYPE;
import static io.cloudslang.content.azure.utils.Constants.Common.DEFAULT_COMPUTE_SSH_API_VERSION;
import static io.cloudslang.content.azure.utils.Constants.Common.DELETE;
import static io.cloudslang.content.azure.utils.Constants.Common.DOT;
import static io.cloudslang.content.azure.utils.Constants.Common.GET;
import static io.cloudslang.content.azure.utils.Constants.Common.HTTP;
import static io.cloudslang.content.azure.utils.Constants.Common.PUT;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_AVAILABILITY_SET_SKU_JSON_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_AVAILABILITY_SET_TYPE;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_AVAILABILITY_SET_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_DATA_DISK_VHD_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_NETWORK_INTERFACES_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_OS_DISK_CACHING_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_OS_DISK_CREATION_OPTION_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_OS_DISK_VHD_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_SSH_PUBLIC_KEYS_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_TYPE_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_USING_CUSTOM_IMAGE_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_CREATE_VM_VHD_URI_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_DATA_DISK_CREATE_OPTION;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_DISK_TYPE_MANAGED;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_PROTOCOL_PREFIX;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.AZURE_SSH_PUBLIC_KEYS_JSON_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.DEFAULT_DATA_DISK_NAME;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.DEFAULT_OS_DISK_NAME;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.DEFAULT_SSH_PUBLIC_KEY_HOME_PATH;
import static io.cloudslang.content.azure.utils.Constants.CreateVMConstants.DEFAULT_SSH_PUBLIC_KEY_PATH;
import static io.cloudslang.content.azure.utils.Constants.RESOURCE_GROUPS_PATH;
import static io.cloudslang.content.azure.utils.Constants.SUBSCRIPTION_PATH;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AzureComputeImpl {

    @NotNull
    public static Map<String, String> createVM(@NotNull final AzureCreateVMInputs azureCreateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateVMUrl(azureCreateVMInputs));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(createVMRequestBody(azureCreateVMInputs));
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    public static Map<String, String> deleteVM(@NotNull final AzureComputeCommonInputs azureComputeCommonInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getDeleteVMUrl(azureComputeCommonInputs));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setHeaders(getAuthHeaders(azureComputeCommonInputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, azureComputeCommonInputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(azureComputeCommonInputs.getAzureCommonInputs().getApiVersion()));
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    private static String getCreateVMUrl(AzureCreateVMInputs azureCreateVMInputs) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(createVMPath(azureCreateVMInputs));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
        //uriBuilder.setPath(createVMPath(azureCreateVMInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getDeleteVMUrl(AzureComputeCommonInputs azureComputeCommonInputs) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(deleteVMPath(azureComputeCommonInputs));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
       // uriBuilder.setPath(deleteVMPath(azureComputeCommonInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String deleteVMPath(AzureComputeCommonInputs azureComputeCommonInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(azureComputeCommonInputs.getAzureProtocol())
                .append(AZURE_PROTOCOL_PREFIX)
                .append(azureComputeCommonInputs.getAzureHost())
                .append(SUBSCRIPTION_PATH)
                .append(azureComputeCommonInputs.getAzureCommonInputs().getSubscriptionId())
                .append(RESOURCE_GROUPS_PATH)
                .append(azureComputeCommonInputs.getAzureCommonInputs().getResourceGroupName())
                .append(AZURE_CREATE_VM_PATH)
                .append(azureComputeCommonInputs.getVmName());
        return pathString.toString();

    }

    @NotNull
    private static String createVMPath(AzureCreateVMInputs azureCreateVMInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureProtocol())
                .append(AZURE_PROTOCOL_PREFIX)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureHost())
                .append(SUBSCRIPTION_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId())
                .append(RESOURCE_GROUPS_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getResourceGroupName())
                .append(AZURE_CREATE_VM_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getVmName());
        return pathString.toString();

    }


    @NotNull
    private static String getAvailabilityInfoUrl(AzureCreateVMInputs azureCreateVMInputs) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(getAvailabilityInfoURLPath(azureCreateVMInputs));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
        //uriBuilder.setPath(getAvailabilityInfoURLPath(azureCreateVMInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getAvailabilityInfoURLPath(AzureCreateVMInputs azureCreateVMInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureProtocol())
                .append(AZURE_PROTOCOL_PREFIX)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureHost())
                .append(SUBSCRIPTION_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId())
                .append(RESOURCE_GROUPS_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getResourceGroupName())
                .append(AZURE_CREATE_VM_AVAILABILITY_SET_PATH)
                .append(azureCreateVMInputs.getAvailabilitySetName());
        return pathString.toString();

    }

    @NotNull
    public static String getAvailabilityInfo(@NotNull final AzureCreateVMInputs azureCreateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getAvailabilityInfoUrl(azureCreateVMInputs));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getApiVersion()));
        Map<String, String> result = new HttpClientService().execute(httpClientInputs);
        final String returnMessage = result.get(RETURN_RESULT);
        return (String) JsonPath.read(returnMessage, AZURE_AVAILABILITY_SET_SKU_JSON_PATH);
    }

    @NotNull
    public static String getSSHPublicKeysINSubscriptionInfo(@NotNull final AzureCreateVMInputs azureCreateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getSSHPublicKeysINSubscriptionInfoURL(azureCreateVMInputs));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(DEFAULT_COMPUTE_SSH_API_VERSION));
        Map<String, String> result = new HttpClientService().execute(httpClientInputs);

        final String returnMessage = result.get(RETURN_RESULT);
        JSONArray jsonArray = JsonPath.read(returnMessage, AZURE_SSH_PUBLIC_KEYS_JSON_PATH);

        Function<JSONArray, String> function = (j) -> {
            String publicKey = "";
            for (Object object : j) {


                LinkedHashMap linkedHashMap = (LinkedHashMap) object;
                if (((String) linkedHashMap.get("name")).equalsIgnoreCase(azureCreateVMInputs.getSshPublicKeyName())) {
                    LinkedHashMap linkedHashMap1 = (LinkedHashMap) linkedHashMap.get("properties");
                    publicKey = (String) linkedHashMap1.get("publicKey");

                    break;
                }


            }
            return publicKey;
        };
        return function.apply(jsonArray);
    }

    @NotNull
    private static String getSSHPublicKeysINSubscriptionInfoURL(AzureCreateVMInputs azureCreateVMInputs) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(getSSHPublicKeysINSubscriptionInfoURLPath(azureCreateVMInputs));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
        //uriBuilder.setPath(getSSHPublicKeysINSubscriptionInfoURLPath(azureCreateVMInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getSSHPublicKeysINSubscriptionInfoURLPath(AzureCreateVMInputs azureCreateVMInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureProtocol())
                .append(AZURE_PROTOCOL_PREFIX)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureHost())
                .append(SUBSCRIPTION_PATH)
                .append(azureCreateVMInputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId())
                .append(AZURE_CREATE_VM_SSH_PUBLIC_KEYS_PATH);
        return pathString.toString();

    }

    private static String createVMRequestBody(@NotNull final AzureCreateVMInputs inputs) throws Exception {
        CreateVMRequestBody createVMRequestBody = new CreateVMRequestBody();
        createVMRequestBody.setLocation(inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getLocation());
        createVMRequestBody.setId(SUBSCRIPTION_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId() +
                RESOURCE_GROUPS_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getResourceGroupName() + AZURE_CREATE_VM_PATH +
                inputs.getAzureComputeCommonInputs().getVmName());
        createVMRequestBody.setName(inputs.getAzureComputeCommonInputs().getVmName());
        createVMRequestBody.setType(AZURE_CREATE_VM_TYPE_PATH);
        if (inputs.getPlan().equalsIgnoreCase("true")) {
            CreateVMRequestBody.Plan plan = new CreateVMRequestBody.Plan();
            plan.setPublisher(inputs.getPublisher());
            plan.setProduct(inputs.getOffer());
            plan.setName(inputs.getSku());
            createVMRequestBody.setPlan(plan);
        }

        if (!StringUtils.isEmpty(inputs.getTagKeyList())) {
            BiFunction<String, String, Map<String, String>> biFunction = (keys, values) -> {
                String[] keyList = keys.split(",");
                String[] valueList = values.split(",");
                Map<String, String> tags = new HashMap<>();

                for (int i = 0; i < keyList.length; i++) {
                    if (keyList.length == valueList.length || i <= valueList.length - 1) {
                        tags.put(keyList[i], valueList[i]);
                    } else {
                        tags.put(keyList[i], "");
                    }
                }

                return tags;
            };

            createVMRequestBody.setTags(biFunction.apply(inputs.getTagKeyList(), inputs.getTagValueList()));
        }


        CreateVMRequestBody.Properties properties = new CreateVMRequestBody.Properties();
        CreateVMRequestBody.Properties.hardwareProfile hardwareProfile = new CreateVMRequestBody.Properties.hardwareProfile();
        hardwareProfile.setVmSize(inputs.getVmSize());
        CreateVMRequestBody.Properties.storageProfile storageProfile = new CreateVMRequestBody.Properties.storageProfile();
        CreateVMRequestBody.Properties.storageProfile.imageReference imageReference = new
                CreateVMRequestBody.Properties.storageProfile.imageReference();

        CreateVMRequestBody.Properties.storageProfile.osDisk osDisk = new
                CreateVMRequestBody.Properties.storageProfile.osDisk();
        CreateVMRequestBody.Properties.storageProfile.dataDisks[] dataDisksArray = new
                CreateVMRequestBody.Properties.storageProfile.dataDisks[1];
        CreateVMRequestBody.Properties.storageProfile.dataDisks dataDisks = new
                CreateVMRequestBody.Properties.storageProfile.dataDisks();

        CreateVMRequestBody.Properties.osProfile osProfile = new
                CreateVMRequestBody.Properties.osProfile();
        if (inputs.getAzureComputeCommonInputs().getVmName().length() > 14) {
            osProfile.setComputerName(inputs.getAzureComputeCommonInputs().getVmName().substring(0, 13));
        } else {
            osProfile.setComputerName(inputs.getAzureComputeCommonInputs().getVmName());
        }
        osProfile.setAdminUsername(inputs.getAdminUsername());


        if (!inputs.getPrivateImageName().isEmpty()) {
            imageReference.setId(SUBSCRIPTION_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId() +
                    RESOURCE_GROUPS_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getResourceGroupName() + AZURE_CREATE_VM_USING_CUSTOM_IMAGE_PATH
                    + inputs.getPrivateImageName());
        } else {
            imageReference.setPublisher(inputs.getPublisher());
            imageReference.setOffer(inputs.getOffer());
            imageReference.setSku(inputs.getSku());
            imageReference.setVersion(inputs.getImageVersion());

            dataDisks.setName(inputs.getDataDiskName() + DEFAULT_DATA_DISK_NAME);
            dataDisks.setDiskSizeGB(Integer.parseInt(inputs.getDiskSizeInGB()));
            dataDisks.setCreateOption(AZURE_DATA_DISK_CREATE_OPTION);
            dataDisks.setLun(0);
        }

        String getAvailabilitySetType = "";

        if (!inputs.getAvailabilitySetName().isEmpty()) {
            getAvailabilitySetType = getAvailabilityInfo(inputs);
        }
        if ((!inputs.getAvailabilitySetName().isEmpty() && AZURE_AVAILABILITY_SET_TYPE.equalsIgnoreCase(getAvailabilitySetType)) || !inputs.getPrivateImageName().isEmpty()
                || inputs.getDiskType().equalsIgnoreCase(AZURE_DISK_TYPE_MANAGED)) {
            CreateVMRequestBody.Properties.availabilitySet availabilitySet = new CreateVMRequestBody.Properties.availabilitySet();
            availabilitySet.setId(AZURE_CREATE_VM_AVAILABILITY_SET_PATH + inputs.getAvailabilitySetName());
            CreateVMRequestBody.Properties.storageProfile.osDisk.managedDisk managedDisk = new
                    CreateVMRequestBody.Properties.storageProfile.osDisk.managedDisk();
            managedDisk.setStorageAccountType(inputs.getStorageAccountType());
            osDisk.setManagedDisk(managedDisk);
            if ((inputs.getPrivateImageName().isEmpty()) && (AZURE_AVAILABILITY_SET_TYPE.equalsIgnoreCase(getAvailabilitySetType) || inputs.getDiskType().equalsIgnoreCase(AZURE_DISK_TYPE_MANAGED))) {
                CreateVMRequestBody.Properties.storageProfile.dataDisks.managedDisk dataDisksManagedDisk = new
                        CreateVMRequestBody.Properties.storageProfile.dataDisks.managedDisk();
                dataDisksManagedDisk.setStorageAccountType(inputs.getStorageAccountType());
                dataDisks.setManagedDisk(dataDisksManagedDisk);
                dataDisksArray[0] = dataDisks;
                storageProfile.setDataDisks(dataDisksArray);
            }

        } else {
            CreateVMRequestBody.Properties.availabilitySet availabilitySet = new CreateVMRequestBody.Properties.availabilitySet();
            availabilitySet.setId(SUBSCRIPTION_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId() +
                    RESOURCE_GROUPS_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getResourceGroupName() + AZURE_CREATE_VM_AVAILABILITY_SET_PATH + inputs.getAvailabilitySetName());
            CreateVMRequestBody.Properties.storageProfile.osDisk.vhd vhd = new
                    CreateVMRequestBody.Properties.storageProfile.osDisk.vhd();
            vhd.setUri(HTTP + AZURE_PROTOCOL_PREFIX + inputs.getStorageAccount() + DOT + AZURE_CREATE_VM_VHD_URI_PATH +
                    inputs.getAzureComputeCommonInputs().getVmName() + AZURE_CREATE_VM_OS_DISK_VHD_PATH);
            osDisk.setVhd(vhd);
            CreateVMRequestBody.Properties.storageProfile.dataDisks.vhd dataDiskvhd = new
                    CreateVMRequestBody.Properties.storageProfile.dataDisks.vhd();
            dataDiskvhd.setUri(HTTP + AZURE_PROTOCOL_PREFIX + inputs.getStorageAccount() + DOT + AZURE_CREATE_VM_VHD_URI_PATH +
                    inputs.getAzureComputeCommonInputs().getVmName() + AZURE_CREATE_VM_DATA_DISK_VHD_PATH);
            dataDisks.setVhd(dataDiskvhd);
            dataDisksArray[0] = dataDisks;
            storageProfile.setDataDisks(dataDisksArray);
        }
        if (!isEmpty(inputs.getAdminPassword())) {
            osProfile.setAdminPassword(inputs.getAdminPassword());
        } else {
            CreateVMRequestBody.Properties.osProfile.linuxConfiguration linuxConfiguration = new
                    CreateVMRequestBody.Properties.osProfile.linuxConfiguration();
            CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh ssh = new
                    CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh();
            CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh.publicKeys publicKeys = new
                    CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh.publicKeys();
            publicKeys.setPath(DEFAULT_SSH_PUBLIC_KEY_HOME_PATH + inputs.getAdminUsername() + DEFAULT_SSH_PUBLIC_KEY_PATH);
            publicKeys.setKeyData(getSSHPublicKeysINSubscriptionInfo(inputs));
            CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh.publicKeys[] publicKeysArray =
                    new CreateVMRequestBody.Properties.osProfile.linuxConfiguration.ssh.publicKeys[1];
            publicKeysArray[0] = publicKeys;
            ssh.setPublicKeys(publicKeysArray);
            linuxConfiguration.setSsh(ssh);
            linuxConfiguration.setDisablePasswordAuthentication(true);
            osProfile.setLinuxConfiguration(linuxConfiguration);
        }
        osDisk.setName(inputs.getOsDiskName() + DEFAULT_OS_DISK_NAME);
        osDisk.setCaching(AZURE_CREATE_VM_OS_DISK_CACHING_PATH);
        osDisk.setCreateOption(AZURE_CREATE_VM_OS_DISK_CREATION_OPTION_PATH);

        CreateVMRequestBody.Properties.networkProfile networkProfile = new
                CreateVMRequestBody.Properties.networkProfile();

        CreateVMRequestBody.Properties.networkProfile.networkInterfaces networkInterfaces1 = new
                CreateVMRequestBody.Properties.networkProfile.networkInterfaces();
        CreateVMRequestBody.Properties.networkProfile.networkInterfaces.properties nicProperties = new
                CreateVMRequestBody.Properties.networkProfile.networkInterfaces.properties();
        nicProperties.setPrimary(true);
        networkInterfaces1.setId(SUBSCRIPTION_PATH + inputs.getAzureComputeCommonInputs().getAzureCommonInputs().getSubscriptionId() +
                RESOURCE_GROUPS_PATH + inputs.getNicResourceGroupName() + AZURE_CREATE_VM_NETWORK_INTERFACES_PATH + inputs.getNicName());
        networkInterfaces1.setProperties(nicProperties);
        CreateVMRequestBody.Properties.networkProfile.networkInterfaces[] arry =
                new CreateVMRequestBody.Properties.networkProfile.networkInterfaces[1];
        arry[0] = networkInterfaces1;
        networkProfile.setNetworkInterfaces(arry);
        storageProfile.setImageReference(imageReference);
        storageProfile.setOsDisk(osDisk);

        properties.setHardwareProfile(hardwareProfile);
        properties.setStorageProfile(storageProfile);
        properties.setOsProfile(osProfile);
        properties.setNetworkProfile(networkProfile);
        createVMRequestBody.setProperties(properties);

        ObjectMapper createInstanceMapper = new ObjectMapper();
        createInstanceMapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);

        return createInstanceMapper.writeValueAsString(createVMRequestBody);
    }
}
