package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.entities.DiskMode;
import io.cloudslang.content.vmware.entities.Levels;
import io.cloudslang.content.vmware.entities.Operation;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.List;

/**
 * Created by Mihai Tusa.
 * 1/27/2016.
 */
public class VmUtils {
    private static final String TEST_CD_ISO = "testcd.iso";
    private static final int DISK_AMOUNT_MULTIPLIER = 1024;

    public VirtualMachineConfigSpec getPopulatedVmConfigSpec(VirtualMachineConfigSpec vmConfigSpec, VmInputs vmInputs) {
        vmConfigSpec.setName(vmInputs.getVirtualMachineName());
        vmConfigSpec.setAnnotation(vmInputs.getDescription());
        vmConfigSpec.setMemoryMB(vmInputs.getLongVmMemorySize());
        vmConfigSpec.setNumCPUs(vmInputs.getIntNumCPUs());
        vmConfigSpec.setGuestId(vmInputs.getGuestOsId());

        return vmConfigSpec;
    }

    public VirtualDeviceConfigSpec getPopulatedDiskSpec(String volumeName, List<VirtualDevice> virtualDevicesList,
                                                        VirtualDeviceConfigSpecOperation operation,
                                                        VirtualDeviceConfigSpecFileOperation fileOperation,
                                                        int controllerKey, int unitNumber, int key, String parameter,
                                                        VmInputs vmInputs) throws Exception {
        VirtualDeviceConfigSpec diskSpecs = new VirtualDeviceConfigSpec();

        if (Operation.CREATE.toString().equalsIgnoreCase(parameter) || Operation.ADD.toString().equalsIgnoreCase(parameter)) {
            VirtualDisk disk = createVirtualDisk(volumeName, controllerKey, unitNumber, key, vmInputs, parameter);
            return getDiskSpecs(diskSpecs, operation, fileOperation, disk);
        } else {
            VirtualDisk disk = findVirtualDevice(VirtualDisk.class, virtualDevicesList, vmInputs);
            if (disk != null) {
                return getDiskSpecs(diskSpecs, operation, fileOperation, disk);
            }
        }
        throw new RuntimeException("No disk device named: [" + vmInputs.getUpdateValue() + "] can be found.");
    }

    public VirtualDeviceConfigSpec getPopulatedCDSpecs(String fileName, ManagedObjectReference dataStoreRef,
                                                       List<VirtualDevice> virtualDevicesList,
                                                       VirtualDeviceConfigSpecOperation operation,
                                                       Integer controllerKey, Integer unitNumber, Integer key,
                                                       String parameter, VmInputs vmInputs) {

        VirtualDeviceConfigSpec cdSpecs = new VirtualDeviceConfigSpec();
        if (Operation.ADD.toString().equalsIgnoreCase(parameter)) {
            VirtualCdromIsoBackingInfo cdIsoBacking = new VirtualCdromIsoBackingInfo();
            cdIsoBacking.setDatastore(dataStoreRef);
            cdIsoBacking.setFileName(fileName + TEST_CD_ISO);

            VirtualCdrom cdRom = new VirtualCdrom();
            cdRom.setBacking(cdIsoBacking);
            cdRom.setControllerKey(controllerKey);
            cdRom.setUnitNumber(unitNumber);
            cdRom.setKey(key);

            return getCDSpec(cdSpecs, operation, cdRom);
        } else {
            VirtualCdrom cdRemove = findVirtualDevice(VirtualCdrom.class, virtualDevicesList, vmInputs);
            if (cdRemove != null) {
                return getCDSpec(cdSpecs, operation, cdRemove);
            }
        }
        throw new RuntimeException("No optical device named: [" + vmInputs.getUpdateValue() + "] can be found.");
    }

    public VirtualDeviceConfigSpec getNicSpecs(String fileName, List<VirtualDevice> virtualDevicesList,
                                               VirtualDeviceConfigSpecOperation operation, String addressType,
                                               Integer key, String parameter, VmInputs vmInputs) {
        VirtualDeviceConfigSpec nicSpecs = new VirtualDeviceConfigSpec();
        VirtualEthernetCard nic;
        if (Operation.ADD.toString().equalsIgnoreCase(parameter)) {
            nic = getEth(fileName, addressType, key);
            return getNicOpSpec(nicSpecs, operation, nic);
        } else {
            nic = findVirtualDevice(VirtualEthernetCard.class, virtualDevicesList, vmInputs);
            if (nic != null) {
                return getNicOpSpec(nicSpecs, operation, nic);
            }
        }
        throw new RuntimeException("No nic named: [" + vmInputs.getUpdateValue() + "] can be found.");
    }

    public <T> SharesInfo getSharesInfo(T value) throws Exception {
        SharesInfo sharesInfo = new SharesInfo();
        if (InputUtils.isInt((String) value)) {
            sharesInfo.setLevel(SharesLevel.CUSTOM);
            sharesInfo.setShares(Integer.parseInt((String) value));
        } else {
            setSharesInfoLevel((String) value, sharesInfo);
        }

        return sharesInfo;
    }

    private void setSharesInfoLevel(String value, SharesInfo sharesInfo) throws Exception {
        String level = Levels.getValue(value);
        if (SharesLevel.HIGH.toString().equalsIgnoreCase(level)) {
            sharesInfo.setLevel(SharesLevel.HIGH);
        } else if (SharesLevel.NORMAL.toString().equalsIgnoreCase(level)) {
            sharesInfo.setLevel(SharesLevel.NORMAL);
        } else {
            sharesInfo.setLevel(SharesLevel.LOW);
        }
    }

    private VirtualDisk createVirtualDisk(String volumeName, int controllerKey, int unitNumber, int key, VmInputs vmInputs,
                                          String parameter) throws Exception {
        VirtualDiskFlatVer2BackingInfo diskFileBacking = new VirtualDiskFlatVer2BackingInfo();
        diskFileBacking.setFileName(volumeName);
        if (Operation.CREATE.toString().equalsIgnoreCase(parameter)) {
            diskFileBacking.setDiskMode(DiskMode.PERSISTENT.toString());
        } else {
            diskFileBacking.setDiskMode(DiskMode.getValue(vmInputs.getVmDiskMode()));
        }

        VirtualDisk disk = new VirtualDisk();
        disk.setBacking(diskFileBacking);
        disk.setControllerKey(controllerKey);
        disk.setUnitNumber(unitNumber);
        disk.setKey(key);
        disk.setCapacityInKB(vmInputs.getLongVmDiskSize() * DISK_AMOUNT_MULTIPLIER);

        return disk;
    }

    private VirtualDeviceConfigSpec getDiskSpecs(VirtualDeviceConfigSpec diskSpecs, VirtualDeviceConfigSpecOperation operation,
                                                 VirtualDeviceConfigSpecFileOperation fileOperation, VirtualDisk disk) {
        diskSpecs.setOperation(operation);
        diskSpecs.setFileOperation(fileOperation);
        diskSpecs.setDevice(disk);

        return diskSpecs;
    }

    private VirtualDeviceConfigSpec getCDSpec(VirtualDeviceConfigSpec cdSpecs, VirtualDeviceConfigSpecOperation operation,
                                              VirtualCdrom cdRom) {
        cdSpecs.setOperation(operation);
        cdSpecs.setDevice(cdRom);

        return cdSpecs;
    }

    private VirtualEthernetCard getEth(String fileName, String addressType, Integer key) {
        VirtualEthernetCardNetworkBackingInfo nicBacking = new VirtualEthernetCardNetworkBackingInfo();
        nicBacking.setDeviceName(fileName);

        VirtualEthernetCard nic = new VirtualPCNet32();
        nic.setBacking(nicBacking);
        nic.setAddressType(addressType);
        nic.setKey(key);

        return nic;
    }

    private VirtualDeviceConfigSpec getNicOpSpec(VirtualDeviceConfigSpec nicSpecs, VirtualDeviceConfigSpecOperation operation,
                                                 VirtualEthernetCard nic) {
        nicSpecs.setOperation(operation);
        nicSpecs.setDevice(nic);

        return nicSpecs;
    }

    private <T extends VirtualDevice> T findVirtualDevice(Class<T> type, List<VirtualDevice> virtualDevicesList,
                                                          VmInputs vmInputs) {
        for (VirtualDevice device : virtualDevicesList) {
            if (type.isAssignableFrom(device.getClass())) {
                if (vmInputs.getUpdateValue().equalsIgnoreCase(device.getDeviceInfo().getLabel())) {
                    return (T) device;
                }
            }
        }
        return null;
    }
}