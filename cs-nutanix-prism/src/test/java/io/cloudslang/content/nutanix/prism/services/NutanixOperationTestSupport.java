package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.APPLICATION_API_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

abstract class NutanixOperationTestSupport {
    private static final ObjectMapper JSON = new ObjectMapper();
    private static final Map<String, String> HTTP_RESPONSE = Map.of("returnResult", "response");

    protected final NutanixCommonInputs commonInputs = NutanixCommonInputs.builder()
            .hostname("myhost")
            .port("9440")
            .username("username")
            .password("password")
            .apiVersion("v2.0")
            .build();

    protected HttpClientInputs invokeAndCapture(ThrowingOperation operation) throws Exception {
        final ArgumentCaptor<HttpClientInputs> requestCaptor = ArgumentCaptor.forClass(HttpClientInputs.class);
        try (MockedStatic<HttpClientService> httpClient = mockStatic(HttpClientService.class)) {
            httpClient.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(HTTP_RESPONSE);
            assertEquals(HTTP_RESPONSE, operation.execute());
            httpClient.verify(() -> HttpClientService.execute(requestCaptor.capture()), times(1));
        }
        return requestCaptor.getValue();
    }

    protected void assertRequest(HttpClientInputs request, String method, String url) {
        assertEquals(method, request.getMethod());
        assertEquals(url, request.getUrl());
        assertEquals("basic", request.getAuthType());
        assertEquals(APPLICATION_API_JSON, request.getContentType());
        assertEquals("username", request.getUsername());
        assertEquals("password", request.getPassword());
    }

    protected void assertJsonEquals(String expected, String actual) throws Exception {
        final JsonNode expectedJson = JSON.readTree(expected);
        final JsonNode actualJson = JSON.readTree(actual);
        assertEquals(expectedJson, actualJson);
    }

    @FunctionalInterface
    protected interface ThrowingOperation {
        Map<String, String> execute() throws Exception;
    }
}
