

package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.TaskImpl.getTaskDetailsURL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskImplTest {
    private static final String EXPECTED_GET_TASK_DETAILS_PATH = "https://myhost:9440/api/nutanix/v2.0/tasks/1234";

    private final NutanixGetTaskDetailsInputs nutanixGetTaskDetailsInputs = NutanixGetTaskDetailsInputs.builder()
            .taskUUID("1234")
            .includeSubtasksInfo("")
            .commonInputs(NutanixCommonInputs.builder()
                    .hostname("myhost")
                    .port("9440")
                    .username("username")
                    .password("password")
                    .apiVersion("v2.0")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .trustAllRoots("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .connectTimeout("")
                    .socketTimeout("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .build()).build();

    @Test
    public void getTaskDetailsPathTest() throws Exception {
        final String path = getTaskDetailsURL(nutanixGetTaskDetailsInputs);
        assertEquals(EXPECTED_GET_TASK_DETAILS_PATH, path);
    }
}
