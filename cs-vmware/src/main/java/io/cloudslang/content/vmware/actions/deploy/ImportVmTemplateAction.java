package io.cloudslang.content.vmware.actions.deploy;

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
import io.cloudslang.content.vmware.services.ImportTemplatesService;
import io.cloudslang.content.vmware.utils.InputUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImportVmTemplateAction {

    private static final String SUCCESSFULLY_DEPLOYMENT = "Template was deployed successfully!";

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
                                              @Param(value = Inputs.USER) String user,
                                              @Param(value = Inputs.PASSWORD, encrypted = true, required = true) String password,
                                              @Param(value = Inputs.PORT) String port,
                                              @Param(value = Inputs.PROTOCOL) String protocol,
                                              @Param(value = Inputs.TRUST_EVERYONE) String trustEveryone,
//                                              @Param(value = Inputs.CLOSE_SESSION, required = true) String closeSession,
                                              @Param(value = Inputs.PATH, required = true) String path,
                                              @Param(value = Inputs.NAME, required = true) String name,
                                              @Param(value = Inputs.DATACENTER, required = true) String datacenter,
                                              @Param(value = Inputs.VM_FOLDER, required = true) String vmFolder,
                                              @Param(value = Inputs.DATA_STORE, required = true) String dataStore,
                                              @Param(value = Inputs.HOST_SYSTEM, required = true) String hostSystem,
                                              @Param(value = Inputs.DISK_PROVISIONING) String diskProvisioning,
//                                              @Param(value = Inputs.HS_IDENTIFIER_TYPE) String hsIdentifierType,
//                                              @Param(value = Inputs.CLUSTER_NAME) String clusterName,
                                              @Param(value = Inputs.RESOURCE_POOL) String resourcePool,
//                                              @Param(value = Inputs.OVF_NETWORK_JS) String ovfNetworkJS,
//                                              @Param(value = Inputs.NET_PORT_GROUP_JS) String netPortGroupJS,
//                                              @Param(value = Inputs.OVF_PROP_KEY_JS) String ovfPropKeyJS,
//                                              @Param(value = Inputs.OVF_PROP_VALUE_JS) String ovfPropValueJS,
                                              @Param(value = Inputs.IP_PROTOCOL) String ipProtocol,
                                              @Param(value = Inputs.IP_ALLOC_SCHEME) String ipAllocScheme,
                                              @Param(value = Inputs.LOCALE_LANG) String localeLang,
                                              @Param(value = Inputs.LOCALE_COUNTRY) String localeCountry,
                                              @Param(value = Inputs.PARALLEL) String parallel) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            Locale locale = InputUtils.getLocale(localeLang, localeCountry);

            HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(user)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();

            VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withHostname(hostSystem)
                    .withVirtualMachineName(name)
                    .withDataCenterName(datacenter)
                    .withDataStore(dataStore)
                    .withCloneDataStore(dataStore)
                    .withFolderName(vmFolder)
                    .withLocale(locale)
                    .withResourcePool(resourcePool)
                    .withIpProtocol(ipProtocol)
                    .withIpAllocScheme(ipAllocScheme)
                    .withDiskProvisioning(diskProvisioning)
                    .build();

            new ImportTemplatesService(InputUtils.getBooleanInput(parallel, true)).deployTemplate(httpInputs, vmInputs, path);
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            resultMap.put(Outputs.RETURN_RESULT, SUCCESSFULLY_DEPLOYMENT);
        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ExceptionUtils.getStackTrace(ex));
        }
        return resultMap;
    }
}
