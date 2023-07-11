/*
 * Copyright 2021-2023 Open Text
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

public class GetUserLicenseDetailsInputs {

    private final String queryParams;
    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"queryParams", "commonInputs"})
    public GetUserLicenseDetailsInputs(String queryParams, AzureActiveDirectoryCommonInputs commonInputs) {
        this.queryParams = queryParams;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetUserLicenseDetailsInputs.GetUserLicenseDetailsInputsBuilder builder() {
        return new GetUserLicenseDetailsInputs.GetUserLicenseDetailsInputsBuilder();
    }

    @NotNull
    public String getQueryParams() {
        return queryParams;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class GetUserLicenseDetailsInputsBuilder {
        private String queryParams = EMPTY;
        private AzureActiveDirectoryCommonInputs commonInputs;

        GetUserLicenseDetailsInputsBuilder() {
        }

        @NotNull
        public GetUserLicenseDetailsInputs.GetUserLicenseDetailsInputsBuilder queryParams(@NotNull final String queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        @NotNull
        public GetUserLicenseDetailsInputs.GetUserLicenseDetailsInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetUserLicenseDetailsInputs build() {
            return new GetUserLicenseDetailsInputs(queryParams, commonInputs);
        }
    }

}
