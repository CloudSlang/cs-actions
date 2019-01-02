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
package io.cloudslang.content.google-drive.entities.utils;
//de updatat
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

    public static class GetFile {

        public static final String FILE_ID_DESC = "The ID of the file.";
        public static final String ACKNOWLEDGE_ABUSE_DESC = "Whether the user is acknowledging the risk of downloading known malware or other abusive files.\n" +
                "This is only applicable when alt=media. Default: false";
        public static final String SUPPORTS_TEAM_DRIVES_DESC = "Whether the requesting application supports Team Drives. Default: false";

        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
    }

    public static class DeleteFile {

        public static final String FILE_ID_DESC = "The ID of the file.";
        public static final String SUPPORTS_TEAM_DRIVES_DESC = "Whether the requesting application supports Team Drives.";

        public static final String RETURN_RESULT_DESC = "A message is returned in case of success, an error message is returned in case of failure.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
    }


    public static class CreateFile {
       public static final String UPLOAD_TYPE = "The type of upload request to the /upload URI. Acceptable values are:\n"+
               "media - Simple upload. Upload the media only, without any metadata.\n" +
               "multipart - Multipart upload. Upload both the media and its metadata, in a single request.\n"+
               "resumable - Resumable upload. Upload the file in a resumable fashion, using a series of at least two requests where the first request includes the metadata.";
        public static final String IGNORE_DEFAULT_VISIILITY = "Whether to ignore the domain's default visibility settings for the created file.\n"+
                "Domain administrators can choose to make all uploaded files visible to the domain by default; this parameter bypasses that behavior for the request.\n" +
                "Permissions are still inherited from parent folders. Defaul: false";
        public static final String KEEP_REVISION_FOREVER = "Whether to set the 'keepForever' field in the new head revision. Defaul: false";
        public static final String OCR_LANGUAGE = "A language hint for OCR processing during image import (ISO 639-1 code).";
        public static final String SUPPORTS_TEAM_DRIVES = "Whether the requesting application supports Team Drives. Default: false";
        public static final String USE_CONTENT_AS_INDEXABLE_TEXT = "Whether to use the uploaded content as indexable text. Default: false";
        public static final String APP_PROPERTIES = "A collection of arbitrary key-value pairs which are private to the requesting app.\n" +
                "Entries with null values are cleared in update and copy requests.";
        public static final String CONTENT_HINTS_INDEXABLE_TEXT = "Text to be indexed for the file to improve fullText queries.\n" +
                "This is limited to 128KB in length and may contain HTML elements.";
        public static final String CONTENT_HINTS_THUMBNAIL_MIME_TYPE = "The thumbnail data encoded with URL-safe Base64 (RFC 4648 section 5).";
        public static final String CONTENT_HINTS_THUMBNAIL_MIME_TYPE = "The MIME type of the thumbnail.";
        public static final String COPY_REQUIERS_WRITER_PERMISSION = "Whether the options to copy, print, or download this file, should be disabled for readers and commenters.";
        public static final String CREATED_TIME = "The time at which the file was created (RFC 3339 date-time).";
        public static final String DESCRIPTION = "A short description of the file.";
        public static final String FOLDER_COLOR_RGB = "The color for a folder as an RGB hex string. The supported colors are published in the folderColorPalette field of the About resource.\n" +
                "If an unsupported color is specified, the closest color in the palette will be used instead.";
        public static final String id = "The ID of the file.";
        public static final String MIME_TYPE = "The MIME type of the file.\n" +
                "Drive will attempt to automatically detect an appropriate value from uploaded content if no value is provided. The value cannot be changed unless a new revision is uploaded.\n" +
                "\n" +
                "If a file is created with a Google Doc MIME type, the uploaded content will be imported if possible.\n" +
                "The supported import formats are published in the About resource.";
        public static final String MODIFIED_TIME = "The last time the file was modified by anyone (RFC 3339 date-time).\n" +
                "Note that setting modifiedTime will also update modifiedByMeTime for the user.";
        public static final String NAME = "The name of the file. This is not necessarily unique within a folder.\n"+
                "Note that for immutable items such as the top level folders of Team Drives, My Drive root folder, and Application Data folder the name is constant.";
        public static final String ORIGINAL_FILENAME = "The original filename of the uploaded content if available, or else the original value of the name field. This is only available for files with binary content in Drive.";
        public static final String PARENTS = "The IDs of the parent folders which contain the file.\n" +
                "If not specified as part of a create request, the file will be placed directly in the user's My Drive folder. If not specified as part of a copy request, the file will inherit any discoverable parents of the source file.\n"+
                "Update requests must use the addParents and removeParents parameters to modify the parents list.";
        public static final String PROPERTIES = "A collection of arbitrary key-value pairs which are visible to all apps.\n" +
                "Entries with null values are cleared in update and copy requests.";
        public static final String STARRERD = "Whether the user has starred the file.";
        public static final String VIEWED_BY_ME_TIME = "The last time the file was viewed by the user (RFC 3339 date-time).";
        public static final String WRITERS_CAN_SHARE = "Whether users with only writer permission can modify the file's permissions. Not populated for Team Drive files.";

        public static final String RETURN_RESULT_DESC = "The full API response in case of success, or error message in case of failure.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Google Drive API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while retrieving the message.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";

        public static final String SUCCESS_DESC = "Email message retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the email message.";

    }
}