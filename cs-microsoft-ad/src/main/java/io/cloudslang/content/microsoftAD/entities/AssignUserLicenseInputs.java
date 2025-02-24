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

public class AssignUserLicenseInputs {

    private final String assignedLicenses;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"assignedLicenses", "commonInputs"})

    public AssignUserLicenseInputs(String assignedLicenses, AzureActiveDirectoryCommonInputs commonInputs) {
        this.commonInputs = commonInputs;
        this.assignedLicenses = assignedLicenses;
    }

    @NotNull
    public static AssignUserLicenseInputs.AssignUserLicenseInputsBuilder builder() {
        return new AssignUserLicenseInputsBuilder();
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getAssignedLicenses() {
        return assignedLicenses;
    }

    public static class AssignUserLicenseInputsBuilder {

        private AzureActiveDirectoryCommonInputs commonInputs;
        private String assignedLicenses = EMPTY;

        AssignUserLicenseInputsBuilder() {
        }


        @NotNull
        public AssignUserLicenseInputs.AssignUserLicenseInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public AssignUserLicenseInputs.AssignUserLicenseInputsBuilder assignedLicenses(@NotNull final String assignedLicenses) {
            this.assignedLicenses = assignedLicenses;
            return this;
        }

        public AssignUserLicenseInputs build() {
            return new AssignUserLicenseInputs(assignedLicenses, commonInputs);

        }
    }
}
