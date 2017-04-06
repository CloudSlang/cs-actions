/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
    public void release() {
        for (final Connection vmWareConnection : vmWareConnectionMap.values()) {
            vmWareConnection.disconnect();
        }
        vmWareConnectionMap = null;
    }
}
