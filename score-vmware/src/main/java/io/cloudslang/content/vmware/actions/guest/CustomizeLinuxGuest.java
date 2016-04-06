package io.cloudslang.content.vmware.actions.guest;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/29/2016.
 */
public class CustomizeLinuxGuest {
    /**
     * Connects to specified data center and customize an existing linux OS based virtual machine identified by the
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
     * @param virtualMachineName name of Windows OS based virtual machine that will be customized
     * @param computerName:      the network host name of the (Windows) virtual machine
     * @param domain:            optional - the fully qualified domain name - Default: ""
     * @param ipAddress:         optional - the static ip address - Default: ""
     * @param subnetMask:        optional - the subnet mask for the virtual network adapter - Default: ""
     * @param defaultGateway:    optional - the default gateway for network adapter with a static IP address - Default: ""
     * @param hwClockUTC:        optional - the MAC address for network adapter with a static IP address - Default: ""
     * @param timeZone:          optional - the time zone for the new virtual machine according with
     *                           https://technet.microsoft.com/en-us/library/ms145276%28v=sql.90%29.aspx
     *                           - Default: "360"
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Customize Linux Guest",
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
    Map<String, String> customizeLinuxGuest(@Param(value = Inputs.HOST, required = true) String host,
                                            @Param(Inputs.PORT) String port,
                                            @Param(Inputs.PROTOCOL) String protocol,
                                            @Param(value = Inputs.USERNAME, required = true) String username,
                                            @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                            @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                            @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                            @Param(value = Inputs.COMPUTER_NAME, required = true) String computerName,
                                            @Param(Inputs.DOMAIN) String domain,
                                            @Param(Inputs.IP_ADDRESS) String ipAddress,
                                            @Param(Inputs.SUBNET_MASK) String subnetMask,
                                            @Param(Inputs.DEFAULT_GATEWAY) String defaultGateway,
                                            @Param(Inputs.UTC_CLOCK) String hwClockUTC,
                                            @Param(Inputs.TIME_ZONE) String timeZone) {

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

            VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName(virtualMachineName).build();

            GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
                    .withComputerName(computerName)
                    .withDomain(domain)
                    .withIpAddress(ipAddress)
                    .withSubnetMask(subnetMask)
                    .withDefaultGateway(defaultGateway)
                    .withHwClockUTC(hwClockUTC)
                    .withTimeZone(timeZone)
                    .build();

            resultMap = new GuestService().customizeVM(httpInputs, vmInputs, guestInputs, false);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}