/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.ImageState;
import io.cloudslang.content.amazon.entities.aws.ImageType;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.amazon.utils.InputsUtil.getEnforcedBooleanCondition;
import static io.cloudslang.content.amazon.utils.InputsUtil.getRelevantBooleanString;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class ImageInputs {
    private final String imageName;
    private final String imageIdsString;
    private final String ownersString;
    private final String userIdsString;
    private final String userGroupsString;
    private final String description;
    private final String type;
    private final String isPublic;
    private final String manifestLocation;
    private final String state;

    private final boolean imageNoReboot;

    private ImageInputs(Builder builder) {

        this.imageName = builder.imageName;
        this.imageIdsString = builder.imageIdsString;
        this.ownersString = builder.ownersString;
        this.userIdsString = builder.userIdsString;
        this.userGroupsString = builder.userGroupsString;
        this.description = builder.description;
        this.type = builder.type;
        this.isPublic = builder.isPublic;
        this.manifestLocation = builder.manifestLocation;
        this.state = builder.state;

        this.imageNoReboot = builder.imageNoReboot;
    }

    public String getImageName() {
        return imageName;
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

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public String getManifestLocation() {
        return manifestLocation;
    }

    public String getState() {
        return state;
    }

    public boolean isImageNoReboot() {
        return imageNoReboot;
    }

    public static class Builder {
        private String imageName;
        private String imageIdsString;
        private String ownersString;
        private String userIdsString;
        private String userGroupsString;
        private String description;
        private String type;
        private String isPublic;
        private String manifestLocation;
        private String state;

        private boolean imageNoReboot;

        public ImageInputs build() {
            return new ImageInputs(this);
        }

        public Builder withImageName(String inputValue) {
            imageName = getDefaultStringInput(inputValue, NOT_RELEVANT);
            return this;
        }

        public Builder withImageIdsString(String inputValue) {
            imageIdsString = getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withOwnersString(String inputValue) {
            ownersString = getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withUserIdsString(String inputValue) {
            userIdsString = inputValue;
            return this;
        }

        public Builder withUserGroupsString(String inputValue) {
            userGroupsString = inputValue;
            return this;
        }

        public Builder withDescription(String inputValue) {
            description = inputValue;
            return this;
        }

        public Builder withType(String inputValue) {
            type = ImageType.getValue(inputValue);
            return this;
        }

        public Builder withIsPublic(String inputValue) {
            isPublic = getRelevantBooleanString(inputValue);
            return this;
        }

        public Builder withManifestLocation(String inputValue) {
            manifestLocation = inputValue;
            return this;
        }

        public Builder withState(String inputValue) {
            state = ImageState.getValue(inputValue);
            return this;
        }

        public Builder withImageNoReboot(String inputValue) {
            imageNoReboot = getEnforcedBooleanCondition(inputValue, true);
            return this;
        }
    }
}
