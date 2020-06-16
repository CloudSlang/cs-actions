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

package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteNICInputs;
import io.cloudslang.content.nutanix.prism.services.models.nics.AddNicRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.AddNICConstants.ADD_NIC_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DeleteNICConstants.DELETE_NIC_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getQueryParams;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NicImpl {

    public static Map<String, String> AddNic(@NotNull final NutanixAddNicInputs nutanixAddNicInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(AddNicURL(nutanixAddNicInputs));
        setCommonHttpInputs(httpClientInputs, nutanixAddNicInputs.getCommonInputs());
        try {
            httpClientInputs.setBody(AddNicBody(nutanixAddNicInputs));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(nutanixAddNicInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixAddNicInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return new HttpClientService().execute(httpClientInputs);
    }

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
    public static String AddNicURL(NutanixAddNicInputs nutanixAttachNicInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixAttachNicInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixAttachNicInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixAttachNicInputs.getVmUUID())
                .append(ADD_NIC_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
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

    @NotNull
    public static String AddNicBody(NutanixAddNicInputs nutanixAttachNicInputs) {
        String requestBody = EMPTY;
        ObjectMapper AttachNicMapper = new ObjectMapper();
        AddNicRequestBody AddNicBody = new AddNicRequestBody();
        ArrayList spec_list = new ArrayList();
        String[] network_uuid = nutanixAttachNicInputs.getNetworkUUID().split(",");
        String[] requested_ip_address = nutanixAttachNicInputs.getRequestedIPAddress().split(",");
        String[] is_connected = nutanixAttachNicInputs.getIsConnected().split(",");
        String[] vlan_Id = nutanixAttachNicInputs.getVlanid().split(",");
        if (requested_ip_address.length == is_connected.length && requested_ip_address.length == vlan_Id.length) {
            for (int i = 0; i < requested_ip_address.length; i++) {
                AddNicRequestBody.VMNics vmNics = AddNicBody.new VMNics();
                vmNics.setNetwork_uuid(network_uuid[i]);
                vmNics.setRequested_ip_address(requested_ip_address[i]);
                vmNics.setIs_connected(Boolean.parseBoolean(is_connected[i]));
                vmNics.setVlan_id(vlan_Id[i]);
                spec_list.add(vmNics);
            }
        } else {
            for (int i = 0; i < requested_ip_address.length; i++) {
                AddNicRequestBody.VMNics vmNics = AddNicBody.new VMNics();
                vmNics.setNetwork_uuid(network_uuid[i]);
                vmNics.setRequested_ip_address(requested_ip_address[i]);
                spec_list.add(vmNics);
            }
        }
        AddNicBody.setSpec_list(spec_list);

        try {
            requestBody = AttachNicMapper.writeValueAsString(AddNicBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;
    }
}
