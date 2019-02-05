/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.services;

import com.microsoft.aad.adal4j.AsymmetricKeyCredential;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import io.cloudslang.content.office365.entities.AuthorizationTokenInputs;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static io.cloudslang.content.office365.utils.Constants.API;
import static io.cloudslang.content.office365.utils.Constants.CERTIFICATE;
import static io.cloudslang.content.office365.utils.HttpUtils.getProxy;

public class AuthorizationTokenImpl {

    @NotNull
    public static AuthenticationResult getToken(@NotNull final AuthorizationTokenInputs inputs) throws Exception {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final AuthenticationContext context = new AuthenticationContext(inputs.getAuthority(), false, service);
        context.setProxy(getProxy(inputs.getProxyHost(), inputs.getProxyPort(), inputs.getProxyUsername(), inputs.getProxyPassword()));

        //Verifying if loginType is API to instantiate ClientCredential object
        if (inputs.getLoginType().equalsIgnoreCase(API)) {
            final ClientCredential credential = new ClientCredential(inputs.getClientId(), inputs.getClientSecret());
            return acquireToken(context, inputs, credential, service);
        }
        if (inputs.getLoginType().equalsIgnoreCase(CERTIFICATE)) {
            final AsymmetricKeyCredential asymmetricKeyCredential = AsymmetricKeyCredential.create(inputs.getClientId(),
                    new FileInputStream(inputs.getCertificatePath()), inputs.getCertificatePassword());
            return acquireToken(context, inputs, asymmetricKeyCredential);
        }
        //Otherwise, the loginType is Native since the verification was already made in the @Action
        return acquireToken(context, inputs, service);
    }

    @NotNull
    private static AuthenticationResult acquireToken(@NotNull final AuthenticationContext context, @NotNull final AuthorizationTokenInputs inputs, @NotNull ClientCredential credential, @NotNull ExecutorService service) throws Exception {
        final Future<AuthenticationResult> future = context.acquireToken(inputs.getResource(), credential, null);
        service.shutdown();
        return future.get();
    }

    @NotNull
    private static AuthenticationResult acquireToken(@NotNull final AuthenticationContext context, @NotNull final AuthorizationTokenInputs inputs, @NotNull ExecutorService service) throws Exception {
        final Future<AuthenticationResult> future = context.acquireToken(inputs.getResource(), inputs.getClientId(), inputs.getUsername(), inputs.getPassword(), null);
        service.shutdown();
        return future.get();
    }

    @NotNull
    private static AuthenticationResult acquireToken(@NotNull final AuthenticationContext context, @NotNull final AuthorizationTokenInputs inputs,
                                                     @NotNull final AsymmetricKeyCredential asymmetricKeyCredential) throws ExecutionException, InterruptedException {
        final Future<AuthenticationResult> future = context.acquireToken(inputs.getResource(), asymmetricKeyCredential, null);
        return future.get();
    }
}