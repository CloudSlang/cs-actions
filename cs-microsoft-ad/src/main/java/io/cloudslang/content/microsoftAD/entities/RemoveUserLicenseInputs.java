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

public class RemoveUserLicenseInputs {

    private final String removedLicenses;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"removedLicenses", "commonInputs"})

    public RemoveUserLicenseInputs(String removedLicenses, AzureActiveDirectoryCommonInputs commonInputs) {
        this.commonInputs = commonInputs;
        this.removedLicenses = removedLicenses;
    }

    @NotNull
    public static RemoveUserLicenseInputs.AssignUserLicenseInputsBuilder builder() {
        return new AssignUserLicenseInputsBuilder();
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getRemovedLicenses() {
        return removedLicenses;
    }

    public static class AssignUserLicenseInputsBuilder {

        private AzureActiveDirectoryCommonInputs commonInputs;
        private String removedLicenses = EMPTY;

        AssignUserLicenseInputsBuilder() {
        }


        @NotNull
        public RemoveUserLicenseInputs.AssignUserLicenseInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public RemoveUserLicenseInputs.AssignUserLicenseInputsBuilder removedLicenses(@NotNull final String removedLicenses) {
            this.removedLicenses = removedLicenses;
            return this;
        }

        public RemoveUserLicenseInputs build() {
            return new RemoveUserLicenseInputs(removedLicenses, commonInputs);

        }
    }
}
