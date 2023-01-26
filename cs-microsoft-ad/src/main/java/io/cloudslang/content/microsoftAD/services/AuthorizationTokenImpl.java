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

package io.cloudslang.content.microsoftAD.services;

import com.microsoft.aad.msal4j.*;
import io.cloudslang.content.microsoftAD.entities.AuthorizationTokenInputs;
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

import static io.cloudslang.content.microsoftAD.utils.Constants.API;
import static io.cloudslang.content.microsoftAD.utils.Constants.EXCEPTION_ACQUIRE_TOKEN_FAILED;

public class AuthorizationTokenImpl {

    @NotNull
    public static IAuthenticationResult getToken(@NotNull final AuthorizationTokenInputs inputs) throws Exception {
        Set<String> scope = new HashSet<>(Arrays.asList(inputs.getScope().split(",")));
        Proxy proxy = getProxy(inputs);

        //Verifying if loginType is API to instantiate ClientCredential object
        if (inputs.getLoginType().equalsIgnoreCase(API)) {
            return getAccessTokenByClientCredentialGrant(inputs, scope, proxy);
        }
        //Otherwise, the loginType is Native since the verification was already made in the @Action
        return getAccessTokenPublicClient(inputs, scope, proxy);
    }

    private static IAuthenticationResult getAccessTokenByClientCredentialGrant(@NotNull final AuthorizationTokenInputs inputs,
                                                                               @NotNull final Set<String> scope,
                                                                               @NotNull final Proxy proxy) throws Exception {

        ConfidentialClientApplication confidentialClientApplication = ConfidentialClientApplication
                .builder(inputs.getClientId(), ClientCredentialFactory.createFromSecret(inputs.getClientSecret()))
                .authority(inputs.getAuthority())
                .proxy(proxy)
                .build();

        ClientCredentialParameters clientCredentialParameters = ClientCredentialParameters.builder(scope).build();

        final CompletableFuture<IAuthenticationResult> future = confidentialClientApplication.acquireToken(clientCredentialParameters);
        try {
            return future.get();
        } catch (Exception e) {
            if (e.getMessage().equals("java.lang.NullPointerException"))
                throw new Exception(EXCEPTION_ACQUIRE_TOKEN_FAILED);
            else throw new Exception(e.getMessage());
        }
    }

    private static IAuthenticationResult getAccessTokenPublicClient(@NotNull final AuthorizationTokenInputs inputs,
                                                                    @NotNull final Set<String> scope,
                                                                    @NotNull final Proxy proxy) throws Exception {
        final PublicClientApplication publicClientApplication = PublicClientApplication
                .builder(inputs.getClientId())
                .authority(inputs.getAuthority())
                .proxy(proxy)
                .build();

        UserNamePasswordParameters userNamePasswordParameters = UserNamePasswordParameters.builder(scope, inputs.getUsername(),
                inputs.getPassword().toCharArray()).build();

        CompletableFuture<IAuthenticationResult> future = publicClientApplication.acquireToken(userNamePasswordParameters);
        try {
            return future.get();
        } catch (Exception e) {
            if (e.getMessage().equals("java.lang.NullPointerException"))
                throw new Exception(EXCEPTION_ACQUIRE_TOKEN_FAILED);
            else throw new Exception(e.getMessage());
        }
    }

    private static Proxy getProxy(@NotNull final AuthorizationTokenInputs inputs) {
        Proxy proxy;

        if (!StringUtils.isEmpty(inputs.getProxyHost())) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(inputs.getProxyHost(), inputs.getProxyPort()));
            if ((!inputs.getProxyUsername().isEmpty()) && (!inputs.getProxyPassword().isEmpty())) {
                Authenticator authenticator = new Authenticator() {

                    public PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(inputs.getProxyUsername(),
                                inputs.getProxyPassword().toCharArray()));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
        } else
            proxy = Proxy.NO_PROXY;

        return proxy;
    }
}