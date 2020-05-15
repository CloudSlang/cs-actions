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
import io.cloudslang.content.oracle.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.entities.inputs.OCIInstanceInputs;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Map;

import static io.cloudslang.content.oracle.utils.Constants.Common.API_VERSION;
import static io.cloudslang.content.oracle.utils.Constants.Common.LIST_INSTANCES;
import static io.cloudslang.content.oracle.utils.HttpUtils.getUriBuilder;
//import io.cloudslang.content.oracle.entities.signature.Signature.RequestSigner;
import static io.cloudslang.content.oracle.entities.signature.Signature.*;

public class InstanceImpl {

    @NotNull
    public static Map<String, String> listInstances(@NotNull final OCIInstanceInputs listInstancesInputs)
            throws Exception {

        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listInstancesUrl(listInstancesInputs.getCompartmentOcid()));
        String uri = httpClientInputs.getUrl();
        InstanceImpl listInstanceResp = new InstanceImpl();
        listInstanceResp.getAPIResponse(listInstancesInputs.getCommonInputs().getTenancyOcId(),listInstancesInputs.getCommonInputs().getuserOcid(),listInstancesInputs.getCommonInputs().getFingerPrint(),listInstancesInputs.getCommonInputs().getPrivateKeyFilename(),uri);


//
//
//        setCommonHttpInputs(httpClientInputs, listInstancesInputs);
//        httpClientInputs.setAuthType(ANONYMOUS);
//        httpClientInputs.setMethod(GET);
//        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
//        httpClientInputs.setQueryParams(getQueryParams(listInstancesInputs.getPageNumber(),
//                listInstancesInputs.getPageSize()));
//        httpClientInputs.setResponseCharacterSet(listInstancesInputs.getResponseCharacterSet());
//        httpClientInputs.setHeaders(getAuthHeaders(listInstancesInputs.getAuthToken()));
       return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String listInstancesUrl(@NotNull final String compartmentOcid) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(listInstancesPath(compartmentOcid));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String listInstancesPath(@NotNull final String compartmentOcid) {
        StringBuilder pathString = new StringBuilder()
                .append(API_VERSION)
                .append(LIST_INSTANCES)
                .append(compartmentOcid);
        return pathString.toString();
    }

    @NotNull
    public String getAPIResponse(String tenancyOcid,
                                 String userOcid,
                                 String fingerPrint,
                                 String privateKeyFile,
                                 String uri)
    {
        HttpRequestBase request;
        String responseAsString = null;

        String apiKey = (tenancyOcid+"/"
                + userOcid+"/"
                + fingerPrint);

        PrivateKey privateKey = loadKey(privateKeyFile);
        RequestSigner signer = new RequestSigner(apiKey, privateKey);

        request = new HttpGet(uri);
        signer.signRequest(request);

        CloseableHttpClient client = HttpClientBuilder.create().build();

        // In case you need a proxy, please uncomment the followin code

        /*HttpHost proxy = new HttpHost("proxy.example.com", 80);
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        request.setConfig(config);*/

        try
        {

            HttpResponse response = client.execute(request);
            responseAsString = EntityUtils.toString(response.getEntity());
            System.out.println(responseAsString);

        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return responseAsString;
    }
}
