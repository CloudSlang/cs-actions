package io.cloudslang.content.oracle.oci.services.models.volumes;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AttachVolumeRequestBody {

    String device;
    String displayName;
    String instanceId;
    String volumeId;
    String type;
    boolean isReadOnly;
    boolean isShareable;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(boolean isReadOnly) {
        isReadOnly = isReadOnly;
    }

    public boolean getIsShareable() {
        return isShareable;
    }

    public void setIsShareable(boolean isShareable) {
        isShareable = isShareable;
    }
}
