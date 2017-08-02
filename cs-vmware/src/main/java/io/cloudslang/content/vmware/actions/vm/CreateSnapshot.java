/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
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
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.vmware.constants.Inputs.CLOSE_SESSION;
import static io.cloudslang.content.vmware.constants.Inputs.HOST;
import static io.cloudslang.content.vmware.constants.Inputs.MEMORY_DUMP;
import static io.cloudslang.content.vmware.constants.Inputs.PASSWORD;
import static io.cloudslang.content.vmware.constants.Inputs.PORT;
import static io.cloudslang.content.vmware.constants.Inputs.PROTOCOL;
import static io.cloudslang.content.vmware.constants.Inputs.QUIESCE;
import static io.cloudslang.content.vmware.constants.Inputs.SNAPSHOT_DESCRIPTION;
import static io.cloudslang.content.vmware.constants.Inputs.SNAPSHOT_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.TRUST_EVERYONE;
import static io.cloudslang.content.vmware.constants.Inputs.USERNAME;
import static io.cloudslang.content.vmware.constants.Inputs.VMWARE_GLOBAL_SESSION_OBJECT;
import static io.cloudslang.content.vmware.constants.Inputs.VM_NAME;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateSnapshot {
    /**
     * Connects to a specified data center and deletes a virtual machine identified by the inputs provided.
     *
     * @param host                VMware host or IP - Example: "vc6.subdomain.example.com"
     * @param port                Optional - the port to connect through - Examples: "443", "80" - Default: "443"
     * @param protocol            Optional - the connection protocol - Valid: "http", "https" - Default: "https"
     * @param username            VMware username used to connect
     * @param password            Password associated with "username" input
     * @param trustEveryone       Optional - if "true" will allow connections from any host, if "false" the connection will
     *                            be allowed only using a valid vCenter certificate
     *                            Default: "true"
     *                            Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html
     *                            to see how to import a certificate into Java Keystore and
     *                            https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html
     *                            to see how to obtain a valid vCenter certificate
     * @param closeSession        Whether to use the flow session context to cache the Connection to the host or not. If
     *                            set to "false" it will close and remove any connection from the session context, otherwise
     *                            the Connection will be kept alive and not removed.
     *                            Valid values: "true", "false"
     *                            Default value: "true"
     * @param virtualMachineName  Name of the virtual machine that will be used to create snapshot of
     * @param snapshotName        Name of the snapshot
     * @param snapshotDescription Optional - A meaningful description for snapshot
     * @param withMemoryDump      If "true", a dump of the internal state of the virtual machine (basically a memory dump)
     *                            is included in the snapshot. Memory snapshots consume time and resources, and thus take
     *                            longer to create. When set to "false", the power state of the snapshot is set to powered
     *                            off. Check https://code.vmware.com/apis/196/vsphere#https%3A%2F%2Fvdc-repo.vmware.com%2Fvmwb-repository%2Fdcr-public%2F6b586ed2-655c-49d9-9029-bc416323cb22%2Ffa0b429a-a695-4c11-b7d2-2cbc284049dc%2Fdoc%2Fvim.vm.ConfigOption.html%23capabilities
     *                            to see if the virtual machine supports this operation.
     *                            Valid values: "true", "false"
     *                            Default: "false"
     * @param quiesce             If "true" and the virtual machine is powered on when the snapshot is taken, VMware Tools
     *                            is used to quiesce the file system in the virtual machine. This assures that a disk
     *                            snapshot represents a consistent state of the guest file systems. If the virtual machine
     *                            is powered off or VMware Tools are not available, the quiesce flag is ignored.
     *                            Valid values: "true", "false"
     *                            Default: "false"
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Delete Virtual Machine",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> deleteVM(@Param(value = HOST, required = true) String host,
                                        @Param(value = PORT) String port,
                                        @Param(value = PROTOCOL) String protocol,
                                        @Param(value = USERNAME, required = true) String username,
                                        @Param(value = PASSWORD, encrypted = true) String password,
                                        @Param(value = TRUST_EVERYONE) String trustEveryone,
                                        @Param(value = CLOSE_SESSION) String closeSession,

                                        @Param(value = VM_NAME, required = true) String virtualMachineName,
                                        @Param(value = SNAPSHOT_NAME, required = true) String snapshotName,
                                        @Param(value = SNAPSHOT_DESCRIPTION) String snapshotDescription,
                                        @Param(value = MEMORY_DUMP, required = true) String withMemoryDump,
                                        @Param(value = QUIESCE, required = true) String quiesce,
                                        @Param(value = VMWARE_GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Connection>> globalSessionObject) {

        try {
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
                    .withVirtualMachineName(virtualMachineName)
                    .withSnapshotName(snapshotName)
                    .withSnapshotDescription(snapshotDescription)
                    .withWithMemoryDump(withMemoryDump)
                    .withQuiesce(quiesce)
                    .build();

            return new VmService().createVMSnapshot(httpInputs, vmInputs);
        } catch (Exception ex) {
            return getFailureResultsMap(ex);
        }
    }
}