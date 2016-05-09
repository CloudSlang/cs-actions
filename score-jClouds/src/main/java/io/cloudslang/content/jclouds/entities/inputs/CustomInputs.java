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
    private String imageName;
    private String imageId;
    private String imageDescription;
    private String identityId;
    private String imageIdsString;
    private String ownersString;

    private int minCount;
    private int maxCount;
    private long checkStateTimeout;
    private long polingInterval;

    private boolean imageNoReboot;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.instanceType = builder.instanceType;
        this.serverId = builder.serverId;
        this.availabilityZone = builder.availabilityZone;
        this.imageName = builder.imageName;
        this.imageId = builder.imageId;
        this.imageDescription = builder.imageDescription;
        this.identityId = builder.identityId;
        this.imageIdsString = builder.imageIdsString;
        this.ownersString = builder.ownersString;

        this.minCount = builder.minCount;
        this.maxCount = builder.maxCount;
        this.checkStateTimeout = builder.checkStateTimeout;
        this.polingInterval = builder.polingInterval;

        this.imageNoReboot = builder.imageNoReboot;
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

    public String getImageName() {
        return imageName;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getImageIdsString() {
        return imageIdsString;
    }

    public String getOwnersString() {
        return ownersString;
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

    public boolean isImageNoReboot() {
        return imageNoReboot;
    }

    public static class CustomInputsBuilder {
        private String region;
        private String instanceType;
        private String serverId;
        private String availabilityZone;
        private String imageName;
        private String imageId;
        private String imageDescription;
        private String identityId;
        private String imageIdsString;
        private String ownersString;

        private int minCount;
        private int maxCount;
        private long checkStateTimeout;
        private long polingInterval;

        private boolean imageNoReboot;

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

        public CustomInputsBuilder withImageName(String inputValue) {
            imageName = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public CustomInputsBuilder withImageId(String inputValue) {
            imageId = inputValue;
            return this;
        }

        public CustomInputsBuilder withImageIdsString(String inputValue) {
            imageIdsString = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public CustomInputsBuilder withOwnersString(String inputValue) {
            ownersString = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public CustomInputsBuilder withImageDescription(String inputValue) {
            imageDescription = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public CustomInputsBuilder withIdentityId(String inputValue) {
            identityId = inputValue;
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

        public CustomInputsBuilder withImageNoReboot(String inputValue) {
            imageNoReboot = InputsUtil.getBoolean(inputValue);
            return this;
        }
    }
}