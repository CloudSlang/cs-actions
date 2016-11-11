package io.cloudslang.content.vmware.entities;

/**
 * Created by giloan on 8/31/2016.
 */
public enum ClusterParameter {
    COMPUTE_RESOURCE("ComputeResource"),
    CLUSTER_COMPUTE_RESOURCE("ClusterComputeResource"),
    CLUSTER_PROFILE("ClusterProfile"),
    CLUSTER_PROFILE_MANAGER("ClusterProfileManager"),
    CONFIGURATION_EX("configurationEx");

    final private String parameter;

    /**
     * Instantiates a VM parameter.
     *
     * @param input the parameter
     */
    ClusterParameter(String input) {
        this.parameter = input;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public String getValue() {
        return parameter;
    }
}
