package io.cloudslang.content.jclouds.actions.signature;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import io.cloudslang.content.jclouds.utils.OutputsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class ComputeSignatureV4 {
    /**
     * Computes the AWS Signature Version 4 used to authenticate requests by using the authorization header.
     * For this signature type the checksum of the entire payload is computed.
     * Note: The "authorizationHeader" output's value should be added in the "Authorization" header.
     *
     * @param endpoint      Service endpoint used to compute the signature. Ex.: "ec2.amazonaws.com", "s3.amazonaws.com"
     *                      Default: "ec2.amazonaws.com"
     * @param identity      ID of the secret access key associated with your Amazon AWS or IAM account.
     * @param credential    Secret access key associated with your Amazon AWS or IAM account.
     * @param amazonApi     Amazon API corresponding to where the request is send.
     *                      Examples: "ec2", "s3"
     * @param uri           request's relative URI. The URI should be from the service endpoint to the query params.
     *                      Default: "/"
     * @param httpVerb      Method used for the request. You need to specify this with upper case.
     *                      Valid values: GET, DELETE, HEAD, POST, PUT
     *                      Default: GET
     * @param payloadHash   Payload's hash that will be included in the signature. The hashing should be computed using
     *                      the "SHA-256" hashing algorithm and then hex encoded.
     * @param securityToken URI-encoded session token. The string you received from AWS STS when you obtained temporary
     *                      security credentials.
     * @param date          Date of the request. The date should be also included in the "x-amz-date" header and should
     *                      be in the in the YYYYMMDD'T'HHMMSS'Z' format form UTC time zone.
     *                      Example: 20150416T112043Z for April 16, 2015 11:20:43 AM UTC
     *                      Default: The current date and time in UTC time zone
     * @param headers       String containing the headers to use for the request separated by new line (CRLF). The header
     *                      name-value pair will be separated by ":".
     *                      Format: Conforming with HTTP standard for headers (RFC 2616)
     *                      Examples: Accept:text/plain
     * @param queryParams   String containing query parameters that will be appended to the URL. The names and the values
     *                      must not be URL encoded because if they are encoded then a double encoded will occur. The
     *                      separator between name-value pairs is "&" symbol. The query name will be separated from query
     *                      value by "="
     *                      Examples: parameterName1=parameterValue1&parameterName2=parameterValue2;
     * @return A map, with strings as keys and values, that contains: outcome of the action, returnCode of the operation
     * or failure message, the exception if there is one, signature value and authorization header value
     */
    @Action(name = "Compute Signature V4",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION),
                    @Output(Constants.AwsParams.SIGNATURE_RESULT),
                    @Output(Constants.AwsParams.AUTHORIZATION_HEADER_RESULT)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> computeSignature(@Param(value = Inputs.CommonInputs.ENDPOINT) String endpoint,
                                                @Param(value = Inputs.CommonInputs.IDENTITY, required = true) String identity,
                                                @Param(value = Inputs.CommonInputs.CREDENTIAL, required = true, encrypted = true) String credential,
                                                @Param(value = Inputs.CommonInputs.HEADERS) String headers,
                                                @Param(value = Inputs.CommonInputs.QUERY_PARAMS) String queryParams,

                                                @Param(value = Inputs.CustomInputs.AMAZON_API) String amazonApi,
                                                @Param(value = Inputs.CustomInputs.URI) String uri,
                                                @Param(value = Inputs.CustomInputs.HTTP_VERB) String httpVerb,
                                                @Param(value = Inputs.CustomInputs.PAYLOAD_HASH) String payloadHash,
                                                @Param(value = Inputs.CustomInputs.DATE) String date,

                                                @Param(value = Inputs.IamInputs.SECURITY_TOKEN) String securityToken) {
        try {
            Map<String, String> headersMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), headers,
                    Constants.AwsParams.HEADER_DELIMITER, Constants.Miscellaneous.COLON, true);
            Map<String, String> queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), queryParams,
                    Constants.Miscellaneous.AMPERSAND, Constants.Miscellaneous.EQUAL, false);

            CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .build();

            InputsWrapper wrapper = new InputsWrapper.InputsWrapperBuilder()
                    .withCommonInputs(commonInputs)
                    .withApiService(amazonApi)
                    .withRequestUri(uri)
                    .withHttpVerb(httpVerb)
                    .withRequestPayload(payloadHash)
                    .withDate(date)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withSecurityToken(securityToken)
                    .build();

            AuthorizationHeader authorizationHeader = new AmazonSignatureService().signRequestHeaders(wrapper, headersMap, queryParamsMap);

            return OutputsUtil.populateSignatureResultsMap(authorizationHeader);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}