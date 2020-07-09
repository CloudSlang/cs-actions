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

package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.*;
import io.cloudslang.content.nutanix.prism.services.models.virtualmachines.CreateVMRequestBody;
import io.cloudslang.content.nutanix.prism.services.models.virtualmachines.SetVMPowerStateRequestBody;
import io.cloudslang.content.nutanix.prism.services.models.virtualmachines.UpdateVMRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.PUT;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.SetVMPowerStateConstants.SET_POWER_STATE_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;


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
        httpClientInputs.setBody(createVMBody(nutanixCreateVMInputs, DELIMITER));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixCreateVMInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixCreateVMInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> updateVM(@NotNull final NutanixUpdateVMInputs nutanixUpdateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(updateVMURL(nutanixUpdateVMInputs));
        setCommonHttpInputs(httpClientInputs, nutanixUpdateVMInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(updateVMBody(nutanixUpdateVMInputs, DELIMITER));
        } catch (JsonProcessingException e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setUsername(nutanixUpdateVMInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixUpdateVMInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> deleteVM(@NotNull final NutanixDeleteVMInputs nutanixDeleteVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(deleteVMURL(nutanixDeleteVMInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setUsername(nutanixDeleteVMInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixDeleteVMInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        if (!nutanixDeleteVMInputs.getLogicalTimestamp().isEmpty()) {
            httpClientInputs.setQueryParams(getDeleteVMQueryParams(nutanixDeleteVMInputs.getDeleteSnapshots(),
                    nutanixDeleteVMInputs.getLogicalTimestamp()));
        }
        setCommonHttpInputs(httpClientInputs, nutanixDeleteVMInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> setVMPowerState(@NotNull final NutanixSetVMPowerStateInputs nutanixSetVMPowerStateInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(setVMPowerStateURL(nutanixSetVMPowerStateInputs));
        setCommonHttpInputs(httpClientInputs, nutanixSetVMPowerStateInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(setVMPowerStateBody(nutanixSetVMPowerStateInputs));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixSetVMPowerStateInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixSetVMPowerStateInputs.getCommonInputs().getPassword());
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
    public static String updateVMURL(NutanixUpdateVMInputs nutanixUpdateVMInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixUpdateVMInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixUpdateVMInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixUpdateVMInputs.getVmUUID());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    public static String deleteVMURL(NutanixDeleteVMInputs nutanixDeleteVMInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixDeleteVMInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixDeleteVMInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixDeleteVMInputs.getVMUUID());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String setVMPowerStateURL(NutanixSetVMPowerStateInputs nutanixSetVMPowerStateInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixSetVMPowerStateInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixSetVMPowerStateInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixSetVMPowerStateInputs.getVMUUID())
                .append(PATH_SEPARATOR)
                .append(SET_POWER_STATE_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createVMBody(NutanixCreateVMInputs nutanixCreateVMInputs, String delimiter) {
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
        if (Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM())) {
            diskAddress.setDevice_bus(nutanixCreateVMInputs.getDeviceBus());
            diskAddress.setDisk_label(nutanixCreateVMInputs.getDiskLabel());
            diskAddress.setDevice_index(Integer.parseInt(nutanixCreateVMInputs.getDeviceIndex()));
            vmDisks.setFlash_mode_enabled(Boolean.parseBoolean(nutanixCreateVMInputs.getFlashModeEnabled()));
            vmDisks.setIs_scsi_pass_through(Boolean.parseBoolean(nutanixCreateVMInputs.getIsSCSIPassThrough()));
            vmDisks.setIs_thin_provisioned(Boolean.parseBoolean(nutanixCreateVMInputs.getIsThinProvisioned()));
            vmDisks.setIs_cdrom(Boolean.parseBoolean(nutanixCreateVMInputs.getIsCDROM()));
            CreateVMRequestBody.VMDiskClone vmDiskClone = createVMBody.new VMDiskClone();
            CreateVMRequestBody.CloneDiskAddress cloneDiskAddress = createVMBody.new CloneDiskAddress();
            if (!nutanixCreateVMInputs.getSourceVMDiskUUID().isEmpty()) {
                cloneDiskAddress.setVmdisk_uuid(nutanixCreateVMInputs.getSourceVMDiskUUID());
            } else {
                cloneDiskAddress.setNdfs_filepath(nutanixCreateVMInputs.getNdfsFilepath());
            }
            vmDiskClone.setMinimum_size(Long.parseLong(nutanixCreateVMInputs.getVmDiskMinimumSize()) * 1024 * 1024 * 1024);
            vmDiskClone.setStorage_container_uuid(nutanixCreateVMInputs.getStorageContainerUUID());
            vmDiskClone.setDisk_address(cloneDiskAddress);
            vmDisks.setVm_disk_clone(vmDiskClone);
        } else {
            diskAddress.setDevice_bus(nutanixCreateVMInputs.getDeviceBus());
            diskAddress.setDisk_label(nutanixCreateVMInputs.getDiskLabel());
            diskAddress.setDevice_index(Integer.parseInt(nutanixCreateVMInputs.getDeviceIndex()));
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

    @NotNull
    public static String setVMPowerStateBody(NutanixSetVMPowerStateInputs nutanixSetVMPowerStateInputs) {
        String requestBody = EMPTY;
        ObjectMapper setPowerStateMapper = new ObjectMapper();
        SetVMPowerStateRequestBody setPowerStateBody = new SetVMPowerStateRequestBody();
        SetVMPowerStateRequestBody.SetPowerStateData setPowerStateData = setPowerStateBody.new SetPowerStateData();
        setPowerStateData.setHost_uuid(nutanixSetVMPowerStateInputs.getHostUUID());
        setPowerStateData.setUuid(nutanixSetVMPowerStateInputs.getVMUUID());
        setPowerStateData.setTransition(nutanixSetVMPowerStateInputs.getTransition());
        if (!nutanixSetVMPowerStateInputs.getVmLogicalTimestamp().isEmpty())
            setPowerStateData.setVm_logical_timestamp(Integer.parseInt(nutanixSetVMPowerStateInputs.getVmLogicalTimestamp()));

        try {
            requestBody = setPowerStateMapper.writeValueAsString(setPowerStateData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;
    }

    @NotNull
    public static String updateVMBody(NutanixUpdateVMInputs nutanixUpdateVMInputs, String delimiter)
            throws JsonProcessingException {
        String requestBody = EMPTY;
        final List<String> hostUUIDsList = new ArrayList<>();
        ObjectMapper createVMMapper = new ObjectMapper();
        UpdateVMRequestBody updateVMBody = new UpdateVMRequestBody();
        UpdateVMRequestBody.UpdateVMData updateVMData = updateVMBody.new UpdateVMData();
        updateVMData.setName(nutanixUpdateVMInputs.getVmName());
        updateVMData.setDescription(nutanixUpdateVMInputs.getDescription());
        if (!isEmpty(nutanixUpdateVMInputs.getVmMemorySize())) {
            updateVMData.setMemory_mb(Integer.parseInt(nutanixUpdateVMInputs.getVmMemorySize()) * 1024);
        }
        if (!isEmpty(nutanixUpdateVMInputs.getNumVCPUs())) {
            updateVMData.setNum_vcpus(Integer.parseInt(nutanixUpdateVMInputs.getNumVCPUs()));
        }
        if (!isEmpty(nutanixUpdateVMInputs.getNumCoresPerVCPU())) {
            updateVMData.setNum_cores_per_vcpu(Integer.parseInt(nutanixUpdateVMInputs.getNumCoresPerVCPU()));
        }
        updateVMData.setTimezone(nutanixUpdateVMInputs.getTimeZone());
        UpdateVMRequestBody.Affinity affinity = updateVMBody.new Affinity();
        if (!nutanixUpdateVMInputs.getHostUUIDs().isEmpty()) {
            String[] hostUUIDs = nutanixUpdateVMInputs.getHostUUIDs().split(delimiter);
            Collections.addAll(hostUUIDsList, hostUUIDs);
            affinity.setHost_uuids(hostUUIDsList);
            affinity.setPolicy(AFFINITY);
            updateVMData.setAffinity(affinity);
        }
        if (!isEmpty(nutanixUpdateVMInputs.getAgentVM())) {
            UpdateVMRequestBody.VMFeatures vmFeatures = updateVMBody.new VMFeatures();
            vmFeatures.setAGENT_VM(Boolean.parseBoolean(nutanixUpdateVMInputs.getAgentVM()));
            updateVMData.setVmFeatures(vmFeatures);
        }


        try {
            requestBody = createVMMapper.writeValueAsString(updateVMData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
