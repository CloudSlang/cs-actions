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

package io.cloudslang.content.hashicorp.terraform.utils;

public class Inputs {
    public static final String AUTH_TOKEN = "authToken";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";

    public static final String PAGE_NUMBER = "page[number]";
    public static final String PAGE_SIZE = "page[size]";

    public static class GetWorkspaceDetails {
        public static final String WORKSPACE_ID = "workspaceId";
    }

    public static class CreateWorkspace {
        public static final String WORKSPACE_NAME = "workspaceName";
        public static final String VCS_REPO_ID = "vcsRepoId";
    }
}
