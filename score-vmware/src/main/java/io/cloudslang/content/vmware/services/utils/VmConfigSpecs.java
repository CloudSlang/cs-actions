package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.Operation;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/8/2016.
 */
public class VmConfigSpecs {
    private static final String GENERATED = "generated";
    private static final String DATASTORE = "datastore";
    private static final String CONFIG_HARDWARE_DEVICE = "config.hardware.device";
    private static final String DEFAULT_FLOPPY_DEVICE_NAME = "/dev/fd0";
    private static final String DEFAULT_VOLUME_NAME = "[Local]";
    private static final String LEFT_SQUARE_BRACKET = "[";

    private static final int DEFAULT_DISK_CONTROLLER_KEY = 1;
    private static final int DEFAULT_DISK_UNIT_NUMBER = 0;
    private static final int DEFAULT_DISK_KEY = 0;
    private static final int DEFAULT_FLOPPY_DEVICE_KEY = 3;
    private static final int DEFAULT_CD_ROM_KEY = 20;
    private static final int DEFAULT_CD_ROM_UNIT_NUMBER = 0;
    private static final int DEFAULT_CONTROLLER_BUS_NUMBER = 0;
    private static final int DEFAULT_NIC_KEY = 4;
    private static final int MAXIMUM_SCSI_SLOTS = 16;
    private static final int MAXIMUM_ATAPI_SLOTS = 2;
    private static final int RESERVED_SCSI_SLOT = 7;
    private static final int SERVER_ASSIGNED = -1;
    private static final int OCCUPIED = 1;

    public VirtualMachineConfigSpec getVmConfigSpec(VmInputs vmInputs, ConnectionResources connectionResources)
            throws Exception {
        VmUtils vmUtils = new VmUtils();
        VirtualMachineConfigSpec vmConfigSpec = createVmConfigSpec(connectionResources, vmInputs);
        vmConfigSpec = vmUtils.getPopulatedVmConfigSpec(vmConfigSpec, vmInputs);

        return vmConfigSpec;
    }

    public VirtualDeviceConfigSpec getDiskDeviceConfigSpec(ConnectionResources connectionResources,
                                                           ManagedObjectReference vmMor,
                                                           VmInputs vmInputs) throws Exception {
        VmUtils vmUtils = new VmUtils();
        if (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation())) {
            String dataStoreName = getDataStoreWithFreeSpaceNeeded(connectionResources, vmMor,
                    vmInputs.getLongVmDiskSize());
            String volumeName = InputUtils.getDiskFileNameString(dataStoreName, vmInputs.getVirtualMachineName(),
                    vmInputs.getUpdateValue());

            int controllerKey = 0;
            int unitNumber = 0;
            List<Integer> getControllerKeysArray = getControllerKey(connectionResources, vmMor);
            if (!getControllerKeysArray.isEmpty()) {
                controllerKey = getControllerKeysArray.get(0);
                unitNumber = getControllerKeysArray.get(1);
            }

            return vmUtils.getPopulatedDiskSpec(volumeName, null, VirtualDeviceConfigSpecOperation.ADD,
                    VirtualDeviceConfigSpecFileOperation.CREATE, controllerKey, unitNumber, SERVER_ASSIGNED,
                    Operation.ADD.toString(), vmInputs);

        } else {
            List<VirtualDevice> deviceList = ((ArrayOfVirtualDevice) connectionResources.getGetMOREF()
                    .entityProps(vmMor, new String[]{CONFIG_HARDWARE_DEVICE}).get(CONFIG_HARDWARE_DEVICE))
                    .getVirtualDevice();

            return vmUtils.getPopulatedDiskSpec(Constants.EMPTY, deviceList, VirtualDeviceConfigSpecOperation.REMOVE,
                    VirtualDeviceConfigSpecFileOperation.DESTROY, DEFAULT_DISK_CONTROLLER_KEY, DEFAULT_DISK_UNIT_NUMBER,
                    SERVER_ASSIGNED, Operation.REMOVE.toString(), vmInputs);
        }
    }

    public VirtualDeviceConfigSpec getCDDeviceConfigSpec(ConnectionResources connectionResources,
                                                         ManagedObjectReference vmMor,
                                                         VmInputs vmInputs) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        List<VirtualDevice> virtualDevicesList = ((ArrayOfVirtualDevice) connectionResources.getGetMOREF()
                .entityProps(vmMor, new String[]{CONFIG_HARDWARE_DEVICE}).get(CONFIG_HARDWARE_DEVICE)).getVirtualDevice();
        VmUtils vmUtils = new VmUtils();

        if (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation())) {
            Map<Integer, VirtualDevice> deviceMap = getVirtualDeviceMap(virtualDevicesList);

            for (VirtualDevice virtualDevice : virtualDevicesList) {
                if (virtualDevice instanceof VirtualIDEController) {
                    VirtualIDEController virtualScsiController = (VirtualIDEController) virtualDevice;
                    List<Integer> deviceList = virtualScsiController.getDevice();
                    int[] slots = new int[MAXIMUM_ATAPI_SLOTS];
                    for (Integer deviceKey : deviceList) {
                        if (deviceMap.get(deviceKey).getUnitNumber() != null) {
                            slots[deviceMap.get(deviceKey).getUnitNumber()] = OCCUPIED;
                        }
                    }

                    int unitNumber = 0;
                    boolean isAtapiCtrlAvailable = false;
                    int controllerKey = 0;
                    for (int counter = 0; counter < slots.length; counter++) {
                        if (slots[counter] != OCCUPIED) {
                            controllerKey = virtualScsiController.getKey();
                            unitNumber = counter;
                            isAtapiCtrlAvailable = true;
                            break;
                        }
                    }
                    if (isAtapiCtrlAvailable) {
                        return vmUtils.getPopulatedCDDeviceConfigSpec(Constants.EMPTY, null, null,
                                VirtualDeviceConfigSpecOperation.ADD, controllerKey, unitNumber, SERVER_ASSIGNED,
                                Operation.ADD.toString(), vmInputs);
                    }
                }
            }
            throw new RuntimeException(ErrorMessages.ATAPI_CONTROLLER_CAPACITY_MAXED_OUT);
        } else {
            return vmUtils.getPopulatedCDDeviceConfigSpec(null, null, virtualDevicesList,
                    VirtualDeviceConfigSpecOperation.REMOVE, null, null, null, Operation.REMOVE.toString(), vmInputs);
        }
    }

    public VirtualDeviceConfigSpec getNICDeviceConfigSpec(ConnectionResources connectionResources,
                                                          ManagedObjectReference vmMor, VmInputs vmInputs)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        VmUtils vmUtils = new VmUtils();

        if (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation())) {
            return vmUtils.getNicSpecs(vmInputs.getUpdateValue(), null, VirtualDeviceConfigSpecOperation.ADD,
                    GENERATED, SERVER_ASSIGNED, Operation.ADD.toString(), vmInputs);
        } else {
            List<VirtualDevice> virtualDevicesList = ((ArrayOfVirtualDevice) connectionResources.getGetMOREF()
                    .entityProps(vmMor, new String[]{CONFIG_HARDWARE_DEVICE}).get(CONFIG_HARDWARE_DEVICE))
                    .getVirtualDevice();

            return vmUtils.getNicSpecs(Constants.EMPTY, virtualDevicesList, VirtualDeviceConfigSpecOperation.REMOVE,
                    Constants.EMPTY, null, Operation.REMOVE.toString(), vmInputs);
        }
    }

    private VirtualMachineConfigSpec createVmConfigSpec(ConnectionResources connectionResources, VmInputs vmInputs)
            throws Exception {
        ConfigTarget configTarget = getHostConfigTarget(connectionResources);
        List<VirtualMachineDatastoreInfo> dataStoresList = configTarget.getDatastore();
        String dataStoreName = vmInputs.getDataStore();

        ManagedObjectReference dataStoreRef = null;

        if (StringUtils.isNotBlank(dataStoreName)) {
            dataStoreRef = getDataStoreRef(dataStoreName, dataStoresList);
        } else {
            boolean isDsAvailable = false;
            for (VirtualMachineDatastoreInfo dataStore : dataStoresList) {
                DatastoreSummary dsSummary = dataStore.getDatastore();
                if (dsSummary.isAccessible()) {
                    dataStoreName = dsSummary.getName();
                    dataStoreRef = dsSummary.getDatastore();
                    isDsAvailable = true;
                    break;
                }
            }
            if (!isDsAvailable) {
                throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND_ON_HOST);
            }
        }

        String volumeName = getVolumeName(dataStoreName);
        VmUtils vmUtils = new VmUtils();

        VirtualDeviceConfigSpec diskSpec = vmUtils.getPopulatedDiskSpec(volumeName, null,
                VirtualDeviceConfigSpecOperation.ADD, VirtualDeviceConfigSpecFileOperation.CREATE,
                DEFAULT_DISK_CONTROLLER_KEY, DEFAULT_DISK_UNIT_NUMBER, DEFAULT_DISK_KEY, Operation.CREATE.toString(), vmInputs);

        VirtualDevice ideController = getFirstFreeIdeController(connectionResources);
        VirtualDeviceConfigSpec cdSpec = new VirtualDeviceConfigSpec();
        if (ideController != null) {
            cdSpec = vmUtils.getPopulatedCDDeviceConfigSpec(volumeName, dataStoreRef, null,
                    VirtualDeviceConfigSpecOperation.ADD, ideController.getKey(), DEFAULT_CD_ROM_UNIT_NUMBER,
                    DEFAULT_CD_ROM_KEY, Operation.ADD.toString(), vmInputs);
        }

        String networkName = getNetworkName(configTarget); // The network name must be set as the device name to create the NIC.
        VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
        if (configTarget.getNetwork() != null) {
            nicSpec = vmUtils.getNicSpecs(networkName, null, VirtualDeviceConfigSpecOperation.ADD, GENERATED,
                    DEFAULT_NIC_KEY, Operation.ADD.toString(), vmInputs);
        }

        VirtualMachineConfigSpec configSpec = getVirtualMachineConfigSpec(dataStoreName);
        VirtualDeviceConfigSpec scsiCtrlSpec = getFirstScsiController(DEFAULT_DISK_CONTROLLER_KEY);
        VirtualDeviceConfigSpec floppySpec = createFloppyConfigSpecs();

        return addDeviceConfigSpecs(configSpec, scsiCtrlSpec, floppySpec, cdSpec, ideController, diskSpec, nicSpec);
    }


    private ConfigTarget getHostConfigTarget(ConnectionResources connectionResources)
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

    private ManagedObjectReference getDataStoreRef(String dataStoreName, List<VirtualMachineDatastoreInfo> dataStoresList) {
        for (VirtualMachineDatastoreInfo dataStore : dataStoresList) {
            DatastoreSummary dsSummary = dataStore.getDatastore();
            if (dataStoreName.equals(dsSummary.getName())) {
                if (!dsSummary.isAccessible()) {
                    throw new RuntimeException(ErrorMessages.SPECIFIED_DATA_STORE_NOT_ACCESSIBLE);
                }
                return dsSummary.getDatastore();
            }
        }
        throw new RuntimeException(ErrorMessages.SPECIFIED_DATA_STORE_IS_NOT_FOUND);
    }

    private VirtualMachineConfigSpec getVirtualMachineConfigSpec(String dataStoreName) {
        VirtualMachineFileInfo virtualMachineFileInfo = new VirtualMachineFileInfo();
        virtualMachineFileInfo.setVmPathName(getVolumeName(dataStoreName));

        VirtualMachineConfigSpec configSpec = new VirtualMachineConfigSpec();
        configSpec.setFiles(virtualMachineFileInfo);

        return configSpec;
    }

    private String getVolumeName(String name) {
        return StringUtils.isBlank(name) ? DEFAULT_VOLUME_NAME : LEFT_SQUARE_BRACKET + name + Constants.RIGHT_SQUARE_BRACKET;
    }

    private VirtualDeviceConfigSpec getFirstScsiController(int diskCtrlKey) {
        VirtualLsiLogicController scsiCtrl = new VirtualLsiLogicController();
        scsiCtrl.setBusNumber(DEFAULT_CONTROLLER_BUS_NUMBER);
        scsiCtrl.setKey(diskCtrlKey);
        scsiCtrl.setSharedBus(VirtualSCSISharing.NO_SHARING);

        VirtualDeviceConfigSpec scsiCtrlSpec = new VirtualDeviceConfigSpec();
        scsiCtrlSpec.setOperation(VirtualDeviceConfigSpecOperation.ADD);
        scsiCtrlSpec.setDevice(scsiCtrl);

        return scsiCtrlSpec;
    }

    private VirtualDevice getFirstFreeIdeController(ConnectionResources connectionResources)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        List<VirtualDevice> defaultDevices = getDefaultDevicesList(connectionResources);
        for (VirtualDevice device : defaultDevices) {
            if (device instanceof VirtualIDEController) {
                return device;
            }
        }

        return null;
    }

    private VirtualDeviceConfigSpec createFloppyConfigSpecs() {
        VirtualFloppyDeviceBackingInfo flpBacking = new VirtualFloppyDeviceBackingInfo();
        flpBacking.setDeviceName(DEFAULT_FLOPPY_DEVICE_NAME);

        VirtualFloppy floppyDisk = new VirtualFloppy();
        floppyDisk.setBacking(flpBacking);
        floppyDisk.setKey(DEFAULT_FLOPPY_DEVICE_KEY);

        VirtualDeviceConfigSpec flpSpecs = new VirtualDeviceConfigSpec();
        flpSpecs.setOperation(VirtualDeviceConfigSpecOperation.ADD);
        flpSpecs.setDevice(floppyDisk);

        return flpSpecs;
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

    private String getNetworkName(ConfigTarget configTarget) {
        if (configTarget.getNetwork() != null) {
            for (VirtualMachineNetworkInfo network : configTarget.getNetwork()) {
                NetworkSummary netSummary = network.getNetwork();
                if (netSummary.isAccessible()) {
                    return netSummary.getName();
                }
            }
        }
        return null;
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

    private String getDataStoreWithFreeSpaceNeeded(ConnectionResources connectionResources, ManagedObjectReference vmMor,
                                                   long minFreeSpace) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<ManagedObjectReference> dataStores = ((ArrayOfManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(vmMor, new String[]{DATASTORE}).get(DATASTORE)).getManagedObjectReference();

        for (ManagedObjectReference dataStore : dataStores) {
            DatastoreSummary datastoreSummary = (DatastoreSummary) connectionResources.getGetMOREF()
                    .entityProps(dataStore, new String[]{Constants.SUMMARY}).get(Constants.SUMMARY);
            if (datastoreSummary.getFreeSpace() > minFreeSpace) {
                return datastoreSummary.getName();
            }
        }
        throw new RuntimeException("Can find any dataStore with: [" + minFreeSpace + "] minimum amount of space available.");
    }

    private List<Integer> getControllerKey(ConnectionResources connectionResources, ManagedObjectReference vmMor)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<VirtualDevice> virtualDevicesList = ((ArrayOfVirtualDevice) connectionResources.getGetMOREF()
                .entityProps(vmMor, new String[]{CONFIG_HARDWARE_DEVICE}).get(CONFIG_HARDWARE_DEVICE))
                .getVirtualDevice();
        Map<Integer, VirtualDevice> deviceMap = getVirtualDeviceMap(virtualDevicesList);

        for (VirtualDevice virtualDevice : virtualDevicesList) {
            if (virtualDevice instanceof VirtualSCSIController) {
                VirtualSCSIController scsiController = (VirtualSCSIController) virtualDevice;
                int[] slots = new int[MAXIMUM_SCSI_SLOTS];
                slots[RESERVED_SCSI_SLOT] = OCCUPIED;
                List<Integer> deviceKeyList = scsiController.getDevice();
                for (Integer deviceKey : deviceKeyList) {
                    if (deviceMap.get(deviceKey).getUnitNumber() != null) {
                        slots[deviceMap.get(deviceKey).getUnitNumber()] = OCCUPIED;
                    }
                }
                for (int counter = 0; counter < slots.length; counter++) {
                    if (slots[counter] != OCCUPIED) {
                        List<Integer> controllerKeys = new ArrayList<>();
                        controllerKeys.add(scsiController.getKey());
                        controllerKeys.add(counter);
                        return controllerKeys;
                    }
                }
            }
        }
        throw new RuntimeException(ErrorMessages.SCSI_CONTROLLER_CAPACITY_MAXED_OUT);
    }

    private Map<Integer, VirtualDevice> getVirtualDeviceMap(List<VirtualDevice> virtualDevicesList) {
        Map<Integer, VirtualDevice> deviceMap = new HashMap<>();
        for (VirtualDevice virtualDevice : virtualDevicesList) {
            deviceMap.put(virtualDevice.getKey(), virtualDevice);
        }
        return deviceMap;
    }
}