/*
 * (c) Copyright 2021 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.cloudslang.content.azure.utils;

/**
 * Created by victor on 07.10.2016.
 */
public final class Constants {
    public static final String FORWARD_SLASH = "/";
    public static final String COMMA = ",";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_CLIENT_ID = "9ba1a5c7-f17a-4de9-a1f1-6178c8d51223";
    public static final String DEFAULT_AUTHORITY = "https://login.windows.net/common";
    public static final String DEFAULT_RESOURCE = "https://management.azure.com";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_TIMEOUT = "0";

    public static final String SHARED_ACCESS_SIGNATURE = "SharedAccessSignature uid=%s&ex=%s&sn=%s";

    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid port";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s is not a valid number";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";

    public static final String PROXY_HTTP_USER = "http.proxyUser";
    public static final String PROXY_HTTP_PASSWORD = "http.proxyPassword";

    public static final String STORAGE_AUTH_ENDPOINT = "DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s";

    public static final String SUBSCRIPTION_PATH = "/subscriptions/";
    public static final String RESOURCE_GROUPS_PATH = "/resourcegroups/";
    public static final String STREAM_ANALYTICS_PATH = "/providers/Microsoft.StreamAnalytics/";
    public static final String STREAMING_JOBS_PATH = "streamingjobs/";
    public static final String OUTPUTS_JOBS_PATH = "/outputs/";
    public static final String INPUTS_JOBS_PATH = "/inputs/";


    public static class Common {
        public static final String SET_TYPE = "Microsoft.Storage/Blob";
        public static final String CONTAINER = "state";
        public static final String PATH_PATTERN = "{date}/{time}";
        public static final String TYPE ="Csv";
        public static final String ENCODING ="UTF8";
        public static final String FIELD_DELIMETER =",";
        public static final String API = "/api";
        public static final String API_VERSION = "/v2";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_API_VERSION = "2016-03-01";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final String STRICT = "strict";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String EXCEPTION_INVALID_NAME = "The %s can only contain letters, numbers, underscores, " +
                "and hyphens";
        public static final String ANONYMOUS = "anonymous";
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String UTF8 = "UTF-8";
        public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
        public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
        public static final String AUTHORIZATION = "Authorization:";
        public static final String CONTENT_TYPE = "application/json";
        public static final String BEARER = "Bearer ";
        public static final String AZURE_HOST = "management.azure.com";
        public static final String STREAM_API_VERSION = "2016-03-01";
        public static final String PATH_SEPARATOR = "/";
        public static final String AND = "&";
        public static final String QUERY = "?";
        public static final String ID = "id";
        public static final String HTTPS = "https";
        public static final String STATUS_CODE = "statusCode";
        public static final String AUTHENTICATION_TOKEN_CONTENT_TYPE = "application/x-www-form-urlencoded";
        public static final String AUTHENTICATION_TOKEN_URL = "https://login.microsoftonline.com/";
        public static final String AUTHENTICATION_TOKEN_PATH = "/oauth2/token";
        public static final String DELIMITER = ",";
        public static final String DEFAULT_PAGE_NUMBER = "1";
        public static final String DEFAULT_PAGE_SIZE = "100";
        public static final String PAGE_NUMBER = "page[number]=";
        public static final String PAGE_SIZE = "page[size]=";
        public static final String SESSION_TIMEOUT = "20160";
        public static final String SESSION_REMEMBER = "20160";
        public static final String COLLABORATOR_AUTH_POLICY = "password";
        public static final String ALLOWED_CYPHERS = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256," +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384," +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256," +
                "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";

    }

    public static class CreateStreamingJobConstants {
        public static final String CREATE_STREAMING_JOB_OPERATION_NAME = "Create Streaming Job";
        public static final String DEFAULT_EVENTS_OUT_OF_ORDER_POLICY = "Adjust";
        public static final String DEFAULT_OUTPUT_ERROR_POLICY = "Stop";
        public static final String DEFAULT_EVENTS_OUT_OF_ORDER_MAX_DELAY_IN_SECONDS = "0";
        public static final String DEFAULT_EVENTS_LATE_ARRIVAL_MAX_DELAY_IN_SECONDS = "5";
        public static final String DEFAULT_DATA_LOCALE = "en-US";
        public static final String DEFAULT_COMPATIBILITY_LEVEL = "1.0";
        public static final String DEFAULT_SKU_NAME = "Standard";
        public static final String PROVISIONING_STATE_JSON_PATH = "$.properties.provisioningState";
        public static final String JOB_ID_JSON_PATH = "$.properties.jobId";
        public static final String JOB_STATE_JSON_PATH = "$.properties.jobState";

    }

    public static class CreateStreamingOutputJobConstants {
        public static final String CREATE_STREAMING_OUTPUT_JOB_OPERATION_NAME = "Create Output for Streaming Job";
        public static final String STREAM_JOB_OUTPUT_NAME_PATH = "$.name";
    }

    public static class CreateStreamingInputJobConstants {
        public static final String CREATE_STREAMING_INPUT_JOB_OPERATION_NAME = "Create Input for Streaming Job";
        public static final String DEFAULT_SOURCE_TYPE = "Reference";
        public static final String STREAM_JOB_INPUT_NAME_PATH = "$.name";
    }

    public static class GetAuthTokenUsingWebAPIConstants {
        public static final String GET_AUTH_TOKEN_USING_WEB_API_OPERATION_NAME = "Get Auth Token Using Web API";

    }

    public static class StartStreamingJobConstants {
        public static final String START_STREAMING_JOB_OPERATION_NAME = "Start Streaming Job";
        public static final String DEFAULT_OUTPUT_START_MODE = "JobStartTime";
        public static final String START_STREAMING_JOB_PATH = "/start";

    }
}
