package io.cloudslang.content.jclouds.actions.volumes;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class CreateVolumeAction {
    /**
     * Creates an EBS volume that can be attached to an instance in the same Availability Zone.
     * <p>
     * Note: The volume is created in the regional endpoint that you send the HTTP request to. For more information see
     * Regions and Endpoints. You can create a new empty volume or restore a volume from an EBS snapshot. Any AWS
     * Marketplace product codes from the snapshot are propagated to the volume. You can create encrypted volumes with
     * the Encrypted parameter. Encrypted volumes may only be attached to instances that support Amazon EBS encryption.
     * Volumes that are created from encrypted snapshots are also automatically encrypted. For more information, see
     * Amazon EBS Encryption in the Amazon Elastic Compute Cloud User Guide. For more information, see Creating or
     * Restoring an Amazon EBS Volume in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param endpoint         Endpoint to which request will be sent.
     *                         Default: "https://ec2.amazonaws.com"
     * @param identity         ID of the secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential       Secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost        Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort        Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                         <proxyPort> inputs or leave them both empty.
     * @param proxyUsername    Optional - proxy server user name.
     * @param proxyPassword    Optional - proxy server password associated with the <proxyUsername> input value.
     * @param availabilityZone Specifies the Availability Zone in which to create the volume. See more on:
     *                         https://aws.amazon.com/about-aws/global-infrastructure
     * @param encrypted        Optional - Specifies whether the volume should be encrypted. Encrypted Amazon EBS volumes
     *                         may only be attached to instances that support Amazon EBS encryption. Volumes that are created
     *                         from encrypted snapshots are automatically encrypted. There is no way to create an encrypted
     *                         volume from an unencrypted snapshot or vice versa. If your AMI uses encrypted volumes, you
     *                         can only launch it on supported instance types. For more information, see Amazon EBS
     *                         Encryption in the Amazon Elastic Compute Cloud User Guide - Valid values: "false", "true"
     *                         Any other but valid values provided will be treated as "false"
     *                         Default: "false"
     * @param iops             Optional - Only valid for Provisioned IOPS SSD volumes. The number of I/O operations per
     *                         second (IOPS) to provision for the volume, with a maximum ratio of 30 IOPS/GiB.
     *                         Constraint: range is 100 to 20000 for Provisioned IOPS SSD volumes.
     * @param kmsKeyId         Optional - The full IAM_INSTANCE_PROFILE_ARN of the AWS Key Management Service (AWS KMS) customer master key
     *                         (CMK) to use when creating the encrypted volume. This parameter is only required if you
     *                         want to use a non-default CMK; if this parameter is not specified, the default CMK for EBS
     *                         is used. The IAM_INSTANCE_PROFILE_ARN contains the arn:aws:kms namespace, followed by the region of the CMK, the
     *                         AWS account ID of the CMK owner, the key namespace, and then the CMK ID.
     *                         Note: If a KmsKeyId is specified, the <encrypted> input must be set on "true".
     *                         Example: "arn:aws:kms:us-east-1:012345678910:key/abcd1234-a123-456a-a12b-a123b4cd56ef"
     * @param size             Optional - size of the volume, in GiBs. If you specify a snapshot, the volume size must be
     *                         equal to or larger than the snapshot size. If you're creating the volume from a snapshot
     *                         and don't specify a volume size, the default is the snapshot size.
     *                         Constraints: 1-16384 for "gp2", 4-16384 for "io1", 500-16384 for "st1", 500-16384 for "sc1",
     *                         and 1-1024 for "standard".
     * @param snapshotId       Optional - Snapshot from which to create the volume
     *                         Default: ""
     * @param volumeType       Optional - Volume type of the Amazon EBS volume - Valid values: "gp2" (for General
     *                         Purpose SSD volumes), "io1" (for Provisioned IOPS SSD volumes), "st1" (for Throughput
     *                         Optimized HDD), "sc1" (for Cold HDD) and "standard" (for Magnetic volumes).
     *                         Default: "standard"
     * @param version          Version of the web service to made the call against it.
     *                         Example: "2014-06-15"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Create Volume",
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
    public Map<String, String> createVolume(@Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                            @Param(value = Inputs.CommonInputs.IDENTITY, required = true) String identity,
                                            @Param(value = Inputs.CommonInputs.CREDENTIAL, required = true, encrypted = true) String credential,
                                            @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                            @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                            @Param(value = Inputs.CommonInputs.PROXY_USERNAME) String proxyUsername,
                                            @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                            @Param(value = Inputs.CommonInputs.HEADERS) String headers,
                                            @Param(value = Inputs.CommonInputs.QUERY_PARAMS) String queryParams,
                                            @Param(value = Inputs.CommonInputs.VERSION, required = true) String version,

                                            @Param(value = Inputs.CustomInputs.AVAILABILITY_ZONE, required = true) String availabilityZone,
                                            @Param(value = Inputs.CustomInputs.KMS_KEY_ID) String kmsKeyId,
                                            @Param(value = Inputs.CustomInputs.VOLUME_TYPE) String volumeType,

                                            @Param(value = Inputs.VolumeInputs.ENCRYPTED) String encrypted,
                                            @Param(value = Inputs.VolumeInputs.IOPS) String iops,
                                            @Param(value = Inputs.VolumeInputs.SIZE) String size,
                                            @Param(value = Inputs.VolumeInputs.SNAPSHOT_ID) String snapshotId) throws Exception {

        try {
            CommonInputs commonInputs = new CommonInputs.Builder()
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withEndpoint(endpoint)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withAction(Constants.QueryApiActions.CREATE_VOLUME)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpClientMethod(Constants.AwsParams.HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder()
                    .withAvailabilityZone(availabilityZone)
                    .withKmsKeyId(kmsKeyId)
                    .withVolumeType(volumeType)
                    .build();

            VolumeInputs volumeInputs = new VolumeInputs.VolumeInputsBuilder()
                    .withEncrypted(encrypted)
                    .withIops(iops)
                    .withSize(size)
                    .withSnapshotId(snapshotId)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, volumeInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}