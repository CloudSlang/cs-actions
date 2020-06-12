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
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteNICInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DeleteNICConstants.DELETE_NIC_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getQueryParams;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class NicImpl {

    @NotNull
    public static Map<String, String> deleteNic(@NotNull final NutanixDeleteNICInputs nutanixDeleteNICInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(deleteNicURL(nutanixDeleteNICInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setUsername(nutanixDeleteNICInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixDeleteNICInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        if (isEmpty(nutanixDeleteNICInputs.getVmLogicalTimestamp())) {
            httpClientInputs.setQueryParams(getQueryParams(nutanixDeleteNICInputs.getVmLogicalTimestamp()));
        }
        setCommonHttpInputs(httpClientInputs, nutanixDeleteNICInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String deleteNicURL(NutanixDeleteNICInputs nutanixDeleteNICInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixDeleteNICInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixDeleteNICInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixDeleteNICInputs.getVMUUID())
                .append(DELETE_NIC_PATH)
                .append(nutanixDeleteNICInputs.getNicMacAddress());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }
}
