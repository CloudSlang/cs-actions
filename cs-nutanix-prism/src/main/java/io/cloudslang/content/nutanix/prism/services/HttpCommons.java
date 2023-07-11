

package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.CHANGEIT;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.*;

public class HttpCommons {

    @NotNull
    static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                    @NotNull final NutanixCommonInputs commonInputs) {
        setProxy(httpClientInputs,
                commonInputs.getProxyHost(),
                commonInputs.getProxyPort(),
                commonInputs.getProxyUsername(),
                commonInputs.getProxyPassword());

        setSecurityInputs(httpClientInputs,
                commonInputs.getTrustAllRoots(),
                commonInputs.getX509HostnameVerifier(),
                commonInputs.getTrustKeystore(),
                commonInputs.getTrustPassword(),
                DEFAULT_JAVA_KEYSTORE,
                CHANGEIT);

        setConnectionParameters(httpClientInputs,
                commonInputs.getConnectTimeout(),
                commonInputs.getSocketTimeout(),
                commonInputs.getKeepAlive(),
                commonInputs.getConnectionsMaxPerRoot(),
                commonInputs.getConnectionsMaxTotal(),
                commonInputs.getPreemptiveAuth());
        setTLSParameters(httpClientInputs);
    }
}
