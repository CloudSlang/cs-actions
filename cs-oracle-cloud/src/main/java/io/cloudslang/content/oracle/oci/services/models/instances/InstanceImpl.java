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

package io.cloudslang.content.oracle.oci.services.models.instances;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.oracle.oci.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.oci.services.SignerImpl;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.oracle.oci.utils.HttpUtils.getQueryParams;

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
        httpClientInputs.setUrl(listInstancesUrl(listInstancesInputs.getCommonInputs().getRegion()));
        httpClientInputs.setQueryParams(getQueryParams(listInstancesInputs.getCompartmentOcid()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        URI uri = URI.create(httpClientInputs.getUrl()+QUERY+httpClientInputs.getQueryParams());
        Map<String, String> headers = signer.signRequest(uri, GET, EMPTY);
        httpClientInputs.setHeaders(HttpUtils.getAuthHeaders(headers));
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    private static String listInstancesUrl(@NotNull final String region) throws Exception {
        final URIBuilder uriBuilder = HttpUtils.getUriBuilder(region);
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