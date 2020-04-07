
    /*
     * (c) Copyright 2020 Micro Focus, L.P.
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

    package io.cloudslang.content.hashicorp.terraform.services;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
    import io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs;
    import io.cloudslang.content.hashicorp.terraform.services.models.organization.CreateOrganizationRequestBody;
    import io.cloudslang.content.httpclient.entities.HttpClientInputs;
    import io.cloudslang.content.httpclient.services.HttpClientService;
    import org.apache.http.client.utils.URIBuilder;
    import org.jetbrains.annotations.NotNull;

    import java.util.Map;

    import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
    import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
    import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateOrganizationConstants.ORGANIZATION_TYPE;
    import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
    import static org.apache.commons.lang3.StringUtils.EMPTY;


    public class OrganizationImpl {

        @NotNull
        public static Map<String, String> createOrganization(@NotNull final TerraformOrganizationInputs createOrganizationInputs)
                throws Exception {
            final HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setUrl(createOrganizationUrl(createOrganizationInputs.getCommonInputs().getOrganizationName()));
            setCommonHttpInputs(httpClientInputs, createOrganizationInputs.getCommonInputs());
            httpClientInputs.setAuthType(ANONYMOUS);
            httpClientInputs.setMethod(POST);
            httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
            if (createOrganizationInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
                httpClientInputs.setBody(createOrganizationBody(createOrganizationInputs, DELIMITER));
            } else {
                httpClientInputs.setBody(createOrganizationInputs.getCommonInputs().getRequestBody());
            }
            httpClientInputs.setResponseCharacterSet(createOrganizationInputs.getCommonInputs().getResponseCharacterSet());
            httpClientInputs.setHeaders(getAuthHeaders(createOrganizationInputs.getCommonInputs().getAuthToken()));
            return new HttpClientService().execute(httpClientInputs);
        }

        @NotNull
        public static Map<String, String> updateOrganization(@NotNull final TerraformOrganizationInputs createOrganizationInputs)
                throws Exception {
            final HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setUrl(getOrganizationDetailsUrl(createOrganizationInputs.getCommonInputs().getOrganizationName()));
            setCommonHttpInputs(httpClientInputs, createOrganizationInputs.getCommonInputs());
            httpClientInputs.setAuthType(ANONYMOUS);
            httpClientInputs.setMethod(PATCH);
            httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
            if (createOrganizationInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
                httpClientInputs.setBody(createOrganizationBody(createOrganizationInputs, DELIMITER));
            } else {
                httpClientInputs.setBody(createOrganizationInputs.getCommonInputs().getRequestBody());
            }
            httpClientInputs.setResponseCharacterSet(createOrganizationInputs.getCommonInputs().getResponseCharacterSet());
            httpClientInputs.setHeaders(getAuthHeaders(createOrganizationInputs.getCommonInputs().getAuthToken()));
            return new HttpClientService().execute(httpClientInputs);
        }


        @NotNull
        public static Map<String, String> deleteOrganization(@NotNull final TerraformOrganizationInputs deleteOrganizationInputs)
                throws Exception {
            final HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setUrl(getOrganizationDetailsUrl(deleteOrganizationInputs.getCommonInputs().getOrganizationName()));
            setCommonHttpInputs(httpClientInputs, deleteOrganizationInputs.getCommonInputs());
            httpClientInputs.setAuthType(ANONYMOUS);
            httpClientInputs.setMethod(DELETE);
            httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
            httpClientInputs.setResponseCharacterSet(deleteOrganizationInputs.getCommonInputs().getResponseCharacterSet());
            httpClientInputs.setHeaders(getAuthHeaders(deleteOrganizationInputs.getCommonInputs().getAuthToken()));
            return new HttpClientService().execute(httpClientInputs);
        }

        @NotNull
        public static Map<String, String> listOrganizations(@NotNull final TerraformCommonInputs listOrganizationsInputs)
                throws Exception {
            final HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setUrl(createOrganizationUrl(listOrganizationsInputs.getOrganizationName()));
            setCommonHttpInputs(httpClientInputs, listOrganizationsInputs);
            httpClientInputs.setAuthType(ANONYMOUS);
            httpClientInputs.setMethod(GET);
            httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
            httpClientInputs.setQueryParams(getQueryParams(listOrganizationsInputs.getPageNumber(),
                    listOrganizationsInputs.getPageSize()));
            httpClientInputs.setResponseCharacterSet(listOrganizationsInputs.getResponseCharacterSet());
            httpClientInputs.setHeaders(getAuthHeaders(listOrganizationsInputs.getAuthToken()));
            return new HttpClientService().execute(httpClientInputs);
        }

        @NotNull
        public static Map<String, String> getOrganizationDetails(@NotNull final TerraformOrganizationInputs
                                                                      getOrganizationDetailsInputs) throws Exception {
            final HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setUrl(getOrganizationDetailsUrl(getOrganizationDetailsInputs.getCommonInputs().getOrganizationName()));
            httpClientInputs.setAuthType(ANONYMOUS);
            httpClientInputs.setMethod(GET);
            httpClientInputs.setHeaders(getAuthHeaders(getOrganizationDetailsInputs.getCommonInputs().getAuthToken()));
            httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
            setCommonHttpInputs(httpClientInputs, getOrganizationDetailsInputs.getCommonInputs());
            return new HttpClientService().execute(httpClientInputs);
        }

        @NotNull
        private static String createOrganizationUrl(@NotNull final String organizationName) throws Exception {
            final URIBuilder uriBuilder = getUriBuilder();
            uriBuilder.setPath(getOrganizationPath(organizationName));
            return uriBuilder.build().toURL().toString();
        }

        @NotNull
        public static String getOrganizationPath(@NotNull final String organizationName) {
            StringBuilder pathString = new StringBuilder()
                    .append(API)
                    .append(API_VERSION)
                    .append(ORGANIZATION_PATH);
//                    .append(organizationName);
            return pathString.toString();
        }

        @NotNull
        private static String getOrganizationDetailsUrl(@NotNull final String organizationName) throws Exception {
            final URIBuilder uriBuilder = getUriBuilder();
            uriBuilder.setPath(getOrganizationDetailsPath(organizationName));
            return uriBuilder.build().toURL().toString();
        }

        @NotNull
        public static String getOrganizationDetailsPath(@NotNull final String organizationName) {
            StringBuilder pathString = new StringBuilder()
                    .append(API)
                    .append(API_VERSION)
                    .append(ORGANIZATION_PATH)
                    .append(organizationName);
            return pathString.toString();
        }


        @NotNull
        public static String createOrganizationBody(TerraformOrganizationInputs createOrganizationInputs, String delimiter) {
            String requestBody = EMPTY;
            ObjectMapper createOrganizationMapper = new ObjectMapper();
            CreateOrganizationRequestBody createBody = new CreateOrganizationRequestBody();
            CreateOrganizationRequestBody.CreateOrganizationData createOrganizationData = createBody.new CreateOrganizationData();
            CreateOrganizationRequestBody.Attributes attributes = createBody.new Attributes();
            attributes.setName(createOrganizationInputs.getCommonInputs().getOrganizationName());
            attributes.setEmail(createOrganizationInputs.getEmail());
            attributes.setSessionTimeout(createOrganizationInputs.getSessionTimeout());
            attributes.setSessionRemember(createOrganizationInputs.getSessionRemember());
            attributes.setDescription(createOrganizationInputs.getOrganizationDescription());
            attributes.setCostEstimationEnabled(Boolean.parseBoolean(createOrganizationInputs.getCostEstimationEnabled()));
            attributes.setCollaboratorAuthPolicy(createOrganizationInputs.getCollaboratorAuthPolicy());
            attributes.setOwnersTeamSamlRoleId(createOrganizationInputs.getOwnersTeamSamlRoleId());

            createOrganizationData.setAttributes(attributes);
            createOrganizationData.setType(ORGANIZATION_TYPE);

            createBody.setData(createOrganizationData);


            try {
                requestBody = createOrganizationMapper.writeValueAsString(createBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return requestBody;

        }
    }

