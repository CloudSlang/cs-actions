package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private String instanceType;
    private String region;
    private String serverId;
    private String availabilityZone;
    private String imageId;
    private String identityId;

    private int minCount;
    private int maxCount;
    private long checkStateTimeout;
    private long polingInterval;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.instanceType = builder.instanceType;
        this.serverId = builder.serverId;
        this.availabilityZone = builder.availabilityZone;
        this.imageId = builder.imageId;
        this.identityId = builder.identityId;

        this.minCount = builder.minCount;
        this.maxCount = builder.maxCount;
        this.checkStateTimeout = builder.checkStateTimeout;
        this.polingInterval = builder.polingInterval;
    }

    public String getRegion() {
        return region;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getServerId() {
        return serverId;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getImageId() {
        return imageId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public long getCheckStateTimeout() {
        return checkStateTimeout;
    }

    public long getPolingInterval() {
        return polingInterval;
    }

    public static class CustomInputsBuilder {
        private String region;
        private String instanceType;
        private String serverId;
        private String availabilityZone;
        private String imageId;
        private String identityId;

        private int minCount;
        private int maxCount;
        private long checkStateTimeout;

        private long polingInterval;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? Constants.Miscellaneous.DEFAULT_AMAZON_REGION : inputValue;
            return this;
        }

        public CustomInputsBuilder withInstanceType(String inputValue) {
            instanceType = inputValue;
            return this;
        }

        public CustomInputsBuilder withServerId(String inputValue) {
            serverId = inputValue;
            return this;
        }

        public CustomInputsBuilder withAvailabilityZone(String inputValue) {
            availabilityZone = inputValue;
            return this;
        }

        public CustomInputsBuilder withImageId(String inputValue) {
            imageId = inputValue;
            return this;
        }

        public CustomInputsBuilder withIdentityId(String inputValue) {
            identityId = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.SELF : inputValue;
            return this;
        }

        public CustomInputsBuilder withMinCount(String inputValue) {
            minCount = InputsUtil.getMinInstancesCount(inputValue);
            return this;
        }

        public CustomInputsBuilder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getMaxInstancesCount(inputValue);
            return this;
        }

        public CustomInputsBuilder withCheckStateTimeout(String inputValue) {
            checkStateTimeout = InputsUtil.getValidLong(inputValue, Constants.Miscellaneous.DEFAULT_TIMING);
            return this;
        }

        public CustomInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue, Constants.Miscellaneous.DEFAULT_TIMING);
            return this;
        }
    }
}