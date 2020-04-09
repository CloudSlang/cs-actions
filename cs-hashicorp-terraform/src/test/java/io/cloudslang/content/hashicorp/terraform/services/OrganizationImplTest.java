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

import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.OrganizationImpl.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OrganizationImpl.class)
public class OrganizationImplTest {

    public static final String DELIMITER = ",";
    private static final String ORGANIZATION_NAME = "test";
    private static final String EXPECTED_ORGANIZATION_REQUEST_BODY = "{\"data\":{\"attributes\":{\"description\":\"test\",\"sessionTimeout\":\"20160\",\"sessionRemember\":\"20160\"," +
            "\"collaboratorAuthPolicy\":\"password\",\"name\":\"test\",\"email\":\"test\",\"cost-estimation\":false,\"owners-team-saml-role-id\":\"\"}," +
            "\"type\":\"organizations\"}}";
    private static final String EXPECTED_GET_ORGANIZATION_PATH = "/api/v2/organizations/test";
    private static final String EXPECTED_ORGANIZATION_PATH = "/api/v2/organizations/";
    private static final String EXPECTED_DELETE_ORGANIZATION_PATH = "/api/v2/organizations/test";
    private static final String EXPECTED_LIST_ORGANIZATIONS_PATH = "/api/v2/organizations/";
    private static final String EXPECTED_UPDATE_ORGANIZATION_PATH = "/api/v2/organizations/test";
    private final TerraformOrganizationInputs invalidCreateOrganizationInputs = TerraformOrganizationInputs.builder()
            .organizationDescription("test")
            .email("test")
            .sessionTimeout(SESSION_TIMEOUT)
            .sessionRemember(SESSION_REMEMBER)
            .collaboratorAuthPolicy(COLLABORATOR_AUTH_POLICY)
            .costEstimationEnabled("false")
            .ownersTeamSamlRoleId("")
            .commonInputs(TerraformCommonInputs.builder()
                    .organizationName(ORGANIZATION_NAME)
                    .requestBody("")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .trustAllRoots("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .connectTimeout("")
                    .socketTimeout("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
            .build();

    private final TerraformCommonInputs getOrganizationName = TerraformCommonInputs.builder()
            .organizationName("test")
            .build();

    @Test(expected = IllegalArgumentException.class)
    public void createOrganizationThrows() throws Exception {
        OrganizationImpl.createOrganization(invalidCreateOrganizationInputs);
    }

    @Test
    public void getOrganizationPathTest() {
        final String path = getOrganizationPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_ORGANIZATION_PATH, path);
    }

    @Test
    public void getOrganizationRequestBody() {
        final String requestBody = createOrganizationBody(invalidCreateOrganizationInputs, DELIMITER);
        assertEquals(EXPECTED_ORGANIZATION_REQUEST_BODY, requestBody);
    }

    @Test
    public void getDeleteOrganizationPathTest() {
        final String path = getOrganizationDetailsPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_DELETE_ORGANIZATION_PATH, path);
    }

    @Test
    public void getListOrganizationsPathTest() {
        final String path = getOrganizationPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_LIST_ORGANIZATIONS_PATH, path);
    }

    @Test
    public void getOrganizationDetailsPathTest() {
        final String path = getOrganizationDetailsPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_GET_ORGANIZATION_PATH, path);
    }

    @Test
    public void getUpdateOrganizationPath() {
        String path = getOrganizationDetailsPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_UPDATE_ORGANIZATION_PATH, path);

    }
}

