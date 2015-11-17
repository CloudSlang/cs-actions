package io.cloudslang.content.constants;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class ErrorMessages {

    // Inputs validation errors
    public static final String NOT_INTEGER = "The input value must be int number.";
    public static final String NOT_LONG = "The input value must be long number.";
    public static final String UNSUPPORTED_PROTOCOL = "Unsupported protocol.";

    // Configurations errors
    public static final String SPECIFIED_DATA_STORE_NOT_ACCESSIBLE = "The specified Datastore is not accessible.";
    public static final String SPECIFIED_DATA_STORE_IS_NOT_FOUND = "The specified Datastore was not found.";
    public static final String DATA_STORE_NOT_FOUND_ON_HOST = "The Datastore was not found on specified host.";
    public static final String DATA_STORE_NOT_FOUND_IN_COMPUTE_RESOURCE = "The specified Datastore can not be found " +
            "in ComputeResource.";
    public static final String CONFIG_TARGET_NOT_FOUND_IN_COMPUTE_RESOURCE = "The ConfigTarget was not found " +
            "in ComputeResource.";
    public static final String VIRTUAL_HARDWARE_INFO_NOT_FOUND_IN_COMPUTE_RESOURCE = "The VirtualHardwareInfo was " +
            "not found in ComputeResource.";
    public static final String COMPUTE_RESOURCE_NOT_FOUND_ON_HOST = "The ComputeResource can not be found on " +
            "specified host";
}