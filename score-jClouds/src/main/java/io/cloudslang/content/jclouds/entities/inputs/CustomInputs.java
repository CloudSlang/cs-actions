package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private static final String DEFAULT_AMAZON_REGION = "us-east-1";
    private static final String SELF = "self";

    private String region;
    private String instanceId;
    private String imageId;
    private String identityId;
    private String volumeId;
    private String groupId;
    private String hostId;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.instanceId = builder.instanceId;
        this.imageId = builder.imageId;
        this.identityId = builder.identityId;
        this.volumeId = builder.volumeId;
        this.groupId = builder.groupId;
        this.hostId = builder.hostId;
    }

    public String getRegion() {
        return region;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getHostId() {
        return hostId;
    }

    public static class CustomInputsBuilder {
        private String region;
        private String instanceId;
        private String imageId;
        private String identityId;
        private String volumeId;
        private String groupId;
        private String hostId;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? DEFAULT_AMAZON_REGION : inputValue;
            return this;
        }

        public CustomInputsBuilder withInstanceId(String inputValue) {
            instanceId = inputValue;
            return this;
        }

        public CustomInputsBuilder withImageId(String inputValue) {
            imageId = inputValue;
            return this;
        }

        public CustomInputsBuilder withIdentityId(String inputValue) {
            identityId = StringUtils.isBlank(inputValue) ? SELF : inputValue;
            return this;
        }

        public CustomInputsBuilder withVolumeId(String inputValue) {
            volumeId = inputValue;
            return this;
        }

        public CustomInputsBuilder withGroupId(String inputValue) {
            groupId = inputValue;
            return this;
        }

        public CustomInputsBuilder withHostId(String inputValue) {
            hostId = inputValue;
            return this;
        }
    }
}