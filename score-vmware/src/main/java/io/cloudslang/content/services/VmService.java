package io.cloudslang.content.services;

import com.vmware.vim25.*;
import io.cloudslang.content.connection.ConnectionResources;
import io.cloudslang.content.connection.helpers.WaitForValues;
import io.cloudslang.content.constants.Constants;
import io.cloudslang.content.constants.ErrorMessages;
import io.cloudslang.content.constants.Outputs;
import io.cloudslang.content.entities.VmInputs;
import io.cloudslang.content.entities.http.HttpInputs;
import io.cloudslang.content.utils.FindObjects;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {

    private Map<String, String> results = new HashMap<>();

    public Map<String, String> createVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference vmFolderMor = connectionResources.getVmFolderMor();
        VirtualMachineConfigSpec vmConfigSpec = getVmConfigSpec(vmInputs, connectionResources);
        ManagedObjectReference resourcePoolMor = connectionResources.getResourcePoolMor();
        ManagedObjectReference hostMor = connectionResources.getHostMor();
        VimPortType vimPort = connectionResources.getVimPortType();

        ManagedObjectReference taskMor = vimPort.createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            results.put(Outputs.RETURN_RESULT, "Success: Created [" + vmInputs.getVirtualMachineName()
                    + "] VM. The taskId is: " + taskMor.getValue());

        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Failure: Creating [" + vmInputs.getVirtualMachineName() + "] VM");
        }

        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> deleteVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.destroyTask(vmMor);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was deleted. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be deleted.");
            }

        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.powerOnVMTask(vmMor, null);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was successfully powered on. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be powered on.");
            }
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [ " + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.powerOffVMTask(vmMor);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was successfully powered off. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be powered off.");
            }
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [ " + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    private VirtualMachineConfigSpec getVmConfigSpec(VmInputs vmInputs, ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        VirtualMachineConfigSpec vmConfigSpec = createVmConfigSpec(vmInputs, connectionResources);

        vmConfigSpec.setName(vmInputs.getVirtualMachineName());
        vmConfigSpec.setAnnotation(vmInputs.getDescription());
        vmConfigSpec.setMemoryMB(vmInputs.getLongVmMemorySize());
        vmConfigSpec.setNumCPUs(vmInputs.getIntNumCPUs());
        vmConfigSpec.setGuestId(vmInputs.getGuestOsId());

        return vmConfigSpec;
    }

    private VirtualMachineConfigSpec createVmConfigSpec(VmInputs vmInputs, ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ConfigTarget configTarget = getConfigTargetForHost(connectionResources);

        ManagedObjectReference datastoreRef = null;
        boolean dataStoreFound = Boolean.FALSE;
        List<VirtualMachineDatastoreInfo> datastoresList = configTarget.getDatastore();

        String datastoreName = vmInputs.getDataStore();

        if (StringUtils.isNotBlank(vmInputs.getDataStore())) {
            datastoreRef = getDatastoreRef(datastoreName, datastoresList);
        } else {
            for (VirtualMachineDatastoreInfo datastore : datastoresList) {
                DatastoreSummary dsSummary = datastore.getDatastore();
                if (dsSummary.isAccessible()) {
                    datastoreName = dsSummary.getName();
                    datastoreRef = dsSummary.getDatastore();
                    dataStoreFound = Boolean.TRUE;
                    break;
                }
            }

            if (!dataStoreFound) {
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
        VirtualDevice ideController = getIdeController(connectionResources);
        if (ideController != null) {
            cdSpec = getCdromConfigSpec(datastoreRef, datastoreVolume, ideController);
        }

        // Create a new file based disk for the VM
        VirtualDeviceConfigSpec diskSpec = createVirtualDisk(datastoreName,
                Constants.DEFAULT_DISK_CONTROLLER_KEY,
                vmInputs.getLongVmDiskSize());

        // Add a NIC. The network name must be set as the device name to create the NIC.
        String networkName = getNetworkName(configTarget);
        VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();

        if (networkName != null) {
            setNicSpec(nicSpec, networkName);
        }

        return addDeviceConfigSpecs(configSpec, scsiCtrlSpec, floppySpec, cdSpec, ideController, diskSpec, nicSpec);
    }

    private ConfigTarget getConfigTargetForHost(ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        ConfigTarget configTarget = connectionResources.getVimPortType()
                .queryConfigTarget(environmentBrowserMor, connectionResources.getHostMor());

        if (configTarget == null) {
            throw new RuntimeException(ErrorMessages.CONFIG_TARGET_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        return configTarget;
    }

    private ManagedObjectReference getDatastoreRef(String datastoreName, List<VirtualMachineDatastoreInfo> datastoresList) {

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

    private String getVolumeName(String volName) {
        String volumeName = Constants.DEFAULT_VOLUME_NAME;
        if (StringUtils.isNotBlank(volName)) {
            volumeName = Constants.LEFT_SQUARE_BRACKET + volName + Constants.RIGHT_SQUARE_BRACKET;
        }

        return volumeName;
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

    private VirtualDevice getIdeController(ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        List<VirtualDevice> defaultDevices = getDefaultDevicesList(connectionResources);
        VirtualDevice ideController = null;
        for (VirtualDevice device : defaultDevices) {
            if (device instanceof VirtualIDEController) {
                ideController = device;
                break;
            }
        }

        return ideController;
    }

    private List<VirtualDevice> getDefaultDevicesList(ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        VirtualMachineConfigOption configOptions = connectionResources.getVimPortType()
                .queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());
        if (configOptions == null) {
            throw new RuntimeException(ErrorMessages.VIRTUAL_HARDWARE_INFO_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        List<VirtualDevice> listVirtualDevices = configOptions.getDefaultDevice();
        if (listVirtualDevices == null) {
            throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND_IN_COMPUTE_RESOURCE);
        }

        return listVirtualDevices;
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

    private VirtualDeviceConfigSpec createVirtualDisk(String volName, int diskCtlrKey, long vmDiskSize) {
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

    private boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        boolean retVal = Boolean.FALSE;

        WaitForValues wfv = new WaitForValues(connectionResources.getConnection());
        Object[] result = wfv.wait(task,
                new String[]{Constants.INFO_STATE, Constants.INFO_ERROR},
                new String[]{Constants.STATE},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[0].equals(TaskInfoState.SUCCESS)) {
            retVal = Boolean.TRUE;
        }
        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }
        return retVal;
    }
}