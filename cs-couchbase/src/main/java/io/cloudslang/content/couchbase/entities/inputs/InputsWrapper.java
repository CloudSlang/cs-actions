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

import io.cloudslang.content.httpclient.HttpClientInputs;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class InputsWrapper {
    private final HttpClientInputs httpClientInputs;
    private final CommonInputs commonInputs;

    private BucketInputs bucketInputs;
    private ClusterInputs clusterInputs;
    private NodeInputs nodeInputs;

    private InputsWrapper(Builder builder) {
        this.httpClientInputs = builder.httpClientInputs;
        this.commonInputs = builder.commonInputs;
    }

    public HttpClientInputs getHttpClientInputs() {
        return httpClientInputs;
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public BucketInputs getBucketInputs() {
        return bucketInputs;
    }

    public void setBucketInputs(BucketInputs bucketInputs) {
        this.bucketInputs = bucketInputs;
    }

    public ClusterInputs getClusterInputs() {
        return clusterInputs;
    }

    public void setClusterInputs(ClusterInputs clusterInputs) {
        this.clusterInputs = clusterInputs;
    }

    public NodeInputs getNodeInputs() {
        return nodeInputs;
    }

    public void setNodeInputs(NodeInputs nodeInputs) {
        this.nodeInputs = nodeInputs;
    }

    public static class Builder {
        private HttpClientInputs httpClientInputs;
        private CommonInputs commonInputs;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withHttpClientInputs(HttpClientInputs httpClientInputs) {
            this.httpClientInputs = httpClientInputs;
            return this;
        }

        public Builder withCommonInputs(CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }
    }
}