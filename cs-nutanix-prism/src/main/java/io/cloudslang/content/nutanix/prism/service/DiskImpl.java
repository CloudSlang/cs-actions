package io.cloudslang.content.nutanix.prism.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.exceptions.NutanixDetachDiskException;
import io.cloudslang.content.nutanix.prism.service.models.disks.AttachDisksRequestBody;
import io.cloudslang.content.nutanix.prism.service.models.disks.DetachDisksRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.AttachDisksConstants.ATTACH_DISKS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DetachDisksConstants.DETACH_DISKS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DiskImpl {

    @NotNull
    public static String detachDisksBody(NutanixDetachDisksInputs nutanixDetachDisksInputs) throws NutanixDetachDiskException {
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
            throw new NutanixDetachDiskException("Size of vmDiskUUIDList, deviceBusList and deviceIndexList should be same");
        }
        return requestBody;

    }

    @NotNull
    public static String AttachDisksBody(NutanixAttachDisksInputs nutanixAttachDisksInputs) throws NutanixDetachDiskException {
        String requestBody = EMPTY;
        ObjectMapper detachDisksMapper = new ObjectMapper();
        AttachDisksRequestBody attachDisksRequestBody = new AttachDisksRequestBody();
        ArrayList vmDiskList = new ArrayList();
        String[] deviceBusArray = nutanixAttachDisksInputs.getDeviceBus().split(",");
        String[] deviceIndexArray = nutanixAttachDisksInputs.getDeviceIndex().split(",");
        String[] vmDisksizeArray = nutanixAttachDisksInputs.getVmDisksize().split(",");
        String[] vmStoragecontainerUUIDDiskArray = nutanixAttachDisksInputs.getStoragecontainerUUIDDisk().split(",");

        if ((vmDisksizeArray.length == deviceBusArray.length) && (vmDisksizeArray.length == deviceIndexArray.length) && (vmDisksizeArray.length == vmStoragecontainerUUIDDiskArray.length)) {
            for (int i = 0; i < vmDisksizeArray.length; i++) {
                AttachDisksRequestBody.VMDisks vmDisks = attachDisksRequestBody.new VMDisks();
                AttachDisksRequestBody.DiskAddress diskAddress = attachDisksRequestBody.new DiskAddress();
                AttachDisksRequestBody.VMDiskCreate vmDiskCreate = attachDisksRequestBody.new VMDiskCreate();
                diskAddress.setDeviceBus(deviceBusArray[i]);
                diskAddress.setDeviceIndex(deviceIndexArray[i]);
                vmDiskCreate.setSize(vmDisksizeArray[i]);
                vmDiskCreate.setStorage_container_uuid(vmStoragecontainerUUIDDiskArray[i]);

                vmDisks.setDiskAddress(diskAddress);
                vmDisks.setVmDiskCreate(vmDiskCreate);

                vmDiskList.add(vmDisks);
            }

            attachDisksRequestBody.setVmDisks(vmDiskList);
            try {
                requestBody = detachDisksMapper.writeValueAsString(attachDisksRequestBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {

            throw new NutanixDetachDiskException("Size of the deviceBus and deviceIndex and diskSize and storageContainerUUID should be same");
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
        System.out.println(AttachDisksURL(nutanixAttachDisksInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);

        httpClientInputs.setBody(AttachDisksBody(nutanixAttachDisksInputs));
        System.out.println(AttachDisksBody(nutanixAttachDisksInputs));

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
