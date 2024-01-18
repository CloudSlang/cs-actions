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

package io.cloudslang.content.sharepoint.utils;

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
                "\"allow_all\" to skip any checking";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total.";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis.";

        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in seconds) to allow the client to complete the execution." +
                " A value of '0' disables this feature. \n" +
                "Default: 60  \n";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
                "default value will be used. You should not use this for method=HEAD or OPTIONS.\n" +
                "Default value: UTF-8";

        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";

        public static final String SESSION_CONNECTION_POOL_DESC = "The GlobalSessionObject that holds the http client pooling connection manager.";
        public static final String SESSION_COOKIES_DESC = "The session object that holds the cookies if the useCookies input is true.";

        public static final String TLS_VERSION_DESCRIPTION = "The version of TLS to use. The value of this input will be ignored if 'protocol'" +
                "is set to 'HTTP'. This capability is provided “as is”, please see product documentation for further information." +
                "Valid values: TLSv1, TLSv1.1, TLSv1.2, TLSv1.3 \n" +
                "Default value: TLSv1.2  \n";
        public static final String ALLOWED_CIPHERS_DESCRIPTION = "A list of ciphers to use. The value of this input will be ignored " +
                "if 'tlsVersion' does " +
                "not contain 'TLSv1.2'. This capability is provided “as is”, please see product documentation for further security considerations." +
                "In order to connect successfully to the target host, it should accept at least one of the following ciphers. If this is not the case, it is " +
                "the user's responsibility to configure the host accordingly or to update the list of allowed ciphers. \n" +
                "Default value: TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, " +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256, " +
                "TLS_RSA_WITH_AES_128_CBC_SHA256.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request";
        public static final String AUTH_TOKEN_DESC = "The authentication token";
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
        public static final String SCOPE_DESC = "The scope URL. The scope consists of a series of resource permissions separated " +
                "by a comma (,), i.e.: 'https://graph.microsoft.com/User.Read, https://graph.microsoft.com/Sites.Read'. " +
                "The 'https://graph.microsoft.com/.default' scope means that the user consent to all of the configured permissions " +
                "present on the specific Azure AD Application. For the 'API' loginType, '/.default' scope should be used. \n" +
                "Default: 'https://graph.microsoft.com/.default'";

        public static final String RETURN_RESULT_DESC = "The authorization token for Office 365 Sharepoint.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String AUTH_TOKEN_DESC = "The authentication token.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the token.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";
    }

    public static class GetRootSite {
        public static final String RETURN_RESULT_DESC = "The root site for Office 365.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the root site.";

        public static final String SUCCESS_DESC = "Root site retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to get the root site.";
        public static final String AUTH_TOKEN_DESC = "The auth token for login.";
        public static final String SITE_ID_DESC = "ID of the requested site.";
        public static final String SITE_NAME_DESC = "Name of the site.";
        public static final String SITE_DISPLAY_NAME_DESC = "The display name of the site.";
        public static final String WEB_URL_DESC = "URL of the site.";
    }

    public static class GetAllSites {
        public static final String SITE_IDS_DESC = "An array of pairs: displayName and id.";
        public static final String SITE_URLS_DESC = "An array of pairs: displayName and url.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the sites.";

    }

    public static class GetSiteIdByName {
        public static final String RETURN_RESULT_DESC = "Information related to the specific site in json format";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the site id.";
        public static final String AUTH_TOKEN_DESC = "The authentication token.";
        public static final String SITE_NAME_DESC = "Name of the site from which ID can be obtained";
        public static final String SITE_ID_DESC = "The id of the site for which the name was provided.";
        public static final String SUCCESS_DESC = "Site id was returned successfully";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve site id.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";

    }

    public static class GetRootDrive {

        public static final String NAME = "get_rot_drive";
        public static final String SITE_ID_DESC = "The id of the site from which to retrieve the root drive";
        public static final String RETURN_RESULT_DESC = "Information related to the specific root drive metadata in json format";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the root drive.";
        public static final String WEB_URL_DESC = "Root drive's web url";
        public static final String DRIVE_NAME_DESC = "Root drive's name";
        public static final String DRIVE_TYPE_DESC = "Root drive's type";
        public static final String DRIVE_ID_DESC = "Root drive's id";
        public static final String AUTH_TOKEN_DESC = "The authentication token";
        public static final String SUCCESS_DESC = "Root drive was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the root drive.";
    }

    public static class GetSiteNameById {
        public static final String SITE_ID_DESC = "ID of the site from which display name can be obtained";
        public static final String SITE_NAME_DESC = "The name of the site for which the id was provided.";
        public static final String SITE_DISPLAY_NAME_DESC = "The display name of the site for which the id was provided.";
        public static final String SUCCESS_DESC = "Site name was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the site name.";
        public static final String RETURN_RESULT_DESC = "Information related to the specific site in JSON format";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the site display name.";
        public static final String AUTH_TOKEN_DESC = "The authentication token.";
    }

    public static class GetSiteDetails {

        public static final String NAME = "get_site_details";

        public static final String SITE_ID_DESC = "The id of the site from which to retrieve the details.";

        public static final String RETURN_RESULT_DESC = "Details of the site.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the site details.";
        public static final String WEB_URL_DESC = "Web url of the site.";
        public static final String SITE_NAME_DESC = "Name of the site.";
        public static final String SITE_ID_OUT_DESC = "Id of the site.";
        public static final String SITE_DISPLAY_NAME_DESC = "Display name of the site.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Site details were returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the site details.";
    }

    public static class GetDriveNameById {

        public static final String NAME = "get_drive_name_by_id";

        public static final String DRIVE_ID_DESC = "The id of the drive from which to retrieve the name.";

        public static final String RETURN_RESULT_DESC = "Details of the drive.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the drive name.";
        public static final String DRIVE_NAME_DESC = "Name of the drive.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Drive name was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the drive name.";
    }

    public static class GetEntitiesFromDrive {

        public static final String NAME = "get_entities_from_drive";

        public static final String DRIVE_ID_DESC = "The id of the drive from which to retrieve the entities.";
        public static final String PATH_DESC = "The path to drive entities relative to root. Leave empty for root.";
        public static final String ENTITIES_TYPE_DESC = "Type of drive entities. Valid values: 'folders', 'files', 'all'.";

        public static final String RETURN_RESULT_DESC = "Json containing a list of all retrieved drive entities.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the drive entities.";
        public static final String ENTITY_IDS_DESC = "List of pairs containing the entity's name and the corresponding id.";
        public static final String ENTITY_URLS_DESC = "List of pairs containing the entity's name and the corresponding url.";
        public static final String ENTITY_TYPES_DESC = "List of pairs containing the entity's name and the corresponding type.";
        public static final String ENTITY_PATHS_DESC = "List of pairs containing the entity's name and the corresponding path.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Drive entities were returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the drive entities.";
    }

    public static class GetAllDrives {

        public static final String NAME = "get_all_drives";

        public static final String SITE_ID_DESC = "The id of the site from which to retrieve the drives.";

        public static final String RETURN_RESULT_DESC = "List of all drives that can be found on the site with the specified id.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the drives.";
        public static final String DRIVE_IDS_DESC = "List of pairs containing the drive's name and the corresponding id.";
        public static final String DRIVE_URLS_DESC = "List of pairs containing the drive's name and the corresponding url.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Drives were returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the drive.";
    }

    public static class GetDriveIdByName {

        public static final String NAME = "get_drive_name_by_id";

        public static final String DRIVE_ID_DESC = "The id of the drive from which to retrieve the name.";

        public static final String RETURN_RESULT_DESC = "Details of the drive.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the drive name.";
        public static final String DRIVE_NAME_DESC = "Name of the drive.";
        public static final String SITE_ID_DESC = "Id of the site.";


        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Drive name was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the drive name.";
        public static final String NO_DRIVE_FOUND = "There is no drive with the specified name";
    }

    public static class UploadFile {
        public static final String SITE_ID_DESC = "Id of the site where the file will be uploaded.";
        public static final String WEB_URL_DESC = "Web url of the uploaded file";

        public static final String FILE_PATH_DESC = "The absolute path to the file that will be uploaded.";
        public static final String DRIVE_ID_DESC = "Id of the drive where the file will be uploaded.";
        public static final String FOLDER_ID_DESC = "Id of the folder where the file will be uploaded.";
        public static final String FILE_ID_DESC = "The Id of the file that was uploaded.";
        public static final String RETURN_RESULT_DESC = "The full API response in case of success.";
        public static final String SUCCESS_DESC = "The file was uploaded successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to upload the file.";
        public static final String EXCEPTION_DESC = "There was an error while trying to upload the file.";
    }

    public static class CreateFolder {
        public static final String CREATE_FOLDER = "Create folder";
        public static final String CREATE_FOLDER_DESC = "Creates an Office 365 Sharepoint folder.";
        public static final String FOLDER_NAME_DESC = "The name of the folder to be created. If body input is not empty, this input is ignored.";

        public static final String JSON_BODY_DESC = "The body to be sent in the request. If empty, folder name input must contain a name for the folder.";
        public static final String SITE_ID_DESC = "The id of the site where the folder will be created.";
        public static final String USER_ID_DESC = "The id of the user for which the folder will be created.";
        public static final String DRIVE_ID_DESC = "The id of the drive where which the folder will be created.";
        public static final String PARENT_ITEM_ID_DESC = "The id of the parent item for which the folder will be created.";
        public static final String GROUP_ID_DESC = "The id of the group where the folder will be created.";
        public static final String EXCEPTION_DESC = "There was an error while trying to create the folder.";
        public static final String ID_DESC = "The id of the created folder.";
        public static final String WEB_URL_DESC = "The web url of the created folder.";
        public static final String HOST_EXCEPTION_DESC = "You must provide exactly one input on the ids inputs(drive_id, group_id, site_id, user_id).";

    }

    public static class DeleteFolder {
        public static final String DELETE_FOLDER = "Delete folder";
        public static final String DELETE_FOLDER_DESC = "Deletes an Office 365 Sharepoint folder.";
        public static final String SITE_ID_DESC = "The id of the site where the folder will be deleted.";
        public static final String EXCEPTION_DESC = "There was an error while trying to delete the folder.";
        public static final String SITE_ID_DELETE_FOLDER_DESC = "The id of the site from where the folder will be deleted.";
        public static final String DRIVE_ID_DELETE_FOLDER_DESC = "The id of the drive from where the folder will be deleted.";
        public static final String DELETE_FOLDER_SUCCESS_DESC = "The folder was deleted successfully.";

        public static final String FOLDER_ID_DESC = "The id of the folder to be deleted.";
        public static final String USER_ID_DESC = "The id of the user for which the folder will be deleted.";
        public static final String GROUP_ID_DESC = "The id of the group where the folder will be deleted.";
    }

    public static class DeleteFile {
        public static final String DELETE_FILE = "Delete file";
        public static final String DELETE_FILE_DESC = "Deletes an Office 365 Sharepoint file.";
        public static final String SITE_ID_DESC = "The id of the site where the file will be deleted.";
        public static final String EXCEPTION_DESC = "There was an error while trying to delete the file.";
        public static final String SITE_ID_DELETE_FILE_DESC = "The id of the site from where the file will be deleted.";
        public static final String DRIVE_ID_DELETE_FILE_DESC = "The id of the drive from where the file will be deleted.";
        public static final String DELETE_FILE_SUCCESS_DESC = "The file was deleted successfully.";

        public static final String FILE_ID_DESC = "The id of the file to be deleted.";
        public static final String USER_ID_DESC = "The id of the user for which the file will be deleted.";
        public static final String GROUP_ID_DESC = "The id of the group where the file will be deleted.";
    }

    public static class GetEntityShareLink {

        public static final String NAME = "get_entity_share_link";

        public static final String ENTITY_ID_DESC = "The id of the entity for which to generate the share link.";
        public static final String SITE_ID_DESC = "The id of the site where the entity is located. Mutually exclusive " +
                "with the drive Id input. Ignored if the drive Id was provided.";
        public static final String DRIVE_ID_DESC = "The id of the drive where the entity is located. Mutually exclusive with the site Id input.";
        public static final String TYPE_DESC = "The type of sharing link to create.\n" +
                "view: Creates a read-only link to the DriveItem.\n" +
                "edit: Creates a read-write link to the DriveItem.\n" +
                "embed: Creates an embeddable link to the DriveItem. This option is only available for files in OneDrive personal.\n" +
                "Valid values: view, edit, embed.";
        public static final String PASSWORD_DESC = "The password of the sharing link that is set by the creator. Optional and OneDrive Personal only.";
        public static final String EXPIRATION_DATE_TIME_DESC = "A String with format of yyyy-MM-ddTHH:mm:ssZ of " +
                "DateTime indicates the expiration time of the permission.";
        public static final String RETAIN_INHERITED_PERMISSIONS_DESC = "If true, any existing " +
                "inherited permissions are retained on the shared item when sharing this item for the first time. " +
                "If false, all existing permissions are removed when sharing for the first time.\n" +
                "Valid value: true, false\n" +
                "Default value: true";
        public static final String SCOPE_DESC = "The scope of link to create. If the scope parameter is not specified, " +
                "the default link type for the organization is created.\n" +
                "anonymous: Anyone with the link has access, without needing to sign in. This may include people " +
                "outside of your organization. Anonymous link support may be disabled by an administrator.\n" +
                "organization: Anyone signed into your organization (tenant) can use the link to get access. Only " +
                "available in OneDrive for Business and SharePoint.\n" +
                "users: Share only with people you choose inside or outside the organization.\n" +
                "Valid values: anonymous, organization, users.";

        public static final String RETURN_RESULT_DESC = "Details related to the generated share link.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to get the share link.";
        public static final String SHARE_LINK_DESC = "The web URL of the share link.";
        public static final String SHARE_ID_DESC = "The id of the share link.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Share link was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the share link.";
    }

    public static class DownloadFile {

        public static final String NAME = "download_file";

        public static final String FILE_ID_DESC = "The id of the file to download.";
        public static final String SITE_ID_DESC = "The id of the site where the file is located. Mutually exclusive " +
                "with the drive Id input. Ignored if the drive Id was provided.";
        public static final String DRIVE_ID_DESC = "The id of the drive where the file is located. Mutually exclusive with the site Id input.";
        public static final String PATH_DESC = "The folder where the file will be downloaded.";
        public static final String OVERWRITE_DESC = "If this input is true and a file with the same name already exists in the specified path, " +
                "overwrite it with the downloaded file. Create a new file without overwriting if false.\n" +
                "Valid values: true, false\n" +
                "Default value: true";

        public static final String RETURN_RESULT_DESC = "Details related to the downloaded file.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to download the file.";
        public static final String SIZE_DESC = "Size of the downloaded file.";
        public static final String CREATED_DATE_TIME_DESC = "Created date and time of the downloaded file.";
        public static final String LAST_MODIFIED_DATE_TIME_DESC = "Last modified date and time of the downloaded file.";
        public static final String LAST_MODIFIED_BY_DESC = "Last user that modified the downloaded file.";
        public static final String FILE_TYPE_DESC = "Type of the downloaded file.";
        public static final String FILE_NAME_DESC = "Name of the downloaded file";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "File downloaded successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to download the file.";
    }

    public static class GetEntityIdByName {

        public static final String NAME = "get_entity_id_by_name";

        public static final String ENTITY_NAME_DESC = "The name of the entity from which to retrieve the id. Only works " +
                "with the drive Id input provided. Mutually exclusive with the entity path input. Ignored if entity path was provided.";
        public static final String PARENT_FOLDER_DESC = "The parent folder of the entity from which to retrieve the id. " +
                "Use this in case there are multiple entities with the same name, but with different parent folders. Only " +
                "works with the drive Id input provided. Mutually exclusive with the entity path input. Ignored if entity path was provided.";
        public static final String ENTITY_PATH_DESC = "The full path of the entity from which to retrieve the id. " +
                "Mutually exclusive with the entity name and parent folder inputs.";
        public static final String SITE_ID_DESC = "The id of the site where the entity is located. Site Id only allows " +
                "full path searches, so the entity name and parent folder inputs will be ignored. Mutually exclusive " +
                "with the drive Id input. Ignored if the drive Id was provided.";
        public static final String DRIVE_ID_DESC = "The id of the drive where the entity is located. Mutually exclusive with the site Id input.";

        public static final String RETURN_RESULT_DESC = "Details related to the retrieved entity.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for the request.";
        public static final String EXCEPTION_DESC = "There was an error while trying to retrieve the entity id.";
        public static final String ENTITY_ID_DESC = "The id of the entity.";
        public static final String WEB_URL_DESC = "The URL of the entity.";

        public static final String AUTH_TOKEN_DESC = "The authentication token.";

        public static final String SUCCESS_DESC = "Entity id was returned successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve the entity id.";
    }

    public static class SearchForEntities {
        public static final String SEARCH_FOR_ENTITIES = "Search for entities";
        public static final String SEARCH_FOR_ENTITIES_DESC = "Retrieve all drive items in SharePoint that match the search query.";
        public static final String SITE_ID_DESC = "The id of the site where the entities will be searched.";
        public static final String USER_ID_DESC = "The id of the user for which the entities will be searched.";
        public static final String DRIVE_ID_DESC = "The id of the drive where the entities will be searched.";
        public static final String GROUP_ID_DESC = "The id of the group where the entities will be searched.";
        public static final String SEARCH_TEXT_DESC = "The search query text to search for entities.";
        public static final String OPTIONAL_PARAMETERS_DESC = "The optional query parameters to search for entities.";
        public static final String EXCEPTION_DESC = "There was an error while trying to search for entities.";
        public static final String NEXT_LINK_DESC = "The link to the next page of results in case there are too many matches.";

    }
}
