
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

public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Terraform service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Terraform service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username " +
                "input value.";
        public static final String RESOURCE_DESC = " Resource URl for which the Authentication Token is intended." +
                "Default: 'https://management.azure.com/'";
        public static final String TENANT_ID_DESC = " The tenantId value used to control who can sign into the application.";
        public static final String CLIENT_ID_DESC = " The Application ID assigned to your app when you registered it with Azure AD.";
        public static final String CLIENT_SECRET_DESC = "The application secret that you created in the app registration portal for your app." +
                " It cannot be used in a native app (public client), because client_secrets cannot be reliably stored on" +
                " devices. It is required for web apps and web APIs (all confidential clients), which have the ability " +
                "to store the client_secret securely on the server side.";
        public static final String SUBSCRIPTION_ID_DESC = " Specifies the unique identifier of Azure subscription.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it." +
                "Default: 'false'";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\"." +
                "Default: 'strict'";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total." +
                "Default: '20'";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis." +
                "Default: '2'";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it." +
                "Default: 'true'";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that " +
                "verifies if the operation finished is executed." +
                "Default: '1000'";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: '10000'";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP " +
                "response. If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header " +
                "will be used. If responseCharacterSet is empty and the charset from the HTTP response Content-Type " +
                "header is empty, the default value will be used. You should not use this for method=HEAD or OPTIONS." +
                "Default: 'UTF-8'";
        public static final String AUTH_TOKEN_DESC = "The authorization token for azure.";

        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client " +
                "to complete the execution of an API call. A value of '0' disables this feature." +
                "Default: '60000'";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode." +
                "Default: 'false'";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Terraform API request.";

        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
    }

    public static class CreateStreamingJob {
        public static final String JOB_NAME_DESC = "The name of the streaming job.";
        public static final String RESOURCE_GROUP_NAME_DESC = "The name of the resource group that contains the resource. " +
                "You can obtain this value from the Azure Resource Manager API or the portal.";
        public static final String SUBSCRIPTION_ID_DESC = "GUID which uniquely identify Microsoft Azure subscription. The subscription ID forms part of the URI for every service call.";
        public static final String API_VERSION_DESC = "Client Api Version.";
        public static final String SKU_NAME_DESC = "The name of the SKU. Describes the SKU of the streaming job." +
                "Default: Standard";
        ;
        public static final String EVENTS_OUT_OF_ORDER_POLICY_DESC = "Indicates the policy to apply to events that arrive out of order in the input event stream." +
                "Valid Values: Drop,Adjust" +
                "Default: Adjust";
        public static final String OUTPUT_ERROR_POLICY_DESC = "Indicates the policy to apply to events that arrive at the output" +
                " and cannot be written to the external storage due to being malformed (missing column values, column values of wrong type or size)." +
                "Valid Values: Drop, Stop" +
                "Default: Stop";
        public static final String EVENTS_OUT_OF_ORDER_MAX_DELAY_IN_SECONDS_DESC = "The maximum tolerable delay in seconds where " +
                "out-of-order events can be adjusted to be back in order." +
                "Default: 0";
        public static final String EVENTS_LATE_ARRIVAL_MAX_DELAY_IN_SECONDS_DESC = "The maximum tolerable delay in seconds where events " +
                "arriving late could be included. Supported range is -1 to 1814399 (20.23:59:59 days) and -1 is " +
                "used to specify wait indefinitely. If the property is absent, it is interpreted to have a value of -1." +
                "Default: 5";
        public static final String DATA_LOCALE_DESC = "The data locale of the stream analytics job. Value should be the name of a supported ." +
                "Default: en-US";
        public static final String COMPATIBILITY_LEVEL_DESC = "Controls certain runtime behaviors of the streaming job." +
                "Default: 1.0";
        public static final String LOCATION_DESC = "Resource location.";
        public static final String TAGS_DESC = "Resource tags." +
                "{\"key1\": \"value1\"}";
        public static final String PROVISIONING_STATE_DESC = "Describes the provisioning status of the streaming job.";
        public static final String JOB_ID_DESC = "A GUID uniquely identifying the streaming job. This GUID is generated upon creation of the streaming job.";
        public static final String JOB_STATE_DESC = "Describes the state of the streaming job.";
    }

}
