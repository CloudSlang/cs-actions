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

/**
 * Created by TusaM
 * 5/11/2017.
 */
public class ClusterInputs {
    private final String ejectedNodes;
    private final String knownNodes;

    private ClusterInputs(ClusterInputs.Builder builder) {
        this.ejectedNodes = builder.ejectedNodes;
        this.knownNodes = builder.knownNodes;
    }

    public String getEjectedNodes() {
        return ejectedNodes;
    }

    public String getKnownNodes() {
        return knownNodes;
    }

    public static class Builder {
        private String ejectedNodes;
        private String knownNodes;

        public ClusterInputs build() {
            return new ClusterInputs(this);
        }

        public ClusterInputs.Builder withEjectedNodes(String inputValue) {
            ejectedNodes = inputValue;
            return this;
        }

        public ClusterInputs.Builder withKnownNodes(String inputValue) {
            knownNodes = inputValue;
            return this;
        }
    }
}