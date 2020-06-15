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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.exceptions.NutanixDetachDiskException;
import io.cloudslang.content.nutanix.prism.service.models.disks.DetachDisksRequestBody;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DetachDisksConstants.DETACH_DISKS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetVMDetailsConstants.GET_VM_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class DiskImpl {

    @NotNull
    public static String detachDisksBody(NutanixDetachDisksInputs nutanixDetachDisksInputs) throws NutanixDetachDiskException {
        String requestBody = EMPTY;
        ObjectMapper detachDisksMapper = new ObjectMapper();
        DetachDisksRequestBody detachDisksRequestBody = new DetachDisksRequestBody();
        detachDisksRequestBody.setVmUUID(nutanixDetachDisksInputs.getVMUUID());
        ArrayList vmDiskList = new ArrayList();
        String[] deviceBusArray = nutanixDetachDisksInputs.getDeviceBusList().split(",");
        String[] deviceIndexArray = nutanixDetachDisksInputs.getDeviceIndexList().split(",");
        String[] vmDiskUUIDArray = nutanixDetachDisksInputs.getVmDiskUUIDList().split(",");
        if((vmDiskUUIDArray.length == deviceBusArray.length) && (vmDiskUUIDArray.length == deviceIndexArray.length)) {
            for (int i = 0; i < vmDiskUUIDArray.length; i++) {
                DetachDisksRequestBody.VMDisks vmDisks = detachDisksRequestBody.new VMDisks();
                DetachDisksRequestBody.DiskAddress diskAddress = detachDisksRequestBody.new DiskAddress();
                diskAddress.setDeviceBus(deviceBusArray[i]);
                diskAddress.setDeviceIndex(deviceIndexArray[i]);
                diskAddress.setVmDiskUUID(vmDiskUUIDArray[i]);
                vmDisks.setDiskAddress(diskAddress);

                vmDiskList.add(vmDisks);
            }

            detachDisksRequestBody.setVmDisks(vmDiskList);
            try {
                requestBody = detachDisksMapper.writeValueAsString(detachDisksRequestBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            throw new NutanixDetachDiskException("Size of vmDiskUUIDList, deviceBusList and deviceIndexList should be same");
        }
        return requestBody;

    }

    @NotNull
    public static Map<String, String> detachDisks(@NotNull final NutanixDetachDisksInputs nutanixDetachDisksInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(detachDisksURL(nutanixDetachDisksInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);

        httpClientInputs.setBody(detachDisksBody(nutanixDetachDisksInputs));

        httpClientInputs.setUsername(nutanixDetachDisksInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixDetachDisksInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, nutanixDetachDisksInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String detachDisksURL(NutanixDetachDisksInputs nutanixDetachDisksInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixDetachDisksInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixDetachDisksInputs.getCommonInputs().getAPIVersion())
                .append(GET_VM_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixDetachDisksInputs.getVMUUID())
                .append(DETACH_DISKS_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }
}
