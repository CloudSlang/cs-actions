/*
 * (c) Copyright 2021 Micro Focus, L.P.
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

package io.cloudslang.content.microsoftAD.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetMemberGroupsInputs {

    private final String securityEnabledOnly;
    private final String body;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"securityEnabledOnly", "body", "commonInputs"})

    public GetMemberGroupsInputs(String securityEnabledOnly, String body, AzureActiveDirectoryCommonInputs commonInputs) {
        this.securityEnabledOnly = securityEnabledOnly;
        this.body = body;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetMemberGroupsInputs.GetMemberGroupsInputsBuilder builder() {
        return new GetMemberGroupsInputs.GetMemberGroupsInputsBuilder();
    }

    @NotNull
    public String getSecurityEnabledOnly() {
        return securityEnabledOnly;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class GetMemberGroupsInputsBuilder {
        private String securityEnabledOnly = EMPTY;
        private String body = EMPTY;
        private AzureActiveDirectoryCommonInputs commonInputs;

        GetMemberGroupsInputsBuilder() {
        }

        @NotNull
        public GetMemberGroupsInputs.GetMemberGroupsInputsBuilder securityEnabledOnly(@NotNull final String securityEnabledOnly) {
            this.securityEnabledOnly = securityEnabledOnly;
            return this;
        }

        @NotNull
        public GetMemberGroupsInputs.GetMemberGroupsInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public GetMemberGroupsInputs.GetMemberGroupsInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        public GetMemberGroupsInputs build() {
            return new GetMemberGroupsInputs(securityEnabledOnly, body, commonInputs);

        }
    }

}
