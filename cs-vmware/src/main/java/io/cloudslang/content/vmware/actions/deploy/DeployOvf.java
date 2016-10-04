package io.cloudslang.content.vmware.actions.deploy;

import com.vmware.vim25.HttpNfcLeaseDeviceUrl;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OvfCreateImportSpecParams;
import com.vmware.vim25.OvfCreateImportSpecResult;
import com.vmware.vim25.OvfFileItem;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TimedoutFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by giloan on 9/26/2016.
 */
public class DeployOvf {

    private static final int DISK_DRIVE_CIM_TYPE = 17;

    public void deployOvf(HttpInputs httpInputs, VmInputs vmInputs, String ovfPath, String vmName, boolean parallel) throws Exception {

        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference ovfManager = getOvfManager(connectionResources);
        VmUtils vmUtils = new VmUtils();
        ManagedObjectReference resourcePool = vmUtils.getMorResourcePool(null, connectionResources);
        ManagedObjectReference hostMor = vmUtils.getMorHost(vmInputs.getHostname(), connectionResources, null);
        ManagedObjectReference datastoreMor = vmUtils.getMorDataStore(vmInputs.getDataStore(), connectionResources, null, vmInputs);
        ManagedObjectReference folderMor = vmUtils.getMorFolder(vmInputs.getFolderName(), connectionResources);

        //get a lease in ready state. Create the VMs and/or vApps that make up the entity.
        OvfCreateImportSpecResult importSpecResult = connectionResources.getVimPortType().
                createImportSpec(ovfManager, getOvfString(ovfPath), resourcePool, datastoreMor, getOvfCreateImportSpecParams(vmName, hostMor));

        ManagedObjectReference httpNfcLease = OvfUtils.getHttpNfcLease(connectionResources, importSpecResult.getImportSpec(), resourcePool, hostMor, folderMor);

        HttpNfcLeaseInfo httpNfcLeaseInfo = getHttpNfcLeaseInfoWhenReady(connectionResources, httpNfcLease);

        //The client must call HttpNfcLeaseProgress on the lease periodically to keep the lease alive and report progress to the server.
        // Failure to do so will cause the lease to time out, and the import process will be aborted.

        List<HttpNfcLeaseDeviceUrl> deviceUrls = httpNfcLeaseInfo.getDeviceUrl();

        ProgressUpdater progressUpdater = new ProgressUpdater(getDisksTotalNoBytes(importSpecResult), connectionResources, httpNfcLease);
        new Thread(progressUpdater).start();

        List<TransferVmdk> transferVmdkThreads = transferVmdkFiles(ovfPath, parallel, importSpecResult, deviceUrls, progressUpdater);

        if (parallel) {
            joinAllTransferVmdkThreads(transferVmdkThreads);
        }

        progressUpdater.shutdown();
        connectionResources.getVimPortType().httpNfcLeaseProgress(httpNfcLease, 100);
        connectionResources.getVimPortType().httpNfcLeaseComplete(httpNfcLease);
    }

    @NotNull
    private List<TransferVmdk> transferVmdkFiles(String ovfPath, boolean parallel, OvfCreateImportSpecResult importSpecResult, List<HttpNfcLeaseDeviceUrl> deviceUrls, ProgressUpdater progressUpdater) throws IOException, RuntimeFaultFaultMsg, TimedoutFaultMsg, InterruptedException {
        List<TransferVmdk> transferVmdkThreads = new ArrayList<>();
        for (HttpNfcLeaseDeviceUrl deviceUrl : deviceUrls) {
            String deviceKey = deviceUrl.getImportKey();
            for (OvfFileItem fileItem : importSpecResult.getFileItem()) {
                if (deviceKey.equals(fileItem.getDeviceId())) {
                    URL vmDiskUrl = new URL(deviceUrl.getUrl());

                    ITransferVmdkFrom fromFile = getTransferVmdK(ovfPath, fileItem.getPath());
                    TransferVmdkToUrl toUrl = new TransferVmdkToUrl("", vmDiskUrl, fileItem.isCreate());

                    TransferVmdk transferVmdkThread = new TransferVmdk(deviceKey, fromFile, toUrl, progressUpdater);
                    transferVmdkThreads.add(transferVmdkThread);
                    transferVmdkThread.start();
                    if (!parallel) {
                        transferVmdkThread.join();
                    }
                }
            }
        }
        return transferVmdkThreads;
    }

    private void joinAllTransferVmdkThreads(List<TransferVmdk> transferVmdkThreads) throws InterruptedException {
        for (TransferVmdk transferVmdkThread : transferVmdkThreads) {
            transferVmdkThread.join();
        }
    }

    private ManagedObjectReference getOvfManager(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        VimPortType vimPort = connectionResources.getVimPortType();
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);
        return serviceContent.getOvfManager();
    }

    private ITransferVmdkFrom getTransferVmdK(String ovfFilePath, String vmdkPath) throws IOException {
        return new TransferVmdkFromFile(new File(new File(ovfFilePath).getParent(), vmdkPath));
    }

    private Map<String, Long> getDeviceIdFromDeviceUrl(final List<HttpNfcLeaseDeviceUrl> deviceUrls) {
        HashMap<String, Long> toReturn = new HashMap<>();
        for (HttpNfcLeaseDeviceUrl deviceUrl : deviceUrls) {
            toReturn.put(deviceUrl.getKey(), deviceUrl.getFileSize());
        }
        return toReturn;
    }

    @NotNull
    private HttpNfcLeaseInfo getHttpNfcLeaseInfoWhenReady(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        String leaseState = OvfUtils.gethttpNfcLeaseState(connectionResources, httpNfcLease);
        while (!"ready".equals(leaseState)) {
            leaseState = OvfUtils.gethttpNfcLeaseState(connectionResources, httpNfcLease);
            if ("error".equals(leaseState)) {
                throw new RuntimeException("Failed to get a HTTP NFC Lease: " + OvfUtils.getHttpNfcLeaseError(connectionResources, httpNfcLease));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                //TODO: log exception
            }
        }
        return OvfUtils.getHttpNfcLeaseInfo(connectionResources, httpNfcLease);
    }

    @NotNull
    private String getOvfString(String ovfPath) throws IOException {
        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(ovfPath), StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (bufferedReader.ready() && (line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }


    public OvfCreateImportSpecParams getOvfCreateImportSpecParams(String vmName, ManagedObjectReference hostSystem) {
        OvfCreateImportSpecParams ovfCreateImportSpecParams = new OvfCreateImportSpecParams();
        ovfCreateImportSpecParams.setDeploymentOption("");
        ovfCreateImportSpecParams.setLocale(Locale.getDefault().toString());
        ovfCreateImportSpecParams.setEntityName(vmName);
        ovfCreateImportSpecParams.setIpAllocationPolicy("dhcpPolicy");
        ovfCreateImportSpecParams.setHostSystem(hostSystem);
        return ovfCreateImportSpecParams;
    }

    public long getDisksTotalNoBytes(OvfCreateImportSpecResult importSpecResult) {
        long disksTotalNoBytes = 0;
        List<OvfFileItem> ovfFileItems = importSpecResult.getFileItem();
        for (OvfFileItem item : ovfFileItems) {
            if (item.getCimType() == DISK_DRIVE_CIM_TYPE) {
                disksTotalNoBytes += item.getSize();
            }
        }
        return disksTotalNoBytes;
    }
}
