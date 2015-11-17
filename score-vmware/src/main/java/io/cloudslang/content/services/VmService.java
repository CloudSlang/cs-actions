package io.cloudslang.content.services;

import com.vmware.vim25.*;
import io.cloudslang.content.connection.Connection;
import io.cloudslang.content.connection.helpers.GetMOREF;
import io.cloudslang.content.connection.helpers.WaitForValues;
import io.cloudslang.content.connection.impl.BasicConnection;
import io.cloudslang.content.constants.Constants;
import io.cloudslang.content.constants.ErrorMessages;
import io.cloudslang.content.constants.Inputs;
import io.cloudslang.content.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class VmService {
    private BasicConnection basicConnection = new BasicConnection();
    private Connection connection;
    private VimPortType vimPortType;
    private GetMOREF getMOREF;

    /**
     * Creates the virtual machine.
     *
     * @throws RemoteException  the remote exception
     * @throws RemoteException, RuntimeFaultFaultMsg, InvalidPropertyFaultMsg, InvalidCollectorVersionFaultMsg,
     *                          OutOfBoundsFaultMsg, DuplicateNameFaultMsg, VmConfigFaultFaultMsg,
     *                          InsufficientResourcesFaultFaultMsg, AlreadyExistsFaultMsg, InvalidDatastoreFaultMsg,
     *                          FileFaultFaultMsg, InvalidStateFaultMsg, InvalidNameFaultMsg, TaskInProgressFaultMsg
     */
    public ManagedObjectReference createVirtualMachine(String host,
                                                       int port,
                                                       String protocol,
                                                       String username,
                                                       String password,
                                                       boolean x509HostnameVerifier,
                                                       String dataCenterName,
                                                       String hostname,
                                                       String virtualMachineName,
                                                       String description,
                                                       String dataStore,
                                                       String resourcePool,
                                                       int numCPUs,
                                                       long vmDiskSize,
                                                       long vmMemorySize,
                                                       String guestOsId) throws Exception {

        String url = InputUtils.getUrlString(host, String.valueOf(port), protocol);

        connection = basicConnection.connect(url, username, password, x509HostnameVerifier);

        vimPortType = connection.getVimPort();
        getMOREF = new GetMOREF(connection);

        ManagedObjectReference managedObjectReference = basicConnection.getServiceContent().getRootFolder();

        ManagedObjectReference dataCenterMor = getdataCenterMor(dataCenterName, managedObjectReference, getMOREF);
        ManagedObjectReference hostMor = getHostMor(hostname, getMOREF, dataCenterMor);
        ManagedObjectReference computeResourceMor = getComputeResourceMor(getMOREF, hostMor);

        ManagedObjectReference resourcePoolMor = (ManagedObjectReference) getMOREF
                .entityProps(computeResourceMor, new String[]{Constants.RESOURCE_POOL})
                .get(Constants.RESOURCE_POOL);

        ManagedObjectReference vmFolderMor = (ManagedObjectReference) getMOREF
                .entityProps(dataCenterMor, new String[]{Inputs.VM_FOLDER})
                .get(Inputs.VM_FOLDER);

        VirtualMachineConfigSpec vmConfigSpec = getVmConfigSpec(virtualMachineName,
                description,
                dataStore,
                numCPUs,
                vmDiskSize,
                vmMemorySize,
                guestOsId,
                hostMor,
                computeResourceMor);

        ManagedObjectReference taskMor = vimPortType.createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

        if (getTaskResultAfterDone(taskMor)) {

            ManagedObjectReference virtualMachineMor = (ManagedObjectReference) getMOREF
                    .entityProps(taskMor, new String[]{Constants.INFO_RESULT})
                    .get(Constants.INFO_RESULT);

            powerOnVM(virtualMachineMor);
            basicConnection.disconnect();

            return taskMor;
        } else {
            basicConnection.disconnect();
            throw new RuntimeException("Failure: Creating [ " + virtualMachineName + "] VM");
        }
    }

    private ManagedObjectReference getdataCenterMor(String dataCenterName,
                                                    ManagedObjectReference mor,
                                                    GetMOREF getMOREF)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference dataCenterMor = getMOREF.inContainerByType(mor, Constants.DATA_CENTER).get(dataCenterName);
        if (dataCenterMor == null) {
            throw new RuntimeException("Datacenter " + dataCenterName + " not found.");
        }

        return dataCenterMor;
    }

    private ManagedObjectReference getHostMor(String hostname,
                                              GetMOREF getMOREF,
                                              ManagedObjectReference dataCenterMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference hostMor = getMOREF.inContainerByType(dataCenterMor, Constants.HOST_SYSTEM).get(hostname);
        if (hostMor == null) {
            throw new RuntimeException("Host " + hostname + " not found");
        }

        return hostMor;
    }

    private ManagedObjectReference getComputeResourceMor(GetMOREF getMOREF,
                                                         ManagedObjectReference hostMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        ManagedObjectReference computeResourceMor = (ManagedObjectReference) getMOREF
                .entityProps(hostMor, new String[]{Constants.PARENT})
                .get(Constants.PARENT);

        if (computeResourceMor == null) {
            throw new RuntimeException(ErrorMessages.COMPUTE_RESOURCE_NOT_FOUND_ON_HOST);
        }

        return computeResourceMor;
    }

    private VirtualMachineConfigSpec getVmConfigSpec(String virtualMachineName,
                                                     String description,
                                                     String dataStore,
                                                     int numCPUs,
                                                     long vmDiskSize,
                                                     long vmMemorySize,
                                                     String guestOsId,
                                                     ManagedObjectReference hostMor,
                                                     ManagedObjectReference computeResourceMor)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        VirtualMachineConfigSpec vmConfigSpec = createVmConfigSpec(dataStore,
                vmDiskSize,
                computeResourceMor,
                hostMor);

        vmConfigSpec.setName(virtualMachineName);
        vmConfigSpec.setAnnotation(description);
        vmConfigSpec.setMemoryMB(vmMemorySize);
        vmConfigSpec.setNumCPUs(numCPUs);
        vmConfigSpec.setGuestId(guestOsId);

        return vmConfigSpec;
    }

    /**
     * Creates the vm config spec object.
     *
     * @param datastoreName      the datastore name
     * @param diskSizeMB         the disk size in mb
     * @param computeResourceMor the compute res moref
     * @param hostMor            the host mor
     * @return the virtual machine config spec object
     * @throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg
     */
    private VirtualMachineConfigSpec createVmConfigSpec(String datastoreName,
                                                        long diskSizeMB,
                                                        ManagedObjectReference computeResourceMor,
                                                        ManagedObjectReference hostMor)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ConfigTarget configTarget = getConfigTargetForHost(computeResourceMor, hostMor);

        ManagedObjectReference datastoreRef = null;
        boolean flag = false;
        List<VirtualMachineDatastoreInfo> datastoresList = configTarget.getDatastore();

        if (StringUtils.isNotBlank(datastoreName)) {
            datastoreRef = getDatastoreRef(datastoreName, datastoresList);
        } else {
            for (VirtualMachineDatastoreInfo datastore : datastoresList) {
                DatastoreSummary dsSummary = datastore.getDatastore();
                if (dsSummary.isAccessible()) {
                    datastoreName = dsSummary.getName();
                    datastoreRef = dsSummary.getDatastore();
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND_ON_HOST);
            }
        }

        VirtualMachineConfigSpec configSpec = getVirtualMachineConfigSpec(datastoreName);
        String datastoreVolume = getVolumeName(datastoreName);

        // Add a SCSI controller
        VirtualDeviceConfigSpec scsiCtrlSpec = addScsiController(Constants.DEFAULT_DISK_CONTROLLER_KEY);

        // Add a floppy disk
        VirtualDeviceConfigSpec floppySpec = getFloppyConfigSpec();

        // Add a CD-ROM based on a physical device
        VirtualDeviceConfigSpec cdSpec = null;
        VirtualDevice ideController = getIdeController(computeResourceMor, hostMor);
        if (ideController != null) {
            cdSpec = getCdromConfigSpec(datastoreRef, datastoreVolume, ideController);
        }

        // Create a new file based disk for the VM
        VirtualDeviceConfigSpec diskSpec = createVirtualDisk(datastoreName,
                Constants.DEFAULT_DISK_CONTROLLER_KEY,
                diskSizeMB);

        // Add a NIC. The network name must be set as the device name to create the NIC.
        String networkName = getNetworkName(configTarget);
        VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();

        if (networkName != null) {
            setNicSpec(nicSpec, networkName);
        }

        return addDeviceConfigSpecs(configSpec, scsiCtrlSpec, floppySpec, cdSpec, ideController, diskSpec, nicSpec);
    }

    private VirtualMachineConfigSpec addDeviceConfigSpecs(VirtualMachineConfigSpec configSpec,
                                                          VirtualDeviceConfigSpec scsiCtrlSpec,
                                                          VirtualDeviceConfigSpec floppySpec,
                                                          VirtualDeviceConfigSpec cdSpec,
                                                          VirtualDevice ideController,
                                                          VirtualDeviceConfigSpec diskSpec,
                                                          VirtualDeviceConfigSpec nicSpec) {

        List<VirtualDeviceConfigSpec> deviceConfigSpec = new ArrayList<>();
        deviceConfigSpec.add(scsiCtrlSpec);
        deviceConfigSpec.add(floppySpec);
        deviceConfigSpec.add(diskSpec);
        deviceConfigSpec.add(nicSpec);

        if (ideController != null) {
            deviceConfigSpec.add(cdSpec);
        }

        configSpec.getDeviceChange().addAll(deviceConfigSpec);

        return configSpec;
    }

    private String getNetworkName(ConfigTarget configTarget) {
        if (configTarget.getNetwork() == null) {
            return null;
        }

        String networkName = null;
        for (VirtualMachineNetworkInfo network : configTarget.getNetwork()) {
            NetworkSummary netSummary = network.getNetwork();
            if (netSummary.isAccessible()) {
                networkName = netSummary.getName();
                break;
            }
        }

        return networkName;
    }

    /**
     * Creates the virtual disk.
     *
     * @param volName     the vol name
     * @param diskCtlrKey the disk controller key
     * @param vmDiskSize  the disk size in mb
     * @return the virtual device config spec object
     */
    VirtualDeviceConfigSpec createVirtualDisk(String volName, int diskCtlrKey, long vmDiskSize) {
        VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();

        diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.CREATE);
        diskSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        VirtualDisk disk = new VirtualDisk();
        VirtualDiskFlatVer2BackingInfo diskfileBacking = new VirtualDiskFlatVer2BackingInfo();

        diskfileBacking.setFileName(getVolumeName(volName));
        diskfileBacking.setDiskMode(Constants.PERSISTENT);

        disk.setKey(Constants.DEFAULT_DISK_KEY);
        disk.setControllerKey(diskCtlrKey);
        disk.setUnitNumber(Constants.DEFAULT_DISK_UNIT_NUMBER);
        disk.setBacking(diskfileBacking);
        disk.setCapacityInKB(vmDiskSize * Constants.THOUSAND_MULTIPLIER);

        diskSpec.setDevice(disk);

        return diskSpec;
    }

    /**
     * This method returns the ConfigTarget for a HostSystem.
     *
     * @param computeResMor A MoRef to the ComputeResource used by the HostSystem
     * @param hostMor       A MoRef to the HostSystem
     * @return Instance of ConfigTarget for the supplied HostSystem/ComputeResource
     * @throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg when no ConfigTarget can be found
     */
    private ConfigTarget getConfigTargetForHost(ManagedObjectReference computeResMor,
                                                ManagedObjectReference hostMor)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) getMOREF
                .entityProps(computeResMor, new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        ConfigTarget configTarget = vimPortType.queryConfigTarget(environmentBrowserMor, hostMor);

        if (configTarget == null) {
            throw new RuntimeException(ErrorMessages.CONFIG_TARGET_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        return configTarget;
    }

    private ManagedObjectReference getDatastoreRef(String datastoreName,
                                                   List<VirtualMachineDatastoreInfo> datastoresList) {

        for (VirtualMachineDatastoreInfo datastore : datastoresList) {
            DatastoreSummary dsSummary = datastore.getDatastore();
            if (datastoreName.equals(dsSummary.getName())) {
                if (!dsSummary.isAccessible()) {
                    throw new RuntimeException(ErrorMessages.SPECIFIED_DATA_STORE_NOT_ACCESSIBLE);
                }

                return dsSummary.getDatastore();
            }
        }
        throw new RuntimeException(ErrorMessages.SPECIFIED_DATA_STORE_IS_NOT_FOUND);
    }

    private VirtualMachineConfigSpec getVirtualMachineConfigSpec(String datastoreName) {
        VirtualMachineConfigSpec configSpec = new VirtualMachineConfigSpec();
        VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();

        vmfi.setVmPathName(getVolumeName(datastoreName));
        configSpec.setFiles(vmfi);

        return configSpec;
    }

    private VirtualDevice getIdeController(ManagedObjectReference computeResMor,
                                           ManagedObjectReference hostMor)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        List<VirtualDevice> defaultDevices = getDefaultDevicesList(computeResMor, hostMor);
        VirtualDevice ideController = null;
        for (VirtualDevice device : defaultDevices) {
            if (device instanceof VirtualIDEController) {
                ideController = device;
                break;
            }
        }

        return ideController;
    }

    /**
     * The method returns the default devices from the HostSystem.
     *
     * @param computeResMor A MoRef to the ComputeResource used by the HostSystem
     * @param hostMor       A MoRef to the HostSystem
     * @return Array of VirtualDevice containing the default devices for the
     * HostSystem
     * @throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg
     */
    private List<VirtualDevice> getDefaultDevicesList(ManagedObjectReference computeResMor,
                                                      ManagedObjectReference hostMor)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) getMOREF
                .entityProps(computeResMor, new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        VirtualMachineConfigOption configOptions = vimPortType.queryConfigOption(environmentBrowserMor, null, hostMor);
        if (configOptions == null) {
            throw new RuntimeException(ErrorMessages.VIRTUAL_HARDWARE_INFO_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        List<VirtualDevice> listVirtualDevices = configOptions.getDefaultDevice();
        if (listVirtualDevices == null) {
            throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        return listVirtualDevices;
    }

    private VirtualDeviceConfigSpec addScsiController(int diskCtlrKey) {
        VirtualDeviceConfigSpec scsiCtrlSpec = new VirtualDeviceConfigSpec();
        scsiCtrlSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        VirtualLsiLogicController scsiCtrl = new VirtualLsiLogicController();
        scsiCtrl.setBusNumber(Constants.DEFAULT_CONTROLLER_BUS_NUMBER);
        scsiCtrlSpec.setDevice(scsiCtrl);
        scsiCtrl.setKey(diskCtlrKey);
        scsiCtrl.setSharedBus(VirtualSCSISharing.NO_SHARING);

        return scsiCtrlSpec;
    }

    private VirtualDeviceConfigSpec getFloppyConfigSpec() {
        VirtualDeviceConfigSpec floppySpec = new VirtualDeviceConfigSpec();
        floppySpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        VirtualFloppy floppy = new VirtualFloppy();

        VirtualFloppyDeviceBackingInfo flpBacking = new VirtualFloppyDeviceBackingInfo();
        flpBacking.setDeviceName(Constants.DEFAULT_FLOPPY_DEVICE_NAME);

        floppy.setBacking(flpBacking);
        floppy.setKey(Constants.DEFAULT_FLOPPY_DEVICE_KEY);
        floppySpec.setDevice(floppy);

        return floppySpec;
    }

    private VirtualDeviceConfigSpec getCdromConfigSpec(ManagedObjectReference datastoreRef,
                                                       String datastoreVolume,
                                                       VirtualDevice ideController) {

        VirtualDeviceConfigSpec cdRomSpec = new VirtualDeviceConfigSpec();
        VirtualCdrom cdRom = new VirtualCdrom();
        VirtualCdromIsoBackingInfo cdDeviceBacking = new VirtualCdromIsoBackingInfo();

        cdRomSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        cdDeviceBacking.setDatastore(datastoreRef);
        cdDeviceBacking.setFileName(datastoreVolume + Constants.TEST_CD_ISO);

        cdRom.setBacking(cdDeviceBacking);
        cdRom.setKey(Constants.DEFAULT_CDROM_KEY);
        cdRom.setControllerKey(ideController.getKey());
        cdRom.setUnitNumber(Constants.DEFAULT_CDROM_UNIT_NUMBER);

        cdRomSpec.setDevice(cdRom);

        return cdRomSpec;
    }

    private void setNicSpec(VirtualDeviceConfigSpec nicSpec, String networkName) {
        nicSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);

        VirtualEthernetCard nic = new VirtualPCNet32();
        VirtualEthernetCardNetworkBackingInfo nicBacking = new VirtualEthernetCardNetworkBackingInfo();
        nicBacking.setDeviceName(networkName);

        nic.setAddressType(Constants.GENERATED);
        nic.setBacking(nicBacking);
        nic.setKey(Constants.DEFAULT_NIC_KEY);

        nicSpec.setDevice(nic);
    }

    private String getVolumeName(String volName) {
        String volumeName = Constants.DEFAULT_VOLUME_NAME;
        if (StringUtils.isNotBlank(volName)) {
            volumeName = Constants.LEFT_SQUARE_BRACKET + volName + Constants.RIGHT_SQUARE_BRACKET;
        }

        return volumeName;
    }

    /**
     * This method returns a boolean value specifying whether the Task is succeeded or failed.
     *
     * @param task ManagedObjectReference representing the Task.
     * @return boolean value representing the Task result.
     * @throws InvalidCollectorVersionFaultMsg
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    private boolean getTaskResultAfterDone(ManagedObjectReference task)
            throws InvalidPropertyFaultMsg,
            RuntimeFaultFaultMsg,
            InvalidCollectorVersionFaultMsg {

        boolean retVal = false;

        WaitForValues wfv = new WaitForValues(connection);
        Object[] result = wfv.wait(task,
                new String[]{Constants.INFO_STATE, Constants.INFO_ERROR},
                new String[]{Constants.STATE},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[0].equals(TaskInfoState.SUCCESS)) {
            retVal = true;
        }
        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }
        return retVal;
    }

    /**
     * Power on vm.
     *
     * @param vmMor the vm moref
     * @throws RemoteException  the remote exception
     * @throws RemoteException, RuntimeFaultFaultMsg, InvalidPropertyFaultMsg, InvalidCollectorVersionFaultMsg,
     *                          TaskInProgressFaultMsg, VmConfigFaultFaultMsg, InsufficientResourcesFaultFaultMsg,
     *                          FileFaultFaultMsg, InvalidStateFaultMsg
     */
    void powerOnVM(ManagedObjectReference vmMor) throws RemoteException,
            RuntimeFaultFaultMsg,
            InvalidPropertyFaultMsg,
            InvalidCollectorVersionFaultMsg,
            TaskInProgressFaultMsg,
            VmConfigFaultFaultMsg,
            InsufficientResourcesFaultFaultMsg,
            FileFaultFaultMsg,
            InvalidStateFaultMsg {
        ManagedObjectReference cssTask = vimPortType.powerOnVMTask(vmMor, null);
        if (!getTaskResultAfterDone(cssTask)) {
            throw new RuntimeException("Failure: starting [ " + vmMor.getValue() + "] VM");
        }
    }
}
