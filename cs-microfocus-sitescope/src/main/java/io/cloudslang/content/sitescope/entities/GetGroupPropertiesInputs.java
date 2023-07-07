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

public class GetGroupPropertiesInputs {
    private String fullPathToGroup;
    private String delimiter;
    private SiteScopeCommonInputs commonInputs;

    private GetGroupPropertiesInputs(String fullPathToGroup, String delimiter, SiteScopeCommonInputs commonInputs) {
        this.fullPathToGroup = fullPathToGroup;
        this.delimiter = delimiter;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetGroupPropertiesInputsBuilder builder() {
        return new GetGroupPropertiesInputsBuilder();
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
    public SiteScopeCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class GetGroupPropertiesInputsBuilder {
        private String fullPathToGroup = EMPTY;
        private String delimiter = EMPTY;
        private SiteScopeCommonInputs commonInputs;

        public GetGroupPropertiesInputsBuilder() {

        }

        @NotNull
        public GetGroupPropertiesInputs.GetGroupPropertiesInputsBuilder fullPathToGroup(@NotNull final String fullPathToGroup) {
            this.fullPathToGroup = fullPathToGroup;
            return this;
        }

        public GetGroupPropertiesInputs.GetGroupPropertiesInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public GetGroupPropertiesInputs.GetGroupPropertiesInputsBuilder commonInputs(@NotNull final SiteScopeCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetGroupPropertiesInputs build() {
            return new GetGroupPropertiesInputs(fullPathToGroup, delimiter, commonInputs);
        }
    }
}
