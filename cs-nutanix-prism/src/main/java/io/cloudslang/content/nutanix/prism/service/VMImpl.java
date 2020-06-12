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

package io.cloudslang.content.nutanix.prism.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateVMInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetVMDetailsInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMsInputs;
import io.cloudslang.content.nutanix.prism.service.models.virtualmachines.AddNicRequestBody;
import io.cloudslang.content.nutanix.prism.service.models.virtualmachines.CreateVMRequestBody;
import io.cloudslang.content.nutanix.prism.utils.Constants;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getQueryParams;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class VMImpl {

    @NotNull
    public static Map<String, String> listVMs(@NotNull final NutanixListVMsInputs nutanixListVMsInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listVMsURL(nutanixListVMsInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixListVMsInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixListVMsInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        httpClientInputs.setQueryParams(getQueryParams(nutanixListVMsInputs.getFilter(),
                nutanixListVMsInputs.getOffset(), nutanixListVMsInputs.getLength(),
                nutanixListVMsInputs.getSortOrder(), nutanixListVMsInputs.getSortAttribute(),
                nutanixListVMsInputs.getIncludeVMDiskConfigInfo(),
                nutanixListVMsInputs.getIncludeVMNicConfigInfo()));
        setCommonHttpInputs(httpClientInputs, nutanixListVMsInputs.getCommonInputs());

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> createVM(@NotNull final NutanixCreateVMInputs nutanixCreateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(createVMURL(nutanixCreateVMInputs));
        setCommonHttpInputs(httpClientInputs, nutanixCreateVMInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(createVMBody(nutanixCreateVMInputs, DELIMITER));
        } catch (JsonProcessingException e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixCreateVMInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixCreateVMInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getVMDetails(@NotNull final NutanixGetVMDetailsInputs nutanixGetVMDetailsInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getVMDetailsURL(nutanixGetVMDetailsInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixGetVMDetailsInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixGetVMDetailsInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        httpClientInputs.setQueryParams(getQueryParams(nutanixGetVMDetailsInputs.getIncludeVMDiskConfigInfo(),
                nutanixGetVMDetailsInputs.getIncludeVMNicConfigInfo()));
        setCommonHttpInputs(httpClientInputs, nutanixGetVMDetailsInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String listVMsURL(NutanixListVMsInputs nutanixListVMsInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixListVMsInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixListVMsInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVMDetailsURL(NutanixGetVMDetailsInputs nutanixGetVMDetailsInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixGetVMDetailsInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixGetVMDetailsInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixGetVMDetailsInputs.getVMUUID());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createVMURL(NutanixCreateVMInputs nutanixCreateVMInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixCreateVMInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixCreateVMInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static Map<String, String> AddNic(@NotNull final NutanixAddNicInputs nutanixAddNicInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(AddNicURL(nutanixAddNicInputs));
        setCommonHttpInputs(httpClientInputs, nutanixAddNicInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(AddNicBody(nutanixAddNicInputs));
        } catch (JsonProcessingException e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixAddNicInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixAddNicInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String AddNicBody(NutanixAddNicInputs nutanixAttachNicInputs)
            throws JsonProcessingException {
        String requestBody = EMPTY;
        ObjectMapper AttachNicMapper = new ObjectMapper();
        AddNicRequestBody AddNicBody = new AddNicRequestBody();
        ArrayList spec_list = new ArrayList();
        String[] network_uuid = nutanixAttachNicInputs.getNetworkUUID().split(",");
        String[] requested_ip_address = nutanixAttachNicInputs.getRequestedIPAddress().split(",");
        String[] is_connected = nutanixAttachNicInputs.getIsConnected().split(",");
        String[] vlan_Id = nutanixAttachNicInputs.getVlanid().split(",");
        for (int i = 0; i < requested_ip_address.length; i++) {
            AddNicRequestBody.VMNics vmNics = AddNicBody.new VMNics();
            vmNics.setNetwork_uuid(network_uuid[i]);
            vmNics.setRequested_ip_address(requested_ip_address[i]);
            vmNics.setIs_connected(Boolean.parseBoolean(is_connected[i]));
            vmNics.setVlan_id(vlan_Id[i]);
            spec_list.add(vmNics);
        }
        AddNicBody.setSpec_list(spec_list);

        try {
            requestBody = AttachNicMapper.writeValueAsString(AddNicBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;
    }

    @NotNull
    public static String AddNicURL(NutanixAddNicInputs nutanixAttachNicInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixAttachNicInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixAttachNicInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixAttachNicInputs.getVmUUID())
                .append(Constants.AddNICConstants.ADD_NIC_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }


    @NotNull
    public static String createVMBody(NutanixCreateVMInputs nutanixCreateVMInputs, String delimiter)
            throws JsonProcessingException {
        String requestBody = EMPTY;
        final List<String> hostUUIDsList = new ArrayList<>();
        ObjectMapper createVMMapper = new ObjectMapper();
        CreateVMRequestBody createVMBody = new CreateVMRequestBody();
        CreateVMRequestBody.CreateVMData createVMData = createVMBody.new CreateVMData();
        createVMData.setName(nutanixCreateVMInputs.getVmName());
        createVMData.setDescription(nutanixCreateVMInputs.getDescription());
        createVMData.setMemory_mb(Integer.parseInt(nutanixCreateVMInputs.getVmMemorySize()) * 1024);
        createVMData.setNum_vcpus(Integer.parseInt(nutanixCreateVMInputs.getNumVCPUs()));
        createVMData.setNum_cores_per_vcpu(Integer.parseInt(nutanixCreateVMInputs.getNumCoresPerVCPU()));
        createVMData.setTimezone(nutanixCreateVMInputs.getTimeZone());
        createVMData.setHypervisor_type(nutanixCreateVMInputs.getHypervisorType());

        CreateVMRequestBody.Affinity affinity = createVMBody.new Affinity();
        if (!nutanixCreateVMInputs.getHostUUIDs().isEmpty()) {
            String[] hostUUIDs = nutanixCreateVMInputs.getHostUUIDs().split(delimiter);
            Collections.addAll(hostUUIDsList, hostUUIDs);
            affinity.setHost_uuids(hostUUIDsList);
            affinity.setPolicy(AFFINITY);
        }
        CreateVMRequestBody.VMDisks vmDisks = createVMBody.new VMDisks();
        CreateVMRequestBody.DiskAddress diskAddress = createVMBody.new DiskAddress();
        if (Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM()) == true) {
            diskAddress.setDevice_bus(nutanixCreateVMInputs.getDeviceBus());
            diskAddress.setDisk_label(nutanixCreateVMInputs.getDiskLabel());
            diskAddress.setDevice_index(Integer.parseInt(nutanixCreateVMInputs.getDeviceIndex()));
            diskAddress.setNdfs_filepath(nutanixCreateVMInputs.getNdfsFilepath());
            vmDisks.setFlash_mode_enabled(Boolean.parseBoolean(nutanixCreateVMInputs.getFlashModeEnabled()));
            vmDisks.setIs_scsi_pass_through(Boolean.parseBoolean(nutanixCreateVMInputs.getIsSCSIPassThrough()));
            vmDisks.setIs_thin_provisioned(Boolean.parseBoolean(nutanixCreateVMInputs.getIsThinProvisioned()));
            vmDisks.setIs_cdrom(Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM()));
            CreateVMRequestBody.VMDiskClone vmDiskClone = createVMBody.new VMDiskClone();
            CreateVMRequestBody.CloneDiskAddress cloneDiskAddress = createVMBody.new CloneDiskAddress();
            cloneDiskAddress.setVmdisk_uuid(nutanixCreateVMInputs.getSourceVMDiskUUID());
            vmDiskClone.setMinimum_size(Integer.parseInt(nutanixCreateVMInputs.getVmDiskMinimumSize()));
            vmDiskClone.setStorage_container_uuid(nutanixCreateVMInputs.getStorageContainerUUID());
            vmDiskClone.setDisk_address(cloneDiskAddress);
            vmDisks.setVm_disk_clone(vmDiskClone);
        } else {
            diskAddress.setDevice_bus(nutanixCreateVMInputs.getDeviceBus());
            diskAddress.setDisk_label(nutanixCreateVMInputs.getDiskLabel());
            diskAddress.setDevice_index(Integer.parseInt(nutanixCreateVMInputs.getDeviceIndex()));
            diskAddress.setNdfs_filepath(nutanixCreateVMInputs.getNdfsFilepath());
            vmDisks.setFlash_mode_enabled(Boolean.parseBoolean(nutanixCreateVMInputs.getFlashModeEnabled()));
            vmDisks.setIs_scsi_pass_through(Boolean.parseBoolean(nutanixCreateVMInputs.getIsSCSIPassThrough()));
            vmDisks.setIs_thin_provisioned(Boolean.parseBoolean(nutanixCreateVMInputs.getIsThinProvisioned()));
            vmDisks.setIs_cdrom(Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM()));
            vmDisks.setIs_empty(Boolean.parseBoolean(nutanixCreateVMInputs.getIsEmpty()));
            CreateVMRequestBody.VMDiskCreate vmDiskCreate = createVMBody.new VMDiskCreate();
            vmDiskCreate.setStorage_container_uuid(nutanixCreateVMInputs.getStorageContainerUUID());
            vmDiskCreate.setSize((Long.parseLong(nutanixCreateVMInputs.getVmDiskSize()) * 1024 * 1024 * 1024));
            vmDisks.setVm_disk_create(vmDiskCreate);
        }
        if (!nutanixCreateVMInputs.getExternalDiskUrl().isEmpty()) {
            diskAddress.setDevice_bus(nutanixCreateVMInputs.getDeviceBus());
            diskAddress.setDisk_label(nutanixCreateVMInputs.getDiskLabel());
            diskAddress.setDevice_index(Integer.parseInt(nutanixCreateVMInputs.getDeviceIndex()));
            diskAddress.setNdfs_filepath(nutanixCreateVMInputs.getNdfsFilepath());
            vmDisks.setFlash_mode_enabled(Boolean.parseBoolean(nutanixCreateVMInputs.getFlashModeEnabled()));
            vmDisks.setIs_scsi_pass_through(Boolean.parseBoolean(nutanixCreateVMInputs.getIsSCSIPassThrough()));
            vmDisks.setIs_thin_provisioned(Boolean.parseBoolean(nutanixCreateVMInputs.getIsThinProvisioned()));
            vmDisks.setIs_cdrom(Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM()));
            vmDisks.setIs_empty(Boolean.parseBoolean(nutanixCreateVMInputs.getIsEmpty()));
            CreateVMRequestBody.VMDiskCloneExternal vmDiskCloneExternal = createVMBody.new VMDiskCloneExternal();
            vmDiskCloneExternal.setExternal_disk_url(nutanixCreateVMInputs.getExternalDiskUrl());
            vmDiskCloneExternal.setSize(Integer.parseInt(nutanixCreateVMInputs.getExternalDiskSize()));
            vmDiskCloneExternal.setStorage_container_uuid(nutanixCreateVMInputs.getStorageContainerUUID());
            vmDisks.setVm_disk_clone_external(vmDiskCloneExternal);
        }
        vmDisks.setDisk_address(diskAddress);

        CreateVMRequestBody.VMNics vmNics = createVMBody.new VMNics();
        vmNics.setNetwork_uuid(nutanixCreateVMInputs.getNetworkUUID());
        vmNics.setIs_connected(Boolean.parseBoolean(nutanixCreateVMInputs.getIsConnected()));
        vmNics.setRequested_ip_address(nutanixCreateVMInputs.getRequestedIPAddress());

        CreateVMRequestBody.VMFeatures vmFeatures = createVMBody.new VMFeatures();
        vmFeatures.setAGENT_VM(Boolean.parseBoolean(nutanixCreateVMInputs.getAgentVM()));

        createVMData.setVmFeatures(vmFeatures);
        ArrayList vmNicsList = new ArrayList();
        vmNicsList.add(vmNics);
        createVMData.setVmNics(vmNicsList);
        ArrayList vmDiskList = new ArrayList();
        vmDiskList.add(vmDisks);
        createVMData.setVmDisks(vmDiskList);
        createVMData.setAffinity(affinity);

        try {
            requestBody = createVMMapper.writeValueAsString(createVMData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
