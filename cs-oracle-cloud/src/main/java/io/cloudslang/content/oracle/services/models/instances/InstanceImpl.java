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

package io.cloudslang.content.oracle.services.models.instances;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.services.SignerImpl;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.oracle.utils.HttpUtils.getQueryParams;
import static io.cloudslang.content.oracle.utils.HttpUtils.getUriBuilder;

public class InstanceImpl {

    @NotNull
    public static Map<String, String> listInstances(@NotNull final OCIInstanceInputs listInstancesInputs)
            throws Exception {

        String apiKey = (listInstancesInputs.getCommonInputs().getTenancyOcid() + "/"
                + listInstancesInputs.getCommonInputs().getUserOcid() + "/"
                + listInstancesInputs.getCommonInputs().getFingerPrint());
        SignerImpl signerImpl = new SignerImpl();
        PrivateKey privateKey = signerImpl.loadPrivateKey(listInstancesInputs.getCommonInputs().getPrivateKeyFilename());
        SignerImpl.RequestSigner signer = new SignerImpl.RequestSigner(apiKey, privateKey);
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(listInstancesInputs.getCommonInputs().getRegion()) + getQueryParams(listInstancesInputs.getCompartmentOcid()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl());
        Map<String, String> headers = signer.signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(getAuthHeaders(headers));
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    private static String listInstancesUrl(@NotNull final String region) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder(region);
        uriBuilder.setPath(listInstancesPath());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String listInstancesPath() {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_INSTANCES);
        return pathString.toString();
    }
}