package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDisksInputs;
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
import static io.cloudslang.content.nutanix.prism.utils.Constants.UpdateDisksConstants.UPDATE_DISKS_PATH;
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

    @NotNull
    public static Map<String, String> updateDisks(@NotNull final io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDisksInputs nutanixUpdateDisksInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(updateDisksURL(nutanixUpdateDisksInputs));

        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(PUT);

        httpClientInputs.setBody(updateDisksBody(nutanixUpdateDisksInputs));

        httpClientInputs.setUsername(nutanixUpdateDisksInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixUpdateDisksInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, nutanixUpdateDisksInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String updateDisksURL(io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDisksInputs nutanixUpdateDisksInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixUpdateDisksInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixUpdateDisksInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixUpdateDisksInputs.getVmUUID())
                .append(UPDATE_DISKS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String updateDisksBody(io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDisksInputs nutanixUpdateDisksInputs) throws
            Exception {
        String requestBody = EMPTY;
        ObjectMapper updateDisksMapper = new ObjectMapper();
        ArrayList vmDiskList = new ArrayList();
        String[] deviceBusArray = nutanixUpdateDisksInputs.getDeviceBusList().split(",");
        String[] deviceIndexArray = nutanixUpdateDisksInputs.getDeviceIndexList().split(",");
        String[] vmDiskUUIDArray = nutanixUpdateDisksInputs.getVmDiskUUIDList().split(",");
        String[] vmDiskSizeArray = nutanixUpdateDisksInputs.getVmDiskSizeList().split(",");
        String[] isCDROMArray = nutanixUpdateDisksInputs.getIsCDROMList().split(",");
        String[] isEmptyArray = nutanixUpdateDisksInputs.getIsEmptyDiskList().split(",");
        String[] storageContainerUUIDArray = nutanixUpdateDisksInputs.getStorageContainerUUIDList().split(",");
        String[] ndfsFilepathArray = nutanixUpdateDisksInputs.getNdfsFilepathList().split(",");
        String[] isFlashModeEnabledArray = nutanixUpdateDisksInputs.getIsFlashModeEnabledList().split(",");
        String[] isSCSIPassThroughArray = nutanixUpdateDisksInputs.getIsSCSIPassThroughList().split(",");
        String[] isThinProvisionedArray = nutanixUpdateDisksInputs.getIsThinProvisionedList().split(",");

        if ((vmDiskUUIDArray.length == deviceBusArray.length) && (vmDiskUUIDArray.length == deviceIndexArray.length)) {
            for (int i = 0; i < vmDiskUUIDArray.length; i++) {
                ObjectMapper mapper = new ObjectMapper();
                com.fasterxml.jackson.databind.node.ObjectNode vmDisk = mapper.createObjectNode();

                vmDisk.put("uuid", vmDiskUUIDArray[i]);

                if (deviceBusArray[0] != "" && (vmDiskUUIDArray.length == deviceBusArray.length)) {
                    com.fasterxml.jackson.databind.node.ObjectNode diskAddress = mapper.createObjectNode();
                    diskAddress.put("device_bus", deviceBusArray[i]);
                    if (deviceIndexArray[0] != "" && (vmDiskUUIDArray.length == deviceIndexArray.length))
                        diskAddress.put("device_index", Integer.parseInt(deviceIndexArray[i]));
                    vmDisk.set("disk_address", diskAddress);
                }

                if (vmDiskSizeArray[0] != "" && (vmDiskUUIDArray.length == vmDiskSizeArray.length))
                    vmDisk.put("disk_size_bytes", Long.parseLong(vmDiskSizeArray[i]) * 1024 * 1024 * 1024);

                if (isCDROMArray[0] != "" && (vmDiskUUIDArray.length == isCDROMArray.length))
                    vmDisk.put("is_cdrom", Boolean.parseBoolean(isCDROMArray[i]));

                if (isEmptyArray[0] != "" && (vmDiskUUIDArray.length == isEmptyArray.length))
                    vmDisk.put("is_empty", Boolean.parseBoolean(isEmptyArray[i]));

                if (storageContainerUUIDArray[0] != "" && (vmDiskUUIDArray.length == storageContainerUUIDArray.length))
                    vmDisk.put("storage_container_uuid", storageContainerUUIDArray[i]);

                if (ndfsFilepathArray[0] != "" && (vmDiskUUIDArray.length == ndfsFilepathArray.length))
                    vmDisk.put("ndfs_filepath", ndfsFilepathArray[i]);

                if (isFlashModeEnabledArray[0] != "" && (vmDiskUUIDArray.length == isFlashModeEnabledArray.length))
                    vmDisk.put("flash_mode_enabled", Boolean.parseBoolean(isFlashModeEnabledArray[i]));

                if (isSCSIPassThroughArray[0] != "" && (vmDiskUUIDArray.length == isSCSIPassThroughArray.length))
                    vmDisk.put("is_scsi_pass_through", Boolean.parseBoolean(isSCSIPassThroughArray[i]));

                if (isThinProvisionedArray[0] != "" && (vmDiskUUIDArray.length == isThinProvisionedArray.length))
                    vmDisk.put("is_thin_provisioned", Boolean.parseBoolean(isThinProvisionedArray[i]));

                vmDiskList.add(vmDisk);
            }
            com.fasterxml.jackson.databind.node.ObjectNode root = updateDisksMapper.createObjectNode();
            root.set("vm_disks", updateDisksMapper.valueToTree(vmDiskList));
            if (nutanixUpdateDisksInputs.getVmLogicalTimestamp() != "" && !nutanixUpdateDisksInputs.getVmLogicalTimestamp().isEmpty())
                root.put("vm_logical_timestamp", Long.parseLong(nutanixUpdateDisksInputs.getVmLogicalTimestamp()));
            try {
                requestBody = updateDisksMapper.writeValueAsString(root);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("Size of vmDiskUUIDList, deviceBusList and deviceIndexList should be same");
        }
        return requestBody;
    }
}
