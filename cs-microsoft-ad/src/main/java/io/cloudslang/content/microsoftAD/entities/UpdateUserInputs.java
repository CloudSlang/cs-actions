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

public class UpdateUserInputs {

    private final String updatedUserPrincipalName;
    private final CreateUserInputs createUserCommonInputs;
    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"updatedUserPrincipalName", "commonInputs", "createUserCommonInputs"})

    public UpdateUserInputs(String updatedUserPrincipalName, CreateUserInputs createUserCommonInputs,
                            AzureActiveDirectoryCommonInputs commonInputs) {
        this.updatedUserPrincipalName = updatedUserPrincipalName;
        this.createUserCommonInputs = createUserCommonInputs;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static UpdateUserInputsBuilder builder() {
        return new UpdateUserInputsBuilder();
    }

    @NotNull
    public String getUpdatedUserPrincipalName() {
        return updatedUserPrincipalName;
    }

    @NotNull
    public CreateUserInputs getCreateUserCommonInputs() {
        return createUserCommonInputs;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class UpdateUserInputsBuilder {
        private String updatedUserPrincipalName = EMPTY;
        private CreateUserInputs createUserCommonInputs;
        private AzureActiveDirectoryCommonInputs commonInputs;

        UpdateUserInputsBuilder() {
        }

        @NotNull
        public UpdateUserInputs.UpdateUserInputsBuilder updatedUserPrincipalName(@NotNull final String updatedUserPrincipalName) {
            this.updatedUserPrincipalName = updatedUserPrincipalName;
            return this;
        }

        @NotNull
        public UpdateUserInputs.UpdateUserInputsBuilder createUserCommonInputs(@NotNull final CreateUserInputs createUserCommonInputs) {
            this.createUserCommonInputs = createUserCommonInputs;
            return this;
        }


        @NotNull
        public UpdateUserInputs.UpdateUserInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public UpdateUserInputs build() {
            return new UpdateUserInputs(updatedUserPrincipalName, createUserCommonInputs, commonInputs);
        }
    }
}
