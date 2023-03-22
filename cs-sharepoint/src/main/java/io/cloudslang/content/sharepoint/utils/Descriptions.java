/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
    public static class GetRootSite{
        public static final String RETURN_RESULT_DESC = "The root site for Office 365.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while getting the root site.";

        public static final String SUCCESS_DESC = "Root site retrieved successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to get the root site.";
        public static final String AUTH_TOKEN_DESC = "The auth token for login.";
        public static final String SITE_ID_DESC = "ID of the requested site.";
        public static final String SITE_NAME_DESC = "Name of the site.";
        public static final String SITE_DISPLAY_NAME_DESC = "The display name of the site.";
        public static final String WEB_URL_DESC = "URL of the site.";
    }
    public static class GetAllSites{
        public static final String SITE_IDS_DESC = "An array of pairs: displayName and id.";
        public static final String SITE_URLS_DESC = "An array of pairs: displayName and url.";
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
}
