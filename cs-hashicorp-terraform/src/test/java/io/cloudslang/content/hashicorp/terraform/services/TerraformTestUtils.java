package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.mockito.MockedConstruction;

import java.util.Collections;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

final class TerraformTestUtils {
    private TerraformTestUtils() {
    }

    static <T> T executeWithMockedHttpClient(Callable<T> operation) throws Exception {
        try (MockedConstruction<HttpClientService> ignored = mockConstruction(HttpClientService.class,
                (mock, context) -> when(mock.execute(any(HttpClientInputs.class))).thenReturn(Collections.emptyMap()))) {
            return operation.call();
        }
    }
}
