package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMsInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.listVMs;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.ListVMOutputs.VM_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class ListVMsTest extends NutanixActionTestSupport {
    @Test
    void extractsVMNamesFromSuccessfulResponse() {
        final Map<String, String> result = execute("200", "{\"entities\":[{\"name\":\"vm-a\"}," +
                "{\"name\":\"vm-b\"}]}");

        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("vm-a,vm-b", result.get(VM_LIST));
    }

    @Test
    void mapsAPIErrorToActionFailure() {
        final Map<String, String> result = execute("401", "{\"message\":\"unauthorized\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("401", result.get(STATUS_CODE));
    }

    @Test
    void rejectsInvalidCommonInputsBeforeCallingService() {
        final Map<String, String> result = executeWithoutServiceCall(VMImpl.class,
                () -> new ListVMs().execute(
                        "prism-host", null, "username", "password",
                        null, null, null, null, null, null, null, null,
                        null, "invalid", null, null, null, null, null, null,
                        null, null, null, null, null),
                service -> service.verify(() -> listVMs(any(NutanixListVMsInputs.class)), never()));

        assertEquals("-1", result.get(RETURN_CODE));
        org.junit.jupiter.api.Assertions.assertTrue(result.get(EXCEPTION).contains("proxyPort"));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> listVMs(any(NutanixListVMsInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new ListVMs().execute(
                        "prism-host", null, "username", "password",
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null),
                service -> service.verify(() -> listVMs(any(NutanixListVMsInputs.class)), times(1)));
    }
}
