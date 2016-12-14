/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.vm;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> updateVM(@Param(value = Inputs.HOST, required = true) String host,
                                        @Param(Inputs.PORT) String port,
                                        @Param(Inputs.PROTOCOL) String protocol,
                                        @Param(value = Inputs.USERNAME, required = true) String username,
                                        @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                        @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                        @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                        @Param(value = Inputs.OPERATION, required = true) String operation,
                                        @Param(value = Inputs.DEVICE, required = true) String device,
                                        @Param(value = Inputs.UPDATE_VALUE, required = true) String updateValue,
                                        @Param(Inputs.VM_DISK_SIZE) String vmDiskSize,
                                        @Param(Inputs.VM_DISK_MODE) String vmDiskMode) {

        Map<String, String> resultMap = new HashMap<>();

        try {
            HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();

            VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withVirtualMachineName(virtualMachineName)
                    .withOperation(operation)
                    .withDevice(device)
                    .withUpdateValue(updateValue)
                    .withLongVmDiskSize(vmDiskSize)
                    .withDiskMode(vmDiskMode)
                    .build();

            resultMap = new VmService().updateVM(httpInputs, vmInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}
