package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class ImageInputs {
    private CustomInputs customInputs;

    private String imageName;
    private String imageDescription;
    private String imageIdsString;
    private String ownersString;
    private String userIdsString;
    private String userGroupsString;

    private boolean imageNoReboot;

    private ImageInputs(ImageInputs.ImageInputsBuilder builder) {
        this.customInputs = builder.customInputs;

        this.imageName = builder.imageName;
        this.imageDescription = builder.imageDescription;
        this.imageIdsString = builder.imageIdsString;
        this.ownersString = builder.ownersString;
        this.userIdsString = builder.userIdsString;
        this.userGroupsString = builder.userGroupsString;

        this.imageNoReboot = builder.imageNoReboot;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public String getImageIdsString() {
        return imageIdsString;
    }

    public String getOwnersString() {
        return ownersString;
    }

    public String getUserIdsString() {
        return userIdsString;
    }

    public String getUserGroupsString() {
        return userGroupsString;
    }

    public boolean isImageNoReboot() {
        return imageNoReboot;
    }

    public static class ImageInputsBuilder {
        private CustomInputs customInputs;

        private String imageName;
        private String imageDescription;
        private String imageIdsString;
        private String ownersString;
        private String userIdsString;
        private String userGroupsString;

        private boolean imageNoReboot;

        public ImageInputs build() {
            return new ImageInputs(this);
        }

        public ImageInputs.ImageInputsBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withImageName(String inputValue) {
            imageName = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withImageIdsString(String inputValue) {
            imageIdsString = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withOwnersString(String inputValue) {
            ownersString = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withImageDescription(String inputValue) {
            imageDescription = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.EMPTY : inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withUserIdsString(String inputValue) {
            userIdsString = inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withUserGroupsString(String inputValue) {
            userGroupsString = inputValue;
            return this;
        }

        public ImageInputs.ImageInputsBuilder withImageNoReboot(String inputValue) {
            imageNoReboot = InputsUtil.getBoolean(inputValue);
            return this;
        }
    }
}