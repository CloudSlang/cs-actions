/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.vmware.actions.vm;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.vmware.constants.Inputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public class UpdateVM {
    /**
     * Connects to a specified data center and updates a specified virtual machine using the inputs provided.
     *
     * @param host               VMware host or IP - Example: "vc6.subdomain.example.com"
     * @param port               optional - the port to connect through - Examples: "443", "80" - Default: "443"
     * @param protocol           optional - the connection protocol - Valid: "http", "https" - Default: "https"
     * @param username           the VMware username use to connect
     * @param password           the password associated with "username" input
     * @param trustEveryone      optional - if "true" will allow connections from any host, if "false" the connection
     *                           will be allowed only using a valid vCenter certificate - Default: "true"
     *                           Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html
     *                           to see how to import a certificate into Java Keystore and
     *                           https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html
     *                           to see how to obtain a valid vCenter certificate
     * @param closeSession       Whether to use the flow session context to cache the Connection to the host or not. If set to
     *                           "false" it will close and remove any connection from the session context, otherwise the Connection
     *                           will be kept alive and not removed.
     *                           Valid values: "true", "false"
     *                           Default value: "true"
     * @param virtualMachineName the name of the virtual machine that will be updated
     * @param operation          the possible operations that can be applied to update a specified attached device
     *                           ("update" operation is only possible for cpu and memory, "add", "remove" are not allowed
     *                           for cpu and memory devices)
     *                           Valid values: "add", "remove", "update"
     * @param device             the device on which the update operation will be applied - Valid values: "cpu", "memory"
     *                           "disk", "cd", "nic"
     * @param updateValue        the value applied on the specified device during the virtual machine update
     *                           Valid values: "high", "low", "normal", numeric value, label of device when removing
     * @param vmDiskSize         optional - the disk capacity amount (in Mb) attached to the virtual machine that will
     *                           be created. This input will be considered only when "add" operation and "disk" device
     *                           are provided
     * @param vmDiskMode         optional - the memory amount (in Mb) attached to the virtual machine that will will
     *                           be created - Valid values: "persistent", "independent_persistent", "independent_nonpersistent"
     *                           This input will be considered only when "add" operation and "disk" device are provided
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Update Virtual Machine",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> updateVM(@Param(value = HOST, required = true) String host,
                                        @Param(value = PORT) String port,
                                        @Param(value = PROTOCOL) String protocol,
                                        @Param(value = USERNAME, required = true) String username,
                                        @Param(value = PASSWORD, encrypted = true) String password,
                                        @Param(value = TRUST_EVERYONE) String trustEveryone,
                                        @Param(value = CLOSE_SESSION) String closeSession,

                                        @Param(value = VM_NAME, required = true) String virtualMachineName,
                                        @Param(value = OPERATION, required = true) String operation,
                                        @Param(value = DEVICE, required = true) String device,
                                        @Param(value = UPDATE_VALUE, required = true) String updateValue,
                                        @Param(value = VM_DISK_SIZE) String vmDiskSize,
                                        @Param(value = VM_DISK_MODE) String vmDiskMode,
                                        @Param(value = VMWARE_GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Connection>> globalSessionObject) {


        try {
            final HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(defaultIfEmpty(trustEveryone, FALSE))
                    .withCloseSession(defaultIfEmpty(closeSession, TRUE))
                    .withGlobalSessionObject(globalSessionObject)
                    .build();

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withVirtualMachineName(virtualMachineName)
                    .withOperation(operation)
                    .withDevice(device)
                    .withUpdateValue(updateValue)
                    .withLongVmDiskSize(vmDiskSize)
                    .withDiskMode(vmDiskMode)
                    .build();

            return new VmService().updateVM(httpInputs, vmInputs);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
