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
 * 3/22/2016.
 */
public class CustomizeWindowsGuest {
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
    public Map<String, String> customizeWindowsGuest(@Param(value = Inputs.HOST, required = true) String host,
                                                     @Param(Inputs.PORT) String port,
                                                     @Param(Inputs.PROTOCOL) String protocol,
                                                     @Param(value = Inputs.USERNAME, required = true) String username,
                                                     @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                                     @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                                     @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                                     @Param(value = Inputs.REBOOT_OPTION, required = true) String rebootOption,
                                                     @Param(value = Inputs.COMPUTER_NAME, required = true) String computerName,
                                                     @Param(value = Inputs.COMPUTER_PASSWORD, required = true) String computerPassword,
                                                     @Param(value = Inputs.OWNER_NAME, required = true) String ownerName,
                                                     @Param(value = Inputs.OWNER_ORGANIZATION, required = true) String ownerOrganization,
                                                     @Param(Inputs.PRODUCT_KEY) String productKey,
                                                     @Param(Inputs.DOMAIN_USERNAME) String domainUsername,
                                                     @Param(Inputs.DOMAIN_PASSWORD) String domainPassword,
                                                     @Param(Inputs.DOMAIN) String domain,
                                                     @Param(Inputs.WORKGROUP) String workgroup,
                                                     @Param(value = Inputs.LICENSE_DATA_MODE, required = true) String licenseDataMode,
                                                     @Param(Inputs.DNS_SERVER) String dnsServer,
                                                     @Param(Inputs.IP_ADDRESS) String ipAddress,
                                                     @Param(Inputs.SUBNET_MASK) String subnetMask,
                                                     @Param(Inputs.DEFAULT_GATEWAY) String defaultGateway,
                                                     @Param(Inputs.MAC_ADDRESS) String macAddress,

                                                     @Param(Inputs.AUTO_LOGON) String autoLogon,
                                                     @Param(Inputs.DELETE_ACCOUNTS) String deleteAccounts,
                                                     @Param(value = Inputs.CHANGE_SID, required = true) String changeSID,
                                                     @Param(Inputs.AUTO_LOGON_COUNT) String autoLogonCount,
                                                     @Param(Inputs.AUTO_USERS) String autoUsers,
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

            VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withVirtualMachineName(virtualMachineName)
                    .build();

            GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
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

            resultMap = new GuestService().customizeWinVM(httpInputs, vmInputs, guestInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }

    public static void main(String[] args) {
        CustomizeWindowsGuest windowsGuest = new CustomizeWindowsGuest();
        Map<String, String> results = windowsGuest.customizeWindowsGuest("oovc6.hpswlabs.adapps.hp.com",
                "",
                "",
                "Administrator@vsphere.local",
                "oovc64testingHP!",
                "true",
                "win2k8R2",
                "noreboot",
                "fromInteliJ",
                "B33f34t3r",
                "Mihai Tusa",
                "HPE",
                "",
                "Administrator",
                "oovc64testingHP!",
                "TESTING924.rosvc5.com",
                "",
                "perServer",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "true",
                "",
                "",
                "");
        System.out.println(results.get("returnResult"));
    }
}