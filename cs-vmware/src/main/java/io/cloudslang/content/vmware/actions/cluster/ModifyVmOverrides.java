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
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_VM_NAME_OR_ID;
import static io.cloudslang.content.vmware.constants.Inputs.CLUSTER_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.HOST;
import static io.cloudslang.content.vmware.constants.Inputs.HOSTNAME;
import static io.cloudslang.content.vmware.constants.Inputs.PASSWORD;
import static io.cloudslang.content.vmware.constants.Inputs.PORT;
import static io.cloudslang.content.vmware.constants.Inputs.PROTOCOL;
import static io.cloudslang.content.vmware.constants.Inputs.RESTART_PRIORITY;
import static io.cloudslang.content.vmware.constants.Inputs.TRUST_EVERYONE;
import static io.cloudslang.content.vmware.constants.Inputs.USERNAME;
import static io.cloudslang.content.vmware.constants.Inputs.VM_ID;
import static io.cloudslang.content.vmware.constants.Inputs.VM_NAME;
import static io.cloudslang.content.vmware.constants.VmRestartPriorities.CLUSTER_RESTART_PRIORITY;
import static io.cloudslang.content.vmware.constants.VmRestartPriorities.DISABLED;
import static io.cloudslang.content.vmware.constants.VmRestartPriorities.HIGH;
import static io.cloudslang.content.vmware.constants.VmRestartPriorities.LOW;
import static io.cloudslang.content.vmware.constants.VmRestartPriorities.MEDIUM;

/**
 * Created by giloan on 9/1/2016.
 */
public class ModifyVmOverrides {

    private static final String SEPARATOR = ",";
    private static final String INVALID_RESTART_PRIORITY_MSG = "The 'restartPriority' input value is not valid! Valid values are ";

    @Action(name = "Modify VM Overrides Priorities",
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
    public Map<String, String> modifyVmOverrides(@Param(value = HOST, required = true) String host,
                                       @Param(value = PORT) String port,
                                       @Param(value = PROTOCOL) String protocol,
                                       @Param(value = USERNAME, required = true) String username,
                                       @Param(value = PASSWORD, encrypted = true) String password,
                                       @Param(value = TRUST_EVERYONE) String trustEveryone,
                                       @Param(value = HOSTNAME, required = true) String hostname,
                                       @Param(value = VM_NAME) String virtualMachineName,
                                       @Param(value = VM_ID) String virtualMachineId,
                                       @Param(value = CLUSTER_NAME, required = true) String clusterName,
                                       @Param(value = RESTART_PRIORITY, required = true) String restartPriority) {
        try {
            final HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();

            InputUtils.checkMutuallyExclusiveInputs(virtualMachineName, virtualMachineId, PROVIDE_VM_NAME_OR_ID);

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withClusterName(clusterName)
                    .withHostname(hostname)
                    .withVirtualMachineName(virtualMachineName)
                    .withVirtualMachineId(virtualMachineId)
                    .build();

            return new ClusterComputeResourceService().updateOrAddVmOverride(httpInputs, vmInputs,
                    validateRestartPriority(restartPriority));
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }

    private String validateRestartPriority(final String restartPriority) {
        final List<String> restartPriorities = Arrays.asList(CLUSTER_RESTART_PRIORITY, DISABLED, HIGH, MEDIUM, LOW);
        for (final String str : restartPriorities) {
            if (str.equalsIgnoreCase(restartPriority)) {
                return restartPriority;
            }
        }
        throw new RuntimeException(INVALID_RESTART_PRIORITY_MSG + StringUtils.join(restartPriorities, SEPARATOR));
    }
}
