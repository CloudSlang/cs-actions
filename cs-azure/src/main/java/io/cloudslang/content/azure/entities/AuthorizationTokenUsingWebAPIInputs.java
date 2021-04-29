/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AuthorizationTokenUsingWebAPIInputs {
    private final String tenantId;
    private final String clientSecret;
    private final String clientId;
    private final String resource;
    private final AzureCommonInputs azureCommonInputs;

    @java.beans.ConstructorProperties({"tenantId", "clientSecret", "clientId", "resource", "azureCommonInputs"})
    private AuthorizationTokenUsingWebAPIInputs(final String tenantId, final String clientSecret, final String clientId, final String resource, final AzureCommonInputs azureCommonInputs) {
        this.tenantId = tenantId;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
        this.resource = resource;
       this.azureCommonInputs = azureCommonInputs;

    }

    @NotNull
    public static AuthorizationTokenUsingWebAPIInputsBuilder builder() {
        return new AuthorizationTokenUsingWebAPIInputsBuilder();
    }

    @NotNull
    public String getTenantId() {
        return this.tenantId;
    }

    @NotNull
    public String getClientSecret() {
        return this.clientSecret;
    }

    @NotNull
    public String getClientId() {
        return this.clientId;
    }



    @NotNull
    public String getResource() {
        return this.resource;
    }

    @NotNull
    public AzureCommonInputs getAzureCommonInputs(){
        return azureCommonInputs;
    }


    public static class AuthorizationTokenUsingWebAPIInputsBuilder {
        private String tenantId = EMPTY;
        private String clientSecret = EMPTY;
        private String clientId = EMPTY;
        private String resource = EMPTY;
        private AzureCommonInputs azureCommonInputs;

        AuthorizationTokenUsingWebAPIInputsBuilder() {
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder tenantId(@NotNull final String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder clientSecret(@NotNull final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder clientId(@NotNull final String clientId) {
            this.clientId = clientId;
            return this;
        }



        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder resource(@NotNull final String resource) {
            this.resource = resource;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputsBuilder azureCommonInputs(@NotNull final AzureCommonInputs azureCommonInputs) {
            this.azureCommonInputs = azureCommonInputs;
            return this;
        }

        @NotNull
        public AuthorizationTokenUsingWebAPIInputs build() {
            return new AuthorizationTokenUsingWebAPIInputs(tenantId, clientSecret, clientId, resource, azureCommonInputs);
        }


    }
}
