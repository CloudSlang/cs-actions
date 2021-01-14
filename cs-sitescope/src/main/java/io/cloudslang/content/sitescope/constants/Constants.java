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
package io.cloudslang.content.sitescope.constants;

public final class Constants {

    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String CHANGEIT = "changeit";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_PLATFORM = "Windows";
    public static final String ZERO = "0";
    public static final String UTF8 = "UTF-8";
    public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
    public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String DEFAULT_JAVA_TRUST_KEYSTORE = DEFAULT_JAVA_KEYSTORE;
    public static final String DEFAULT_DELIMITER = "/";
    public static final String COMMA =",";
    public static final String COLON = ":";
    public static final String NEW_LINE = "\n";
    public static final String SITE_SCOPE_DELIMITER = "_sis_path_delimiter_";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String X_WWW_FORM = "application/x-www-form-urlencoded";

    public static final String SITESCOPE_MONITORS_API = "/SiteScope/api/monitors";
    public static final String SITESCOPE_TEMPLATES_API = "/SiteScope/api/templates";
    public static final String SITESCOPE_ADMIN_API = "/SiteScope/api/admin";
    public static final String GET_GROUP_PROPERTIES_ENDPOINT = "/group/properties";
    public static final String ENABLE_MONITOR_GROUP_ENDPOINT = "/group/status";
    public static final String ENABLE_MONITOR_ENDPOINT = "/monitor/status";
    public static final String DELETE_MONITOR_GROUP_ENDPOINT = "/group";
    public static final String DEPLOY_TEMPLATE_ENDPOINT = "/templateDeployment";
    public static final String DELETE_REMOTE_SERVER_ENDPOINT = "/remote";
    public static final String REDEPLOY_TEMPLATE_ENDPOINT = "/template";
    public static final String GET_FULL_CONFIGURATION_SNAPSHOT = "/config/snapshot";

    public static class GetMonitorsDeployedAt{
        public static final String PREFERENCE_SNAPSHOT = "snapshot_preferenceSnapShot";
        public static final String ENTITY_PROPERTIES = "entitySnapshot_properties";
        public static final String MONITOR_CHILDREN = "snapshot_monitorSnapshotChildren";
        public static final String GROUP_CHILDREN = "snapshot_groupSnapshotChildren";
        public static final String NAME = "_name";
        public static final String REMOTE_ID = "_remoteID";
        public static final String ENABLED = "_enabled";
        public static final String DISABLED = "disabled";
        public static final String HOST = "_host";
        public static final String ID = "_id";
        public static final String OS = "_os";
        public static final String SNAPSHOT_REMOTE = "snapshot_remote";
        public static final String SNAPSHOT_CHILDREN = "SnapshotChildren";
        public static final String REMOTE = "Remote";
        public static final String INSTANCE_PREFERENCES = "InstancePreferences_";
    }

}
