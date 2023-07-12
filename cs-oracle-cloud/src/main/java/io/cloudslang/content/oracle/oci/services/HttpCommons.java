


package io.cloudslang.content.oracle.oci.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.oci.utils.HttpUtils;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.oracle.oci.utils.HttpUtils.setTLSParameters;

public class HttpCommons {

    @NotNull
    public static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                           @NotNull final OCICommonInputs commonInputs) {
        HttpUtils.setProxy(httpClientInputs,
                commonInputs.getProxyHost(),
                commonInputs.getProxyPort(),
                commonInputs.getProxyUsername(),
                commonInputs.getProxyPassword());

        HttpUtils.setSecurityInputs(httpClientInputs);

        HttpUtils.setConnectionParameters(httpClientInputs,
                commonInputs.getConnectTimeout(),
                commonInputs.getSocketTimeout(),
                commonInputs.getKeepAlive(),
                commonInputs.getConnectionsMaxPerRoot(),
                commonInputs.getConnectionsMaxTotal());
        setTLSParameters(httpClientInputs);
    }
}
