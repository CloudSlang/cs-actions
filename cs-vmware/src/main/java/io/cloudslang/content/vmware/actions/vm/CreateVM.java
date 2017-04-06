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
 * 10/18/2015.
 */
public class CreateVM {
    /**
     * Connects to a specified data center and creates a virtual machine using the inputs provided.
     *
     * @param host               VMware host or IP - Example: "vc6.subdomain.example.com"
     * @param port               optional - the port to connect through - Examples: "443", "80" - Default: "443"
     * @param protocol           optional - the connection protocol - Valid: "http", "https" - Default: "https"
     * @param username           the VMware username use to connect
     * @param password           the password associated with "username" input
     * @param trustEveryone      optional - if "true" will allow connections from any host, if "false" the connection will
     *                           be allowed only using a valid vCenter certificate - Default: "true"
     *                           Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html
     *                           to see how to import a certificate into Java Keystore and
     *                           https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html
     *                           to see how to obtain a valid vCenter certificate
     * @param dataCenterName     the data center name where the host system is - Example: 'DataCenter2'
     * @param hostname           the name of the target host to be queried to retrieve the supported guest OSes
     *                           - Example: "host123.subdomain.example.com"
     * @param virtualMachineName the name of the virtual machine that will be created
     * @param dataStore          the datastore where the disk of the new created virtual machine will reside
     *                           - Example: "datastore2-vc6-1"
     * @param guestOsId          the operating system associated with the new created virtual machine. The value for this
     *                           input can be obtained by running GetOSDescriptors operation - Examples: "winXPProGuest",
     *                           "win95Guest", "centosGuest", "fedoraGuest", "freebsd64Guest"... etc.
     * @param folderName:        optional - name of the folder where the virtual machine will be created. If not
     *                           provided then the top parent folder will be used - Default: ""
     * @param resourcePool:      optional - the resource pool for the cloned virtual machine. If not provided then the
     *                           parent resource pool be will be used - Default: ""
     * @param description        optional - the description of the virtual machine that will be created - Default: ""
     * @param numCPUs            optional - the number that indicates how many processors will have the virtual machine
     *                           that will be created - Default: "1"
     * @param vmDiskSize         optional - the disk capacity amount (in Mb) attached to the virtual machine that will
     *                           be created - Default: "1024"
     * @param vmMemorySize       optional - the memory amount (in Mb) attached to the virtual machine that will will
     *                           be created - Default: "1024"
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Create Virtual Machine",
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
    public Map<String, String> createVM(@Param(value = HOST, required = true) String host,
                                        @Param(value = PORT) String port,
                                        @Param(value = PROTOCOL) String protocol,
                                        @Param(value = USERNAME, required = true) String username,
                                        @Param(value = PASSWORD, encrypted = true) String password,
                                        @Param(value = TRUST_EVERYONE) String trustEveryone,
                                        @Param(value = CLOSE_SESSION) String closeSession,

                                        @Param(value = DATA_CENTER_NAME, required = true) String dataCenterName,
                                        @Param(value = HOSTNAME, required = true) String hostname,
                                        @Param(value = VM_NAME, required = true) String virtualMachineName,
                                        @Param(value = DATA_STORE, required = true) String dataStore,
                                        @Param(value = GUEST_OS_ID, required = true) String guestOsId,
                                        @Param(value = FOLDER_NAME) String folderName,
                                        @Param(value = RESOURCE_POOL) String resourcePool,
                                        @Param(value = VM_DESCRIPTION) String description,
                                        @Param(value = VM_CPU_COUNT) String numCPUs,
                                        @Param(value = VM_DISK_SIZE) String vmDiskSize,
                                        @Param(value = VM_MEMORY_SIZE) String vmMemorySize,
                                        @Param(value = VMWARE_GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Connection>> globalSessionObject) {

        try {
            final HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(defaultIfEmpty(trustEveryone, TRUE))
                    .withCloseSession(defaultIfEmpty(closeSession, FALSE))
                    .withGlobalSessionObject(globalSessionObject)
                    .build();

            final VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withDataCenterName(dataCenterName)
                    .withHostname(hostname)
                    .withVirtualMachineName(virtualMachineName)
                    .withDescription(description)
                    .withDataStore(dataStore)
                    .withGuestOsId(guestOsId)
                    .withFolderName(folderName)
                    .withResourcePool(resourcePool)
                    .withDescription(description)
                    .withGuestOsId(guestOsId)
                    .withDescription(description)
                    .withIntNumCPUs(numCPUs)
                    .withLongVmDiskSize(vmDiskSize)
                    .withLongVmMemorySize(vmMemorySize)
                    .build();

            return new VmService().createVM(httpInputs, vmInputs);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
