package io.cloudslang.content.jclouds.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.execute.images.DescribeImagesInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/6/2016.
 */
public class DescribeImagesInRegionAction {
    /**
     * Describes one or more of the images (AMIs, AKIs, and ARIs) available to you. Images available to you include
     * public images, private images that you own, and private images owned by other AWS accounts but for which you have
     * explicit launch permissions.
     * Note:
     * De-registered images are included in the returned results for an unspecified interval after de-registration.
     *
     * @param provider                     Cloud provider on which you have the images - Default: "amazon"
     * @param endpoint                     Endpoint to which request will be sent - Example: "https://ec2.amazonaws.com"
     * @param identity                     Optional - Username of your account or the Access Key ID.
     * @param credential                   Optional - Password of the user or the Secret Access Key that correspond to
     *                                     the identity input.
     * @param proxyHost                    Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort                    Optional - Proxy server port - Default: "8080"
     * @param delimiter                    Optional - Delimiter that will be used - Default: ","
     * @param debugMode                    Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region                       Optional - Region where image will be created. ListRegionAction can be used
     *                                     in order to get all regions. For further details check:
     *                                     http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region
     *                                     Default: "us-east-1".
     * @param identityId                   Optional - Scopes the images by users with explicit launch permissions. Specify
     *                                     an AWS account ID, "self" (the sender of the request), or "all" (public AMIs).
     *                                     Valid values: "self", "all" or AWS account ID - Default: ""
     * @param architecture                 Optional - Instance architecture - Valid values: "i386" or "x86_64".
     * @param deleteOnTermination          Optional - A Boolean that indicates whether the EBS volume is deleted on instance
     *                                     termination.
     * @param blockMappingDeviceName       Optional - Device name for the EBS volume - Example: "/dev/sdh".
     * @param blockDeviceMappingSnapshotId Optional - ID of the snapshot used for the Amazon EBS volume.
     * @param volumeSize                   Optional - Volume size of the Amazon EBS volume, in GiB.
     * @param volumeType                   Optional - Volume type of the Amazon EBS volume - Valid values: "gp2" (for General
     *                                     Purpose SSD volumes), "io1" (for Provisioned IOPS SSD volumes), "st1" (for Throughput
     *                                     Optimized HDD), "sc1" (for Cold HDD) and "standard" (for Magnetic volumes).
     * @param hypervisor                   Optional - Hypervisor type of the instance. Valid values: "ovm", "xen".
     * @param imageId                      Optional - ID of the specified image to search for.
     * @param kernelId                     Kernel ID.
     * @param ownerAlias                   Optional - AWS account alias. Example: "amazon"
     * @param ownerId                      Optional - AWS account ID of the instance owner.
     * @param platform                     Optional - platform used. Use "windows" if you have Windows instances; otherwise,
     *                                     leave blank. Valid values: "", "windows".
     * @param productCode                  Optional - product code associated with the AMI used to launch the instance.
     * @param productCodeType              Optional - type of product code. Valid values: "devpay", "marketplace".
     * @param ramdiskId                    Optional - RAM disk ID.
     * @param rootDeviceName               Optional - name of the root device for the instance. Example: "/dev/sda1"
     * @param rootDeviceType               Optional - type of root device that the instance uses.
     *                                     Valid values: "ebs", "instance-store".
     * @param stateReasonCode              Optional - reason code for the state change.
     * @param stateReasonMessage           Optional - a message that describes the state change.
     * @param keyTagsString                Optional - A string that contains: none, one or more key tags separated
     *                                     by delimiter - Default: ""
     * @param valueTagsString              Optional - A string that contains: none, one or more tag values separated
     *                                     by delimiter - Default: ""
     * @param virtualizationType           Optional - virtualization type of the instance - Valid values: "paravirtual", "hvm".
     * @param idsString                    Optional - A string that contains: none, one or more image IDs separated by
     *                                     delimiter - Default: ","
     * @param ownersString                 Optional - Filters the images by the owner. Specify an AWS account ID,
     *                                     "amazon" (owner is Amazon), "aws-marketplace" (owner is AWS Marketplace),
     *                                     "self" (owner is the sender of the request). Omitting this option returns all
     *                                     images for which you have launch permissions, regardless of ownership.
     *                                     Valid values: "", "amazon", "aws-marketplace", or "self" - Default: ""
     * @param description                  Optional - Description of the image (provided during image creation).
     * @param type                         Optional - Image type - Valid values: "machine", "kernel", "ramdisk".
     * @param isPublic                     Optional - A Boolean that indicates whether the image is public.
     *                                     Valid values: "true", "false"
     * @param manifestLocation             Optional - Location of the image manifest.
     * @param name                         Optional - Name of the AMI (provided during image creation).
     * @param state                        Optional - State of the image - Valid values: "available", "pending", "failed".
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Images in Region",
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
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.DELIMITER) String delimiter,
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.IDENTITY_ID) String identityId,
                                       @Param(value = Inputs.CustomInputs.ARCHITECTURE) String architecture,
                                       @Param(value = Inputs.CustomInputs.DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(value = Inputs.CustomInputs.BLOCK_MAPPING_DEVICE_NAME) String blockMappingDeviceName,
                                       @Param(value = Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_SNAPSHOT_ID) String blockDeviceMappingSnapshotId,
                                       @Param(value = Inputs.CustomInputs.VOLUME_SIZE) String volumeSize,
                                       @Param(value = Inputs.CustomInputs.VOLUME_TYPE) String volumeType,
                                       @Param(value = Inputs.CustomInputs.HYPERVISOR) String hypervisor,
                                       @Param(value = Inputs.CustomInputs.IMAGE_ID) String imageId,
                                       @Param(value = Inputs.CustomInputs.KERNEL_ID) String kernelId,
                                       @Param(value = Inputs.CustomInputs.OWNER_ALIAS) String ownerAlias,
                                       @Param(value = Inputs.CustomInputs.OWNER_ID) String ownerId,
                                       @Param(value = Inputs.CustomInputs.PLATFORM) String platform,
                                       @Param(value = Inputs.CustomInputs.PRODUCT_CODE) String productCode,
                                       @Param(value = Inputs.CustomInputs.PRODUCT_CODE_TYPE) String productCodeType,
                                       @Param(value = Inputs.CustomInputs.RAMDISK_ID) String ramdiskId,
                                       @Param(value = Inputs.CustomInputs.ROOT_DEVICE_NAME) String rootDeviceName,
                                       @Param(value = Inputs.CustomInputs.ROOT_DEVICE_TYPE) String rootDeviceType,
                                       @Param(value = Inputs.CustomInputs.STATE_REASON_CODE) String stateReasonCode,
                                       @Param(value = Inputs.CustomInputs.STATE_REASON_MESSAGE) String stateReasonMessage,
                                       @Param(value = Inputs.CustomInputs.KEY_TAGS_STRING) String keyTagsString,
                                       @Param(value = Inputs.CustomInputs.VALUE_TAGS_STRING) String valueTagsString,
                                       @Param(value = Inputs.CustomInputs.VIRTUALIZATION_TYPE) String virtualizationType,

                                       @Param(value = Inputs.ImageInputs.IDS_STRING) String idsString,
                                       @Param(value = Inputs.ImageInputs.OWNERS_STRING) String ownersString,
                                       @Param(value = Inputs.ImageInputs.DESCRIPTION) String description,
                                       @Param(value = Inputs.ImageInputs.TYPE) String type,
                                       @Param(value = Inputs.ImageInputs.IS_PUBLIC) String isPublic,
                                       @Param(value = Inputs.ImageInputs.MANIFEST_LOCATION) String manifestLocation,
                                       @Param(value = Inputs.ImageInputs.NAME) String name,
                                       @Param(value = Inputs.ImageInputs.STATE) String state) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withIdentityId(identityId)
                .withArchitecture(architecture)
                .withDeleteOnTermination(deleteOnTermination)
                .withBlockMappingDeviceName(blockMappingDeviceName)
                .withBlockDeviceMappingSnapshotId(blockDeviceMappingSnapshotId)
                .withVolumeSize(volumeSize)
                .withVolumeType(volumeType)
                .withHypervisor(hypervisor)
                .withImageId(imageId)
                .withKernelId(kernelId)
                .withOwnerAlias(ownerAlias)
                .withOwnerId(ownerId)
                .withPlatform(platform)
                .withProductCode(productCode)
                .withProductCodeType(productCodeType)
                .withRamdiskId(ramdiskId)
                .withRootDeviceName(rootDeviceName)
                .withRootDeviceType(rootDeviceType)
                .withStateReasonCode(stateReasonCode)
                .withStateReasonMessage(stateReasonMessage)
                .withKeyTagsString(keyTagsString)
                .withValueTagsString(valueTagsString)
                .withVirtualizationType(virtualizationType)
                .build();

        ImageInputs imageInputs = new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(customInputs)
                .withImageIdsString(idsString)
                .withOwnersString(ownersString)
                .withDescription(description)
                .withType(type)
                .withIsPublic(isPublic)
                .withManifestLocation(manifestLocation)
                .withImageName(name)
                .withState(state)
                .build();

        try {
            return new DescribeImagesInRegionExecutor().execute(inputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}