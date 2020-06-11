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
import io.cloudslang.content.nutanix.prism.entities.*;
import io.cloudslang.content.nutanix.prism.service.models.virtualmachines.CreateVMRequestBody;
import io.cloudslang.content.nutanix.prism.service.models.virtualmachines.SetPowerStateRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.SetPowerStateConstants.SET_POWER_STATE_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.*;
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
        httpClientInputs.setBody(createVMBody(nutanixCreateVMInputs, DELIMITER));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixCreateVMInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixCreateVMInputs.getCommonInputs().getPassword());
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
    public static Map<String, String> setPowerState(@NotNull final NutanixSetPowerStateInputs nutanixSetPowerStateInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(setPowerStateURL(nutanixSetPowerStateInputs));
        setCommonHttpInputs(httpClientInputs, nutanixSetPowerStateInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(setPowerStateBody(nutanixSetPowerStateInputs));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixSetPowerStateInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixSetPowerStateInputs.getCommonInputs().getPassword());
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
    public static String setPowerStateURL(NutanixSetPowerStateInputs nutanixSetPowerStateInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixSetPowerStateInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixSetPowerStateInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixSetPowerStateInputs.getVMUUID())
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

    @NotNull
    public static String setPowerStateBody(NutanixSetPowerStateInputs nutanixSetPowerStateInputs) {
        String requestBody = EMPTY;
        ObjectMapper setPowerStateMapper = new ObjectMapper();
        SetPowerStateRequestBody setPowerStateBody = new SetPowerStateRequestBody();
        SetPowerStateRequestBody.SetPowerStateData setPowerStateData = setPowerStateBody.new SetPowerStateData();
        setPowerStateData.setHost_uuid(nutanixSetPowerStateInputs.getHostUUID());
        setPowerStateData.setUuid(nutanixSetPowerStateInputs.getVMUUID());
        setPowerStateData.setTransition(nutanixSetPowerStateInputs.getTransition());
        if (!nutanixSetPowerStateInputs.getVmLogicalTimestamp().isEmpty())
            setPowerStateData.setVm_logical_timestamp(Integer.parseInt(nutanixSetPowerStateInputs.getVmLogicalTimestamp()));

        try {
            requestBody = setPowerStateMapper.writeValueAsString(setPowerStateData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;
    }
}
