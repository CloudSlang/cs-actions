package io.cloudslang.content.connection;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

public interface Connection {
    VimPortType getVimPort();

    ServiceContent getServiceContent();

    ManagedObjectReference getServiceInstanceReference();

    Connection connect(String url, String username, String password, boolean x509HostnameVerifier);

    boolean isConnected();

    Connection disconnect();
}