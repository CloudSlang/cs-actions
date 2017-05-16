/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.inputs;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidInternalNodeIpAddress;

/**
 * Created by TusaM
 * 5/8/2017.
 */
public class NodeInputs {
    private final String internalNodeIpAddress;

    private NodeInputs(NodeInputs.Builder builder) {
        this.internalNodeIpAddress = builder.internalNodeIpAddress;
    }

    public String getInternalNodeIpAddress() {
        return internalNodeIpAddress;
    }

    public static class Builder {
        private String internalNodeIpAddress;

        public NodeInputs build() {
            return new NodeInputs(this);
        }

        public NodeInputs.Builder withInternalNodeIpAddress(String inputValue) {
            internalNodeIpAddress = getValidInternalNodeIpAddress(inputValue);
            return this;
        }
    }
}