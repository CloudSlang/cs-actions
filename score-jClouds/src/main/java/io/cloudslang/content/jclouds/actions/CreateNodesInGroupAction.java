package io.cloudslang.content.jclouds.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CreateServerCustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.execute.CreateNodesInGroupExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 4/28/2016.
 */
public class CreateNodesInGroupAction {
    @Action(name = "Create Nodes In Group",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.PROVIDER, required = true) String provider,
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String identityEndpoint,
                                       @Param(Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(Inputs.CommonInputs.DELIMITER) String delimiter,

                                       @Param(Inputs.CustomInputs.REGION) String region,

                                       @Param(Inputs.CreateServerInputs.IMAGE_URI) String imageUri,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_URI) String hardwareUri,
                                       @Param(Inputs.CreateServerInputs.PRODUCT_CODES) String productCodes,
                                       @Param(Inputs.CreateServerInputs.IMAGE_TAGS) String imageTags,
                                       @Param(Inputs.CreateServerInputs.GROUP) String group,
                                       @Param(Inputs.CreateServerInputs.IMAGE_PROVIDER_ID) String imageProviderId,
                                       @Param(Inputs.CreateServerInputs.IMAGE_NAME) String imageName,
                                       @Param(Inputs.CreateServerInputs.IMAGE_ID) String imageId,
                                       @Param(Inputs.CreateServerInputs.IMAGE_DESCRIPTION) String imageDescription,
                                       @Param(Inputs.CreateServerInputs.LOCATION_ID) String locationId,
                                       @Param(Inputs.CreateServerInputs.LOCATION_DESCRIPTION) String locationDescription,
                                       @Param(Inputs.CreateServerInputs.OS_FAMILY) String osFamily,
                                       @Param(Inputs.CreateServerInputs.OS_DESCRIPTION) String osDescription,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_PROVIDER_ID) String hardwareProviderId,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_NAME) String hardwareName,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_ID) String hardwareId,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_USER_METADATA_KEYS) String hardwareUserMetadataKeys,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_USER_METADATA_VALUES) String hardwareUserMetadataValues,
                                       @Param(Inputs.CreateServerInputs.HARDWARE_TAGS) String hardwareTags,
                                       @Param(Inputs.CreateServerInputs.CORES_PER_PROCESSOR) String coresPerProcessor,
                                       @Param(Inputs.CreateServerInputs.PROCESSORS_GHZ_SPEED) String processorsGhzSpeed,
                                       @Param(Inputs.CreateServerInputs.VOLUME_ID) String volumeId,
                                       @Param(Inputs.CreateServerInputs.VOLUME_TYPE) String volumeType,
                                       @Param(Inputs.CreateServerInputs.VOLUME_NAME) String volumeName,
                                       @Param(Inputs.CreateServerInputs.HYPERVISOR) String hypervisor,
                                       @Param(Inputs.CreateServerInputs.INBOUND_PORTS) String inboundPorts,
                                       @Param(Inputs.CreateServerInputs.PUBLIC_KEY) String publicKey,
                                       @Param(Inputs.CreateServerInputs.PRIVATE_KEY) String privateKey,
                                       @Param(Inputs.CreateServerInputs.RUN_SCRIPT) String runScript,
                                       @Param(Inputs.CreateServerInputs.TEMPLATE_TAGS_STRING) String templateTagsString,
                                       @Param(Inputs.CreateServerInputs.NETWORKS_STRING) String networksString,
                                       @Param(Inputs.CreateServerInputs.NODE_NAMES) String nodeNames,
                                       @Param(Inputs.CreateServerInputs.SECURITY_GROUPS) String securityGroups,
                                       @Param(Inputs.CreateServerInputs.TEMPLATE_USER_METADATA_KEYS) String templateUserMetadataKeys,
                                       @Param(Inputs.CreateServerInputs.TEMPLATE_USER_METADATA_VALUES) String templateUserMetadataValues,
                                       @Param(Inputs.CreateServerInputs.VOLUME_GB_SIZE) String volumeGbSize,
                                       @Param(Inputs.CreateServerInputs.NODES_COUNT) String nodesCount,
                                       @Param(Inputs.CreateServerInputs.PROCESSORS_NUMBER) String processorsNumber,
                                       @Param(Inputs.CreateServerInputs.RAM_GB_AMOUNT) String ramGbAmount,
                                       @Param(Inputs.CreateServerInputs.BLOCK_PORT) String blockPort,
                                       @Param(Inputs.CreateServerInputs.BLOCK_PORT_SECONDS) String blockPortSeconds,
                                       @Param(Inputs.CreateServerInputs.IS_64_BIT) String is64Bit,
                                       @Param(Inputs.CreateServerInputs.BOOT_DEVICE) String bootDevice,
                                       @Param(Inputs.CreateServerInputs.IS_DURABLE) String isDurable,
                                       @Param(Inputs.CreateServerInputs.BLOCK_UNTIL_RUNNING) String blockUntilRunning,
                                       @Param(Inputs.CreateServerInputs.BLOCK_ON_COMPLETE) String blockOnComplete) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .build();

        CreateServerCustomInputs serverCustomInputs = new CreateServerCustomInputs.CreateServerCustomInputsBuilder()
                .withCustomInputs(customInputs)
                .withImageUri(imageUri)
                .withHardwareUri(hardwareUri)
                .withProductCodes(productCodes)
                .withImageTags(imageTags)
                .withGroup(group)
                .withImageProviderId(imageProviderId)
                .withImageName(imageName)
                .withImageId(imageId)
                .withImageDescription(imageDescription)
                .withLocationId(locationId)
                .withLocationDescription(locationDescription)
                .withOsFamily(osFamily)
                .withOsDescription(osDescription)
                .withHardwareProviderId(hardwareProviderId)
                .withHardwareName(hardwareName)
                .withHardwareId(hardwareId)
                .withHardwareUserMetadataKeys(hardwareUserMetadataKeys)
                .withHardwareUserMetadataValues(hardwareUserMetadataValues)
                .withHardwareTags(hardwareTags)
                .withCoresPerProcessor(coresPerProcessor)
                .withProcessorGhzSpeed(processorsGhzSpeed)
                .withVolumeId(volumeId)
                .withVolumeType(volumeType)
                .withVolumeName(volumeName)
                .withHypervisor(hypervisor)
                .withInboundPorts(inboundPorts)
                .withPublicKey(publicKey)
                .withPrivateKey(privateKey)
                .withRunScript(runScript)
                .withTemplateTagsString(templateTagsString)
                .withNetworksString(networksString)
                .withNodeNames(nodeNames)
                .withSecurityGroups(securityGroups)
                .withTemplateUserMetadataKeys(templateUserMetadataKeys)
                .withTemplateUserMetadataValues(templateUserMetadataValues)
                .withVolumeGbSize(volumeGbSize)
                .withNodesCount(nodesCount)
                .withProcessorsNumber(processorsNumber)
                .withRamGbAmount(ramGbAmount)
                .withBlockPort(blockPort)
                .withBlockPortSeconds(blockPortSeconds)
                .withIs64Bit(is64Bit)
                .withIsBootDevice(bootDevice)
                .withIsDurable(isDurable)
                .withBlockUntilRunning(blockUntilRunning)
                .withBlockOnComplete(blockOnComplete)
                .build();

        try {
            return new CreateNodesInGroupExecutor().execute(inputs, serverCustomInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }

    public static void main(String[] args) throws Exception {
        CreateNodesInGroupAction createNodesInGroup = new CreateNodesInGroupAction();
        Map<String, String> results = createNodesInGroup.execute("amazon",
                "https://ec2.us-east-1.amazonaws.com",
                "AKIAIXTNUGGYBYFYZR2A",
                "6othnN8+PE5+8UVsirHqJkTE2BvanmtZvTKZzhqu",
                "proxy.houston.hp.com",
                "8080",
                "",
                "",
                "", "", "", "", "default", "", "", "", "", "",
                "", "ubuntu", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "",
                "", "testIntelliJ", "", "", "", "", "1", "1", "2", "0",
                "0", "", "", "", "", "");
        System.out.println(results.get("returnResult"));
    }
}