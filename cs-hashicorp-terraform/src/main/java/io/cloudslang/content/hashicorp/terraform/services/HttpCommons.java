


package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.jetbrains.annotations.NotNull;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.setConnectionParameters;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.setProxy;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.setSecurityInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.setTLSParameters;

public class HttpCommons {

    @NotNull
    static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                    @NotNull final TerraformCommonInputs commonInputs) {
        setProxy(httpClientInputs,
                commonInputs.getProxyHost(),
                commonInputs.getProxyPort(),
                commonInputs.getProxyUsername(),
                commonInputs.getProxyPassword());

        setSecurityInputs(httpClientInputs,
                commonInputs.getTrustAllRoots(),
                commonInputs.getX509HostnameVerifier(),
                commonInputs.getTrustKeystore(),
                commonInputs.getTrustPassword());

        setConnectionParameters(httpClientInputs,
                commonInputs.getConnectTimeout(),
                commonInputs.getSocketTimeout(),
                commonInputs.getKeepAlive(),
                commonInputs.getConnectionsMaxPerRoot(),
                commonInputs.getConnectionsMaxTotal());
        setTLSParameters(httpClientInputs);
    }
}
