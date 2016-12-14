/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
