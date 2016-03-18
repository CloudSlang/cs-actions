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
 * 3/3/2016.
 */
public class CloneVM {
    /**
     * Connects to specified data center and clone an existing virtual machine identified by the inputs provided.
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
     * @param virtualMachineName the name of the virtual machine that will be cloned
     * @param cloneName          the name of the clone virtual machine
     * @param folderName:        optional - name of the folder where the cloned virtual machine will be reside. If not
     *                           provided then the top parent folder will be used - Default: ""
     * @param cloneHost          optional - the host for the cloned virtual machine. If not provided then the same host
     *                           of the virtual machine that will be cloned will be used - Default: ""
     *                           - Example: 'host123.subdomain.example.com'
     * @param cloneResourcePool  optional - the resource pool for the cloned virtual machine. If not provided then the
     *                           parent resource pool will be used - Default: ""
     * @param cloneDataStore     datastore where disk of newly cloned virtual machine will reside. If not provided then
     *                           the datastore of the cloned virtual machine will be used
     *                           - Example: "datastore2-vc6-1"
     * @param thickProvision:    optional - whether the provisioning of the cloned virtual machine will be thick or not
     *                           - Default: "false"
     * @param isTemplate:        optional - whether the cloned virtual machine will be a template or not - Default: "false"
     * @param cpuNum:            optional - number that indicates how many processors the newly cloned virtual machine will have
     *                           - Default: "1"
     * @param coresPerSocket:    optional - number that indicates how many cores per socket the newly cloned virtual machine
     *                           will have - Default: "1"
     * @param memory:            optional - amount of memory (in Mb) attached to cloned virtual machined - Default: "1024"
     * @param cloneDescription:  optional - description of virtual machine that will be cloned
     *                           - Default: ""
     * @return resultMap with String as key and value that contains returnCode of the operation, success message with
     * task id of the execution or failure message and the exception if there is one
     */
    @Action(name = "Clone Virtual Machine",
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
    public Map<String, String> cloneVM(@Param(value = Inputs.HOST, required = true) String host,
                                       @Param(Inputs.PORT) String port,
                                       @Param(Inputs.PROTOCOL) String protocol,
                                       @Param(value = Inputs.USERNAME, required = true) String username,
                                       @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                       @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                       @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                       @Param(value = Inputs.CLONE_NAME, required = true) String cloneName,
                                       @Param(Inputs.FOLDER_NAME) String folderName,
                                       @Param(Inputs.CLONE_HOST) String cloneHost,
                                       @Param(Inputs.CLONE_RESOURCE_POOL) String cloneResourcePool,
                                       @Param(Inputs.CLONE_DATA_STORE) String cloneDataStore,
                                       @Param(Inputs.THICK_PROVISION) String thickProvision,
                                       @Param(Inputs.IS_TEMPLATE) String isTemplate,
                                       @Param(Inputs.CPU_NUM) String cpuNum,
                                       @Param(Inputs.CORES_PER_SOCKET) String coresPerSocket,
                                       @Param(Inputs.MEMORY) String memory,
                                       @Param(Inputs.CLONE_DESCRIPTION) String cloneDescription) {

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
                    .withCloneName(cloneName)
                    .withFolderName(folderName)
                    .withCloneHost(cloneHost)
                    .withCloneResourcePool(cloneResourcePool)
                    .withCloneDataStore(cloneDataStore)
                    .withThickProvision(thickProvision)
                    .withTemplate(isTemplate)
                    .withIntNumCPUs(cpuNum)
                    .withCoresPerSocket(coresPerSocket)
                    .withLongVmMemorySize(memory)
                    .withDescription(cloneDescription)
                    .build();

            resultMap = new VmService().cloneVM(httpInputs, vmInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}