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

public class GetUserInputs {
    private final String oDataQuery;
    private final AzureActiveDirectoryCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"oDataQuery", "commonInputs"})
    public GetUserInputs(String oDataQuery, AzureActiveDirectoryCommonInputs commonInputs) {
        this.oDataQuery = oDataQuery;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static GetUserInputsBuilder builder() {
        return new GetUserInputsBuilder();
    }

    @NotNull
    public String getODataQuery() {
        return this.oDataQuery;
    }

    @NotNull
    public AzureActiveDirectoryCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class GetUserInputsBuilder {
        private AzureActiveDirectoryCommonInputs commonInputs;
        private String oDataQuery;

        GetUserInputsBuilder() {
        }

        @NotNull
        public GetUserInputs.GetUserInputsBuilder oDataQuery(@NotNull final String oDataQuery) {
            this.oDataQuery = oDataQuery;
            return this;
        }

        @NotNull
        public GetUserInputs.GetUserInputsBuilder commonInputs(@NotNull final AzureActiveDirectoryCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public GetUserInputs build() {
            return new GetUserInputs(oDataQuery, commonInputs);
        }
    }

}
