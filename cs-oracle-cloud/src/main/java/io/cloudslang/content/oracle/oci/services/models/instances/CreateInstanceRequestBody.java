/*
 * (c) Copyright 2020 Micro Focus, L.P.
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
package io.cloudslang.content.oracle.oci.services.models.instances;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateInstanceRequestBody {

    String compartmentId;
    AgentConfig agentConfig;
    String availabilityDomain;
    CreateVnicDetails createVnicDetails;
    String dedicatedVmHostId;
    JSONObject definedTags;
    String displayName;
    JSONObject extendedMetadata;
    String faultDomain;
    JSONObject freeformTags;
    String ipxeScript;
    boolean isPvEncryptionInTransitEnabled;
    String launchMode;
    LaunchOptions launchOptions;
    Metadata metadata;
    String shape;
    ShapeConfig shapeConfig;
    SourceDetails sourceDetails;

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }

    public AgentConfig getAgentConfig() {
        return agentConfig;
    }

    public void setAgentConfig(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    public String getAvailabilityDomain() {
        return availabilityDomain;
    }

    public void setAvailabilityDomain(String availabilityDomain) {
        this.availabilityDomain = availabilityDomain;
    }

    public CreateVnicDetails getCreateVnicDetails() {
        return createVnicDetails;
    }

    public void setCreateVnicDetails(CreateVnicDetails createVnicDetails) {
        this.createVnicDetails = createVnicDetails;
    }

    public String getDedicatedVmHostId() {
        return dedicatedVmHostId;
    }

    public void setDedicatedVmHostId(String dedicatedVmHostId) {
        this.dedicatedVmHostId = dedicatedVmHostId;
    }

    public JSONObject getDefinedTags() {
        return definedTags;
    }

    public void setDefinedTags(JSONObject definedTags) {
        this.definedTags = definedTags;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public JSONObject getExtendedMetadata() {
        return extendedMetadata;
    }

    public void setExtendedMetadata(JSONObject extendedMetadata) {
        this.extendedMetadata = extendedMetadata;
    }

    public String getFaultDomain() {
        return faultDomain;
    }

    public void setFaultDomain(String faultDomain) {
        this.faultDomain = faultDomain;
    }

    public JSONObject getFreeformTags() {
        return freeformTags;
    }

    public void setFreeformTags(JSONObject freeformTags) {
        this.freeformTags = freeformTags;
    }

    public String getIpxeScript() {
        return ipxeScript;
    }

    public void setIpxeScript(String ipxeScript) {
        this.ipxeScript = ipxeScript;
    }

    public boolean getIsPvEncryptionInTransitEnabled() {
        return isPvEncryptionInTransitEnabled;
    }

    public void setIsPvEncryptionInTransitEnabled(boolean isPvEncryptionInTransitEnabled) {
        isPvEncryptionInTransitEnabled = isPvEncryptionInTransitEnabled;
    }

    public String getLaunchMode() {
        return launchMode;
    }

    public void setLaunchMode(String launchMode) {
        this.launchMode = launchMode;
    }

    public LaunchOptions getLaunchOptions() {
        return launchOptions;
    }

    public void setLaunchOptions(LaunchOptions launchOptions) {
        this.launchOptions = launchOptions;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public ShapeConfig getShapeConfig() {
        return shapeConfig;
    }

    public void setShapeConfig(ShapeConfig shapeConfig) {
        this.shapeConfig = shapeConfig;
    }

    public SourceDetails getSourceDetails() {
        return sourceDetails;
    }

    public void setSourceDetails(SourceDetails sourceDetails) {
        this.sourceDetails = sourceDetails;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public class AgentConfig {

        boolean isManagementDisabled;
        boolean isMonitoringDisabled;

        public boolean getIsManagementDisabled() {
            return isManagementDisabled;
        }

        public void setIsManagementDisabled(boolean isManagementDisabled) {
            isManagementDisabled = isManagementDisabled;
        }

        public boolean getIsMonitoringDisabled() {
            return isMonitoringDisabled;
        }

        public void setIsMonitoringDisabled(boolean isMonitoringDisabled) {
            isMonitoringDisabled = isMonitoringDisabled;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public class Metadata {
        @JsonProperty("ssh_authorized_keys")
        String sshAuthorizedKeys;
        @JsonProperty("user_data")
        String userData;

        public String getSshAuthorizedKeys() {
            return sshAuthorizedKeys;
        }

        public void setSshAuthorizedKeys(String sshAuthorizedKeys) {
            this.sshAuthorizedKeys = sshAuthorizedKeys;
        }

        public String getUserData() {
            return userData;
        }

        public void setUserData(String userData) {
            this.userData = userData;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public class CreateVnicDetails {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean assignPublicIp;
        JSONObject definedTags;
        String displayName;
        JSONObject freeformTags;
        String hostnameLabel;
        String[] nsgIds;
        String privateIp;
        boolean skipSourceDestCheck;
        String subnetId;

        public boolean isAssignPublicIp() {
            return assignPublicIp;
        }

        public void setAssignPublicIp(boolean assignPublicIp) {
            this.assignPublicIp = assignPublicIp;
        }

        public JSONObject getDefinedTags() {
            return definedTags;
        }

        public void setDefinedTags(JSONObject definedTags) {
            this.definedTags = definedTags;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public JSONObject getFreeformTags() {
            return freeformTags;
        }

        public void setFreeformTags(JSONObject freeformTags) {
            this.freeformTags = freeformTags;
        }

        public String getHostnameLabel() {
            return hostnameLabel;
        }

        public void setHostnameLabel(String hostnameLabel) {
            this.hostnameLabel = hostnameLabel;
        }

        public String[] getNsgIds() {
            return nsgIds;
        }

        public void setNsgIds(String[] nsgIds) {
            this.nsgIds = nsgIds;
        }

        public String getPrivateIp() {
            return privateIp;
        }

        public void setPrivateIp(String privateIp) {
            this.privateIp = privateIp;
        }

        public boolean isSkipSourceDestCheck() {
            return skipSourceDestCheck;
        }

        public void setSkipSourceDestCheck(boolean skipSourceDestCheck) {
            this.skipSourceDestCheck = skipSourceDestCheck;
        }

        public String getSubnetId() {
            return subnetId;
        }

        public void setSubnetId(String subnetId) {
            this.subnetId = subnetId;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public class LaunchOptions {
        String bootVolumeType;
        String firmware;
        boolean isConsistentVolumeNamingEnabled;
        String networkType;
        String remoteDataVolumeType;

        public String getBootVolumeType() {
            return bootVolumeType;
        }

        public void setBootVolumeType(String bootVolumeType) {
            this.bootVolumeType = bootVolumeType;
        }

        public String getFirmware() {
            return firmware;
        }

        public void setFirmware(String firmware) {
            this.firmware = firmware;
        }

        public boolean getIsConsistentVolumeNamingEnabled() {
            return isConsistentVolumeNamingEnabled;
        }

        public void setIsConsistentVolumeNamingEnabled(boolean isConsistentVolumeNamingEnabled) {
            isConsistentVolumeNamingEnabled = isConsistentVolumeNamingEnabled;
        }

        public String getNetworkType() {
            return networkType;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }

        public String getRemoteDataVolumeType() {
            return remoteDataVolumeType;
        }

        public void setRemoteDataVolumeType(String remoteDataVolumeType) {
            this.remoteDataVolumeType = remoteDataVolumeType;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class ShapeConfig {
        int ocpus;

        public int getOcpus() {
            return ocpus;
        }

        public void setOcpus(int ocpus) {
            this.ocpus = ocpus;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public class SourceDetails {
        String sourceType;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int bootVolumeSizeInGBs;
        String imageId;
        String kmsKeyId;
        String bootVolumeId;

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public int getBootVolumeSizeInGBs() {
            return bootVolumeSizeInGBs;
        }

        public void setBootVolumeSizeInGBs(int bootVolumeSizeInGBs) {
            this.bootVolumeSizeInGBs = bootVolumeSizeInGBs;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getKmsKeyId() {
            return kmsKeyId;
        }

        public void setKmsKeyId(String kmsKeyId) {
            this.kmsKeyId = kmsKeyId;
        }

        public String getBootVolumeId() {
            return bootVolumeId;
        }

        public void setBootVolumeId(String bootVolumeId) {
            this.bootVolumeId = bootVolumeId;
        }
    }

}
