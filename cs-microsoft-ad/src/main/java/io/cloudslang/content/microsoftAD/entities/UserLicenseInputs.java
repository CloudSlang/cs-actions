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

public class UserLicenseInputs {

    private final String body;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"body", "commonInputs"})

    public UserLicenseInputs(String body, AzureActiveDirectoryCommonInputs commonInputs) {
        this.commonInputs = commonInputs;
        this.body = body;
    }

    @NotNull
    public static UserLicenseInputs.AssignUserLicenseInputsBuilder builder() {
        return new AssignUserLicenseInputsBuilder();
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    public static class AssignUserLicenseInputsBuilder {

        private AzureActiveDirectoryCommonInputs commonInputs;
        private String body = EMPTY;

        AssignUserLicenseInputsBuilder() {
        }


        @NotNull
        public UserLicenseInputs.AssignUserLicenseInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public UserLicenseInputs.AssignUserLicenseInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        public UserLicenseInputs build() {
            return new UserLicenseInputs(body, commonInputs);

        }
    }
}
