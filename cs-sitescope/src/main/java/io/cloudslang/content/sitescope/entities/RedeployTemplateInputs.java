/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RedeployTemplateInputs {
    private String fullPathToTemplate;
    private String properties;
    private String delimiter;
    private String identifier;
    private SiteScopeCommonInputs commonInputs;

    private RedeployTemplateInputs(String fullPathToTemplate, String properties, String delimiter, String identifier,
                                   SiteScopeCommonInputs commonInputs) {
        this.fullPathToTemplate = fullPathToTemplate;
        this.properties = properties;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static RedeployTemplateInputsBuilder builder() {
        return new RedeployTemplateInputsBuilder();
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


    public static class RedeployTemplateInputsBuilder {
        private String fullPathToTemplate = EMPTY;
        private String properties = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public RedeployTemplateInputsBuilder() {

        }

        @NotNull
        public RedeployTemplateInputsBuilder fullPathToTemplate(@NotNull final String fullPathToTemplate) {
            this.fullPathToTemplate = fullPathToTemplate;
            return this;
        }


        @NotNull
        public RedeployTemplateInputsBuilder properties(@NotNull final String properties) {
            this.properties = properties;
            return this;
        }

        @NotNull
        public RedeployTemplateInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public RedeployTemplateInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public RedeployTemplateInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public RedeployTemplateInputs build() {
            return new RedeployTemplateInputs(fullPathToTemplate, properties, delimiter, identifier, commonInputs);
        }
    }
}
