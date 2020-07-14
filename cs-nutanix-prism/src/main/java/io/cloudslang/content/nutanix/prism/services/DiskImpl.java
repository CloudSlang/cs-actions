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
import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.exceptions.NutanixDetachDiskException;
import io.cloudslang.content.nutanix.prism.services.models.disks.AttachDisksRequestBody;
import io.cloudslang.content.nutanix.prism.services.models.disks.DetachDisksRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.AttachDisksConstants.ATTACH_DISKS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DetachDisksConstants.DETACH_DISKS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DiskImpl {

    @NotNull
    public static String detachDisksBody(NutanixDetachDisksInputs nutanixDetachDisksInputs) throws
            NutanixDetachDiskException {
        String requestBody = EMPTY;
        ObjectMapper detachDisksMapper = new ObjectMapper();
        DetachDisksRequestBody detachDisksRequestBody = new DetachDisksRequestBody();
        detachDisksRequestBody.setVmUUID(nutanixDetachDisksInputs.getVMUUID());
        ArrayList vmDiskList = new ArrayList();
        String[] deviceBusArray = nutanixDetachDisksInputs.getDeviceBusList().split(",");
        String[] deviceIndexArray = nutanixDetachDisksInputs.getDeviceIndexList().split(",");
        String[] vmDiskUUIDArray = nutanixDetachDisksInputs.getVmDiskUUIDList().split(",");

        if ((vmDiskUUIDArray.length == deviceBusArray.length) && (vmDiskUUIDArray.length == deviceIndexArray.length)) {
            for (int i = 0; i < vmDiskUUIDArray.length; i++) {
                DetachDisksRequestBody.VMDisks vmDisks = detachDisksRequestBody.new VMDisks();
                DetachDisksRequestBody.DiskAddress diskAddress = detachDisksRequestBody.new DiskAddress();
                diskAddress.setDeviceBus(deviceBusArray[i]);
                diskAddress.setDeviceIndex(deviceIndexArray[i]);
                diskAddress.setVmDiskUUID(vmDiskUUIDArray[i]);
                vmDisks.setDiskAddress(diskAddress);

                vmDiskList.add(vmDisks);
            }

            detachDisksRequestBody.setVmDisks(vmDiskList);
            try {
                requestBody = detachDisksMapper.writeValueAsString(detachDisksRequestBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            throw new NutanixDetachDiskException("Size of vmDiskUUIDList, deviceBusList and deviceIndexList should " +
                    "be same");
        }
        return requestBody;

    }

    @NotNull
    public static String AttachDisksBody(NutanixAttachDisksInputs nutanixAttachDisksInputs) throws
            NutanixDetachDiskException {
        String requestBody = EMPTY;
        ObjectMapper detachDisksMapper = new ObjectMapper();
        AttachDisksRequestBody attachDisksRequestBody = new AttachDisksRequestBody();
        ArrayList vmDiskList = new ArrayList();
        String[] deviceBusArray = nutanixAttachDisksInputs.getDeviceBusList().split(",");
        String[] deviceIndexArray = nutanixAttachDisksInputs.getDeviceIndexList().split(",");
        String[] isCDROMArray = nutanixAttachDisksInputs.getIsCDROMList().split(",");
        String[] isEmptyDiskArray = nutanixAttachDisksInputs.getIsEmptyList().split(",");
        String[] sourceVMDiskUUIDArray = nutanixAttachDisksInputs.getSourceVMDiskUUIDList().split(",");
        String[] vmDiskMinimumSizeArray = nutanixAttachDisksInputs.getVmDiskMinimumSizeList().split(",");
        String[] ndfsFilepathArray = nutanixAttachDisksInputs.getNdfsFilepathList().split(",");
        String[] vmDiskSizeArray = nutanixAttachDisksInputs.getVmDiskSizeList().split(",");
        String[] vmStorageContainerUUIDArray = nutanixAttachDisksInputs.getStorageContainerUUIDList().split(",");
        String[] isSCSIPassThroughArray = nutanixAttachDisksInputs.getIsSCSIPassThroughList().split(",");
        String[] isThinProvisionedArray = nutanixAttachDisksInputs.getIsThinProvisionedList().split(",");
        String[] isFlashModeEnabledArray = nutanixAttachDisksInputs.getIsFlashModeEnabledList().split(",");

        if (isCDROMArray[0] != "" && isCDROMArray.length > 0) {
            for (int i = 0; i < isCDROMArray.length; i++) {
                AttachDisksRequestBody.VMDisks vmDisks = attachDisksRequestBody.new VMDisks();

                AttachDisksRequestBody.VMDiskCreate vmDiskCreate = attachDisksRequestBody.new VMDiskCreate();
                AttachDisksRequestBody.VMDiskClone vmDiskClone = attachDisksRequestBody.new VMDiskClone();

                if ((deviceBusArray[0] != "" && (isCDROMArray.length == deviceBusArray.length)) || (deviceIndexArray[0]
                        != "" && (isCDROMArray.length == deviceIndexArray.length))) {
                    AttachDisksRequestBody.DiskAddress diskAddress = attachDisksRequestBody.new DiskAddress();
                    if (deviceBusArray[0] != "" && (isCDROMArray.length == deviceBusArray.length))
                        diskAddress.setDevice_bus(deviceBusArray[i]);
                    if (deviceIndexArray[0] != "" && (isCDROMArray.length == deviceIndexArray.length))
                        diskAddress.setDevice_index(Integer.parseInt(deviceIndexArray[i]));
                    vmDisks.setDisk_address(diskAddress);
                }

                if ((isCDROMArray.length == vmStorageContainerUUIDArray.length) &&
                        (isCDROMArray.length == vmDiskSizeArray.length) && (vmStorageContainerUUIDArray[0] != "") &&
                        (vmDiskSizeArray[0] != "")) {
                    vmDiskCreate.setSize(Long.parseLong(vmDiskSizeArray[i]) * 1024 * 1024 * 1024);
                    vmDiskCreate.setStorage_container_uuid(vmStorageContainerUUIDArray[i]);
                } else if ((isCDROMArray.length == sourceVMDiskUUIDArray.length) && (sourceVMDiskUUIDArray[0] != "")) {
                    AttachDisksRequestBody.CloneDiskAddress cloneDiskAddress = attachDisksRequestBody.new
                            CloneDiskAddress();
                    cloneDiskAddress.setVmdisk_uuid(sourceVMDiskUUIDArray[i]);
                    vmDiskClone.setDisk_address(cloneDiskAddress);
                    if (vmStorageContainerUUIDArray[0] != "" && (isCDROMArray.length == vmStorageContainerUUIDArray.length))
                        vmDiskClone.setStorage_container_uuid(vmStorageContainerUUIDArray[i]);
                    if (vmDiskMinimumSizeArray[0] != "")
                        vmDiskClone.setMinimum_size((Long.parseLong(vmDiskMinimumSizeArray[i])) * 1024 * 1024 * 1024);
                } else if ((isCDROMArray.length == ndfsFilepathArray.length) && (ndfsFilepathArray[0] != "")) {
                    AttachDisksRequestBody.CloneDiskAddress cloneDiskAddress = attachDisksRequestBody.new
                            CloneDiskAddress();
                    cloneDiskAddress.setNdfs_filepath(ndfsFilepathArray[i]);
                    vmDiskClone.setDisk_address(cloneDiskAddress);
                    if (vmStorageContainerUUIDArray[0] != "" && (isCDROMArray.length == vmStorageContainerUUIDArray.length))
                        vmDiskClone.setStorage_container_uuid(vmStorageContainerUUIDArray[i]);
                    if (vmDiskMinimumSizeArray[i] != "")
                        vmDiskClone.setMinimum_size((Long.parseLong(vmDiskMinimumSizeArray[i])) * 1024 * 1024 * 1024);
                } else {
                    throw new NutanixDetachDiskException("Size of the isCDROMList, storageContainerUUIDList, " +
                            "vmDiskSizeList should be same in case of empty disk creation, and size of isCDROMList, " +
                            "sourceVMDiskUUIDList or ndfsFilepathList, vmDiskSizeList should be same in case of disk clone.");
                }

                vmDisks.setIs_cdrom(Boolean.parseBoolean(isCDROMArray[i]));
                if (isEmptyDiskArray[0] != "" && (isCDROMArray.length == isEmptyDiskArray.length))
                    vmDisks.setIs_empty(Boolean.parseBoolean(isEmptyDiskArray[i]));
                if (isSCSIPassThroughArray[0] != "" && (isCDROMArray.length == isSCSIPassThroughArray.length))
                    vmDisks.setIs_scsi_pass_through(Boolean.parseBoolean(isSCSIPassThroughArray[i]));
                if (isThinProvisionedArray[0] != "" && (isCDROMArray.length == isThinProvisionedArray.length))
                    vmDisks.setIs_thin_provisioned(Boolean.parseBoolean(isThinProvisionedArray[i]));
                if (isFlashModeEnabledArray[0] != "" && (isCDROMArray.length == isFlashModeEnabledArray.length))
                    vmDisks.setFlash_mode_enabled(Boolean.parseBoolean(isFlashModeEnabledArray[i]));
                if ((isCDROMArray.length == vmStorageContainerUUIDArray.length) &&
                        (isCDROMArray.length == vmDiskSizeArray.length) && (vmStorageContainerUUIDArray[0] != "") &&
                        (vmDiskSizeArray[0] != "")) {
                    vmDisks.setVm_disk_create(vmDiskCreate);
                } else {
                    vmDisks.setVm_disk_clone(vmDiskClone);
                }
                vmDiskList.add(vmDisks);
            }
            attachDisksRequestBody.setVmDisks(vmDiskList);
            try {
                requestBody = detachDisksMapper.writeValueAsString(attachDisksRequestBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return requestBody;
    }

    @NotNull
    public static Map<String, String> detachDisks(@NotNull final NutanixDetachDisksInputs nutanixDetachDisksInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(detachDisksURL(nutanixDetachDisksInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);

        httpClientInputs.setBody(detachDisksBody(nutanixDetachDisksInputs));

        httpClientInputs.setUsername(nutanixDetachDisksInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixDetachDisksInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, nutanixDetachDisksInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> AttachDisk(@NotNull final NutanixAttachDisksInputs nutanixAttachDisksInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(AttachDisksURL(nutanixAttachDisksInputs));

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);

        httpClientInputs.setBody(AttachDisksBody(nutanixAttachDisksInputs));

        httpClientInputs.setUsername(nutanixAttachDisksInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixAttachDisksInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, nutanixAttachDisksInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String detachDisksURL(NutanixDetachDisksInputs nutanixDetachDisksInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixDetachDisksInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixDetachDisksInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixDetachDisksInputs.getVMUUID())
                .append(DETACH_DISKS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String AttachDisksURL(NutanixAttachDisksInputs nutanixAttachDisksInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixAttachDisksInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixAttachDisksInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixAttachDisksInputs.getVmUUID())
                .append(ATTACH_DISKS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }
}
