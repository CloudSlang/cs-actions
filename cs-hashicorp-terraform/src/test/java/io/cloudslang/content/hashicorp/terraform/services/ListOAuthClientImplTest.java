package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListOAuthClientImplTest.class)
public class ListOAuthClientImplTest {
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

    @Test(expected = IllegalArgumentException.class)
    public void listOAuthClient() throws Exception {
        ListOauthClientImpl.listOAuthClient(getListOAuthClientInputs);

    }
}
