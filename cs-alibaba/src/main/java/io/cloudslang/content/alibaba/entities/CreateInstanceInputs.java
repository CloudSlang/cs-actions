/*
 * (c) Copyright 2018 Micro Focus, L.P.
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

package io.cloudslang.content.alibaba.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateInstanceInputs {

    private  final String regionId;
    private  final String accessKeyId;
    private  final String accessKeySecret;
    private  final String imageId;
    private  final String instanceName;
    private  final String securityGroupId;
    private  final String instanceType;

    private  final String proxyHost;
    private  final String proxyPort;
    private  final String proxyUsername;
    private  final String proxyPassword;
    private final String internetMaxBandwidthOut;

    @java.beans.ConstructorProperties({"regionId", "accessKeyId", "accessKeySecret", "imageId", "instanceName", "securityGroupId","instanceType","proxyHost", "proxyPort", "proxyUsername", "proxyPassword","internetMaxBandwidthOut"})
    private CreateInstanceInputs(final String regionId,final String accessKeyId,final String accessKeySecret,  final String imageId, final String instanceName, final String securityGroupId, final String instanceType,final String proxyHost, final String proxyPort, final String proxyUsername, final String proxyPassword, final String internetMaxBandwidthOut){
       this.regionId=regionId;
       this.accessKeyId=accessKeyId;
       this.accessKeySecret=accessKeySecret;
       this.imageId=imageId;
       this.instanceName=instanceName;
       this.securityGroupId=securityGroupId;
       this.instanceType=instanceType;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.internetMaxBandwidthOut=internetMaxBandwidthOut;
    }
    @NotNull
    public static CreateInstanceInputsBuilder builder() {
        return new CreateInstanceInputsBuilder();
    }
    @NotNull
    public String getRegionId() {
        return regionId;
    }
    @NotNull
    public String getAccessKeyId() {
        return accessKeyId;
    }
    @NotNull
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    @NotNull
    public String getImageId() {
        return imageId;
    }
    @NotNull
    public String getInstanceName() {
        return instanceName;
    }
    @NotNull
    public String getSecurityGroupId() {
        return securityGroupId;
    }
    @NotNull
    public String getInstanceType() {
        return instanceType;
    }
    @NotNull
    public String getProxyHost() {
        return proxyHost;
    }
    @NotNull
    public String getProxyPort() {
        return proxyPort;
    }
    @NotNull
    public String getProxyUsername() {
        return proxyUsername;
    }
    @NotNull
    public String getProxyPassword() {
        return proxyPassword;
    }
    @NotNull
    public String getInternetMaxBandwidthOut(){ return internetMaxBandwidthOut; }
    @NotNull
    public static class CreateInstanceInputsBuilder {


        private String regionId = EMPTY;
        private String accessKeyId = EMPTY;
        private String accessKeySecret = EMPTY;
        private String imageId = EMPTY;
        private String instanceName = EMPTY;
        private String securityGroupId = EMPTY;
        private String instanceType = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String internetMaxBandwidthOut=EMPTY;

        CreateInstanceInputsBuilder() {
        }

        @NotNull
        public CreateInstanceInputsBuilder regionId(@NotNull final String regionId) {
            this.regionId = regionId;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder accessKeyId(@NotNull final String accessKeyId) {
            this.accessKeyId = accessKeyId;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder accessKeySecret(@NotNull final String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder imageId(@NotNull final String imageId) {
            this.imageId = imageId;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder securityGroupId(@NotNull final String securityGroupId) {
            this.securityGroupId = securityGroupId;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder instanceName(@NotNull final String instanceName) {
            this.instanceName = instanceName;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder instanceType(@NotNull final String instanceType) {
            this.instanceType = instanceType;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public CreateInstanceInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }
        @NotNull
        public CreateInstanceInputsBuilder internetMaxBandwidthOut(@NotNull final String internetMaxBandwidthOut){
            this.internetMaxBandwidthOut=internetMaxBandwidthOut;
            return this;
        }
        @NotNull
        public CreateInstanceInputs build() {
            return new CreateInstanceInputs(regionId, accessKeyId, accessKeySecret, imageId, instanceName,securityGroupId,instanceType, proxyHost, proxyPort, proxyUsername, proxyPassword,internetMaxBandwidthOut);
        }

        @Override
        public String toString() {
            return String.format("CreateInstanceInputsBuilder(regionId=%s, accessKeyId=%s, accessKeySecret=%s, imageId=%s," +
                            " instanceName=%s,securityGroupId=%s,instanceType=%s, proxyHost=%s, proxyPort=%s, proxyUsername=%s, proxyPassword=%s,internetMaxBandwidthOut=%s)",
                    this.regionId, this.accessKeyId, this.accessKeySecret, this.imageId, this.instanceName,this.securityGroupId,this.instanceType, this.proxyHost,
                    this.proxyPort, this.proxyUsername, this.proxyPassword, this.internetMaxBandwidthOut);

        }
    }
}
