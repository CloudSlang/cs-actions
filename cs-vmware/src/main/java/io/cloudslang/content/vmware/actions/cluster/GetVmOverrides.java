/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.cluster;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_VM_NAME_OR_ID_OR_NONE;
import static io.cloudslang.content.vmware.constants.Inputs.CLUSTER_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.HOST;
import static io.cloudslang.content.vmware.constants.Inputs.HOSTNAME;
import static io.cloudslang.content.vmware.constants.Inputs.PASSWORD;
import static io.cloudslang.content.vmware.constants.Inputs.PORT;
import static io.cloudslang.content.vmware.constants.Inputs.PROTOCOL;
import static io.cloudslang.content.vmware.constants.Inputs.TRUST_EVERYONE;
import static io.cloudslang.content.vmware.constants.Inputs.USERNAME;
import static io.cloudslang.content.vmware.constants.Inputs.VM_ID;
import static io.cloudslang.content.vmware.constants.Inputs.VM_NAME;

/**
 * Created by Tirla Alin.
 * Date: 31/3/2017.
 */
public class GetVmOverrides {

    /**
     * @param host                  VMware host or IP.
     *                              Example: "vc6.subdomain.example.com"
     * @param port                  optional - The port to connect through.
     *                              Default Value: "443"
     *                              Examples: "443", "80"
     * @param protocol              optional - The connection protocol.
     *                              Default Value: "https"
     *                              Valid Values: "http", "https"
     * @param username              The VMware username used to connect.
     * @param password              The password associated with "username" input.
     * @param trustEveryone         optional - If "true" will allow connections from any host, if "false" the connection
     *                              will be allowed only using a valid vCenter certificate.
     *                              Default Value: "false"
     * @param hostname              The name of the target host to be queried to retrieve the supported guest OSes
     *                              Example: "host123.subdomain.example.com"
     * @param virtualMachineName    optional - The name of the virtual machine for which the override will be created. This input
     *                              is mutually exclusive with virtualMachineId.
     * @param virtualMachineId      optional - The id of the virtual machine for which the override will be created. This input is
     *                              mutually exclusive with virtualMachineName.
     *                              Example: "vm-1230"
     * @param clusterName           The name of the cluster.
     * @return                      A map containing the output of the operation. Keys present in the map are:
     * <br><b>returnResult</b>      The primary output.
     * <br><b>returnCode</b>        The return code of the operation. 0 if the operation goes to success, -1 if the
     *                              operation goes to failure.
     * <br><b>exception</b>         The exception message if the operation goes to failure.
     */

    @Action(name = "Get VM Overrides Priority",
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

    public Map<String, String> getVmOverrides(@Param(value = HOST, required = true) String host,
                                              @Param(value = PORT) String port,
                                              @Param(value = PROTOCOL) String protocol,
                                              @Param(value = USERNAME, required = true) String username,
                                              @Param(value = PASSWORD, encrypted = true, required = true) String password,
                                              @Param(value = TRUST_EVERYONE) String trustEveryone,
                                              @Param(value = HOSTNAME, required = true) String hostname,
                                              @Param(value = VM_NAME) String virtualMachineName,
                                              @Param(value = VM_ID) String virtualMachineId,
                                              @Param(value = CLUSTER_NAME, required = true) String clusterName) {
        try {
            final HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();

            InputUtils.checkOptionalMutuallyExclusiveInputs(virtualMachineName, virtualMachineId, PROVIDE_VM_NAME_OR_ID_OR_NONE);

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withClusterName(clusterName)
                    .withHostname(hostname)
                    .withVirtualMachineName(virtualMachineName)
                    .withVirtualMachineId(virtualMachineId)
                    .build();

            return OutputUtilities.getSuccessResultsMap(new ClusterComputeResourceService().getVmOverride(httpInputs, vmInputs));
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
