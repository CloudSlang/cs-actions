/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Azure service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Azure service." +
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
        public static final String STATUS_CODE_DESC = "The HTTP status code for Azure API request.";

        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
    }

    public static class GetAuthTokenUsingWebAPIConstants {
        public static final String GET_AUTH_TOKEN_USING_WEB_API_OPERATION_DESC = "Get the authorization token for Azure using Web API.";


    }

    public static class ComputeCommonDescriptions {

        public static final String AZURE_PROTOCOL_DESC = "Specifies a connection protocol.\n" +
                "Valid values: https, http \n" +
                "Default: https";
        public static final String AZURE_HOST_DESC = "Specifies the Portal to which requests will be sent.\n" +
                "Default: management.azure.com";
        public static final String COMPUTE_LOCATION_DESC = "Specifies the Azure location where the resource exists.\n" +
                "Example: eastasia, westus, westeurope, japanwest";

        public static final String COMPUTE_API_VERSION_DESC = "Client Api Version." +
                "Default: 2019-07-01";
    }

    public static class DeleteVMDescriptions {

        public static final String DELETE_VM_DESC = "The operation delete a virtual machine.";

    }

    public static class CreateVMDescriptions {

        public static final String CREATE_VM_DESC = "The operation to create a virtual machine.";
        public static final String NIC_NAME_DESC = "This is the user entered NIC name for the VM.";
        public static final String AVAILABILITY_SET_NAME_DESC = "Name of the Availability set in which the VM  to be deploy.";
        public static final String DISK_TYPE_DESC = "Type of disk.\n" +
                "Allowed Values: Managed, Unmanaged \n" +
                "Default: Managed";
        public static final String ADMIN_USERNAME_DESC = "Admin Username of the VM to be provided by the User. The user " +
                "name should not contain uppercase characters A-Z, special characters except $. It should start with " +
                "alphanumeric characters and should not be any reserved word like a, admin, root, test etc.";
        public static final String ADMIN_PASSWORD_DESC = "Admin Password of the VM to be provided by the User. " +
                "The password should be with character length of 12-72. Password should be a combination of at " +
                "least 1 lower case character, 1 upper case character, 1 number, and 1 special character.";
        public static final String SSH_PUBLIC_KEY_NAME_DESC = "The name of the SSH public key.";
        public static final String VM_SIZE_DESC = "Size of the VM to be created. Options include " +
                "A0 standard , A1 standard, D1 standard, D2 standard etc   ";
        public static final String VM_NAME_DESC = "Name of the VM Instance provided by the user.";
        public static final String IMAGE_VERSION_DESC = "version of the image.\n" +
                "Default: latest";
        public static final String DISK_SIZE_IN_GB_DESC = "Size of the disk in GB. \n" +
                "Default: 10";
        public static final String STORAGE_ACCOUNT_DESC = "Name of the storageAccount. If availabilitySet is classic, " +
                "then storageAccount is used.";
        public static final String STORAGE_ACCOUNT_TYPE_DESC = "Type of the storageAccount. If availabilitySet is " +
                "aligned, then storageAccountType is used\n" +
                "Example - Standard_LRS";
        public static final String PUBLISHER_DESC = "Publisher information of the OS Image for the VM.";
        public static final String SKU_DESC = "A VM service is provisioned at a specific pricing tier or SKU. " +
                "Options include Free, Basic, or Standard.";
        public static final String OFFER_DESC = "This contains partial OS image info.";
        public static final String PLAN_DESC = " Set to true when the image you are using requires a marketplace plan, " +
                "otherwise set to false.\n" +
                "Default value: False";
        public static final String PRIVATE_IMAGE_NAME_DESC = "Name of the private image.";
        public static final String DATA_DISK_NAME_DESC = "This is the name for the data disk which is a VHD that’s " +
                "attached to a virtual machine to store application data, or other data you need to keep.";
        public static final String OS_DISK_NAME_DESC = "Name of the VM disk used as a place to store operating system, " +
                "applications and data.";
        public static final String TAG_KEY_LIST_DESC = "The keys of the tags that you want to add to your instance. The " +
                "length of this list must be equal to the length of the list contained by the tagValueList input. Use " +
                "the tag key Name in this list if you want to set a specific instance name and it will remain unchanged, " +
                "will not receive the suffix as in case of the instanceName input.";
        public static final String TAG_VALUE_LIST_DESC = "The values for the keys provided in the tagKeyList. The " +
                "lengths of these two list must be the same, and the order of the tag values has to correspond the " +
                "order of the tag keys.";
    }

    public static class CreateStreamingJob {
        public static final String CREATE_STREAMING_JOB_OPERATION_DESC = "Creates a streaming job.";
        public static final String JOB_NAME_DESC = "The name of the streaming job.";
        public static final String RESOURCE_GROUP_NAME_DESC = "The name of the resource group that contains the resource. " +
                "You can obtain this value from the Azure Resource Manager API or the portal.";

        public static final String NIC_RESOURCE_GROUP_NAME_DESC = "The name of the resource group that contains the resource. " +
                "You can obtain this value from the Azure Resource Manager API or the portal for the nic.";
        public static final String SUBSCRIPTION_ID_DESC = "GUID which uniquely identify Microsoft Azure subscription. The subscription ID forms part of the URI for every service call.";
        public static final String API_VERSION_DESC = "Client Api Version." +
                "Default: 2016-03-01";
        public static final String SKU_NAME_DESC = "The name of the SKU. Describes the SKU of the streaming job." +
                "Default: Standard";
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
                "Example: {\"key1\": \"value1\"}";
        public static final String PROVISIONING_STATE_DESC = "Describes the provisioning status of the streaming job.";
        public static final String JOB_ID_DESC = "A GUID uniquely identifying the streaming job. This GUID is generated upon creation of the streaming job.";
        public static final String JOB_STATE_DESC = "Describes the state of the streaming job.";

        public static final String ACCOUNT_NAME_DESC = "Provide the existing storage account name.";
        public static final String ACCOUNT_KEY_DESC = "Access keys to authenticate your applications when making requests to this Azure storage account.";
        public static final String SOURCE_TYPE_DESC = "Type of source . Excepted values are Reference and Stream. Default: Reference ";

    }

    public static class StartStreamingJob {

        public static final String START_STREAMING_JOB_OPERATION_DESC = "Starts a streaming job. Once a job is started it will start processing input events and produce output.";
        public static final String OUTPUT_START_MODE_DESC = "Value may be JobStartTime, CustomTime, or LastOutputEventTime to indicate whether the starting " +
                "point of the output event stream should start whenever the job is started, start at a custom user time stamp specified via the " +
                "outputStartTime property, or start from the last event output time." +
                "Valid Values: CustomTime, JobStartTime, LastOutputEventTime" +
                "Default: JobStartTime";
        public static final String OUTPUT_START_TIME_DESC = "Value is either an ISO-8601 formatted time stamp that indicates the starting point of the " +
                "output event stream, or null to indicate that the output event stream will start whenever the streaming job is started. This property must " +
                "have a value if outputStartMode is set to CustomTime.";
    }

    public static class StopStreamingJob {

        public static final String STOP_STREAMING_JOB_OPERATION_DESC = "Stops a running streaming job. This will cause a running streaming job to stop processing " +
                "input events and producing output.";
    }

    public static class GetStreamingJob {

        public static final String GET_STREAMING_JOB_OPERATION_DESC = "Gets details about the specified streaming job.";
        public static final String EXPAND_DESC = "The $expand OData query parameter. This is a comma-separated list of additional " +
                "streaming job properties to include in the response, beyond the default set returned when this parameter " +
                "is absent. The default set is all streaming job properties other than 'inputs', 'transformation', 'outputs', and 'functions'." +
                "Example: inputs,outputs,transformation,functions";
    }

    public static class CreateStreamingInputJob {
        public static final String CREATE_STREAMING_INPUT_JOB_OPERATION_DESC = "Creates a Input for streaming job.";
        public static final String STREAM_JOB_INPUT_NAME_DESC = "The name of the input.";
        public static final String SUBSCRIPTION_ID_DESC = "GUID which uniquely identify Microsoft Azure subscription. The subscription ID forms part of the URI for every service call.";
        public static final String API_VERSION_DESC = "Client Api Version." +
                "Default: 2016-03-01";
        public static final String CONTAINER_NAME_STREAM_INPUT_DESC = "creates a new container under the specified account if not exists.";

    }


    public static class CreateStreamingOutputJob {
        public static final String CREATE_STREAMING_OUTPUT_JOB_OPERATION_DESC = "Creates a Output for streaming job.";
        public static final String STREAM_JOB_OUTPUT_NAME_DESC = "The name of the output.";
        public static final String SUBSCRIPTION_ID_DESC = "GUID which uniquely identify Microsoft Azure subscription. The subscription ID forms part of the URI for every service call.";
        public static final String API_VERSION_DESC = "Client Api Version." +
                "Default: 2016-03-01";
        public static final String CONTAINER_NAME_STREAM_OUTPUT_DESC = "creates a new container under the specified account if not exists.";
    }

    public static class CreateTransformationsInputs {
        public static final String CREATE_TRANSFORMATION_OPERATION_DESC = "Creates a transformation or replaces an already existing transformation under an existing streaming job.";
        public static final String TRANSFORMATION_NAME_DESC = "The name of the transformation.";
        public static final String QUERY_DESC = "Specifies the query that will be run in the streaming job. You can learn more about the Stream Analytics Query Language (SAQL) " +
                "here:https://msdn.microsoft.com/library/azure/dn834998 . Required on PUT (CreateOrReplace) requests.";
        public static final String STREAMING_UNITS_DESC = "Specifies the number of streaming units that the streaming job uses." +
                "Default: 1";
        public static final String TRANSFORMATION_ID_DESC = "Transformation Resource Id.";
    }

    public static class Counter {
        public static final String FAILURE_MESSAGE = "Something went wrong";
        public static final String COUNTER_DESC = "Counts from one number to another number.";
        public static final String RESULT_STRING_DESC = "The primary result is resultString, Result can also be used. result (All lower case) should not be used as it is the response code.";
        public static final String RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
        public static final String FROM_DESC = "The number to start counting at.";
        public static final String TO_DESC = "The number to count to.";
        public static final String RESET_DESC = "If true, then the counter will restart counting from the beginning.";
        public static final String INCREMENT_BY_DESC = "The number to increment by while counting. If unspecified this is 1. If you wanted to count 2,4,6,8 this would be 2.";


    }

    public static class SchedulerTime {

        public static final String SCHEDULER_TIME_OPERATION_DESC = "Converts time with specified timezone and return the scheduler time.";
        public static final String SCHEDULER_START_TIME_DESC = "Scheduler start time.";
        public static final String TIME_ZONE_DESC = "Scheduler timeZone.";
        public static final String TRIGGER_EXPRESSION_DESC = "Scheduler trigger expression.";
        public static final String SCHEDULER_TIME_DESC = "Scheduler time in HH:MM:SS format";


    }

    public static class GetTimeFormat {
        public static final String GET_TIME_FORMAT_OPERATION_DESC = "This operation converts the unix time into given format.";
        public static final String EPOCH_TIME_DESC = "Epoch time.";
        public static final String DATE_FORMAT_DESC = "Date format.";
    }
}
