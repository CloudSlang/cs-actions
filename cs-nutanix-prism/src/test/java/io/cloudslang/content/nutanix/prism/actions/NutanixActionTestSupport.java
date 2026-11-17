package io.cloudslang.content.nutanix.prism.actions;

import org.mockito.MockedStatic;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public abstract class NutanixActionTestSupport {
    protected Map<String, String> serviceResponse(String statusCode, String body) {
        return Map.of(RETURN_RESULT, body, STATUS_CODE, statusCode);
    }

    protected <T> Map<String, String> executeWithMockedService(
            Class<T> serviceClass,
            Consumer<MockedStatic<T>> stub,
            Supplier<Map<String, String>> action,
            Consumer<MockedStatic<T>> verify) {
        try (MockedStatic<T> service = mockStatic(serviceClass)) {
            stub.accept(service);
            final Map<String, String> result = action.get();
            verify.accept(service);
            return result;
        }
    }

    protected void assertTaskSuccess(Map<String, String> result, String expectedTaskUUID) {
        assertEquals("0", result.get("returnCode"));
        assertEquals(expectedTaskUUID, result.get("taskUUID"));
    }

    protected <T> Map<String, String> executeWithoutServiceCall(
            Class<T> serviceClass,
            Supplier<Map<String, String>> action,
            Consumer<MockedStatic<T>> verify) {
        try (MockedStatic<T> service = mockStatic(serviceClass)) {
            final Map<String, String> result = action.get();
            assertEquals("-1", result.get("returnCode"));
            verify.accept(service);
            return result;
        }
    }
}
