/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.sitescope.constants;

public class Descriptions {

    public static class Common {

        public static final String HOST_DESC = "The DNS name or IP Address of your SiteScope host.";
        public static final String PORT_DESC = "The port to connect to your SiteScope host on.";
        public static final String PROTOCOL_DESC = "The protocol to connect to your SiteScope host with (HTTP or HTTPS).";
        public static final String USERNAME_DESC = " The username for the SiteScope host.";
        public static final String PASSWORD_DESC = "The password for the SiteScope user.";
        public static final String FULL_PATH_TO_GROUP_DESC = "A string array specifying the full path to the group. The " +
                "path starts with the name of the first child under the SiteScope root directory and ends with the name " +
                "of the group with the elements separated by a delimiter.";
        public static final String FULL_PATH_TO_MONITOR_DESC = "A string array specifying the full path to the monitor. The " +
                "path starts with the name of the first child under the SiteScope root directory and ends with the name " +
                "of the monitor with the elements separated by the delimiter.";
        public static final String FULL_PATH_TO_TEMPLATE_DESC = "A string array specifying the full path to the template. The " +
                "path starts with the name of the first child under the SiteScope root directory and ends with the name " +
                "of the template with the elements separated by the delimiter.";
        public static final String IDENTIFIER_DESC = "Identifier to be written to the audit log.";
        public static final String DELIMITER_DESC = "The delimiter used in the path to the group.";
        public static final String PROXY_HOST_DESC = "Proxy server host used to access the Site Scope service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Site Scope service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TLS. " +
                "A certificate is trusted even if no trusted certification authority issued it." +
                "Valid values: true, false" +
                "Default: false";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String KEYSTORE_DESC = "The path to the KeyStore file. This file should contain a certificate " +
                "the client is capable of authenticate with on the Sitescope server.";
        public static final String KEYSTORE_PASSWORD_DESC = "The password associated with the KeyStore file.";
        public static final String TRUST_KEYSTORE_DESC = "The path to the Java TrustKeyStore file. This file should contain " +
                "the Sitescope server certificates.";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustKeyStore file. If trustAllRoots " +
                "is false and trustKeystore is empty, trustPassword default will be supplied.\n" +
                "Default value: changeit";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total." +
                "Default: 20";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis." +
                "Default: 2";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it." +
                "Default: false";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout." +
                "Default: 0";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: 0";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
                "default value will be used. You should not use this for method=HEAD or OPTIONS.\n" +
                "Default: UTF-8";

        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESCRIPTION = "The exception's stack trace if the operation failed. Empty otherwise.";
        public static final String STATUS_CODE_DESC = "The status code of the http call. Empty if the http call failed.";
    }

    public static class GetGroupPropertiesAction {

        public static final String GET_GROUP_PROPERTIES_DESC = "Retrieves the properties for a specified group.";
        public static final String GET_GROUP_PROP_RETURN_RESULT_DESC = "The specified group properties were successfully retrieved.";
        public static final String SUCCESS_DESC = "The specified group properties were successfully retrieved.";
        public static final String FAILURE_DESC = "The specified group properties could not be retrieved.";

    }

    public static class DeleteMonitorGroupAction {

        public static final String DELETE_MONITOR_GROUP_DESC = "Deletes a SiteScope monitor group.";
        public static final String EXTERNAL_ID_DESC = "External ID of the group.";
        public static final String RETURN_RESULT_DESC = "A message describing the success or failure of the operation.";
        public static final String SUCCESS_DESC = "The specified monitor group was successfully deleted.";
        public static final String FAILURE_DESC = "The specified monitor group could not be deleted.";
    }

    public static class ChangeMonitorGroupStatusAction {

        public static final String CHANGE_MONITOR_GROUP_STATUS_DESC = "Enables or disables a group whether it was disabled indefinitely " +
                "or for a specified time period. Enabling a group that is already enabled has no effect.";
        public static final String STATUS_DESC = "Group is enabled if set to true and group is disabled if set to false" +
                " or if the string is empty.";
        public static final String TIME_PERIOD_DESC = "The duration (in seconds) for which the group should be disabled. " +
                "If set to 0, group is disabled until explicitly enabled. Applicable only for disabling a group.";
        public static final String FROM_TIME_DESC = "The time difference in milliseconds from the [current time] and the " +
                "required [start time]. For example, if the current time is 15:00:00 and the required start time is 15:10:00," +
                " the value that should be sent is [15:10:00] - [15:00:00] = 10*60*1000 (600000 milliseconds). Applicable " +
                "only for disabling a group.";
        public static final String TO_TIME_DESC = "The time difference in milliseconds from the [current time] and the" +
                " required [end time]. For example, if the current time is 15:00:00 and the required end time is 15:30:00, " +
                "the value that should be sent is [15:30:00] - [15:00:00] = 30*60*1000 (1800000 milliseconds). Applicable " +
                "only for disabling a group.";
        public static final String DESCRIPTION_DESC = "Description to be associated with enable/disable operation.";
        public static final String IDENTIFIER_ENABLE_DESC = "Identifier to be associated with enable/disable operation and written" +
                "to audit log. Identifier to be associated with enable/disable operation and written to audit log.";
        public static final String CHANGE_MONITOR_GROUP_STATUS_RETURN_RESULT_DESC = "The operation completed successfully.";
    }

    public static class ChangeMonitorStatusAction{

        public static final String CHANGE_MONITOR_STATUS_DESC = "Enables or disables a monitor whether it was disabled " +
                "indefinitely or for a specified time period. Enabling a monitor that is already enabled has no effect.";
        public static final String MONITOR_ID_DESC = "A string specifying the monitor ID.";
    }

    public static class DeployTemplateAction{
        public static final String DEPLOY_TEMPLATE_DESC = "This operation deploys a SiteScope template to a destination path.";
        public static final String PATH_TO_TEMPLATE_DESC = "A string array specifying the full path to the template name to deploy." +
                " The path starts with the name of the first child under the SiteScope root directory and ends with the name of the template." +
                " The elements of the path are separated by the delimiter";
        public static final String PATH_TO_TARGET_GROUP_DESC = "A string array specifying the full path to the group where the template is to be deployed." +
                " If the last element in the path does not exist, the function creates it and deploys the template under the new path element." +
                " The elements of the path are separated by the delimiter.";
        public static final String CONNECT_TO_SERVER_DESC = "Set it to \"true\" to verify monitor measurements against the remote server during deployment. " +
                "If set to \"false\" monitor measurements are not verified against the remote server.";
        public static final String TEST_REMOTES_DESC = "Set it \"true\" to run the test on deployed remote server.";
        public static final String CUSTOM_PARAMETERS_DESC = "All other parameters required for the deployment as specified by the template being deployed.";
        public static final String SUCCESS_DESC = "The specified template was successfully deployed.";
        public static final String FAILURE_DESC = "The specified template could not be deployed.";
    }

    public static class DeleteRemoteServer{
        public static final String DELETE_REMOTE_SERVER_DESC = "This operation deletes a SiteScope remote server.";
        public static final String PLATFORM_DESC = "The type of the platform. Specify \"Windows\" for Windows remote servers or \"UNIX\" for Unix remote servers. Default value: Windows";
        public static final String REMOTE_NAME_DESC = "Name of the remote server to be deleted.";
        public static final String DELETE_REMOTE_SERVER_RETURN_RESULT_DESC = "The specified remote server was successfully deleted.";
        public static final String SUCCESS_DESC = "The specified remote server was successfully deleted.";
        public static final String FAILURE_DESC = "The specified remote server could not be deleted.";
    }

    public static class UpdateTemplateAction {
        public static final String UPDATE_TEMPLATE_DESC = "This operation updates the variables of a previously deployed " +
                "SiteScope template";
        public static final String PROPERTIES_DESC = "Contains the properties to be updated.";
    }

    public static class DeleteMonitorAction {

        public static final String DELETE_MONITOR_DESC = "Deletes a SiteScope monitor.";
        public static final String RETURN_RESULT_DESC = "A message describing the success or failure of the operation.";
        public static final String SUCCESS_DESC = "The specified monitor was successfully deleted.";
        public static final String FAILURE_DESC = "The specified monitor could not be deleted.";
    }

    public static class RunMonitorAction {

        public static final String RUN_MONITOR_DESC = "Runs a SiteScope monitor. The monitor must be deployed before invoking this method.";
        public static final String RETURN_RESULT_DESC = "A HashMap representation of the status of the run and the status message as it would appear on the UI.";
        public static final String EXECUTION_TIMEOUT_DESC = " Timeout value in milliseconds.";
        public static final String SUCCESS_DESC = "The specified monitor was run successfully.";
        public static final String FAILURE_DESC = "The specified monitor could not be run.";
    }

    public static class GetMonitorsDeployedAt{
        public static final String GET_MONITORS_DEPLOYED_AT_DESC = "Returns a list with the monitors that have been deployed on the given server.";
        public static final String SUCCESS_DESC = "Monitors were successfully retrieved.";
        public static final String FAILURE_DESC = "The monitors could not be retrieved.";
        public static final String MULTIPLE_SERVERS_DESC = "The operation executed successfully but multiple servers were found.";
        public static final String TARGET_SERVER_DESC = "The target server for which to get the deployed monitors." +
                " The value can be an IP address, host name, FQDN or a string that represents the name of the remote server as it is displayed in the SiteScope web UI. " +
                "The value must match exactly the name of the remote server. " +
                "If the input contains any backslashes (\"\\\"), each of them need to be properly escaped by another backslash (\"\\\\\").";
        public static final String COL_DELIMITER_DESC ="The column delimiter for the monitor results.";
        public static final String ROW_DELIMITER_DESC = "The row delimiter for the monitor results.";

    }
}