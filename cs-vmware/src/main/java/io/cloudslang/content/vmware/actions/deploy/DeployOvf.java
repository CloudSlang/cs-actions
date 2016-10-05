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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by giloan on 9/26/2016.
 */
public class DeployOvf {

    private static final int DISK_DRIVE_CIM_TYPE = 17;
    private final ThreadPoolExecutor executor;
    private final boolean parallel;

    public DeployOvf(final boolean parallel) {
        this.parallel = parallel;
        if (parallel) {
            this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        } else {
            this.executor = null;
        }
    }



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

        final ProgressUpdater progressUpdater = parallel ? new AsyncProgressUpdater(getDisksTotalNoBytes(importSpecResult), httpNfcLease, connectionResources) : new SyncProgressUpdater(getDisksTotalNoBytes(importSpecResult), httpNfcLease, connectionResources);
        if (parallel) {
            executor.execute(progressUpdater);
        }
        System.out.println("start");
        long time1 = System.currentTimeMillis();
        transferVmdkFiles(ovfPath, importSpecResult, deviceUrls, progressUpdater, parallel);
        if (parallel) {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        }
        System.out.println("completed in: " + String.valueOf((System.currentTimeMillis() - time1) / 1000));
    }

    @NotNull
    private void transferVmdkFiles(final String ovfPath, final OvfCreateImportSpecResult importSpecResult,
                                   final List<HttpNfcLeaseDeviceUrl> deviceUrls, final ProgressUpdater progressUpdater, final boolean parallel) throws IOException, RuntimeFaultFaultMsg, TimedoutFaultMsg, InterruptedException {
        for (HttpNfcLeaseDeviceUrl deviceUrl : deviceUrls) {
            final String deviceKey = deviceUrl.getImportKey();
            for (OvfFileItem fileItem : importSpecResult.getFileItem()) {
                if (deviceKey.equals(fileItem.getDeviceId())) {
                    final URL vmDiskUrl = new URL(deviceUrl.getUrl());
                    final ITransferVmdkFrom fromFile = getTransferVmdK(ovfPath, fileItem.getPath());
                    final TransferVmdkToUrl toUrl = new TransferVmdkToUrl("", vmDiskUrl, fileItem.isCreate());

                    final TransferVmdkTask transferVmdkTask = new TransferVmdkTask(fromFile, toUrl, progressUpdater);
                    if (parallel) {
                        executor.execute(transferVmdkTask);
                    } else {
                        transferVmdkTask.run();
                    }
                    break;
                }
            }
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

    @NotNull
    private HttpNfcLeaseInfo getHttpNfcLeaseInfoWhenReady(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        String leaseState = OvfUtils.gethttpNfcLeaseState(connectionResources, httpNfcLease);
        while (!"ready".equals(leaseState)) {
            leaseState = OvfUtils.gethttpNfcLeaseState(connectionResources, httpNfcLease);
            if ("error".equals(leaseState)) {
                throw new RuntimeException("Failed to get a HTTP NFC Lease: " + OvfUtils.getHttpNfcLeaseError(connectionResources, httpNfcLease));
            }
            try {
                Thread.sleep(100);
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
