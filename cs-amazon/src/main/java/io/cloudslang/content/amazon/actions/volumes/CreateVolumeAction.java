/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.actions.volumes;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.VolumeInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.VOLUMES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.CREATE_VOLUME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KMS_KEY_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VOLUME_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.ENCRYPTED;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.IOPS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.SIZE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.SNAPSHOT_ID;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class CreateVolumeAction {
    /**
     * Creates an EBS volume that can be attached to an instance in the same Availability Zone.
     * Note: The volume is created in the regional endpoint that you send the HTTP request to. For more information see
     * Regions and Endpoints. You can create a new empty volume or restore a volume from an EBS snapshot. Any AWS
     * Marketplace product codes from the snapshot are propagated to the volume. You can create encrypted volumes with
     * the Encrypted parameter. Encrypted volumes may only be attached to instances that support Amazon EBS encryption.
     * Volumes that are created from encrypted snapshots are also automatically encrypted. For more information, see
     * Amazon EBS Encryption in the Amazon Elastic Compute Cloud User Guide. For more information, see Creating or
     * Restoring an Amazon EBS Volume in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param endpoint         Optional - Endpoint to which request will be sent.
     *                         Default: "https://ec2.amazonaws.com"
     * @param identity         ID of the secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential       Secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost        Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                         Default: ""
     * @param proxyPort        Optional - proxy server port. You must either specify values for both proxyHost and
     *                         proxyPort inputs or leave them both empty.
     *                         Default: ""
     * @param proxyUsername    Optional - proxy server user name.
     *                         Default: ""
     * @param proxyPassword    Optional - proxy server password associated with the proxyUsername input value.
     *                         Default: ""
     * @param headers          Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                         The header name-value pair will be separated by ":".
     *                         Format: Conforming with HTTP standard for headers (RFC 2616)
     *                         Examples: "Accept:text/plain"
     *                         Default: ""
     * @param queryParams      Optional - string containing query parameters that will be appended to the URL. The names
     *                         and the values must not be URL encoded because if they are encoded then a double encoded
     *                         will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                         separated from query value by "=".
     *                         Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                         Default: ""
     * @param version          Optional - Version of the web service to made the call against it.
     *                         Example: "2016-11-15"
     *                         Default: "2016-11-15"
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
     * @param kmsKeyId         Optional - The full IAM_INSTANCE_PROFILE_ARN of the AWS Key Management Service (AWS KMS)
     *                         customer master key (CMK) to use when creating the encrypted volume. This parameter is only
     *                         required if you want to use a non-default CMK; if this parameter is not specified, the default
     *                         CMK for EBS is used. The IAM_INSTANCE_PROFILE_ARN contains the arn:aws:kms namespace, followed
     *                         by the region of the CMK, the AWS account ID of the CMK owner, the key namespace, and then
     *                         the CMK ID.
     *                         Note: If a KmsKeyId is specified, the encrypted input must be set on "true".
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
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *         operation, or failure message and the exception if there is one
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
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,

                                       @Param(value = AVAILABILITY_ZONE, required = true) String availabilityZone,
                                       @Param(value = KMS_KEY_ID) String kmsKeyId,
                                       @Param(value = VOLUME_TYPE) String volumeType,

                                       @Param(value = ENCRYPTED) String encrypted,
                                       @Param(value = IOPS) String iops,
                                       @Param(value = SIZE) String size,
                                       @Param(value = SNAPSHOT_ID) String snapshotId) {
        try {
            version = getDefaultStringInput(version, VOLUMES_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, EC2_API, EMPTY)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withAction(CREATE_VOLUME)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withAvailabilityZone(availabilityZone)
                    .withKmsKeyId(kmsKeyId)
                    .withVolumeType(volumeType)
                    .build();

            final VolumeInputs volumeInputs = new VolumeInputs.Builder()
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
