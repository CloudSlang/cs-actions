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
    public Map<String, String> customizeLinuxGuest(@Param(value = Inputs.HOST, required = true) String host,
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

            resultMap = new GuestService().customizeLinuxVM(httpInputs, vmInputs, guestInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}