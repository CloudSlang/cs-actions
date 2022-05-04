package io.cloudslang.content.google.actions.authentication;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;

public class GoogleComputeImpl {
    @NotNull
    public static Map<String, String> listinstance(@NotNull final GoogleCreateVMInputs googleCreateVMInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl("https://compute.googleapis.com/compute/v1/projects/itom-gcpcontent-nonprod/zones/instances");
        httpClientInputs.setMethod("GET");
        return new HttpClientService().execute(httpClientInputs);

    }
}
