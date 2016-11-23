package io.cloudslang.content.amazon.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.CREATE_SUBNET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VPC_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.CIDR_BLOCK;

/**
 * Created by TusaM
 * 11/22/2016.
 */
public class CreateSubnetAction {
    /**
     * Creates a subnet in an existing VPC.
     * Note: When you create each subnet, you provide the VPC ID and the CIDR block you want for the subnet. After you
     * create a subnet, you can't change its CIDR block. The subnet's CIDR block can be the same as the VPC's CIDR block
     * (assuming you want only a single subnet in the VPC), or a subset of the VPC's CIDR block. If you create more than
     * one subnet in a VPC, the subnets' CIDR blocks must not overlap. The smallest subnet (and VPC) you can create uses
     * a /28 netmask (16 IP addresses), and the largest uses a /16 netmask (65,536 IP addresses).
     * Important: AWS reserves both the first four and the last IP address in each subnet's CIDR block. They're not available
     * for use. If you add more than one subnet to a VPC, they're set up in a star topology with a logical router in the
     * middle. If you launch an instance in a VPC using an Amazon EBS-backed AMI, the IP address doesn't change if you
     * stop and restart the instance (unlike a similar instance launched outside a VPC, which gets a new IP address when
     * restarted). It's therefore possible to have a subnet with no running instances (they're all stopped), but no remaining
     * IP addresses available.
     * For more information about subnets, see Your VPC and Subnets: http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Subnets.html
     * in the Amazon Virtual Private Cloud User Guide.
     *
     * @param endpoint         Optional - Endpoint to which request will be sent.
     *                         Default: "https://ec2.amazonaws.com"
     * @param identity         ID of the secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential       Secret access key associated with your Amazon AWS or IAM account.
     *                         Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost        Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                         Default: ""
     * @param proxyPort        Optional - proxy server port. You must either specify values for both <proxyHost> and <proxyPort>
     *                         inputs or leave them both empty.
     *                         Default: ""
     * @param proxyUsername    Optional - proxy server user name.
     *                         Default: ""
     * @param proxyPassword    Optional - proxy server password associated with the <proxyUsername> input value.
     * @param version          Optional - Version of the web service to made the call against it.
     *                         Example: "2016-09-15"
     *                         Default: "2016-09-15"
     * @param headers          Optional - string containing the headers to use for the request separated by new line
     *                         (CRLF). The header name-value pair will be separated by ":"
     *                         Format: Conforming with HTTP standard for headers (RFC 2616)
     *                         Examples: "Accept:text/plain"
     *                         Default: ""
     * @param queryParams      Optional - string containing query parameters that will be appended to the URL. The names
     *                         and the values must not be URL encoded because if they are encoded then a double encoded
     *                         will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                         separated from query value by "="
     *                         Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                         Default: ""
     * @param availabilityZone Optional - Availability Zone for the subnet.
     *                         Default: AWS selects one for you. If you create more than one subnet in your VPC, we may
     *                         not necessarily select a different zone for each subnet.
     * @param vpcId            ID of the VPC
     * @param cidrBlock        Network range for the subnet, in CIDR notation.
     *                         Example: "10.0.0.0/24"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Subnet",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
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
                                       @Param(value = AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = VPC_ID, required = true) String vpcId,
                                       @Param(value = CIDR_BLOCK, required = true) String cidrBlock) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2016-09-15");

            final CommonInputs commonInputs = new CommonInputs.Builder()
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
                    .withAction(CREATE_SUBNET)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withAvailabilityZone(availabilityZone)
                    .withVpcId(vpcId)
                    .build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder().withCidrBlock(cidrBlock).build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, networkInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}