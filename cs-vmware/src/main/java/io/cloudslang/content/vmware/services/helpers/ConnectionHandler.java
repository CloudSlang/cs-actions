/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.services.helpers;

import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.http.HttpInputs;

import static io.cloudslang.content.vmware.utils.ConnectionUtils.clearConnectionFromContext;

public class ConnectionHandler {
    private ConnectionHandler() {
        // prevent instantiation
    }

    public static void cleanupResources(HttpInputs httpInputs, ConnectionResources connectionResources) {
        connectionResources.getConnection().disconnect();
        clearConnectionFromContext(httpInputs.getGlobalSessionObject());
    }
}
