package io.cloudslang.content.jclouds.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.*;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.DELIMITER;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.CustomInputs.INSTANCE_ID;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.EbsInputs.EBS_OPTIMIZED;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.InstanceInputs.ATTRIBUTE;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.InstanceInputs.DISABLE_API_TERMINATION;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.InstanceInputs.ENA_SUPPORT;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.InstanceInputs.INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.QueryApiActions.MODIFY_INSTANCE_ATTRIBUTE;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class ModifyInstanceAttributeAction {
    /**
     * Modifies the specified attribute of the specified instance.
     * Note: You can specify only one attribute at a time. To modify some attributes, the instance must be stopped.
     * For more information, see Modifying Attributes of a Stopped Instance in the Amazon Elastic Compute Cloud User Guide:
     * http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/Stop_Start.html
     *
     * @param endpoint                          Endpoint to which request will be sent.
     *                                          Default: "https://ec2.amazonaws.com"
     * @param identity                          ID of the secret access key associated with your Amazon AWS or IAM account.
     *                                          Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                        Secret access key associated with your Amazon AWS or IAM account.
     *                                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                         Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                                          Default: ""
     * @param proxyPort                         Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                                          <proxyPort> inputs or leave them both empty.
     *                                          Default: ""
     * @param proxyUsername                     Optional - proxy server user name.
     *                                          Default: ""
     * @param proxyPassword                     Optional - proxy server password associated with the <proxyUsername> input value.
     *                                          Default: ""
     * @param headers                           Optional - string containing the headers to use for the request separated by new line
     *                                          (CRLF). The header name-value pair will be separated by ":".
     *                                          Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                          Examples: "Accept:text/plain"
     *                                          Default: ""
     * @param queryParams                       Optional - string containing query parameters that will be appended to the URL. The
     *                                          names and the values must not be URL encoded because if they are encoded then a double
     *                                          encoded will occur. The separator between name-value pairs is "&" symbol. The query
     *                                          name will be separated from query value by "=".
     *                                          Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                          Default: ""
     * @param version                           Version of the web service to made the call against it.
     *                                          Example: "2016-09-15"
     * @param delimiter                         Optional - Delimiter that will be used.
     *                                          Default: ","
     * @param attribute                         Optional - name of the attribute.
     *                                          Valid values: "instanceType | kernel | ramdisk | userData | disableApiTermination |
     *                                          instanceInitiatedShutdownBehavior | rootDeviceName | blockDeviceMapping | productCodes |
     *                                          sourceDestCheck | groupSet | ebsOptimized | sriovNetSupport | enaSupport"
     * @param disableApiTermination             Optional - If the value is "true", you can't terminate the instance using the Amazon
     *                                          EC2 console, CLI, or API; otherwise, you can. You cannot use this paramater for Spot
     *                                          Instances.
     *                                          Valid values: "true", "false"
     *                                          Default: "false"
     * @param ebsOptimized                      Optional - Specifies whether the instance is optimized for EBS I/O. This optimization
     *                                          provides dedicated throughput to Amazon EBS and an optimized configuration stack to
     *                                          provide optimal EBS I/O performance. This optimization isn't available with all instance
     *                                          types. Additional usage charges apply when using an EBS Optimized instance.
     *                                          Valid values: "true", "false"
     *                                          Default: "false"
     * @param enaSupport                        Optional - Set to "true" to enable enhanced networking with ENA for the instance.
     *                                          This option is supported only for HVM instances. Specifying this option with a PV
     *                                          instance can make it unreachable.
     *                                          Valid values: "true", "false"
     *                                          Default: "false"
     * @param securityGroupIdsString            Optional - [EC2-VPC] Changes the security groups of the instance. You must specify
     *                                          at least one security group, even if it's just the default security group for the VPC.
     *                                          You must specify the security group IDs, not the security group names.
     *                                          Default: ""
     * @param instanceId                        ID of the instance.
     *                                          Example: "i-12345678"
     * @param instanceInitiatedShutdownBehavior Optional - Specifies whether an instance stops or terminates when you initiate
     *                                          shutdown from the instance (using the operating system command for system
     *                                          shutdown).
     *                                          Valid values: "stop", "terminate"
     *                                          Default: "stop"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Modify Instance Attribute",
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
    public Map<String, String> modifyInstanceAttributeAction(@Param(value = ENDPOINT, required = true) String endpoint,
                                                             @Param(value = IDENTITY, required = true) String identity,
                                                             @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                                             @Param(value = PROXY_HOST) String proxyHost,
                                                             @Param(value = PROXY_PORT) String proxyPort,
                                                             @Param(value = PROXY_USERNAME) String proxyUsername,
                                                             @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                                             @Param(value = HEADERS) String headers,
                                                             @Param(value = QUERY_PARAMS) String queryParams,
                                                             @Param(value = VERSION, required = true) String version,
                                                             @Param(value = DELIMITER) String delimiter,
                                                             @Param(value = ATTRIBUTE) String attribute,
                                                             @Param(value = DISABLE_API_TERMINATION) String disableApiTermination,
                                                             @Param(value = EBS_OPTIMIZED) String ebsOptimized,
                                                             @Param(value = ENA_SUPPORT) String enaSupport,
                                                             @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                                             @Param(value = INSTANCE_ID, required = true) String instanceId,
                                                             @Param(value = INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR) String instanceInitiatedShutdownBehavior) {
        try {
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
                    .withAction(MODIFY_INSTANCE_ATTRIBUTE)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withInstanceId(instanceId).build();
            EbsInputs ebsInputs = new EbsInputs.Builder().withEbsOptimized(ebsOptimized).build();
            IamInputs iamInputs = new IamInputs.Builder().withSecurityGroupIdsString(securityGroupIdsString).build();

            InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withAttribute(attribute)
                    .withDisableApiTermination(disableApiTermination)
                    .withEnaSupport(enaSupport)
                    .withInstanceInitiatedShutdownBehavior(instanceInitiatedShutdownBehavior)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, ebsInputs, iamInputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}