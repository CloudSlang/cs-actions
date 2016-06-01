package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CustomInputs {
    private String region;
    private String serverId;
    private String imageId;
    private String identityId;

    public CustomInputs(CustomInputsBuilder builder) {
        this.region = builder.region;
        this.serverId = builder.serverId;
        this.imageId = builder.imageId;
        this.identityId = builder.identityId;
    }

    public String getRegion() {
        return region;
    }

    public String getServerId() {
        return serverId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public static class CustomInputsBuilder {
        private String region;
        private String serverId;
        private String imageId;
        private String identityId;

        public CustomInputs build() {
            return new CustomInputs(this);
        }

        public CustomInputsBuilder withRegion(String inputValue) {
            region = (StringUtils.isBlank(inputValue)) ? Constants.Miscellaneous.DEFAULT_AMAZON_REGION : inputValue;
            return this;
        }

        public CustomInputsBuilder withServerId(String inputValue) {
            serverId = inputValue;
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
    }
}