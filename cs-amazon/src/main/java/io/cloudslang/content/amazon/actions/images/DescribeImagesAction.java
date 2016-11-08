package io.cloudslang.content.amazon.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.ImageInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_IMAGES;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ARCHITECTURE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_SNAPSHOT_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.BLOCK_MAPPING_DEVICE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.HYPERVISOR;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.IDENTITY_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KERNEL_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.OWNER_ALIAS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PLATFORM;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PRODUCT_CODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PRODUCT_CODE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RAMDISK_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ROOT_DEVICE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ROOT_DEVICE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.STATE_REASON_CODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.STATE_REASON_MESSAGE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VIRTUALIZATION_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VOLUME_SIZE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VOLUME_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.IMAGE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.IS_PUBLIC;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.MANIFEST_LOCATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.OWNERS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.STATE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.TYPE;

/**
 * Created by Mihai Tusa.
 * 5/6/2016.
 */
public class DescribeImagesAction {
    /**
     * Describes one or more of the images (AMIs, AKIs, and ARIs) available to you. Images available to you include
     * public images, private images that you own, and private images owned by other AWS accounts but for which you have
     * explicit launch permissions.
     * Note:
     * De-registered images are included in the returned results for an unspecified interval after de-registration.
     *
     * @param endpoint                     Optional - Endpoint to which request will be sent.- Example: "https://ec2.amazonaws.com"
     * @param identity                     Username of your account or the Access Key ID.
     * @param credential                   Password of the user or the Secret Access Key that correspond to
     *                                     the identity input.
     * @param proxyHost                    Optional - Proxy server used to access the web site. If empty no proxy will be
     *                                     used.
     * @param proxyPort                    Optional - Proxy server port - Default: "8080"
     * @param proxyUsername                Optional - proxy server user name.
     * @param proxyPassword                Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers                      Optional - string containing the headers to use for the request separated by
     *                                     new line (CRLF).
     *                                     The header name-value pair will be separated by ":".
     *                                     Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                     Examples: "Accept:text/plain"
     * @param queryParams                  Optional - string containing query parameters that will be appended to the URL.
     *                                     The names and the values must not be URL encoded because if they are encoded
     *                                     then a double encoded will occur. The separator between name-value pairs is
     *                                     "&" symbol. The query name will be separated from query value by "=".
     *                                     Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version                      Optional - Version of the web service to made the call against it.
     *                                     Example: "2016-04-01"
     *                                     Default: "2016-04-01"
     * @param delimiter                    Optional - Delimiter that will be used - Default: ","
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
     *                                     use "others". Valid values: "", "windows".
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
     * @param virtualizationType           Optional - virtualization type of the instance
     *                                     Valid values: "paravirtual", "hvm".
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
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = ENDPOINT, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = IDENTITY_ID) String identityId,
                                       @Param(value = ARCHITECTURE) String architecture,
                                       @Param(value = DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(value = BLOCK_MAPPING_DEVICE_NAME) String blockMappingDeviceName,
                                       @Param(value = BLOCK_DEVICE_MAPPING_SNAPSHOT_ID) String blockDeviceMappingSnapshotId,
                                       @Param(value = VOLUME_SIZE) String volumeSize,
                                       @Param(value = VOLUME_TYPE) String volumeType,
                                       @Param(value = HYPERVISOR) String hypervisor,
                                       @Param(value = IMAGE_ID) String imageId,
                                       @Param(value = KERNEL_ID) String kernelId,
                                       @Param(value = OWNER_ALIAS) String ownerAlias,
                                       @Param(value = OWNER_ID) String ownerId,
                                       @Param(value = PLATFORM) String platform,
                                       @Param(value = PRODUCT_CODE) String productCode,
                                       @Param(value = PRODUCT_CODE_TYPE) String productCodeType,
                                       @Param(value = RAMDISK_ID) String ramdiskId,
                                       @Param(value = ROOT_DEVICE_NAME) String rootDeviceName,
                                       @Param(value = ROOT_DEVICE_TYPE) String rootDeviceType,
                                       @Param(value = STATE_REASON_CODE) String stateReasonCode,
                                       @Param(value = STATE_REASON_MESSAGE) String stateReasonMessage,
                                       @Param(value = KEY_TAGS_STRING) String keyTagsString,
                                       @Param(value = VALUE_TAGS_STRING) String valueTagsString,
                                       @Param(value = VIRTUALIZATION_TYPE) String virtualizationType,
                                       @Param(value = IMAGE_DESCRIPTION) String description,
                                       @Param(value = IDS_STRING) String idsString,
                                       @Param(value = OWNERS_STRING) String ownersString,
                                       @Param(value = TYPE) String type,
                                       @Param(value = IS_PUBLIC) String isPublic,
                                       @Param(value = MANIFEST_LOCATION) String manifestLocation,
                                       @Param(value = NAME) String name,
                                       @Param(value = STATE) String state) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2016-04-01");
            CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withDelimiter(delimiter)
                    .withAction(DESCRIBE_IMAGES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder()
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

            ImageInputs imageInputs = new ImageInputs.Builder()
                    .withDescription(description)
                    .withImageIdsString(idsString)
                    .withOwnersString(ownersString)
                    .withType(type)
                    .withIsPublic(isPublic)
                    .withManifestLocation(manifestLocation)
                    .withImageName(name)
                    .withState(state)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}