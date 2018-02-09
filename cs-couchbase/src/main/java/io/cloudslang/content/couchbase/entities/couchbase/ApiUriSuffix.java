/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.couchbase.entities.couchbase;

public enum ApiUriSuffix {
    ADD_NODE("/addNode"),
    AUTO_FAILOVER("/autoFailover"),
    DEFAULT("/default"),
    DO_JOIN_CLUSTER("/doJoinCluster"),
    EJECT_NODE_ENTRY("/ejectNodeentry"),
    SELF("/self"),
    WEB("/web");

    private final String value;

    ApiUriSuffix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
