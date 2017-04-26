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
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_AFFINE_OR_ANTI_AFFINE_HOST_GROUP;
import static io.cloudslang.content.vmware.constants.Inputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


/**
 * Created by pinteae on 10/6/2016.
 */
public class CreateAffinityRule {

    /**
     * @param host                    VMware host or IP.
     *                                Example: "vc6.subdomain.example.com"
     * @param port                    optional - the port to connect through.
     *                                Default Value: "443"
     *                                Examples: "443", "80"
     * @param protocol                optional - the connection protocol.
     *                                Default Value: "https"
     *                                Valid Values: "http", "https"
     * @param username                the VMware username used to connect.
     * @param password                the password associated with "username" input.
     * @param trustEveryone           optional - if "true" will allow connections from any host, if "false" the connection
     *                                will be allowed only using a valid vCenter certificate.
     *                                Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html
     *                                to see how to import a certificate into Java Keystore and https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html to see how to obtain a valid vCenter certificate
     *                                Default Value: "true"
     * @param closeSession            Whether to use the flow session context to cache the Connection to the host or not. If set to
     *                                "false" it will close and remove any connection from the session context, otherwise the Connection
     *                                will be kept alive and not removed.
     *                                Valid values: "true", "false"
     *                                Default value: "true"
     * @param clusterName             the name of the cluster.
     * @param ruleName                the name of the affinity rule.
     * @param affineHostGroupName     optional - the name of the affine host group. The affine host group represents the
     *                                host group on which the virtual machines in the specified VM group must run.
     * @param antiAffineHostGroupName optional - the name of the anti-affine host group. The anti-affine host group
     *                                represents the host group on which the virtual machines in the specified VM group
     *                                must not run.
     *                                Note: Only one of the affineHostGroupName and antiAffineHostGroupName should be
     *                                provided. If the affineHostGroupName is provided, there will be created an affinity
     *                                rule between the VM group and the host Group, otherwise an anti-affinity rule will
     *                                be created.
     * @param vmGroupName             the name of the VM group for which the affinity rule will be applied.
     * @return
     */
    @Action(name = "Create Affinity Rule",
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
    public Map<String, String> createAffinityRule(@Param(value = HOST, required = true) String host,
                                                  @Param(value = PORT) String port,
                                                  @Param(value = PROTOCOL) String protocol,
                                                  @Param(value = USERNAME, required = true) String username,
                                                  @Param(value = PASSWORD, encrypted = true) String password,
                                                  @Param(value = TRUST_EVERYONE) String trustEveryone,
                                                  @Param(value = CLOSE_SESSION) String closeSession,
                                                  @Param(value = CLUSTER_NAME, required = true) String clusterName,
                                                  @Param(value = RULE_NAME, required = true) String ruleName,
                                                  @Param(value = AFFINE_HOST_GROUP_NAME) String affineHostGroupName,
                                                  @Param(value = ANTI_AFFINE_HOST_GROUP_NAME) String antiAffineHostGroupName,
                                                  @Param(value = VM_GROUP_NAME, required = true) String vmGroupName,
                                                  @Param(value = VMWARE_GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Connection>> globalSessionObject) {
        try {
            InputUtils.checkMutuallyExclusiveInputs(affineHostGroupName, antiAffineHostGroupName, PROVIDE_AFFINE_OR_ANTI_AFFINE_HOST_GROUP);
            final HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(defaultIfEmpty(trustEveryone, TRUE))
                    .withCloseSession(defaultIfEmpty(closeSession, TRUE))
                    .withGlobalSessionObject(globalSessionObject)
                    .build();

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withClusterName(clusterName)
                    .withVmGroupName(vmGroupName)
                    .withRuleName(ruleName)
                    .build();

            return new ClusterComputeResourceService().createAffinityRule(httpInputs, vmInputs, affineHostGroupName, antiAffineHostGroupName);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
