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

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ListOAuthClientImplTest.class)
public class ListOAuthClientImplTest {
    public final String EXPECTED_OAUTH_PATH="/api/v2/organizations/test/oauth-clients";
    private final ListOAuthClientInputs getListOAuthClientInputs=ListOAuthClientInputs.builder().commonInputs(TerraformCommonInputs.builder()
            .organizationName("")
            .authToken("")
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

    private final TerraformCommonInputs getOrganizationName=TerraformCommonInputs.builder()
            .organizationName("test")
            .build();


    @Test(expected = IllegalArgumentException.class)
    public void listOAuthClient() throws Exception {
        ListOauthClientImpl.listOAuthClient(getListOAuthClientInputs);

    }
    @Test
    public void getListOAuthClientPathTest() {
        String listOAuthPath=ListOauthClientImpl.getListOAuthClientPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_OAUTH_PATH,listOAuthPath);
    }


}
