

package io.cloudslang.content.vmware.actions.cluster;

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
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_VM_NAME_OR_ID_OR_NONE;
import static io.cloudslang.content.vmware.constants.Inputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Tirla Alin.
 * Date: 31/3/2017.
 */
public class GetVmOverrides {

    /**
     * @param host               VMware host or IP.
     *                           Example: "vc6.subdomain.example.com"
     * @param port               optional - The port to connect through.
     *                           Default Value: "443"
     *                           Examples: "443", "80"
     * @param protocol           optional - The connection protocol.
     *                           Default Value: "https"
     *                           Valid Values: "http", "https"
     * @param username           The VMware username used to connect.
     * @param password           The password associated with "username" input.
     * @param trustEveryone      optional - If "true" will allow connections from any host, if "false" the connection
     *                           will be allowed only using a valid vCenter certificate.
     *                           Default Value: "false"
     * @param closeSession       Whether to use the flow session context to cache the Connection to the host or not. If set to
     *                           "false" it will close and remove any connection from the session context, otherwise the Connection
     *                           will be kept alive and not removed.
     *                           Valid values: "true", "false"
     *                           Default value: "true"
     * @param hostname           The name of the target host to be queried to retrieve the supported guest OSes
     *                           Example: "host123.subdomain.example.com"
     * @param virtualMachineName optional - The name of the virtual machine for which the override will be created. This input
     *                           is mutually exclusive with virtualMachineId.
     * @param virtualMachineId   optional - The id of the virtual machine for which the override will be created. This input is
     *                           mutually exclusive with virtualMachineName.
     *                           Example: "vm-1230"
     * @param clusterName        The name of the cluster.
     * @return A map containing the output of the operation. Keys present in the map are:
     * <br><b>returnResult</b>      The primary output.
     * <br><b>returnCode</b>        The return code of the operation. 0 if the operation goes to success, -1 if the
     * operation goes to failure.
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
                                              @Param(value = CLOSE_SESSION) String closeSession,
                                              @Param(value = HOSTNAME, required = true) String hostname,
                                              @Param(value = VM_NAME) String virtualMachineName,
                                              @Param(value = VM_ID) String virtualMachineId,
                                              @Param(value = CLUSTER_NAME, required = true) String clusterName,
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
