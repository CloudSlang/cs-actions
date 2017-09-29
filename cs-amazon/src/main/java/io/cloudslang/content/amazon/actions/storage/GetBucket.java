/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.actions.storage;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.StorageInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.S3_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.STORAGE_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.S3QueryApiActions.GET_BUCKET;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.BUCKET_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.CONTINUATION_TOKEN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.ENCODING_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.FETCH_OWNER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.MAX_KEYS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.PREFIX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.StorageInputs.START_AFTER;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class GetBucket {
    /**
     * Returns some or all (up to 1,000) of the objects in a bucket. You can use the request parameters as selection criteria
     * to return a subset of the objects in a bucket. A 200 OK response can contain valid or invalid XML. Make sure to
     * design your application to parse the contents of the response and handle it appropriately. To use this implementation
     * of the operation, you must have READ access to the bucket.
     * Note: This section describe the latest revision of the API. We recommend that you use this revised API, GET Bucket
     * (List Objects) version 2, for application development. For backward compatibility, Amazon S3 continues to support
     * the prior version of this API, GET Bucket (List Objects) version 1. For more information about the previous version,
     * see GET Bucket (List Objects) Version 1: http://docs.aws.amazon.com/AmazonS3/latest/API/RESTBucketGET.html
     *
     * @param endpoint          Optional - Endpoint to which request will be sent.
     *                          Default: "https://s3.amazonaws.com"
     * @param identity          ID of the secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential        Secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost         Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                          Default: ""
     * @param proxyPort         Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                          <proxyPort> inputs or leave them both empty.
     *                          Default: ""
     * @param proxyUsername     Optional - proxy server user name.
     *                          Default: ""
     * @param proxyPassword     Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers           Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                          The header name-value pair will be separated by ":"
     *                          Format: Conforming with HTTP standard for headers (RFC 2616)
     *                          Examples: "Accept:text/plain"
     *                          Default: ""
     * @param queryParams       Optional - string containing query parameters that will be appended to the URL. The names
     *                          and the values must not be URL encoded because if they are encoded then a double encoded
     *                          will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                          separated from query value by "="
     *                          Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                          Default: ""
     * @param version           Optional - Version of the web service to made the call against it.
     *                          Example: "2006-03-01"
     *                          Default: "2006-03-01"
     * @param bucketName        Optional - HTTP Host Header Bucket Specification as it is described in Virtual Hosting of
     *                          Buckets API Guide: http://docs.aws.amazon.com/AmazonS3/latest/dev/VirtualHosting.html
     *                          Default: ""
     * @param continuationToken Optional - When the Amazon S3 response to this API call is truncated (that is, IsTruncated
     *                          response element value is true), the response also includes the NextContinuationToken element,
     *                          the value of which you can use in the next request as the continuation-token to list the
     *                          next set of objects. The continuation token is an opaque value that Amazon S3 understands.
     *                          Amazon S3 lists objects in UTF-8 character encoding in lexicographical order.
     *                          Default: ""
     * @param delimiter         Optional - Character you use to group keys. If you specify a prefix, all keys that contain
     *                          the same string between the prefix and the first occurrence of the delimiter after the prefix
     *                          are grouped under a single result element called CommonPrefixes. If you don't specify the
     *                          prefix parameter, the substring starts at the beginning of the key. The keys that are grouped
     *                          under the CommonPrefixes result element are not returned elsewhere in the response.
     *                          Default: ""
     * @param encodingType      Optional - Requests Amazon S3 to encode the response and specifies the encoding method to use.
     *                          An object key can contain any Unicode character. However, XML 1.0 parsers cannot parse some
     *                          characters, such as characters with an ASCII value from 0 to 10. For characters that are not
     *                          supported in XML 1.0, you can add this parameter to request that Amazon S3 encode the keys in
     *                          the response.
     *                          Examples: "url"
     *                          Default: ""
     * @param fetchOwner        Optional - By default, the API does not return the Owner information in the response.
     *                          If you want the owner information in the response, you can specify this parameter with the
     *                          value set to true.
     *                          Valid values: "false", "true"
     *                          Default: "false"
     * @param maxKeys           Optional - Sets the maximum number of keys returned in the response body. If you want to
     *                          retrieve fewer than the default 1,000 keys, you can add this to your request. The response
     *                          might contain fewer keys, but it will never contain more. If there are additional keys
     *                          that satisfy the search criteria, but these keys were not returned because max-keys was
     *                          exceeded, the response contains <IsTruncated>true</IsTruncated>. To return the additional
     *                          keys, see NextContinuationToken.
     *                          Examples: "3"
     *                          Default: "1000"
     * @param prefix            Optional - Limits the response to keys that begin with the specified prefix. You can use
     *                          prefixes to separate a bucket into different groupings of keys. (You can think of using
     *                          prefix to make groups in the same way you'd use a folder in a file system.)
     *                          Examples: "E"
     *                          Default: ""
     * @param startAfter        Optional - If you want the API to return key names after a specific object key in your
     *                          key space, you can add this parameter. Amazon S3 lists objects in UTF-8 character encoding
     *                          in lexicographical order. This parameter is valid only in your first request. In case the
     *                          response is truncated, you can specify this parameter along with the continuation-token
     *                          parameter, and then Amazon S3 will ignore this parameter.
     *                          Examples: "ExampleGuide.pdf"
     *                          Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Get Bucket",
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
                                       @Param(value = BUCKET_NAME) String bucketName,
                                       @Param(value = CONTINUATION_TOKEN) String continuationToken,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = ENCODING_TYPE) String encodingType,
                                       @Param(value = FETCH_OWNER) String fetchOwner,
                                       @Param(value = MAX_KEYS) String maxKeys,
                                       @Param(value = PREFIX) String prefix,
                                       @Param(value = START_AFTER) String startAfter) {

        try {
            version = getDefaultStringInput(version, STORAGE_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, S3_API, bucketName)
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
                    .withAction(GET_BUCKET)
                    .withApiService(S3_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final StorageInputs storageInputs = new StorageInputs.Builder()
                    .withBucketName(bucketName)
                    .withContinuationToken(continuationToken)
                    .withEncodingType(encodingType)
                    .withFetchOwner(fetchOwner)
                    .withMaxKeys(maxKeys)
                    .withPrefix(prefix)
                    .withStartAfter(startAfter)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, storageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}