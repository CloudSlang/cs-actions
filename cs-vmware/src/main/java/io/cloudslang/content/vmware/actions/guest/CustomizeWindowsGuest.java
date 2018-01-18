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

package io.cloudslang.content.vmware.actions.guest;

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
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.vmware.constants.Inputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class CustomizeWindowsGuest {
    /**
     * Connects to specified data center and customize an existing Windows OS based virtual machine identified by the
     * inputs provided.
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
     * @param closeSession       Whether to use the flow session context to cache the Connection to the host or not. If set to
     *                           "false" it will close and remove any connection from the session context, otherwise the Connection
     *                           will be kept alive and not removed.
     *                           Valid values: "true", "false"
     *                           Default value: "true"
     * @param virtualMachineName name of Windows OS based virtual machine that will be customized
     * @param rebootOption       specifies whether to shutdown, reboot or not the machine in the customization process
     *                           - Valid: "noreboot", "reboot", "shutdown" - Default: "reboot"
     * @param computerName:      the network host name of the (Windows) virtual machine
     * @param computerPassword   the new password for the (Windows) virtual machine
     * @param ownerName          the user's full name
     * @param ownerOrganization: the user's organization
     * @param productKey:        optional - a valid serial number to be included in the answer file - Default: ""
     * @param domainUsername:    optional - the domain user account used for authentication if the virtual machine is
     *                           joining a domain. The user must have the privileges required to add computers to the domain
     *                           - Default: ""
     * @param domainPassword:    optional - the password for the domain user account used for authentication if the virtual
     *                           machine is joining a domain - Default: ""
     * @param domain:            optional - the fully qualified domain name - Default: ""
     * @param workgroup:         optional - the workgroup that the virtual machine should join. If this is supplied, then
     *                           the domain name and authentication fields should not be supplied (mutually exclusive)
     *                           - Default: ""
     * @param licenseDataMode:   the type of the windows license. "perServer" indicates that a client access license has
     *                           been purchased for each computer that accesses the VirtualCenter server. "perSeat" indicates
     *                           that client access licenses have been purchased for the server, allowing a certain number
     *                           of concurrent connections to the VirtualCenter server - Valid: "perServer", "perSeat"
     *                           - Default: "perServer"
     * @param dnsServer:         optional - the server IP address to use for DNS lookup in a Windows guest operating system
     *                           - Default: ""
     * @param ipAddress:         optional - the static ip address - Default: ""
     * @param subnetMask:        optional - the subnet mask for the virtual network adapter - Default: ""
     * @param defaultGateway:    optional - the default gateway for network adapter with a static IP address - Default: ""
     * @param macAddress:        optional - the MAC address for network adapter with a static IP address - Default: ""
     * @param autoLogon:         optional - specifies whether or not the machine automatically logs on as Administrator
     *                           - Valid: "true", "false" - Default: "false"
     * @param deleteAccounts:    optional - specifies whether if all user accounts will be removed from the system as part
     *                           of the customization or not. This input can be use only for older than API 2.5 versions.
     *                           Since API 2.5 this value is ignored and removing user accounts during customization is
     *                           no longer supported. For older API versions: if deleteAccounts is true, then all user
     *                           accounts are removed from the system as part of the customization. Mini-setup creates a
     *                           new Administrator account with a blank password - Default: ""
     * @param changeSID:         specifies whether the customization process should modify or not the machine's security
     *                           identifier (SID). For Vista OS, SID will always be modified - Valid: "true", "false"
     *                           - Default: "true"
     * @param autoLogonCount:    optional - if the AutoLogon flag is set, then the AutoLogonCount property specifies the
     *                           number of times the machine should automatically log on as Administrator. Generally it
     *                           should be 1, but if your setup requires a number of reboots, you may want to increase it
     *                           - Default: "1"
     * @param autoUsers:         optional - this key is valid only if license_data_mode input is set 'perServer', otherwise
     *                           is ignored. The integer value indicates the number of client licenses purchased for the
     *                           VirtualCenter server being installed - Default: ""
     * @param timeZone:          optional - the time zone for the new virtual machine according with
     *                           https://technet.microsoft.com/en-us/library/ms145276%28v=sql.90%29.aspx
     *                           - Default: "360"
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Customize Windows Guest",
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
    public Map<String, String> customizeWindowsGuest(@Param(value = HOST, required = true) String host,
                                                     @Param(value = PORT) String port,
                                                     @Param(value = PROTOCOL) String protocol,
                                                     @Param(value = USERNAME, required = true) String username,
                                                     @Param(value = PASSWORD, encrypted = true) String password,
                                                     @Param(value = TRUST_EVERYONE) String trustEveryone,
                                                     @Param(value = CLOSE_SESSION) String closeSession,


                                                     @Param(value = VM_NAME, required = true) String virtualMachineName,
                                                     @Param(value = REBOOT_OPTION, required = true) String rebootOption,
                                                     @Param(value = COMPUTER_NAME, required = true) String computerName,
                                                     @Param(value = COMPUTER_PASSWORD, required = true) String computerPassword,
                                                     @Param(value = OWNER_NAME, required = true) String ownerName,
                                                     @Param(value = OWNER_ORGANIZATION, required = true) String ownerOrganization,
                                                     @Param(value = PRODUCT_KEY) String productKey,
                                                     @Param(value = DOMAIN_USERNAME) String domainUsername,
                                                     @Param(value = DOMAIN_PASSWORD) String domainPassword,
                                                     @Param(value = DOMAIN) String domain,
                                                     @Param(value = WORKGROUP) String workgroup,
                                                     @Param(value = LICENSE_DATA_MODE, required = true) String licenseDataMode,
                                                     @Param(value = DNS_SERVER) String dnsServer,
                                                     @Param(value = IP_ADDRESS) String ipAddress,
                                                     @Param(value = SUBNET_MASK) String subnetMask,
                                                     @Param(value = DEFAULT_GATEWAY) String defaultGateway,
                                                     @Param(value = MAC_ADDRESS) String macAddress,

                                                     @Param(value = AUTO_LOGON) String autoLogon,
                                                     @Param(value = DELETE_ACCOUNTS) String deleteAccounts,
                                                     @Param(value = CHANGE_SID, required = true) String changeSID,
                                                     @Param(value = AUTO_LOGON_COUNT) String autoLogonCount,
                                                     @Param(value = AUTO_USERS) String autoUsers,
                                                     @Param(value = TIME_ZONE) String timeZone,
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
                    .build();

            final GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
                    .withRebootOption(rebootOption)
                    .withComputerName(computerName)
                    .withComputerPassword(computerPassword)
                    .withOwnerName(ownerName)
                    .withOwnerOrganization(ownerOrganization)
                    .withProductKey(productKey)
                    .withDomainUsername(domainUsername)
                    .withDomainPassword(domainPassword)
                    .withDomain(domain)
                    .withWorkgroup(workgroup)
                    .withLicenseDataMode(licenseDataMode)
                    .withDnsServer(dnsServer)
                    .withIpAddress(ipAddress)
                    .withSubnetMask(subnetMask)
                    .withDefaultGateway(defaultGateway)
                    .withMacAddress(macAddress)
                    .withAutoLogon(autoLogon)
                    .withDeleteAccounts(deleteAccounts)
                    .withChangeSID(changeSID)
                    .withAutoLogonCount(autoLogonCount)
                    .withAutoUsers(autoUsers)
                    .withTimeZone(timeZone)
                    .build();

            return new GuestService().customizeVM(httpInputs, vmInputs, guestInputs, true);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }

    }
}
