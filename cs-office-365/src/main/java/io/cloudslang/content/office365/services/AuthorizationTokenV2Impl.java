package io.cloudslang.content.office365.services;

import com.microsoft.aad.msal4j.*;
import io.cloudslang.content.office365.entities.AuthorizationTokenInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.cloudslang.content.office365.utils.Constants.API;
import static io.cloudslang.content.office365.utils.Constants.EXCEPTION_ACQUIRE_TOKEN_FAILED;

public class AuthorizationTokenV2Impl {

    @NotNull
    public static IAuthenticationResult getToken(@NotNull final AuthorizationTokenInputs inputs) throws Exception {
        Set<String> scopes = new HashSet<>(Arrays.asList(inputs.getScope().split(",")));

        //Verifying if loginType is API to instantiate ClientCredential object
        if (inputs.getLoginType().equalsIgnoreCase(API)) {
            ConfidentialClientApplication clientApplication = ConfidentialClientApplication.builder(
                    inputs.getClientId(), ClientCredentialFactory.createFromSecret(inputs.getClientSecret()))
                    .authority(inputs.getAuthority()).proxy(getProxy(inputs)).build();

            ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                    scopes)
                    .build();

            return acquireToken(clientApplication, clientCredentialParam);
        } else {
            //Otherwise, the loginType is Native since the verification was already made in the @Action
            PublicClientApplication clientApplication = PublicClientApplication.builder(inputs.getClientId()).authority(inputs.getAuthority()).proxy(getProxy(inputs)).build();

            UserNamePasswordParameters userNamePasswordParameters = UserNamePasswordParameters.builder(scopes, inputs.getUsername(), inputs.getPassword().toCharArray()).build();

            return acquireToken(clientApplication, userNamePasswordParameters);
        }
    }

    private static IAuthenticationResult acquireToken(@NotNull final ConfidentialClientApplication clientApplication, @NotNull final ClientCredentialParameters clientCredentialParam) throws Exception {
       //ClientCredentialParameters

        final CompletableFuture<IAuthenticationResult> future = clientApplication.acquireToken(clientCredentialParam);
        try{
            return future.get();
        }catch(Exception e){
            if (e.getMessage().equals("java.lang.NullPointerException"))
                throw new Exception(EXCEPTION_ACQUIRE_TOKEN_FAILED);
            else throw new Exception(e.getMessage());
        }
    }

    private static IAuthenticationResult acquireToken(@NotNull final  PublicClientApplication  clientApplication, @NotNull final UserNamePasswordParameters userNamePasswordParameters) throws Exception {
        //UsernamePasswordParameters

        final CompletableFuture<IAuthenticationResult> future = clientApplication.acquireToken(userNamePasswordParameters);
        try{
            return future.get();
        }catch(Exception e){
            if (e.getMessage().equals("java.lang.NullPointerException"))
                throw new Exception(EXCEPTION_ACQUIRE_TOKEN_FAILED);
            else throw new Exception(e.getMessage());
        }
    }

    private static Proxy getProxy(@NotNull final AuthorizationTokenInputs inputs) {
        Proxy proxy;
        if (!StringUtils.isEmpty(inputs.getProxyHost())) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(inputs.getProxyHost(), inputs.getProxyPort()));
            if (!inputs.getProxyUsername().isEmpty() && !inputs.getPassword().isEmpty()) {
                Authenticator authenticator = new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(inputs.getProxyUsername(), inputs.getProxyPassword().toCharArray()));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
        } else
            proxy = Proxy.NO_PROXY;

        return proxy;
    }

}
