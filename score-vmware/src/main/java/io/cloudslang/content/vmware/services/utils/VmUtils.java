package io.cloudslang.content.vmware.services.utils;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.*;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/27/2016.
 */
public class VmUtils {
    private static final String TEST_CD_ISO = "testcd.iso";
    private static final String DISK = "disk";
    private static final String CD = "cd";
    private static final String NIC = "nic";

    private static final int DEFAULT_CORES_PER_SOCKET = 1;
    private static final int DISK_AMOUNT_MULTIPLIER = 1024;

    public VirtualMachineConfigSpec getPopulatedVmConfigSpec(VirtualMachineConfigSpec vmConfigSpec, VmInputs vmInputs, String name) {
        vmConfigSpec.setName(name);
        vmConfigSpec.setNumCPUs(vmInputs.getIntNumCPUs());
        vmConfigSpec.setMemoryMB(vmInputs.getLongVmMemorySize());
        vmConfigSpec.setAnnotation(vmInputs.getDescription());

        if (vmInputs.getCoresPerSocket() != null) {
            vmConfigSpec.setNumCoresPerSocket(InputUtils.getIntInput(vmInputs.getCoresPerSocket(), DEFAULT_CORES_PER_SOCKET));
        }

        if (vmInputs.getGuestOsId() != null) {
            vmConfigSpec.setGuestId(vmInputs.getGuestOsId());
        }

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

    public ManagedObjectReference getMorDataStore(String dataStoreName, ConnectionResources connectionResources,
                                           ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference dataStore = null;
        if (StringUtils.isNotBlank(dataStoreName)) {
            dataStore = getDataStore(dataStoreName, connectionResources, vmMor);
            if (dataStore == null) {
                throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND);
            }
        } else {
            ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                    new String[]{VmParameter.SUMMARY.getValue()});

            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    String vmPathName = virtualMachineSummary.getConfig().getVmPathName();
                    dataStoreName = vmPathName.substring(1, vmPathName.indexOf(Constants.RIGHT_SQUARE_BRACKET));
                    dataStore = getDataStore(dataStoreName, connectionResources, vmMor);
                    break;
                }
                break;
            }
        }
        return dataStore;
    }

    public ManagedObjectReference getMorHost(String hostname, ConnectionResources connectionResources,
                                      ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference host = null;
        if (StringUtils.isNotBlank(hostname)) {
            Map<String, ManagedObjectReference> hostsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), VmParameter.HOST_SYSTEM.getValue(), new RetrieveOptions());
            host = getMorObject(hostsMap, hostname);
            if (host == null) {
                throw new RuntimeException(ErrorMessages.HOST_NOT_FOUND);
            }
        } else {
            ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                    new String[]{VmParameter.SUMMARY.getValue()});

            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    host = virtualMachineSummary.getRuntime().getHost();
                    break;
                }
                break;
            }
        }
        return host;
    }

    public ManagedObjectReference getMorResourcePool(String resourcePoolName, ConnectionResources connectionResources)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference resourcePool;
        if (StringUtils.isNotBlank(resourcePoolName)) {
            Map<String, ManagedObjectReference> poolsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), VmParameter.RESOURCE_POOL.getValue(),
                            new RetrieveOptions());
            resourcePool = getMorObject(poolsMap, resourcePoolName);
            if (resourcePool == null) {
                throw new RuntimeException(ErrorMessages.RESOURCE_POOL_NOT_FOUND);
            }
        } else {
            Map<String, ManagedObjectReference> poolsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), VmParameter.RESOURCE_POOL.getValue(),
                            new RetrieveOptions());
            resourcePool = getMorObject(poolsMap, VmParameter.RESOURCES.getValue());
        }
        return resourcePool;
    }

    public ManagedObjectReference getMorFolder(String folderName, ConnectionResources connectionResources)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference folder;
        if (StringUtils.isNotBlank(folderName)) {
            Map<String, ManagedObjectReference> foldersMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), VmParameter.FOLDER.getValue(),
                            new RetrieveOptions());
            folder = getMorObject(foldersMap, folderName);
            if (folder == null) {
                throw new RuntimeException(ErrorMessages.FOLDER_NOT_FOUND);
            }
        } else {
            folder = connectionResources.getVmFolderMor();
        }
        return folder;
    }

    private ManagedObjectReference getDataStore(String dataStoreName, ConnectionResources connectionResources,
                                                ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<ManagedObjectReference> dataStores = ((ArrayOfManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(vmMor, new String[]{VmParameter.DATA_STORE.getValue()})
                .get(VmParameter.DATA_STORE.getValue()))
                .getManagedObjectReference();

        for (ManagedObjectReference dataStore : dataStores) {
            DatastoreSummary datastoreSummary = (DatastoreSummary) connectionResources.getGetMOREF()
                    .entityProps(dataStore, new String[]{VmParameter.SUMMARY.getValue()}).get(VmParameter.SUMMARY.getValue());
            if (dataStoreName.equalsIgnoreCase(datastoreSummary.getName())) {
                return dataStore;
            }
        }
        return null;
    }

    public VirtualMachineRelocateSpec getVirtualMachineRelocateSpec(ManagedObjectReference resourcePool,
                                                                     ManagedObjectReference host,
                                                                     ManagedObjectReference dataStore,
                                                                     VmInputs vmInputs) {
        VirtualMachineRelocateSpec vmRelocateSpec = new VirtualMachineRelocateSpec();
        vmRelocateSpec.setPool(resourcePool);
        vmRelocateSpec.setHost(host);
        vmRelocateSpec.setDatastore(dataStore);

        if (vmInputs.isThickProvision()) {
            vmRelocateSpec.setTransform(VirtualMachineRelocateTransformation.FLAT);
        } else {
            vmRelocateSpec.setTransform(VirtualMachineRelocateTransformation.SPARSE);
        }

        return vmRelocateSpec;
    }

    public VirtualMachineConfigSpec getUpdateConfigSpec(VmInputs vmInputs, VirtualMachineConfigSpec vmConfigSpec,
                                                         String device) throws Exception {
        if (!InputUtils.isUpdateOperation(vmInputs)) {
            throw new RuntimeException(ErrorMessages.CPU_OR_MEMORY_INVALID_OPERATION);
        }
        VmConfigSpecs specs = new VmConfigSpecs();
        ResourceAllocationInfo resourceAllocationInfo = specs.getResourceAllocationInfo(vmInputs.getUpdateValue());
        if (Constants.CPU.equalsIgnoreCase(device)) {
            vmConfigSpec.setCpuAllocation(resourceAllocationInfo);
        } else {
            vmConfigSpec.setMemoryAllocation(resourceAllocationInfo);
        }

        return vmConfigSpec;
    }

    public VirtualMachineConfigSpec getAddOrRemoveSpecs(ConnectionResources connectionResources, ManagedObjectReference vmMor,
                                                         VmInputs vmInputs, VirtualMachineConfigSpec vmConfigSpec, String device)
            throws Exception {
        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();
        switch (device) {
            case DISK:
                InputUtils.checkValidOperation(vmInputs, device);
                InputUtils.validateDiskInputs(vmInputs);
                deviceConfigSpec = vmConfigSpecs.getDiskDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case CD:
                InputUtils.checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getCDDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case NIC:
                InputUtils.checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getNICDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
        }
        List<VirtualDeviceConfigSpec> specs = new ArrayList<>();
        specs.add(deviceConfigSpec);
        vmConfigSpec.getDeviceChange().addAll(specs);

        return vmConfigSpec;
    }

    public ManagedObjectReference getMorObject(Map<String, ManagedObjectReference> morMap, String input) {
        for (String key : morMap.keySet()) {
            if (input.equalsIgnoreCase(key)) {
                return morMap.get(key);
            }
        }
        return null;
    }

    private void setSharesInfoLevel(String value, SharesInfo sharesInfo) throws Exception {
        String level = Level.getValue(value);
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