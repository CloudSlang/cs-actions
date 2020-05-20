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

package io.cloudslang.content.nutanix.prism.service;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetVMDetailsInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMdetailsInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getQueryParams;


public class VMImpl {

    @NotNull
    public static Map<String, String> listVMs(@NotNull final NutanixListVMdetailsInputs nutanixListVMdetailsInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getListVMsURL(nutanixListVMdetailsInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixListVMdetailsInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixListVMdetailsInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
      httpClientInputs.setQueryParams(getQueryParams(nutanixListVMdetailsInputs.getFilter(),nutanixListVMdetailsInputs.getOffset(),
                nutanixListVMdetailsInputs.getLength(),nutanixListVMdetailsInputs.getSortorder(),nutanixListVMdetailsInputs.getSortattribute(),nutanixListVMdetailsInputs.getIncludeVMDiskConfigInfo(),
                nutanixListVMdetailsInputs.getIncludeVMNicConfigInfo()));
        HttpCommons.setCommonHttpInputs(httpClientInputs, nutanixListVMdetailsInputs.getCommonInputs());

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getVMDetails(@NotNull final NutanixGetVMDetailsInputs nutanixGetVMDetailsInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getVMDetailsURL(nutanixGetVMDetailsInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixGetVMDetailsInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixGetVMDetailsInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        httpClientInputs.setQueryParams(getQueryParams(nutanixGetVMDetailsInputs.getIncludeVMDiskConfigInfo(),
                nutanixGetVMDetailsInputs.getIncludeVMNicConfigInfo()));
        HttpCommons.setCommonHttpInputs(httpClientInputs, nutanixGetVMDetailsInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String getListVMsURL(NutanixListVMdetailsInputs nutanixListVMdetailsInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixListVMdetailsInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixListVMdetailsInputs.getCommonInputs().getAPIVersion())
                .append(PATH_SEPARATOR)
                .append(GET_VM_DETAILS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVMDetailsURL(NutanixGetVMDetailsInputs nutanixGetVMDetailsInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixGetVMDetailsInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixGetVMDetailsInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixGetVMDetailsInputs.getVMUUID());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static URIBuilder getUriBuilder(NutanixCommonInputs nutanixCommonInputs) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(nutanixCommonInputs.getHostname());
        uriBuilder.setPort(Integer.parseInt(nutanixCommonInputs.getPort()));
        uriBuilder.setScheme(nutanixCommonInputs.getProtocol());
        return uriBuilder;
    }
}
