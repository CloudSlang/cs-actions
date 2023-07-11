/*
 * Copyright 2020-2023 Open Text
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

public class DeleteMonitorGroupInputs {
    private String fullPathToGroup;
    private String delimiter;
    private String identifier;
    private String externalId;
    private SiteScopeCommonInputs commonInputs;

    private DeleteMonitorGroupInputs(String fullPathToGroup, String delimiter, String identifier, String externalId,
                                     SiteScopeCommonInputs commonInputs) {
        this.fullPathToGroup = fullPathToGroup;
        this.delimiter = delimiter;
        this.identifier = identifier;
        this.externalId = externalId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeleteMonitorGroupInputsBuilder builder() {
        return new DeleteMonitorGroupInputsBuilder();
    }

    @NotNull
    public String getFullPathToGroup() {
        return fullPathToGroup;
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
    public String getExternalId() {
        return externalId;
    }

    @NotNull
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class DeleteMonitorGroupInputsBuilder {
        private String fullPathToGroup = EMPTY;
        private String delimiter = EMPTY;
        private String identifier = EMPTY;
        private String externalId = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public DeleteMonitorGroupInputsBuilder() {

        }

        @NotNull
        public DeleteMonitorGroupInputs.DeleteMonitorGroupInputsBuilder fullPathToGroup(@NotNull final String fullPathToGroup) {
            this.fullPathToGroup = fullPathToGroup;
            return this;
        }

        @NotNull
        public DeleteMonitorGroupInputs.DeleteMonitorGroupInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        @NotNull
        public DeleteMonitorGroupInputs.DeleteMonitorGroupInputsBuilder identifier(@NotNull final String identifier) {
            this.identifier = identifier;
            return this;
        }

        @NotNull
        public DeleteMonitorGroupInputs.DeleteMonitorGroupInputsBuilder externalId(@NotNull final String externalId) {
            this.externalId = externalId;
            return this;
        }

        @NotNull
        public DeleteMonitorGroupInputs.DeleteMonitorGroupInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public DeleteMonitorGroupInputs build() {
            return new DeleteMonitorGroupInputs(fullPathToGroup, delimiter, identifier, externalId, commonInputs);
        }
    }
}
