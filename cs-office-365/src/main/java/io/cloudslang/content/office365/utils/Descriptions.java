/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

package io.cloudslang.content.office365.utils;

public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Office 365 service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Office 365 service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total.";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis.";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it.";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String RESPONSC_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
                "default value will be used. You should not use this for method=HEAD or OPTIONS.\n" +
                "Default value: UTF-8";

        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
    }

    public static class GetEmail {
        public static final String AUTH_TOKEN_DESC = "The authorization token for Office 365.";
        public static final String EMAIL_ADDRESS_DESC = "The email address on which to perform the action.";
        public static final String USER_PRINCIPAL_NAME_DESC = "The email address of the user to perform the action on. " +
                "The input is mutually exclusive with the userId input.";
        public static final String USER_ID_DESC = "The ID of the user to perform the action on.";
        public static final String O_DATA_QUERY_DESC = "Query parameters which can be used to specify and control the amount of data returned " +
                "in a response specified in 'key1=val1&key2=val2' format. $top and $select options should be not passed for this input because the values" +
                "for these options can be passed in topQuery and selectQuery inputs." +
                " Example: &filter=Subject eq 'Test' AND IsRead eq true " +
                "          &filter=HasAttachments eq true"+
                "          &search=\"from:help@contoso.com\""+
                "          &search=\"subject:Test\"";
        public static final String TOP_QUERY_DESC = "Query parameter use to specify the number of results. Default value: 10";
        public static final String SELECT_QUERY_DESC = "A list of query parameters in the form of a comma delimited list." +
                " Example: id,internetMessageHeaders";

        public static final String MESSAGE_ID_DESC = "The ID of the message to retrieve.";
        public static final String FOLDER_ID_DESC = "The ID of the folder which contains the message to retrieve.";

        public static final String RETURN_RESULT_DESC = "The full API response in case of success, or error message in case of failure.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Office 365 API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the message.";
        public static final String ATTACHMENT_ID_DESC = "The ID of the added attachment.";

        public static final String SUCCESS_DESC = "Email message retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the email message.";

    }

    public static class GetAuthorizationToken {
        public static final String LOGIN_TYPE_DESC = "Login method according to application type\n" +
                "Valid values: 'API', 'Native'\n" +
                "Default: 'API'";
        public static final String CLIENT_ID_DESC = "Service Client ID";
        public static final String CLIENT_SECRET_DESC = "Service Client Secret";
        public static final String USERNAME_DESC = "Office 365 username";
        public static final String PASSWORD_DESC = "Office 365 password";
        public static final String LOGIN_AUTHORITY_DESC = "The authority URL. Usually, the format for this input is:\n" +
                "'https://login.windows.net/TENANT_NAME/oauth2/token' where TENANT_NAME is your application\n" +
                "tenant.";
        public static final String RESOURCES_DESC = "The resource URL\n" +
                "Default: 'https://graph.microsoft.com'";

        public static final String RETURN_RESULT_DESC = "The authorization token for Office 365.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String AUTH_TOKEN_DESC = "The authentication token.";
        public static final String AUTH_TOKEN_TYPE_DESC = "The authentication token type.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the token.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";
    }

    public static class SendMail {
        public static final String TENANT_NAME_DESC = "Your application tenant.";
        public static final String MESSAGE_ID_DESC = "The ID of the sent mail.";
    }

    public static class SendMessage {

        public static final String MESSAGE_ID_DESC = "The ID of the message to send.";
        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while sending the email.";

    }

    public static class ListMessages {
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response containing the messages.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while trying to get the messages.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String MESSAGE_ID_LIST_DESC = "A comma-separated list of message IDs from the retrieved document.";
    }

    public static class CreateMessage {
        public static final String AUTH_TOKEN_DESC = "The Office 365 authorization token.";
        public static final String USER_PRINCIPAL_NAME_DESC = "The email address of the user to perform the action on." +
                "The input is mutually exclusive with the 'userId' input.";
        public static final String USER_ID_DESC = "The ID of the user to perform the action on. The input is mutually exclusive with the " +
                "'userPrincipalName' input.";
        public static final String FOLDER_ID_DESC = "The ID of the folder to perform the action on.";
        public static final String BCCC_RECIPIENTS_DESC = "The Bcc recipients for the message. Updatable only if 'isDraft' = true.";
        public static final String CATEGORIES_DESC = "The categories associated with the message.";
        public static final String CC_RECIPIENTS_DESC = "The Cc recipients for the message. Updatable only if 'isDraft' = true.";
        public static final String FROM_DESC = "The mailbox owner and sender of the message. Updatable only if isDraft = true. Must" +
                "correspond to the actual mailbox used.";
        public static final String IMPORTANCE_DESC = "The importance of the message. The possible values are: 'Low', 'Normal', 'High'.";
        public static final String INFERENCE_CLASSIFICATION_DESC = "The classification of the message for the user, based on inferred relevance or\n" +
                "importance, or on an explicit override. The possible values are: 'focused' or 'other'.";
        public static final String INTERNET_MESSAGE_ID_DESC = "The message ID in the format specified by RFC2822. Updatable only if 'isDraft' = true.";
        public static final String IS_READ_DESC = "Indicates whether the message has been read.";
        public static final String REPLY_TO_DESC = "The email addresses to use when replying. Updatable only if 'isDraft' = true.";
        public static final String SENDER_DESC = "The account that is actually used to generate the message. Updatable only if 'isDraft' = true,\n" +
                "and when sending a message from a shared mailbox, or sending a message as a delegate.\n" +
                "In any case, the value must correspond to the actual mailbox used.";
        public static final String TO_RECIPIENTS_DESC = "The 'To recipients' for the message. Updatable only if 'isDraft' = true.";
        public static final String BODY_DESC = "The body of the message. Updatable only if 'isDraft' = true.";
        public static final String IS_DELIVERY_RECEIPT_REQUESTED_DESC = "Indicates whether a delivery receipt is requested for the message.";
        public static final String IS_READ_RECEIPT_REQUESTED_DESC = "Indicates whether a read receipt is requested for the message.";
        public static final String SUBJECT_DESC = "The subject of the message. Updatable only if 'isDraft' = true.";
        public static final String CREATE_MESSAGE_EXCEPTION_DESC = "An error message in case there was an error while creating the message.";
        public static final String CREATE_MESSAGE_RETURN_RESULT_DESC = "The body of the created message.";
        public static final String DOCUMENT_DESC = "The full API response in case of success";
        public static final String MESSAGE_ID_DESC = "The Id of the created message";
    }

    public static class MoveMessage {
        public static final String DESTINATION_ID_DESC = "The destination folder ID, or a well-known folder name.";
    }

    public static class CreateUser {
        public static final String ACCOUNT_ENABLED_DESC = "true if the account is enabled; otherwise, false.";
        public static final String DISPLAY_NAME_DESC = "The name to display in the address book for the user.";
        public static final String ON_PREMISES_IMMUTABLE_ID_DESC = "Only needs to be specified when creating a new user account if you are using " +
                "a federated domain for the user's userPrincipalName (UPN) property.";
        public static final String MAIL_NICKNAME_DESC = "The mail alias for the user.";
        public static final String CREATE_USER_PRINCIPAL_NAME_DESC = "The user principal name (someuser@contoso.com).";
        public static final String FORCE_CHANGE_PASSWORD_DESC = "A boolean value to specify if the user must change his password on the next login.";
        public static final String CREATE_USER_PASSWORD_DESC = "The password for the user. This property is required when a user is created." +
                " It can be updated, but the user will be required to change the password on the next login." +
                " The password must satisfy minimum requirements as specified by the user’s passwordPolicies property. By default, a strong password is required.";
        public static final String CREATE_USER_RETURN_RESULT_DESC = "If successful, returns the complete API response.";
        public static final String CREATE_USER_DOCUMENT_DESC = "The full API response in case of success";
        public static final String CREATE_USER_EXCEPTION_DESC = "An error message in case there was an error while creating the user.";
    }

    public static class UpdateUser {
        public static final String UPDATE_USER_RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String UPDATE_USER_EXCEPTION_DESC = "An error message in case there was an error while updating the user.";
        public static final String UPDATE_USER_PASSWORD_DESC = "The new password for the user.";
        public static final String UPDATE_USER_ON_PREMISES_IMMUTABLE_ID_DESC = "Only needs to be specified when updating a user account if you are using " +
                "a federated domain for the user's userPrincipalName (UPN) property.";
        public static final String UPDATE_USER_ID_TO_UPDATE_DESC = "The ID of the user to perform the action on.";
        public static final String UPDATE_USER_PRINCIPAL_NAME_TO_UPDATE_DESC = "The userPrincipalName of the user to perform the action on.";
        public static final String CREATE_USER_PRINCIPAL_NAME_TO_UPDATE_WITH_DESC = "The new userPrincipalName fot the user.";
    }

    public static class GetUser {
        public static final String GET_USER_RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String GET_USER_EXCEPTION_DESC = "An error message in case there was an error while retrieving the user properties and relationships.";
        public static final String ID_OUTPUT_DESC = "The ID of the user";
    }


    public static class DeleteUser {
        public static final String DELETE_USER_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content response code. It does not return anything in the response body.";
        public static final String DELETE_USER_EXCEPTION_DESC = "An error message in case there was an error while deleting the user.";
        public static final String SUCCESS_RETURN_RESULT_DESC = "The user was successfully deleted.";
    }

    public static class ListUser {
        public static final String LIST_USERS_RESULT_DESC = "If successful, returns the complete API response.";
        public static final String LIST_USERS_DOCUMENT_DESC = "The full API response in case of success";
        public static final String LIST_USERS_EXCEPTION_DESC = "An error message in case there was an error while retrieving the list of user objects.";
    }

    public static class AddAttachment {
        public static final String FILE_PATH_DESC = "The absolute path to the file that will be attached.";
        public static final String CONTENT_NAME_DESC = "The name of the file that will be attached.";
        public static final String CONTENT_BYTES_DESC = "The representation in bytes of the file that will be attached.";
        public static final String ADD_ATTACHMENT_RETURN_RESULT_DESC = "If the attachment is successful, returns the complete API response.";
        public static final String ADD_ATTACHMENT_EXCEPTION_DESC = "An error message in case there was an error adding the attachment.";
        public static final String SUCCESS_DESC = "Attachment was added successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to add the attachment.";
    }

    public static class ListAttachments {
        public static final String ATTACHMENT_ID_DESC = "A list of attachment id's delimited by a comma";
        public static final String SUCCESS_DESC = "Attachment ID retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the attachment id of the message.";
    }

    public static class GetAttachments {
        public static final String ATTACHMENT_ID_DESC = "The ID of the attachment to be retrieved.";
        public static final String FILE_PATH_DESC = "The file path under which the attachment will be downloaded. The attachment will not be downloaded if a path is not provided.";

        public static final String CONTENT_NAME_DESC = "The name of the attachment.";
        public static final String CONTENT_TYPE_DESC = "The content type of the attachment.";
        public static final String CONTENT_BYTES_DESC = "The bytes contained in the attachment.";
        public static final String CONTENT_SIZE_DESC = "The size of the attachment.";

        public static final String SUCCESS_DESC = "Attachment was retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the attachment.";
    }
}