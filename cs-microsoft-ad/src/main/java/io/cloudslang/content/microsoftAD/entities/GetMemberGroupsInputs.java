/*
 * Copyright 2021-2025 Open Text
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


package io.cloudslang.content.microsoftAD.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetMemberGroupsInputs {

    private final String securityEnabledGroups;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"securityEnabledGroups", "commonInputs"})
    public GetMemberGroupsInputs(String securityEnabledGroups, AzureActiveDirectoryCommonInputs commonInputs) {
        this.securityEnabledGroups = securityEnabledGroups;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetMemberGroupsInputs.GetMemberGroupsInputsBuilder builder() {
        return new GetMemberGroupsInputs.GetMemberGroupsInputsBuilder();
    }

    @NotNull
    public String getSecurityEnabledGroups() {
        return securityEnabledGroups;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class GetMemberGroupsInputsBuilder {
        private String securityEnabledGroups = EMPTY;
        private AzureActiveDirectoryCommonInputs commonInputs;

        GetMemberGroupsInputsBuilder() {
        }

        @NotNull
        public GetMemberGroupsInputs.GetMemberGroupsInputsBuilder securityEnabledGroups(@NotNull final String securityEnabledGroups) {
            this.securityEnabledGroups = securityEnabledGroups;
            return this;
        }

        @NotNull
        public GetMemberGroupsInputs.GetMemberGroupsInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetMemberGroupsInputs build() {
            return new GetMemberGroupsInputs(securityEnabledGroups, commonInputs);
        }
    }

}
