package io.cloudslang.content.jclouds.entities.inputs;

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
    private int minCount;
    private int maxCount;

    public CustomInputs(SpecificInputsBuilder builder) {
        this.region = builder.region;
        this.serverId = builder.serverId;
        this.availabilityZone = builder.availabilityZone;
        this.imageRef = builder.imageRef;
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

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public static class SpecificInputsBuilder {
        private String region;
        private String serverId;
        private String availabilityZone;
        private String imageRef;
        private int minCount;
        private int maxCount;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public SpecificInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? AMAZON_DEFAULT_REGION : inputValue;
            return this;
        }

        public SpecificInputsBuilder withServerId(String inputValue) {
            serverId = inputValue;
            return this;
        }

        public SpecificInputsBuilder withAvailabilityZone(String inputValue) {
            availabilityZone = inputValue;
            return this;
        }

        public SpecificInputsBuilder withImageRef(String inputValue) {
            imageRef = inputValue;
            return this;
        }

        public SpecificInputsBuilder withMinCount(String inputValue) {
            minCount = InputsUtil.getMinInstancesCount(inputValue);
            return this;
        }

        public SpecificInputsBuilder withMaxCount(String inputValue) {
            maxCount = InputsUtil.getMaxInstancesCount(inputValue);
            return this;
        }
    }
}