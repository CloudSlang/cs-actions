

package io.cloudslang.content.vmware.connection;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;

import java.io.Serializable;

public interface Connection extends Serializable {
    VimPortType getVimPort();

    ServiceContent getServiceContent();

    ManagedObjectReference getServiceInstanceReference();

    Connection connect(String url, String username, String password, boolean trustEveryone);

    boolean isConnected();

    Connection disconnect();
}
