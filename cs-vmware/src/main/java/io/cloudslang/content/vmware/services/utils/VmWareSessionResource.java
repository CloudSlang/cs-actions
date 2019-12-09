

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
