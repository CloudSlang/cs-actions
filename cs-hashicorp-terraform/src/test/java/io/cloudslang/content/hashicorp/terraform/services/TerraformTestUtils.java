package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

final class TerraformTestUtils {
    private TerraformTestUtils() {
    }

    static <T> T executeWithMockedHttpClient(Callable<T> operation) throws Exception {
        try (MockedStatic<HttpClientService> httpClientService = mockStatic(HttpClientService.class)) {
            httpClientService.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Collections.emptyMap());
            return operation.call();
        }
    }
}
