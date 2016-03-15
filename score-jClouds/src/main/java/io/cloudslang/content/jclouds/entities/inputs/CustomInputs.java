package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.InstanceType;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private static final String AMAZON_DEFAULT_REGION = "us-east-1";

    private String region;
    private String serverId;
    private String availabilityZone;
    private String imageRef;
    private String instanceType;
    private long checkStateTimeout;
    private long polingInterval;
    private int minCount;
    private int maxCount;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.serverId = builder.serverId;
        this.availabilityZone = builder.availabilityZone;
        this.imageRef = builder.imageRef;
        this.instanceType = builder.instanceType;
        this.checkStateTimeout = builder.checkStateTimeout;
        this.polingInterval = builder.polingInterval;
        this.minCount = builder.minCount;
        this.maxCount = builder.maxCount;
    }

    public String getRegion() {
        return region;
    }

    public String getServerId() {
        return serverId;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getImageRef() {
        return imageRef;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public long getCheckStateTimeout() {
        return checkStateTimeout;
    }

    public long getPolingInterval() {
        return polingInterval;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public static class CustomInputsBuilder {
        private String region;
        private String serverId;
        private String availabilityZone;
        private String imageRef;
        private String instanceType;
        private long checkStateTimeout;
        private long polingInterval;
        private int minCount;
        private int maxCount;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? AMAZON_DEFAULT_REGION : inputValue;
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

        public CustomInputsBuilder withImageRef(String inputValue) {
            imageRef = inputValue;
            return this;
        }

        public CustomInputsBuilder withInstanceType(String inputValue) {
            instanceType = InstanceType.getInstanceType(inputValue);
            return this;
        }

        public CustomInputsBuilder withCheckStateTimeout(String inputValue) {
            checkStateTimeout = InputsUtil.getValidLong(inputValue);
            return this;
        }

        public CustomInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue);
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
    }
}