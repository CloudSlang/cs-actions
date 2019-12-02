package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ListOAuthClientImplTest.class)
public class ListOAuthClientImplTest {
    public final String EXPECTED_OAUTH_PATH="/api/v2/organizations/test/oauth-clients";
    private final ListOAuthClientInputs getListOAuthClientInputs=ListOAuthClientInputs.builder().commonInputs(Inputs.builder()
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

    private final Inputs getOrganizationName=Inputs.builder()
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
