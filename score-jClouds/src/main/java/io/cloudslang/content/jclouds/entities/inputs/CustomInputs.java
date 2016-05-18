package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private static final String DEFAULT_AMAZON_REGION = "us-east-1";
    private static final long DEFAULT_TIMING = 20000;

    private String instanceType;
    private String region;
    private String serverId;
    private String availabilityZone;
    private String imageRef;

    private int minCount;
    private int maxCount;
    private long checkStateTimeout;
    private long polingInterval;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.instanceType = builder.instanceType;
        this.serverId = builder.serverId;
        this.availabilityZone = builder.availabilityZone;
        this.imageRef = builder.imageRef;

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

    public String getImageRef() {
        return imageRef;
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
        private String imageRef;

        private int minCount;
        private int maxCount;
        private long checkStateTimeout;
        private long polingInterval;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? DEFAULT_AMAZON_REGION : inputValue;
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

        public CustomInputsBuilder withImageRef(String inputValue) {
            imageRef = inputValue;
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
            checkStateTimeout = InputsUtil.getValidLong(inputValue, DEFAULT_TIMING);
            return this;
        }

        public CustomInputsBuilder withPolingInterval(String inputValue) {
            polingInterval = InputsUtil.getValidLong(inputValue, DEFAULT_TIMING);
            return this;
        }
    }
}