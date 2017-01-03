/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

public interface Connection {
    VimPortType getVimPort();

    ServiceContent getServiceContent();

    ManagedObjectReference getServiceInstanceReference();

    Connection connect(String url, String username, String password, boolean trustEveryone);

    boolean isConnected();

    Connection disconnect();
}
