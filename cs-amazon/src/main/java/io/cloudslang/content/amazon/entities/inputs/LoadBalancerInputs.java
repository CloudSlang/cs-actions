/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Scheme;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancerInputs {
    private final String arnsString;
    private final String loadBalancerArn;
    private final String loadBalancerName;
    private final String marker;
    private final String memberNamesString;
    private final String pageSize;
    private final String scheme;

    public String getArnsString() {
        return arnsString;
    }

    public String getLoadBalancerArn() {
        return loadBalancerArn;
    }

    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    public String getMarker() {
        return marker;
    }

    public String getMemberNamesString() {
        return memberNamesString;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getScheme() {
        return scheme;
    }

    private LoadBalancerInputs(LoadBalancerInputs.Builder builder) {
        this.arnsString = builder.arnsString;
        this.loadBalancerArn = builder.loadBalancerArn;
        this.loadBalancerName = builder.loadBalancerName;
        this.marker = builder.marker;
        this.memberNamesString = builder.memberNamesString;
        this.pageSize = builder.pageSize;
        this.scheme = builder.scheme;
    }

    public static class Builder {
        private String arnsString;
        private String loadBalancerArn;
        private String loadBalancerName;
        private String marker;
        private String memberNamesString;
        private String pageSize;
        private String scheme;

        public LoadBalancerInputs build() {
            return new LoadBalancerInputs(this);
        }

        public LoadBalancerInputs.Builder withArnsString(String inputValue) {
            arnsString = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withLoadBalancerArn(String inputValue) {
            loadBalancerArn = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withLoadBalancerName(String inputValue) {
            loadBalancerName = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withMarker(String inputValue) {
            marker = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withMemberNamesString(String inputValue) {
            memberNamesString = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withPageSize(String inputValue) {
            pageSize = inputValue;
            return this;
        }

        public LoadBalancerInputs.Builder withScheme(String inputValue) {
            scheme = Scheme.getValue(inputValue);
            return this;
        }
    }
}
