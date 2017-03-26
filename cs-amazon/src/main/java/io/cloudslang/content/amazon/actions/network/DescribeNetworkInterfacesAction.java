/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.aws.NetworkFilter;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_NETWORK_INTERFACES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ADDRESSES_ASSOCIATION_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ADDRESSES_ASSOCIATION_PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ADDRESSES_PRIMARY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ADDRESSES_PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ASSOCIATION_ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ASSOCIATION_ASSOCIATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ASSOCIATION_IP_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ASSOCIATION_PUBLIC_DNS_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ASSOCIATION_PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_ATTACHMENT_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_ATTACH_TIME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_INSTANCE_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_NAT_GATEWAY_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_ATTACHMENT_STATUS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_IPV6_ADDRESSES_IPV6_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_MAC_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_PRIVATE_DNS_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_REQUESTER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_REQUESTER_MANAGED;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_SOURCE_DESK_CHECK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_STATUS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_SUBNET_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_TAG;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_TAG_KEY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_TAG_VALUE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.FILTER_VPC_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.processTagFilter;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.ResponseNames.FAILURE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.tuple.ImmutablePair.of;

/**
 * Created by Tirla Alin.
 * 9/5/2016.
 */
public class DescribeNetworkInterfacesAction {

    /**
     * Describes one or more of your network interfaces.
     *
     * @param endpoint                              Optional - Endpoint to which request will be sent.
     *                                              Default: "https://ec2.amazonaws.com"
     * @param identity                              ID of the secret access key associated with your Amazon AWS or
     *                                              IAM account.
     *                                              Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                            Secret access key associated with your Amazon AWS or IAM account.
     *                                              Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                             Optional - proxy server used to connect to Amazon API. If empty no
     *                                              proxy will be used.
     * @param proxyPort                             Optional - proxy server port. You must either specify values for both
     *                                              proxyHost and proxyPort inputs or leave them both empty.
     * @param proxyUsername                         Optional - proxy server user name.
     *                                              Default: ""
     * @param proxyPassword                         Optional - proxy server password associated with the proxyUsername
     *                                              input value.
     * @param version                               Optional - Version of the web service to made the call against it.
     *                                              Example: "2016-11-15"
     *                                              Default: "2016-11-15"
     * @param headers                               Optional - string containing the headers to use for the request
     *                                              separated by new line (CRLF). The header name-value pair will be
     *                                              separated by ":"
     *                                              Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                              Examples: "Accept:text/plain"
     *                                              Default: ""
     * @param queryParams                           Optional - string containing query parameters that will be appended
     *                                              to the URL. The names and the values must not be URL encoded because
     *                                              if they are encoded then a double encoded will occur. The separator
     *                                              between name-value pairs is "&" symbol. The query name will be
     *                                              separated from query value by "="
     *                                              Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                              Default: ""
     * @param delimiter                             Optional - Delimiter that will be used.
     * @param filterAddressesPrivateIpAddress       Optional - The private IPv4 addresses associated with the network
     *                                              interface.
     * @param filterAddressesPrimary                Optional - Whether the private IPv4 address is the primary IP address
     *                                              associated with the network interface.
     * @param filterAddressesAssociationPublicIp    Optional - The association ID returned when the network interface was
     *                                              associated with the Elastic IP address (IPv4).
     * @param filterAddressesAssociationOwnerId     Optional - The owner ID of the addresses associated with the network
     *                                              interface.
     * @param filterAssociationAssociationId        Optional - The association ID returned when the network interface
     *                                              was associated with an IPv4 address.
     * @param filterAssociationAllocationId         Optional - The allocation ID returned when you allocated the Elastic
     *                                              IP address (IPv4) for your network interface.
     * @param filterAssociationIpOwnerId            Optional - The owner of the Elastic IP address (IPv4) associated
     *                                              with the network interface.
     * @param filterAssociationPublicIp             Optional - The address of the Elastic IP address (IPv4) bound to the
     *                                              network interface.
     * @param filterAssociationPublicDnsName        Optional - The public DNS name for the network interface (IPv4).
     * @param filterAttachmentAttachmentId          Optional - The ID of the interface attachment.
     * @param filterAttachmentAttachTime            Optional - The time that the network interface was attached to an
     *                                              instance.
     * @param filterAttachmentDeleteOnTermination   Optional - Indicates whether the attachment is deleted when an
     *                                              instance is terminated.
     * @param filterAttachmentDeviceIndex           Optional - The device index to which the network interface is attached.
     * @param filterAttachmentInstanceId            Optional - The ID of the instance to which the network interface is
     *                                              attached.
     * @param filterAttachmentInstanceOwnerId       Optional - The owner ID of the instance to which the network
     *                                              interface is attached.
     * @param filterAttachmentNatGatewayId          Optional - The ID of the NAT gateway to which the network interface
     *                                              is attached.
     * @param filterAttachmentStatus                Optional - The status of the attachment.
     *                                              Valid values: attaching, attached, detaching, detached.
     * @param filterAvailabilityZone                Optional - The Availability Zone of the network interface.
     * @param filterDescription                     Optional - The description of the network interface.
     * @param filterGroupId                         Optional - The ID of a security group associated with the network
     *                                              interface.
     * @param filterGroupName                       Optional - The name of a security group associated with the network
     *                                              interface.
     * @param filterIpv6AddressesIpv6Address        Optional - An IPv6 address associated with the network interface.
     * @param filterMacAddress                      Optional - The MAC address of the network interface.
     * @param filterNetworkInterfaceId              Optional - The ID of the network interface.
     * @param filterOwnerId                         Optional - The AWS account ID of the network interface owner.
     * @param filterPrivateIpAddress                Optional - The private IPv4 address or addresses of the network
     *                                              interface.
     * @param filterPrivateDnsName                  Optional - The private DNS name of the network interface (IPv4).
     * @param filterRequesterId                     Optional - The ID of the entity that launched the instance on your
     *                                              behalf (for example, AWS Management Console, Auto Scaling, and so on).
     * @param filterRequesterManaged                Optional - Indicates whether the network interface is being managed
     *                                              by an AWS service (for example, AWS Management Console, Auto Scaling,
     *                                              and so on).
     * @param filterSourceDeskCheck                 Optional - Indicates whether the network interface performs
     *                                              source/destination checking. A value of true means checking is
     *                                              enabled, and false means checking is disabled. The value must be
     *                                              false for the network interface to perform network address
     *                                              translation (NAT) in your VPC.
     * @param filterStatus                          Optional - The status of the network interface. If the network
     *                                              interface is not attached to an instance, the status is available;
     *                                              if a network interface is attached to an instance the status is in-use.
     *                                              Valid values: in-use, available.
     * @param filterSubnetId                        Optional - The ID of the subnet for the network interface.
     * @param filterTag                             Optional - The key/value combination of a tag assigned to the resource.
     *                                              Specify the key of the tag in the filter name and the value of the
     *                                              tag in the filter value.
     *                                              Example: Purpose1=X,Purpose2=B
     * @param filterTagKey                          Optional - The key of a tag assigned to the resource. This filter is
     *                                              independent of the filterTagValue filter. For example, if you use both
     *                                              filterTagKey = "Purpose" and filterTagValue = "X", you get any
     *                                              resources assigned both the tag key Purpose (regardless of what
     *                                              the tag's value is), and the tag value X (regardless of what the
     *                                              tag's key is). If you want to list only resources where Purpose is X,
     *                                              see the filterTag.
     * @param filterTagValue                        Optional - The value of a tag assigned to the resource. This filter
     *                                              is independent of the filterTagKey.
     * @param filterVpcId                           Optional - The ID of the VPC for the network interface.
     * @param networkInterfaceId                    Optional - String that contains one or more network interface IDs.
     *                                              Example: "eni-12345678,eni-87654321"
     *                                              Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Describe Network Interfaces",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
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
                                       @Param(value = FILTER_ADDRESSES_PRIVATE_IP_ADDRESS) String filterAddressesPrivateIpAddress,
                                       @Param(value = FILTER_ADDRESSES_PRIMARY) String filterAddressesPrimary,
                                       @Param(value = FILTER_ADDRESSES_ASSOCIATION_PUBLIC_IP) String filterAddressesAssociationPublicIp,
                                       @Param(value = FILTER_ADDRESSES_ASSOCIATION_OWNER_ID) String filterAddressesAssociationOwnerId,
                                       @Param(value = FILTER_ASSOCIATION_ASSOCIATION_ID) String filterAssociationAssociationId,
                                       @Param(value = FILTER_ASSOCIATION_ALLOCATION_ID) String filterAssociationAllocationId,
                                       @Param(value = FILTER_ASSOCIATION_IP_OWNER_ID) String filterAssociationIpOwnerId,
                                       @Param(value = FILTER_ASSOCIATION_PUBLIC_IP) String filterAssociationPublicIp,
                                       @Param(value = FILTER_ASSOCIATION_PUBLIC_DNS_NAME) String filterAssociationPublicDnsName,
                                       @Param(value = FILTER_ATTACHMENT_ATTACHMENT_ID) String filterAttachmentAttachmentId,
                                       @Param(value = FILTER_ATTACHMENT_ATTACH_TIME) String filterAttachmentAttachTime,
                                       @Param(value = FILTER_ATTACHMENT_DELETE_ON_TERMINATION) String filterAttachmentDeleteOnTermination,
                                       @Param(value = FILTER_ATTACHMENT_DEVICE_INDEX) String filterAttachmentDeviceIndex,
                                       @Param(value = FILTER_ATTACHMENT_INSTANCE_ID) String filterAttachmentInstanceId,
                                       @Param(value = FILTER_ATTACHMENT_INSTANCE_OWNER_ID) String filterAttachmentInstanceOwnerId,
                                       @Param(value = FILTER_ATTACHMENT_NAT_GATEWAY_ID) String filterAttachmentNatGatewayId,
                                       @Param(value = FILTER_ATTACHMENT_STATUS) String filterAttachmentStatus,
                                       @Param(value = FILTER_AVAILABILITY_ZONE) String filterAvailabilityZone,
                                       @Param(value = FILTER_DESCRIPTION) String filterDescription,
                                       @Param(value = FILTER_GROUP_ID) String filterGroupId,
                                       @Param(value = FILTER_GROUP_NAME) String filterGroupName,
                                       @Param(value = FILTER_IPV6_ADDRESSES_IPV6_ADDRESS) String filterIpv6AddressesIpv6Address,
                                       @Param(value = FILTER_MAC_ADDRESS) String filterMacAddress,
                                       @Param(value = FILTER_NETWORK_INTERFACE_ID) String filterNetworkInterfaceId,
                                       @Param(value = FILTER_OWNER_ID) String filterOwnerId,
                                       @Param(value = FILTER_PRIVATE_IP_ADDRESS) String filterPrivateIpAddress,
                                       @Param(value = FILTER_PRIVATE_DNS_NAME) String filterPrivateDnsName,
                                       @Param(value = FILTER_REQUESTER_ID) String filterRequesterId,
                                       @Param(value = FILTER_REQUESTER_MANAGED) String filterRequesterManaged,
                                       @Param(value = FILTER_SOURCE_DESK_CHECK) String filterSourceDeskCheck,
                                       @Param(value = FILTER_STATUS) String filterStatus,
                                       @Param(value = FILTER_SUBNET_ID) String filterSubnetId,
                                       @Param(value = FILTER_TAG) String filterTag,
                                       @Param(value = FILTER_TAG_KEY) String filterTagKey,
                                       @Param(value = FILTER_TAG_VALUE) String filterTagValue,
                                       @Param(value = FILTER_VPC_ID) String filterVpcId,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId) {
        try {
            version = getDefaultStringInput(version, NETWORK_DEFAULT_API_VERSION);

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
                    .withAction(DESCRIBE_NETWORK_INTERFACES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfaceId(networkInterfaceId)
                    .build();

            final List<ImmutablePair<String, String>> filterPairs = Arrays.asList(
                    of(NetworkFilter.ADDRESSES_ASSOCIATION_OWNER_ID, filterAttachmentAttachTime),
                    of(NetworkFilter.ADDRESSES_PRIVATE_IP_ADDRESS, filterAddressesPrivateIpAddress),
                    of(NetworkFilter.ADDRESSES_PRIMARY, filterAddressesPrimary),
                    of(NetworkFilter.ADDRESSES_ASSOCIATION_PUBLIC_IP, filterAddressesAssociationPublicIp),
                    of(NetworkFilter.ADDRESSES_ASSOCIATION_OWNER_ID, filterAddressesAssociationOwnerId),
                    of(NetworkFilter.ASSOCIATION_ASSOCIATION_ID, filterAssociationAssociationId),
                    of(NetworkFilter.ASSOCIATION_ALLOCATION_ID, filterAssociationAllocationId),
                    of(NetworkFilter.ASSOCIATION_IP_OWNER_ID, filterAssociationIpOwnerId),
                    of(NetworkFilter.ASSOCIATION_PUBLIC_IP, filterAssociationPublicIp),
                    of(NetworkFilter.ASSOCIATION_PUBLIC_DNS_NAME, filterAssociationPublicDnsName),
                    of(NetworkFilter.ATTACHMENT_ATTACHMENT_ID, filterAttachmentAttachmentId),
                    of(NetworkFilter.ATTACHMENT_ATTACH_TIME, filterAttachmentAttachTime),
                    of(NetworkFilter.ATTACHMENT_DELETE_ON_TERMINATION, filterAttachmentDeleteOnTermination),
                    of(NetworkFilter.ATTACHMENT_DEVICE_INDEX, filterAttachmentDeviceIndex),
                    of(NetworkFilter.ATTACHMENT_INSTANCE_ID, filterAttachmentInstanceId),
                    of(NetworkFilter.ATTACHMENT_INSTANCE_OWNER_ID, filterAttachmentInstanceOwnerId),
                    of(NetworkFilter.ATTACHMENT_NAT_GATEWAY_ID, filterAttachmentNatGatewayId),
                    of(NetworkFilter.ATTACHMENT_STATUS, filterAttachmentStatus),
                    of(NetworkFilter.AVAILABILITY_ZONE, filterAvailabilityZone),
                    of(NetworkFilter.DESCRIPTION, filterDescription),
                    of(NetworkFilter.GROUP_ID, filterGroupId),
                    of(NetworkFilter.GROUP_NAME, filterGroupName),
                    of(NetworkFilter.IPV6_ADDRESSES_IPV6_ADDRESS, filterIpv6AddressesIpv6Address),
                    of(NetworkFilter.MAC_ADDRESS, filterMacAddress),
                    of(NetworkFilter.NETWORK_INTERFACE_ID, filterNetworkInterfaceId),
                    of(NetworkFilter.OWNER_ID, filterOwnerId),
                    of(NetworkFilter.PRIVATE_IP_ADDRESS, filterPrivateIpAddress),
                    of(NetworkFilter.PRIVATE_DNS_NAME, filterPrivateDnsName),
                    of(NetworkFilter.REQUESTER_ID, filterRequesterId),
                    of(NetworkFilter.REQUESTER_MANAGED, filterRequesterManaged),
                    of(NetworkFilter.SOURCE_DESK_CHECK, filterSourceDeskCheck),
                    of(NetworkFilter.STATUS, filterStatus),
                    of(NetworkFilter.SUBNET_ID, filterSubnetId),
                    of(NetworkFilter.TAG_KEY, filterTagKey),
                    of(NetworkFilter.TAG_VALUE, filterTagValue),
                    of(NetworkFilter.VPC_ID, filterVpcId)
            );

            FilterInputs.Builder filterInputsBuilder = new FilterInputs.Builder()
                    .withDelimiter(commonInputs.getDelimiter());

            for (ImmutablePair<String, String> filterPair : filterPairs) {
                if (isNotEmpty(filterPair.getRight())) {
                    filterInputsBuilder.withNewFilter(filterPair.getLeft(), filterPair.getRight());
                }
            }

            if (isNotEmpty(filterTag)) {
                processTagFilter(filterTag, commonInputs.getDelimiter(), filterInputsBuilder);
            }

            FilterInputs filterInputs = filterInputsBuilder.build();

            return new QueryApiExecutor().execute(commonInputs, networkInputs, filterInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
