/*
 * Copyright 2021-2024 Open Text
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

public class ChangeUserPasswordInputs {

    private final String currentPassword;
    private final String newPassword;
    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"currentPassword", "newPassword", "commonInputs"})

    public ChangeUserPasswordInputs(String currentPassword, String newPassword, AzureActiveDirectoryCommonInputs commonInputs) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static ChangeUserPasswordInputsBuilder builder() {
        return new ChangeUserPasswordInputsBuilder();
    }

    @NotNull
    public String getCurrentPassword() {
        return currentPassword;
    }

    @NotNull
    public String getNewPassword() {
        return newPassword;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class ChangeUserPasswordInputsBuilder {
        private String currentPassword = EMPTY;
        private String newPassword = EMPTY;
        private AzureActiveDirectoryCommonInputs commonInputs;

        ChangeUserPasswordInputsBuilder() {
        }

        @NotNull
        public ChangeUserPasswordInputs.ChangeUserPasswordInputsBuilder currentPassword(@NotNull final String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        @NotNull
        public ChangeUserPasswordInputs.ChangeUserPasswordInputsBuilder newPassword(@NotNull final String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        @NotNull
        public ChangeUserPasswordInputs.ChangeUserPasswordInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public ChangeUserPasswordInputs build() {
            return new ChangeUserPasswordInputs(currentPassword, newPassword, commonInputs);
        }
    }
}
