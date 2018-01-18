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

package io.cloudslang.content.vmware.services.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.vmware.connection.Connection;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by victor on 06.04.2017.
 */
public class VmWareSessionResource extends SessionResource<Map<String, Connection>> {
    private Map<String, Connection> vmWareConnectionMap;

    public VmWareSessionResource(@NotNull final Map<String, Connection> vmWareConnectionMap) {
        this.vmWareConnectionMap = vmWareConnectionMap;
    }

    @Override
    @NotNull
    public Map<String, Connection> get() {
        return vmWareConnectionMap;
    }

    @Override
    public synchronized void release() {
        for (final Connection vmWareConnection : vmWareConnectionMap.values()) {
            if (vmWareConnection != null && vmWareConnection.isConnected()) {
                vmWareConnection.disconnect();
            }
        }
        vmWareConnectionMap = null;
    }
}
