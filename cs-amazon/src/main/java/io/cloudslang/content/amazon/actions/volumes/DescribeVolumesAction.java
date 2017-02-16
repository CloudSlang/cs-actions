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
import io.cloudslang.content.amazon.entities.aws.VolumeFilter;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.entities.inputs.VolumeInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.VOLUMES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_VOLUMES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COMMA_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_RESULTS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.NEXT_TOKEN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.VolumeInputs.*;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.processTagFilter;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.tuple.ImmutablePair.of;

/**
 * Created by sandorr
 * 2/13/2017.
 */
public class DescribeVolumesAction {
    /**
     * Describes one or more volumes.
     * Note: If you are describing a long list of volumes, you can paginate the output to make the list more manageable.
     * The maxResults parameter sets the maximum number of results returned in a single page.
     * If the list of results exceeds your maxResults value, then that number of results is returned along with a
     * nextToken value that can be passed to a subsequent DescribeVolumes operation to retrieve the remaining results.
     *
     * @param endpoint                            Endpoint to which request will be sent.
     *                                            Default: "https://ec2.amazonaws.com"
     * @param identity                            ID of the secret access key associated with your Amazon AWS or IAM account.
     *                                            Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                          Secret access key associated with your Amazon AWS or IAM account.
     *                                            Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                           Optional - proxy server used to connect to Amazon API. If empty no proxy
     *                                            will be used.
     *                                            Default: ""
     * @param proxyPort                           Optional - proxy server port. You must either specify values for both
     *                                            <proxyHost> and <proxyPort> inputs or leave them both empty.
     *                                            Default: ""
     * @param proxyUsername                       Optional - proxy server user name.
     *                                            Default: ""
     * @param proxyPassword                       Optional - proxy server password associated with the <proxyUsername>
     *                                            input value.
     *                                            Default: ""
     * @param headers                             Optional - string containing the headers to use for the request separated
     *                                            by new line (CRLF). The header name-value pair will be separated by ":".
     *                                            Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                            Examples: "Accept:text/plain"
     *                                            Default: ""
     * @param queryParams                         Optional - string containing query parameters that will be appended to
     *                                            the URL. The names and the values must not be URL encoded because if
     *                                            they are encoded then a double encoded will occur. The separator between
     *                                            name-value pairs is "&" symbol. The query name will be separated from
     *                                            query value by "=".
     *                                            Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                            Default: ""
     * @param version                             Version of the web service to made the call against it.
     *                                            Example: "2016-11-15"
     *                                            Default: "2016-11-15"
     * @param delimiter                           Optional - Delimiter that will be used.
     *                                            Default: ","
     * @param filterAttachmentAttachTime          Optional - The time stamp when the attachment initiated.
     *                                            Example: "2016-12-02T10:28:20.000Z"
     *                                            Default: ""
     * @param filterAttachmentDeleteOnTermination Optional -  Whether the volume is deleted on instance termination.
     *                                            Valid values: true | false
     *                                            Default: ""
     * @param filterAttachmentDevice              Optional - The device name that is exposed to the instance.
     *                                            Example: "/dev/sda1"
     *                                            Default: ""
     * @param filterAttachmentInstanceId          Optional - The ID of the instance the volume is attached to.
     *                                            Example: "i-468cisID"
     *                                            Default: ""
     * @param filterAttachmentStatus              Optional - The attachment state.
     *                                            Valid values: attaching | attached | detaching | detached
     *                                            Default: ""
     * @param filterAvailabilityZone              Optional - The Availability Zone in which the volume was created.
     *                                            Example: "us-east-xx"
     *                                            Default: ""
     * @param filterCreateTime                    Optional - The time stamp when the volume was created.
     *                                            Default: ""
     * @param filterEncrypted                     Optional - The encryption status of the volume.
     *                                            Valid values: true | false
     *                                            Default: ""
     * @param filterSize                          Optional - The size of the volume, in GiB.
     *                                            Example: "50"
     *                                            Default: ""
     * @param filterSnapshotId                    Optional - The snapshot from which the volume was created.
     *                                            Example: "snap-1234567890abcdef0"
     *                                            Default: ""
     * @param filterStatus                        Optional - The status of the volume.
     *                                            Valid values: creating | available | in-use | deleting | deleted | error
     *                                            Default: ""
     * @param filterTag                           Optional - The key/value combination of a tag assigned to the resource.
     *                                            Example: "tagKey=tagValue"
     *                                            Default: ""
     * @param filterTagKey                        Optional - The key of a tag assigned to the resource. This filter is
     *                                            independent of the filterTagValue filter. For example, if you use both
     *                                            the filter filterTagKey="Purpose" and the filter filterTagValue="X",
     *                                            you get any resources assigned both the tag key Purpose (regardless of what the tag's value is),
     *                                            and the tag value X (regardless of what the tag's key is).
     *                                            Note: If you want to list only resources where Purpose is X,
     *                                            see the filterTag filter.
     *                                            Example: "tagKey"
     *                                            Default: ""
     * @param filterTagValue                      Optional - The value of a tag assigned to the resource.
     *                                            This filter is independent of the filterTagKey filter.
     *                                            Example: "tagValue"
     * @param filterVolumeId                      Optional - The volume ID.
     *                                            Example: "vol-049df61146c4d7901"
     *                                            Default: ""
     * @param filterVolumeType                    Optional - The Amazon EBS volume type.
     *                                            Valid values: gp2 | io1 | st1 | sc1 | standard
     *                                            Default: ""
     * @param maxResults                          Optional - The maximum number of results to return in a single call. To retrieve the
     *                                            remaining results, make another call with the returned NextToken value. This value can
     *                                            be between 5 and 1000. You cannot specify this parameter and the tag filters in the same call.
     *                                            Default: ""
     * @param nextToken                           Optional - The token to use to retrieve the next page of results. This value is null when
     *                                            there are no more results to return.
     *                                            Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Volumes",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
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
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = VOLUME_IDS_STRING) String volumeIdsString,
                                       @Param(value = FILTER_ATTACHMENT_ATTACH_TIME) String filterAttachmentAttachTime,
                                       @Param(value = FILTER_ATTACHMENT_DELETE_ON_TERMINATION) String filterAttachmentDeleteOnTermination,
                                       @Param(value = FILTER_ATTACHMENT_DEVICE) String filterAttachmentDevice,
                                       @Param(value = FILTER_ATTACHMENT_INSTANCE_ID) String filterAttachmentInstanceId,
                                       @Param(value = FILTER_ATTACHMENT_STATUS) String filterAttachmentStatus,
                                       @Param(value = FILTER_AVAILABILITY_ZONE) String filterAvailabilityZone,
                                       @Param(value = FILTER_CREATE_TIME) String filterCreateTime,
                                       @Param(value = FILTER_ENCRYPTED) String filterEncrypted,
                                       @Param(value = FILTER_SIZE) String filterSize,
                                       @Param(value = FILTER_SNAPSHOT_ID) String filterSnapshotId,
                                       @Param(value = FILTER_STATUS) String filterStatus,
                                       @Param(value = FILTER_TAG) String filterTag,
                                       @Param(value = FILTER_TAG_KEY) String filterTagKey,
                                       @Param(value = FILTER_TAG_VALUE) String filterTagValue,
                                       @Param(value = FILTER_VOLUME_ID) String filterVolumeId,
                                       @Param(value = FILTER_VOLUME_TYPE) String filterVolumeType,
                                       @Param(value = MAX_RESULTS) String maxResults,
                                       @Param(value = NEXT_TOKEN) String nextToken) {
        try {
            version = getDefaultStringInput(version, VOLUMES_DEFAULT_API_VERSION);
            delimiter = getDefaultStringInput(delimiter, COMMA_DELIMITER);

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
                    .withDelimiter(delimiter)
                    .withAction(DESCRIBE_VOLUMES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final VolumeInputs volumeInputs = new VolumeInputs.Builder()
                    .withVolumeIdsString(volumeIdsString)
                    .withMaxResults(maxResults)
                    .withNextToken(nextToken)
                    .build();

            final List<ImmutablePair<String, String>> filterPairs = Arrays.asList(
                    of(VolumeFilter.ATTACHMENT_ATTACH_TIME, filterAttachmentAttachTime),
                    of(VolumeFilter.ATTACHMENT_DELETE_ON_TERMINATION, filterAttachmentDeleteOnTermination),
                    of(VolumeFilter.ATTACHMENT_DEVICE, filterAttachmentDevice),
                    of(VolumeFilter.ATTACHMENT_INSTANCE_ID, filterAttachmentInstanceId),
                    of(VolumeFilter.ATTACHMENT_STATUS, filterAttachmentStatus),
                    of(VolumeFilter.AVAILABILITY_ZONE, filterAvailabilityZone),
                    of(VolumeFilter.CREATE_TIME, filterCreateTime),
                    of(VolumeFilter.ENCRYPTED, filterEncrypted),
                    of(VolumeFilter.SIZE, filterSize),
                    of(VolumeFilter.SNAPSHOT_ID, filterSnapshotId),
                    of(VolumeFilter.STATUS, filterStatus),
                    of(VolumeFilter.TAG_KEY, filterTagKey),
                    of(VolumeFilter.TAG_VALUE, filterTagValue),
                    of(VolumeFilter.VOLUME_ID, filterVolumeId),
                    of(VolumeFilter.VOLUME_TYPE, filterVolumeType)
            );
            final FilterInputs.Builder filterInputsBuilder = new FilterInputs.Builder()
                    .withDelimiter(delimiter);
            for (ImmutablePair<String, String> filterPair : filterPairs) {
                if (isNotEmpty(filterPair.getRight())) {
                    filterInputsBuilder.withNewFilter(filterPair.getLeft(), filterPair.getRight());
                }
            }

            if (isNotEmpty(filterTag)) {
                processTagFilter(filterTag, delimiter, filterInputsBuilder);
            }

            final FilterInputs filterInputs = filterInputsBuilder.build();

            return new QueryApiExecutor().execute(commonInputs, volumeInputs, filterInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
