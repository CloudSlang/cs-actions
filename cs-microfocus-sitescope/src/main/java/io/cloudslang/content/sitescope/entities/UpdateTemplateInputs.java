/*
 * Copyright 2020-2025 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UpdateTemplateInputs {
    private String fullPathToTemplate;
    private String properties;
    private String delimiter;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private UpdateTemplateInputs(String fullPathToTemplate, String properties, String delimiter, String identifier,
                                 SiteScopeCommonInputs commonInputs) {
        this.fullPathToTemplate = fullPathToTemplate;
        this.properties = properties;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static UpdateTemplateInputs.UpdateTemplateInputsBuilder builder() {
        return new UpdateTemplateInputsBuilder();
    }

    @NotNull
    public String getFullPathToTemplate() {
        return fullPathToTemplate;
    }

    @NotNull
    public String getProperties() {
        return properties;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static class UpdateTemplateInputsBuilder {
        private String fullPathToTemplate = EMPTY;
        private String properties = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public UpdateTemplateInputsBuilder() {

        }

        @NotNull
        public UpdateTemplateInputs.UpdateTemplateInputsBuilder fullPathToTemplate(@NotNull final String fullPathToTemplate) {
            this.fullPathToTemplate = fullPathToTemplate;
            return this;
        }


        @NotNull
        public UpdateTemplateInputs.UpdateTemplateInputsBuilder properties(@NotNull final String properties) {
            this.properties = properties;
            return this;
        }

        @NotNull
        public UpdateTemplateInputs.UpdateTemplateInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public UpdateTemplateInputs.UpdateTemplateInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public UpdateTemplateInputs.UpdateTemplateInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public UpdateTemplateInputs build() {
            return new UpdateTemplateInputs(fullPathToTemplate, properties, delimiter, identifier, commonInputs);
        }
    }
}
