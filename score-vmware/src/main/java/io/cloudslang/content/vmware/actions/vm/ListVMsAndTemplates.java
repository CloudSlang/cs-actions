package io.cloudslang.content.vmware.actions.vm;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/13/2016.
 */
public class ListVMsAndTemplates {
    /**
     * Connects to a specified data center and to retrieve retrieve a list with all the virtual machines and templates
     * within.
     *
     * @param host          VMware host or IP - Example: "vc6.subdomain.example.com"
     * @param port          optional - the port to connect through - Examples: "443", "80" - Default: "443"
     * @param protocol      optional - the connection protocol - Valid: "http", "https" - Default: "https"
     * @param username      the VMware username use to connect
     * @param password      the password associated with "username" input
     * @param trustEveryone optional - if "true" will allow connections from any host, if "false" the connection will
     *                      be allowed only using a valid vCenter certificate - Default: "true"
     * @param delimiter     the delimiter that will be used in response list - Default: ","
     * @return resultMap with String as key and value that contains returnCode of the operation, a list that contains
     * all the virtual machines and templates within the data center  or failure message and the exception if there is
     * one
     */
    @Action(name = "List VMs and Templates",
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
    public Map<String, String> listVMsAndTemplates(@Param(value = Inputs.HOST, required = true) String host,
                                                   @Param(Inputs.PORT) String port,
                                                   @Param(Inputs.PROTOCOL) String protocol,
                                                   @Param(value = Inputs.USERNAME, required = true) String username,
                                                   @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                                   @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                                   @Param(Inputs.DELIMITER) String delimiter) {

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

            VmInputs vmInputs = new VmInputs.VmInputsBuilder().build();

            resultMap = new VmService().listVMsAndTemplates(httpInputs, vmInputs, delimiter);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}