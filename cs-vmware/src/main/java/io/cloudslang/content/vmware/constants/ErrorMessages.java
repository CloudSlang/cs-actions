package io.cloudslang.content.vmware.constants;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class ErrorMessages {
    public static final String DATA_STORE_NOT_FOUND = "The specified Datastore was not found.";
    public static final String DATA_STORE_NOT_ACCESSIBLE = "The specified Datastore is not accessible.";
    public static final String DATA_STORE_NOT_FOUND_ON_HOST = "The Datastore was not found on specified host.";
    public static final String DATA_STORE_NOT_FOUND_IN_COMPUTE_RESOURCE = "The specified Datastore can not be found " +
            "in ComputeResource.";

    public static final String FOLDER_NOT_FOUND = "Cannot find the specified folder.";
    public static final String RESOURCE_POOL_NOT_FOUND = "Cannot find the specified folder.";
    public static final String HOST_NOT_FOUND = "Cannot find the specified hostr.";

    public static final String CONFIG_TARGET_NOT_FOUND_IN_COMPUTE_RESOURCE = "The ConfigTarget was not found " +
            "in ComputeResource.";

    public static final String VIRTUAL_HARDWARE_INFO_NOT_FOUND_IN_COMPUTE_RESOURCE = "The VirtualHardwareInfo was " +
            "not found in ComputeResource.";

    public static final String COMPUTE_RESOURCE_NOT_FOUND_ON_HOST = "The ComputeResource can not be found on " +
            "specified host";

    public static final String CPU_OR_MEMORY_INVALID_OPERATION = "Unsupported operation specified for CPU or memory " +
            "device. The CPU or memory can only be updated.";

    public static final String INVALID_VM_DISK_SIZE = "The disk size must be positive long.";

    public static final String SCSI_CONTROLLER_CAPACITY_MAXED_OUT = "The SCSI controller on the vm has maxed out its " +
            "capacity. Please add an additional SCSI controller";

    public static final String ATAPI_CONTROLLER_CAPACITY_MAXED_OUT = "The IDE controller on the vm has maxed out its " +
            "capacity. Please add an additional IDE controller";

    public static final String NOT_BYTE = "The input value must be a positive number between 0 and 127 values range.";
    public static final String NOT_ZERO_OR_POSITIVE_NUMBER = "The input value must be 0 or positive number.";

    public static final String DOMAIN_AND_WORKGROUP_BOTH_PRESENT = "The domain and workgroup are mutually exclusive. " +
            "If the workgroup value is supplied, then the domain name and authentication fields must be empty.";
    public static final String PROVIDE_AFFINE_OR_ANTI_AFFINE_HOST_GROUP = "One of the affineHostGroupName and antiAffineHostGroupName inputs must not be empty, and the other one must be empty.";
    public static final String RULE_ALREADY_EXISTS = "Rule with name %s already exists.";
    public static final String AFFINE_HOST_GROUP_DOES_NOT_EXIST = "The affineHostGroup provided does not exist.";
    public static final String ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST = "The antiAffineHostGroup provided does not exist.";
    public static final String VM_GROUP_DOES_NOT_EXIST = "The vmGroup provided does not exist.";
    public static final String SUCCESS_MSG = "Success: The [%s] cluster was successfully reconfigured. The taskId is: %s";
    public static final String FAILURE_MSG = "Failure: The [%s] cluster could not be reconfigured.";
    public static final String ANOTHER_FAILURE_MSG = "Could not retrieve the configurations for: [%s] cluster.";
    public static final String CLUSTER_RULE_COULD_NOT_BE_FOUND = "Cluster rule with the name %s could not be found.";
}
