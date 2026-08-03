/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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





package io.cloudslang.content.azure.services;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;
import io.cloudslang.content.azure.entities.AuthorizationTokenInputs;
import io.cloudslang.content.azure.utils.DateUtilities;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Mac;
import java.net.Proxy;
import java.util.Collections;
import java.util.Date;

import static io.cloudslang.content.azure.utils.Constants.SHARED_ACCESS_SIGNATURE;
import static io.cloudslang.content.azure.utils.HttpUtils.getProxy;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by victor on 28.09.2016.
 */
public class AuthorizationTokenImpl {

    @NotNull
    public static String getToken(@NotNull final String identifier, @NotNull final String primaryOrSecondaryKey, @NotNull final Date expiryDate) {
        final Mac sha512Hmac = HmacUtils.getHmacSha512(primaryOrSecondaryKey.getBytes(UTF_8));
        final String dataToSign = String.format("%s\n%s", identifier, DateUtilities.formatDate(expiryDate));
        final byte[] encodedBytes = Base64.encodeBase64(sha512Hmac.doFinal(dataToSign.getBytes(UTF_8)));
        final String encodedString = new String(encodedBytes, UTF_8);
        return String.format(SHARED_ACCESS_SIGNATURE, identifier, DateUtilities.formatDate(expiryDate), encodedString);
    }

    @NotNull
    public static IAuthenticationResult getToken(@NotNull final AuthorizationTokenInputs inputs) throws Exception {
        final Proxy proxy = getProxy(inputs.getProxyHost(), inputs.getProxyPort(), inputs.getProxyUsername(), inputs.getProxyPassword());
        final PublicClientApplication.Builder appBuilder = PublicClientApplication
                .builder(inputs.getClientId())
                .authority(inputs.getAuthority());
        if (proxy != Proxy.NO_PROXY) {
            appBuilder.proxy(proxy);
        }
        final PublicClientApplication app = appBuilder.build();
        final UserNamePasswordParameters parameters = UserNamePasswordParameters
                .builder(Collections.singleton(inputs.getResource() + "/.default"), inputs.getUsername(), inputs.getPassword().toCharArray())
                .build();
        return app.acquireToken(parameters).get();
    }
}
