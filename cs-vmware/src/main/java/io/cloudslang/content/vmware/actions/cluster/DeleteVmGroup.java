

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

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.vmware.constants.Inputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by pinteae on 10/3/2016.
 */
public class DeleteVmGroup {

    /**
     * @param host          VMware host or IP.
     *                      Example: "vc6.subdomain.example.com"
     * @param port          optional - the port to connect through.
     *                      Default Value: "443"
     *                      Examples: "443", "80"
     * @param protocol      optional - The connection protocol.
     *                      Default Value: "https"
     *                      Valid Values: "http", "https"
     * @param username      The VMware username used to connect.
     * @param password      The password associated with "username" input.
     * @param trustEveryone optional - If "true" will allow connections from any host, if "false" the connection will be allowed only using a valid vCenter certificate. Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html to see how to import a certificate into Java Keystore and https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html to see how to obtain a valid vCenter certificate
     *                      Default Value: "true"
     * @param closeSession  Whether to use the flow session context to cache the Connection to the host or not. If set to
     *                      "false" it will close and remove any connection from the session context, otherwise the Connection
     *                      will be kept alive and not removed.
     *                      Valid values: "true", "false"
     *                      Default value: "true"
     * @param clusterName   The name of the cluster from which the VM group will be deleted.
     * @param vmGroupName   The name of the VM group that will be deleted.
     * @return
     */
    @Action(name = "Delete DRS VM Group",
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
    public Map<String, String> deleteVmGroup(@Param(value = HOST, required = true) String host,
                                             @Param(value = PORT) String port,
                                             @Param(value = PROTOCOL) String protocol,
                                             @Param(value = USERNAME, required = true) String username,
                                             @Param(value = PASSWORD, encrypted = true) String password,
                                             @Param(value = TRUST_EVERYONE) String trustEveryone,
                                             @Param(value = CLOSE_SESSION) String closeSession,
                                             @Param(value = VM_GROUP_NAME, required = true) String vmGroupName,
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

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withClusterName(clusterName)
                    .withVmGroupName(vmGroupName)
                    .build();
            return new ClusterComputeResourceService().deleteVmGroup(httpInputs, vmInputs);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
