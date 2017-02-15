/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.deployment;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.DeployOvfTemplateService;
import io.cloudslang.content.vmware.utils.InputUtils;
import io.cloudslang.content.vmware.utils.OvfUtils;

import java.util.Locale;
import java.util.Map;

public class DeployOvfTemplateAction {

    private static final String SUCCESSFULLY_DEPLOYED = "Template was deployed successfully!";

    /**
     * @param host             VMware host or IP - Example: "vc6.subdomain.example.com"
     * @param username         The VMware username use to connect
     * @param password         The password associated with "username" input
     * @param port             optional - the port to connect through - Examples: "443", "80" - Default: "443"
     * @param protocol         optional - the connection protocol - Valid: "http", "https" - Default: "https"
     * @param trustEveryone    optional - if "true" will allow connections from any host, if "false" the connection will
     *                         be allowed only using a valid vCenter certificate - Default: "true"
     *                         Check the: https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_java_development.4.3.html
     *                         to see how to import a certificate into Java Keystore and
     *                         https://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.dsg.doc_50%2Fsdk_sg_server_certificate_Appendix.6.4.html
     *                         to see how to obtain a valid vCenter certificate
     * @param path             Path to the .ovf or .ova file on the RAS filesystem or network to import.
     * @param name             Name of the newly deployed virtual machine.
     * @param datacenter       Datacenter of the host system or cluster.
     * @param dataStore        Name of dataStore to store the new virtual machine.
     *                         - Example: "datastore2-vc6-1"
     * @param hostname         The name of the target host.
     *                         - Example: "host123.subdomain.example.com"
     * @param clusterName      The cluster name on which the template will be deployed.
     * @param resourcePool     The resource pool name on the specified cluster.  If not provided then the
     *                         parent resource pool be will be used
     * @param vmFolder         Virtual machine's inventory folder name. This input is case sensitive.
     * @param diskProvisioning Represents types of disk provisioning that can be set for the disk in the deployed OVF package.
     *                         - Valid values:  monolithicSparse, monolithicFlat, twoGbMaxExtentSparse, twoGbMaxExtentFlat, thin, thick, sparse, flat
     * @param ipProtocol       Specifies how the guest software gets configured with IP addresses.
     *                         - Valid values: IPv4, IPv6
     * @param ipAllocScheme    The deployer / operator of a vApp, specifies what IP allocation policy should be used:
     *                         - Valid values:
     *                         dhcpPolicy - Specifies that DHCP must be used to allocate IP addresses to the vApp.
     *                         fixedPolicy - The IP addresses are allocated when the vApp is deployed and
     *                              will be kept with the server as long as it is deployed.
     *                         transientPolicy - The IP addresses are allocated when needed, typically
     *                              at power-on, and deallocated during power-off.
     * @param localeLang       The locale language in which to process the OVF. If you do not specify a value for this input,
     *                         the default locale language of the system will be used.
     * @param localeCountry    The locale country in which to process the OVF. If you do not specify a value for this input,
     *                         the default locale country of the system will be used.
     * @param ovfNetworkJS     A JSON array of network in the ovf template to be mapped to vm port groups. The netPortGroupJS input will
     *                         be a complimentary array that defined the target port groups for these networks.
     *                         - Example: ["Network 1","Network 2"].
     * @param netPortGroupJS   A JSON array of port groups that the ovf networks in the template will attach to.  The ovfNetworkJS input
     *                         defined the source networks in the ovf template for these portgroups.
     *                         - Example: ["VM Network", "dvPortGroup"].
     *                         Including the example from ovfNetworkJS input, "Network 1" will be mapped to "VM Network" and
     *                         "Network 2" will be mapped to "dvPortGroup".
     * @param ovfPropKeyJS     A JSON array of property names to be configured during import of the ovf template.
     *                         - Example: ["vami.ip0.vmName","vami.ip1.vmName"]
     * @param ovfPropValueJS   A JSON array of property values respective to the property names defined in ovfPropKeyJS to be applied
     *                         during import of the ovf template.
     *                         - Example: ["10.10.10.10","10.20.30.40"].
     *                         Including the example from ovfPropKeyJS input, property "vami.ip0.vmName" will have value
     *                         "10.10.10.10" and "vami.ip1.vmName" will have value "10.20.30.40".
     * @param parallel         If the ovf template has multiple .vmdk files, should they be uploaded in parallel?
     *                         If true, all .vmdk files will be uploaded using separate threads.
     *                         If false, .vmdk files will be uploaded individually.  Depending on the performance characteristics
     *                         of the network between the host and the RAS and the RAS system storage, parallel upload will be faster.
     * @return
     */
    @Action(name = "Deploy OVF Template",
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
    public Map<String, String> deployTemplate(@Param(value = Inputs.HOST, required = true) String host,
                                              @Param(value = Inputs.USERNAME) String username,
                                              @Param(value = Inputs.PASSWORD, encrypted = true, required = true) String password,
                                              @Param(value = Inputs.PORT) String port,
                                              @Param(value = Inputs.PROTOCOL) String protocol,
                                              @Param(value = Inputs.TRUST_EVERYONE) String trustEveryone,
                                              @Param(value = Inputs.PATH, required = true) String path,
                                              @Param(value = Inputs.NAME, required = true) String name,
                                              @Param(value = Inputs.DATACENTER, required = true) String datacenter,
                                              @Param(value = Inputs.DATA_STORE, required = true) String dataStore,
                                              @Param(value = Inputs.HOSTNAME, required = true) String hostname,
                                              @Param(value = Inputs.CLUSTER_NAME) String clusterName,
                                              @Param(value = Inputs.RESOURCE_POOL) String resourcePool,
                                              @Param(value = Inputs.VM_FOLDER) String vmFolder,
                                              @Param(value = Inputs.DISK_PROVISIONING) String diskProvisioning,
                                              @Param(value = Inputs.IP_PROTOCOL) String ipProtocol,
                                              @Param(value = Inputs.IP_ALLOC_SCHEME) String ipAllocScheme,
                                              @Param(value = Inputs.LOCALE_LANG) String localeLang,
                                              @Param(value = Inputs.LOCALE_COUNTRY) String localeCountry,
                                              @Param(value = Inputs.OVF_NETWORK_JS) String ovfNetworkJS,
                                              @Param(value = Inputs.NET_PORT_GROUP_JS) String netPortGroupJS,
                                              @Param(value = Inputs.OVF_PROP_KEY_JS) String ovfPropKeyJS,
                                              @Param(value = Inputs.OVF_PROP_VALUE_JS) String ovfPropValueJS,
                                              @Param(value = Inputs.PARALLEL) String parallel) {
        try {
            Locale locale = InputUtils.getLocale(localeLang, localeCountry);

            HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();

            VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withHostname(hostname)
                    .withVirtualMachineName(name)
                    .withDataCenterName(datacenter)
                    .withDataStore(dataStore)
                    .withCloneDataStore(dataStore)
                    .withFolderName(vmFolder)
                    .withLocale(locale)
                    .withClusterName(clusterName)
                    .withResourcePool(resourcePool)
                    .withIpProtocol(ipProtocol)
                    .withIpAllocScheme(ipAllocScheme)
                    .withDiskProvisioning(diskProvisioning)
                    .build();

            Map<String, String> ovfNetworkMappings = OvfUtils.getOvfMappings(ovfNetworkJS, netPortGroupJS);
            Map<String, String> ovfPropertyMappings = OvfUtils.getOvfMappings(ovfPropKeyJS, ovfPropValueJS);

            new DeployOvfTemplateService(InputUtils.getBooleanInput(parallel, true))
                    .deployOvfTemplate(httpInputs, vmInputs, path, ovfNetworkMappings, ovfPropertyMappings);
            return OutputUtilities.getSuccessResultsMap(SUCCESSFULLY_DEPLOYED);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
