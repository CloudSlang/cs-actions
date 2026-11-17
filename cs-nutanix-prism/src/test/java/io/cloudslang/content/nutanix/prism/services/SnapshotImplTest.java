/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateSnapshotInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteSnapshotInputs;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.services.SnapshotImpl.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class SnapshotImplTest {
    private static final String EXPECTED_CREATE_SNAPSHOT_PATH =
            "https://myhost:9440/api/nutanix/v2.0/snapshots";
    private static final String EXPECTED_DELETE_SNAPSHOT_PATH =
            "https://myhost:9440/api/nutanix/v2.0/snapshots/snapshot-uuid";
    private static final String EXPECTED_CREATE_SNAPSHOT_REQUEST_BODY =
            "{\"snapshot_specs\":[{\"snapshot_name\":\"before-maintenance\",\"uuid\":\"snapshot-uuid\"," +
                    "\"vm_logical_timestamp\":42,\"vm_uuid\":\"1234\"}]}";

    private final NutanixCommonInputs commonInputs = NutanixCommonInputs.builder()
            .hostname("myhost")
            .port("9440")
            .username("username")
            .password("password")
            .apiVersion("v2.0")
            .build();

    @Test
    public void createSnapshotPathTest() throws Exception {
        final String path = createSnapshotURL(NutanixCreateSnapshotInputs.builder()
                .commonInputs(commonInputs)
                .build());
        assertEquals(EXPECTED_CREATE_SNAPSHOT_PATH, path);
    }

    @Test
    public void deleteSnapshotPathTest() throws Exception {
        final String path = deleteSnapshotURL(NutanixDeleteSnapshotInputs.builder()
                .snapshotUUID("snapshot-uuid")
                .commonInputs(commonInputs)
                .build());
        assertEquals(EXPECTED_DELETE_SNAPSHOT_PATH, path);
    }

    @Test
    public void snapshotPathsUseConfiguredApiVersion() throws Exception {
        final NutanixCommonInputs customVersionInputs = NutanixCommonInputs.builder()
                .hostname("myhost")
                .port("9440")
                .apiVersion("v3.0")
                .build();
        final String createPath = createSnapshotURL(NutanixCreateSnapshotInputs.builder()
                .commonInputs(customVersionInputs)
                .build());
        final String deletePath = deleteSnapshotURL(NutanixDeleteSnapshotInputs.builder()
                .snapshotUUID("snapshot-uuid")
                .commonInputs(customVersionInputs)
                .build());

        assertEquals("https://myhost:9440/api/nutanix/v3.0/snapshots", createPath);
        assertEquals("https://myhost:9440/api/nutanix/v3.0/snapshots/snapshot-uuid", deletePath);
    }

    @Test
    public void createSnapshotRequestBodyTest() throws Exception {
        final String body = createSnapshotBody(NutanixCreateSnapshotInputs.builder()
                .vmUUID("1234")
                .snapshotName("before-maintenance")
                .snapshotUUID("snapshot-uuid")
                .vmLogicalTimestamp("42")
                .commonInputs(commonInputs)
                .build());
        assertEquals(EXPECTED_CREATE_SNAPSHOT_REQUEST_BODY, body);
    }

    @Test
    public void createSnapshotRequestBodyOmitsOptionalFieldsTest() throws Exception {
        final String body = createSnapshotBody(NutanixCreateSnapshotInputs.builder()
                .vmUUID("1234")
                .snapshotName("before-maintenance")
                .commonInputs(commonInputs)
                .build());
        assertEquals("{\"snapshot_specs\":[{\"snapshot_name\":\"before-maintenance\",\"vm_uuid\":\"1234\"}]}", body);
    }

    @Test
    public void createSnapshotRequestBodyRejectsNonNumericVMLogicalTimestamp() {
        final NutanixCreateSnapshotInputs inputs = NutanixCreateSnapshotInputs.builder()
                .vmUUID("1234")
                .snapshotName("before-maintenance")
                .vmLogicalTimestamp("not-a-number")
                .commonInputs(commonInputs)
                .build();

        assertThrows(NumberFormatException.class, () -> createSnapshotBody(inputs));
    }

    @Test
    public void deleteSnapshotQueryParamsTest() {
        assertEquals("logical_timestamp=88", getDeleteSnapshotQueryParams("88"));
    }

    @Test
    public void createSnapshotSendsPostRequestWithJsonBody() throws Exception {
        final NutanixCreateSnapshotInputs inputs = NutanixCreateSnapshotInputs.builder()
                .vmUUID("1234")
                .snapshotName("before-maintenance")
                .commonInputs(commonInputs)
                .build();
        final ArgumentCaptor<HttpClientInputs> requestCaptor = ArgumentCaptor.forClass(HttpClientInputs.class);

        try (MockedStatic<HttpClientService> httpClient = org.mockito.Mockito.mockStatic(HttpClientService.class)) {
            httpClient.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of("returnResult", "created"));

            assertEquals(Map.of("returnResult", "created"), createSnapshot(inputs));

            httpClient.verify(() -> HttpClientService.execute(requestCaptor.capture()), times(1));
        }

        final HttpClientInputs request = requestCaptor.getValue();
        assertEquals(EXPECTED_CREATE_SNAPSHOT_PATH, request.getUrl());
        assertEquals(POST, request.getMethod());
        assertEquals(BASIC, request.getAuthType());
        assertEquals("username", request.getUsername());
        assertEquals("password", request.getPassword());
        assertEquals(APPLICATION_API_JSON, request.getContentType());
        assertEquals("{\"snapshot_specs\":[{\"snapshot_name\":\"before-maintenance\",\"vm_uuid\":\"1234\"}]}",
                request.getBody());
    }

    @Test
    public void deleteSnapshotSendsDeleteRequestWithoutOptionalTimestamp() throws Exception {
        final NutanixDeleteSnapshotInputs inputs = NutanixDeleteSnapshotInputs.builder()
                .snapshotUUID("snapshot-uuid")
                .commonInputs(commonInputs)
                .build();
        final ArgumentCaptor<HttpClientInputs> requestCaptor = ArgumentCaptor.forClass(HttpClientInputs.class);

        try (MockedStatic<HttpClientService> httpClient = org.mockito.Mockito.mockStatic(HttpClientService.class)) {
            httpClient.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of("returnResult", "deleted"));

            assertEquals(Map.of("returnResult", "deleted"), deleteSnapshot(inputs));

            httpClient.verify(() -> HttpClientService.execute(requestCaptor.capture()), times(1));
        }

        final HttpClientInputs request = requestCaptor.getValue();
        assertEquals(EXPECTED_DELETE_SNAPSHOT_PATH, request.getUrl());
        assertEquals(DELETE, request.getMethod());
        assertEquals(BASIC, request.getAuthType());
        assertEquals("username", request.getUsername());
        assertEquals("password", request.getPassword());
        assertEquals(APPLICATION_API_JSON, request.getContentType());
        assertEquals("", request.getQueryParams());
    }

    @Test
    public void deleteSnapshotSendsLogicalTimestampQueryParameterWhenProvided() throws Exception {
        final NutanixDeleteSnapshotInputs inputs = NutanixDeleteSnapshotInputs.builder()
                .snapshotUUID("snapshot-uuid")
                .logicalTimestamp("88")
                .commonInputs(commonInputs)
                .build();
        final ArgumentCaptor<HttpClientInputs> requestCaptor = ArgumentCaptor.forClass(HttpClientInputs.class);

        try (MockedStatic<HttpClientService> httpClient = org.mockito.Mockito.mockStatic(HttpClientService.class)) {
            httpClient.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of("returnResult", "deleted"));

            assertEquals(Map.of("returnResult", "deleted"), deleteSnapshot(inputs));

            httpClient.verify(() -> HttpClientService.execute(requestCaptor.capture()), times(1));
        }

        assertEquals("logical_timestamp=88", requestCaptor.getValue().getQueryParams());
    }
}
