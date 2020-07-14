package io.cloudslang.content.oracle.oci.services.models.instances;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.simple.JSONObject;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AttachVnicRequestBody {

    AttachVnicRequestBody.CreateVnicDetails createVnicDetails;
    String displayName;
    String instanceId;
    String nicIndex;


    public AttachVnicRequestBody.CreateVnicDetails getCreateVnicDetails() {
        return createVnicDetails;
    }

    public void setCreateVnicDetails(AttachVnicRequestBody.CreateVnicDetails createVnicDetails) {
        this.createVnicDetails = createVnicDetails;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getNicIndex() {
        return nicIndex;
    }

    public void setNicIndex(String nicIndex) {
        this.nicIndex = nicIndex;
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
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean skipSourceDestCheck;
        String subnetId;

        public boolean getAssignPublicIp() {
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

        public boolean getSkipSourceDestCheck() {
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
}
