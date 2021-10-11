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

public class AssignUserLicenseInputs {

    private final String body;

    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"body", "commonInputs"})

    public AssignUserLicenseInputs(String body, AzureActiveDirectoryCommonInputs commonInputs) {
        this.commonInputs = commonInputs;
        this.body = body;
    }

    @NotNull
    public static CreateUserInputsBuilder builder() {
        return new CreateUserInputsBuilder();
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    public static class CreateUserInputsBuilder {

        private AzureActiveDirectoryCommonInputs commonInputs;
        private String body = EMPTY;

        CreateUserInputsBuilder() {
        }


        @NotNull
        public AssignUserLicenseInputs.CreateUserInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public AssignUserLicenseInputs.CreateUserInputsBuilder body(@NotNull final String body) {
            this.body = body;
            return this;
        }

        public AssignUserLicenseInputs build() {
            return new AssignUserLicenseInputs(body, commonInputs);

        }
    }
}
