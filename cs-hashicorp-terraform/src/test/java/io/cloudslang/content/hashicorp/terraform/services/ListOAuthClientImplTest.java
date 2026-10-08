

package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.hashicorp.terraform.services.HttpClientTestSupport.assertHttpClientException;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


    @Test
    public void listOAuthClient() throws Exception {
        assertHttpClientException(() -> ListOauthClientImpl.listOAuthClient(getListOAuthClientInputs));
    }
    @Test
    public void getListOAuthClientPathTest() {
        String listOAuthPath=ListOauthClientImpl.getListOAuthClientPath(getOrganizationName.getOrganizationName());
        assertEquals(EXPECTED_OAUTH_PATH,listOAuthPath);
    }


}
