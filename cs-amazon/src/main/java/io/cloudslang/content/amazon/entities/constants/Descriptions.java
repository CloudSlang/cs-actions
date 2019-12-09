/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.amazon.entities.constants;

/**
 * Created by moldovai on 21-Aug-18.
 */
public class Descriptions {
    public static class Common {
        // Inputs
        public static final String IDENTITY_DESC = "ID of the secret access key associated with your Amazon AWS or IAM account." +
                "Example: 'AKIAIOSFODNN7EXAMPLE'";
        public static final String CREDENTIAL_DESC = "Secret access key associated with your Amazon AWS or IAM account." +
                "Example: 'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY'";
        public static final String PROXY_HOST_DESC = "Proxy server used to connect to Amazon API. If empty no proxy will be used.";
        public static final String PROXY_PORT_DESC = "Proxy server port. You must either specify values for both proxyHost and " +
                "proxyPort inputs or leave them both empty." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxyUsername input value.";
        public static final String CONNECT_TIMEOUT_DESC = "The amount of time to wait (in milliseconds) when initially establishing " +
                "a connection before giving up and timing out. " +
                "Default: '10000'";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client to complete the execution " +
                "of an API call. A value of '0' disables this feature." +
                "Default: '60000'";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode." +
                "Default: 'false'";

        //Results
        public static final String RETURN_RESULT_DESC = "The full API response in case of success, or an error" +
                " message in case of failure.";
        public static final String RETURN_CODE_DESC = "\"0\" if operation was successfully executed, \"-1\" otherwise.";
        public static final String EXCEPTION_DESC = "Exception if there was an error when executing, empty otherwise.";
    }

    public static class ProvisionProductAction {
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that verifies if the operation finished\n" +
                "is executed." +
                "Default: '1000'";
        public static final String PROVISION_PRODUCT_DESCRIPTION = "Provisions the specified product.\n" +
                "A provisioned product is a resourced instance of a product. For example, provisioning a product based on " +
                "a CloudFormation template launches a CloudFormation stack and its underlying resources.";
        public static final String PRODUCT_ID_DESC = "The product identifier." +
                "Example: 'prod-n3frsv3vnznzo'";
        public static final String PROVISIONED_PRODUCT_NAME_DESC = "A user-friendly name for the provisioned product." +
                "This value must be unique for the AWS account and cannot be updated after the product is provisioned.";
        public static final String PROVISIONING_ARTIFACT_ID_DESC = "The identifier of the provisioning artifact also known as version Id." +
                "Example: 'pa-o5nvsxzzyuzjk'";
        public static final String PROVISIONING_PARAMETERS_DESC = "Template parameters in key value format, one key=value, delimited by the value from delimiter input.";
        public static final String DELIMITER_DESC = "The delimiter used to separate the values from provisioningParameters and tags inputs." +
                "Default: ','";
        public static final String TAGS_DESC = "One or more tags.";
        public static final String PROVISION_TOKEN_DESC = "An idempotency token that uniquely identifies the provisioning request.";
        public static final String ACCEPT_LANGUAGE_DESC = "String that contains the language code." +
                "Example: en (English), jp (Japanese), zh(Chinese)" +
                "Default: 'en'";
        public static final String NOTIFICATION_ARNS_DESC = "Strings that are passed to CloudFormation." +
                "The Simple Notification Service topic Amazon Resource Names to which to publish stack-related events.";
        public static final String PATH_ID_DESC = "String that contains the identifier path of the product." +
                "This value is optional if the product has a default path, and required if the product has more than one path.";
        public static final String REGION_DESC = "String that contains the Amazon AWS region name.";

        public static final String CREATED_TIME_DESC = "The UTC time stamp of the creation time.";
        public static final String PROVISIONED_PRODUCT_ID_DESC = "The identifier of the provisioned product.";
        public static final String PROVISIONED_PRODUCT_TYPE_DESC = "The type of provisioned product. The supported value is 'CFN_STACK'.";
        public static final String PROVISIONING_ARTIFACT_ID_OUT_DESC = "The identifier of the provisioned artifact.";
        public static final String STATUS_DESC = "The status of the provisioned product." +
                "Valid values: 'CREATED' - The request was created but the operation has not started." +
                "              'IN_PROGRESS' - The requested operation is in progress." +
                "              'IN_PROGRESS_IN_ERROR' - The provisioned product is under change but the requested operation failed and some remediation is occurring. For example, a rollback." +
                "              'SUCCEEDED' - The requested operation has successfully completed.+" +
                "              'FAILED' - The requested operation has unsuccessfully completed. Investigate using the error messages returned.";
        public static final String STACK_ID_DESC = "The unique stack ID that is associated with the stack.";
        public static final String STACK_NAME_DESC = "The name that is associated with the stack. The name must be unique in the region in which you are creating the stack." +
                "A stack name can contain only alphanumeric characters (case sensitive) and hyphens. It must start with an alphabetic character and cannot be longer than 128 characters.";
        public static final String STACK_OUTPUTS_DESC = "The optional Outputs section declares output values that you can import into other stacks (to create cross-stack references), return in response (to describe stack calls), or view on the AWS CloudFormation console. " +
                "The Outputs section can include the following fields: " +
                "Logical ID - An identifier for the current output. The logical ID must be alphanumeric (a-z, A-Z, 0-9) and unique within the template." +
                "Description(optional) - A String type that describes the output value" +
                "Value (required) - The value of the property returned by the aws cloudformation describe-stacks command. The value of an output can include literals, parameter references, pseudo-parameters, a mapping value, or intrinsic functions." +
                "Export (optional) - The name of the resource output to be exported for a cross-stack reference.";
        public static final String STACK_RESOURCES_DESC = "The key name of the AWS Resources that you want to include in the stack, such as an Amazon EC2 instance or an Amazon S3 bucket." +
                "The Resources section can include the following fields: " +
                "Logical ID - The logical ID must be alphanumeric (A-Za-z0-9) and unique within the template." +
                "Resource type - The resource type identifies the type of resource that you are declaring." +
                "Resource properties - Resource properties are additional options that you can specify for a resource.";
        public static final String SUCCESS_DESC = "The product was successfully provisioned.";
        public static final String FAILURE_DESC = "An error has occurred while trying to provision the product.";
    }

    public static class DescribeProvisionedProductAction {
        //Inputs
        public static final String DESCRIBE_PROVISIONED_PRODUCT_DESCRIPTION = "Gets information about the specified provisioned product.";
        public static final String PROVISIONED_PRODUCT_ACCEPTED_LANGUAGE_DESCRIPTION = "The language code.\n" +
                "Example: en (English), jp (Japanese), zh(Chinese)" +
                "Default: 'en'";
        public static final String PROVISIONED_PRODUCT_ID_DESCRIPTION = "The provisioned product identifier.";
        public static final String PROVISIONED_PRODUCT_ARN_DESC = "The ARN of the provisioned product.\n" +
                "Pattern: '[a-zA-Z0-9][a-zA-Z0-9._-]{0,127}|arn:[a-z0-9-\\.]{1,63}:[a-z0-9-\\.]{0,63}:[a-z0-9-\\.]{0,63}:[a-z0-9-\\.]{0,63}:[^/].{0,1023}'";
        public static final String PROVISIONED_PRODUCT_CREATED_TIME_DESC = "The UTC time stamp of the creation time.";
        public static final String PROVISIONED_PRODUCT_ID_DESC = "The identifier of the provisioned product.\n" +
                "Example: 'pp-almi4aq6ylmoa'";
        public static final String PROVISIONED_PRODUCT_STATUS_DESC = "The current status of the provisioned product.\n" +
                "AVAILABLE - Stable state, ready to perform any operation. The most recent operation succeeded and completed. " +
                "UNDER_CHANGE - Transitive state, operations performed might not have valid results. Wait for an AVAILABLE status before performing operations." +
                "TAINTED - Stable state, ready to perform any operation. The stack has completed the requested operation but is not exactly what was requested. For example, a request to update to a new version failed and the stack rolled back to the current version." +
                "ERROR - An unexpected error occurred, the provisioned product exists but the stack is not running. For example, CloudFormation received a parameter value that was not valid and could not launch the stack.";
        public static final String PROVISIONED_PRODUCT_NAME_DESC = "The user-friendly name of the provisioned product.";
        public static final String PROVISIONED_PRODUCT_TYPE_DESC = "The type of provisioned product. The supported value is CFN_STACK. ";
        //Results
        public static final String SUCCESS_DESC = "The action ended successfully.";
        public static final String FAILURE_DESC = "An error has occurred while trying to get details about the product.";
    }

    public static class UpdateProvisionedProductDescriptions {

        //Inputs
        public static final String PATH_ID_DESC = "The new path identifier. This value is optional if the product has a default path, and required if the product has more than one path.";
        public static final String UPDATE_PROVISIONED_PRODUCT_DESCRIPTION = "Requests updates to the configuration of the specified provisioned product.";
        public static final String PROVISIONING_ARTIFACT_ID_DESC = "The identifier of the provisioning artifact.";
        public static final String PRODUCT_ID_DESC = "The product identifier." +
                "Example: 'prod-n3frsv3vnznzo'";
        public static final String PROVISIONING_PARAMETERS_DESC = "The new parameters.";
        public static final String RECORD_ID_DESC = "The identifier of the record.";
        public static final String RECORD_ERRORS_DESC = "The errors that occurred.";
        public static final String RECORD_TAGS_DESC = "One or more tags.";
        public static final String UPDATE_TIME_DESC = "The time when the record was last updated.";
        public static final String RECORD_TYPE_DESC = "The record type.\n" +
                "PROVISION_PRODUCT\n" +
                "UPDATE_PROVISIONED_PRODUCT\n" +
                "TERMINATE_PROVISIONED_PRODUCT";
        public static final String PROVISIONED_PRODUCT_ID_DESCRIPTION = "The identifier of the provisioned product. You cannot specify both ProvisionedProductName and ProvisionedProductId.";
        public static final String PROVISIONING_USE_PREVIOUS_VALUE_DES = "If set to true, The new parameters are ignored and the previous parameter value is kept." +
                "Default: 'false'";
        public static final String PROVISIONED_PRODUCT_NAME_DESCRIPTION = "The updated name of the provisioned product. You cannot specify both ProvisionedP" +
                "roductName and ProvisionedProductId.";
        public static final String UPDATE_TOKEN_DESCRIPTION = "The idempotency token that uniquely identifies the provisioning update request.";

        //Results
        public static final String SUCCESS_DESCRIPTION = "The specified product was successfully update.";
        public static final String FAILURE_DESCRIPTION = "An error has occured while trying to update the specified product.";
    }

    public static class UnprovisionProductAction {
        public static final String UNPROVISION_PRODUCT_DESCRIPTION = "Terminates the specified provisioned product.";
        public static final String PROVISIONED_PRODUCT_ID_DESC = "The identifier of the provisioned product. You cannot specify both ProvisionedProductName and ProvisionedProductId inputs.\n" +
                "Example: 'pp-almi4aq6ylmoa'";
        public static final String PROVISIONED_PRODUCT_NAME_DESC = "The user-friendly name of the provisioned product. You cannot specify both ProvisionedProductName and ProvisionedProductId inputs.";
        public static final String IGNORE_ERRORS_DESC = "If set to 'true', AWS Service Catalog stops managing the specified provisioned product even if it cannot delete the underlying resources.\n" +
                "Default: 'false'";
        public static final String TERMINATE_TOKEN_DESC = "An idempotency token that uniquely identifies the termination request. This token is only valid during the termination process. " +
                "After the provisioned product is terminated, subsequent requests to terminate the same provisioned product always return ResourceNotFound" +
                "Pattern: [a-zA-Z0-9][a-zA-Z0-9_-]*";
        public static final String SUCCESS_DESC = "The product was successfully unprovisioned.";
        public static final String FAILURE_DESC = "An error has occurred while trying to unprovision the product.";
    }
}
