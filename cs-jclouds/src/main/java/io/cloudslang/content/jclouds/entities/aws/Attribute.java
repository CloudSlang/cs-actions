package io.cloudslang.content.jclouds.entities.aws;

/**
 * Created by TusaM
 * 10/19/2016.
 */
public enum Attribute {
    INSTANCE_TYPE("instanceType"),
    KERNEL("kernel"),
    RAMDISK("ramdisk"),
    USER_DATA("userData"),
    DISABLE_API_TERMINATION("disableApiTermination"),
    INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR("instanceInitiatedShutdownBehavior"),
    ROOT_DEVICE_NAME("rootDeviceName"),
    BLOCK_DEVICE_MAPPING("blockDeviceMapping"),
    PRODUCT_CODES("productCodes"),
    SOURCE_DEST_CHECK("sourceDestCheck"),
    GROUP_SET("groupSet"),
    EBS_OPTIMIZED("ebsOptimized"),
    SRIOV_NET_SUPPORT("sriovNetSupport"),
    ENA_SUPPORT("enaSupport");

    private final String value;

    Attribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getAttribute(String input) {
        for (InstanceType type : InstanceType.values()) {
            if (type.getValue().equalsIgnoreCase(input)) {
                return type.getValue();
            }
        }

        throw new IllegalArgumentException("Invalid attribute value: [" + input + "]. Valid values: instanceType | kernel | " +
                "ramdisk | userData | disableApiTermination | instanceInitiatedShutdownBehavior | rootDeviceName | " +
                "blockDeviceMapping | productCodes | sourceDestCheck | groupSet | ebsOptimized | sriovNetSupport | enaSupport");
    }
}