/*
 * Copyright 2019-2024 Open Text
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

package io.cloudslang.content.vmware.services;

import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.ImportSpec;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OvfCreateImportSpecParams;
import com.vmware.vim25.OvfCreateImportSpecResult;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.AsyncProgressUpdater;
import io.cloudslang.content.vmware.entities.CustomExecutor;
import io.cloudslang.content.vmware.entities.ProgressUpdater;
import io.cloudslang.content.vmware.entities.SyncProgressUpdater;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import io.cloudslang.content.vmware.utils.OvfUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Answers.CALLS_REAL_METHODS;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeployOvfTemplateServiceTest {
    private static final String RESOURCE_POOL_NAME = "test_resourcePool";
    private static final String CLUSTER_NAME = "test_cluster";
    private static final String OVF_TEMPLATE_AS_STRING = "template content";

    @TempDir
    Path tempDir;

    @Mock private HttpInputs httpInputsMock;
    @Mock private VmInputs vmInputsMock;
    @Mock private ConnectionResources connectionResourcesMock;
    @Mock private ManagedObjectReference leaseMock;
    @Mock private ManagedObjectReference ovfManagerMock;
    @Mock private ManagedObjectReference resourcePoolMock;
    @Mock private ManagedObjectReference hostMock;
    @Mock private ManagedObjectReference datastoreMock;
    @Mock private ManagedObjectReference folderMock;
    @Mock private ManagedObjectReference clusterMock;
    @Mock private ManagedObjectReference rootFolderMock;
    @Mock private ManagedObjectReference serviceInstanceMock;
    @Mock private VimPortType vimPortMock;
    @Mock private ServiceContent serviceContentMock;
    @Mock private HttpNfcLeaseInfo leaseInfoMock;
    @Mock private ImportSpec importSpecMock;

    private MockedConstruction<ConnectionResources> connectionResourcesConstruction;
    private MockedConstruction<CustomExecutor> executorConstruction;
    private MockedConstruction<VmUtils> vmUtilsConstruction;
    private MockedConstruction<MorObjectHandler> morObjectHandlerConstruction;
    private MockedStatic<OvfUtils> ovfUtilsMock;

    @BeforeEach
    void setUp() throws Exception {
        when(httpInputsMock.isCloseSession()).thenReturn(false);
        when(vmInputsMock.getResourcePool()).thenReturn(RESOURCE_POOL_NAME);
        when(vmInputsMock.getClusterName()).thenReturn(CLUSTER_NAME);
        when(vmInputsMock.getHostname()).thenReturn("host");
        when(vmInputsMock.getDataStore()).thenReturn("datastore");
        when(vmInputsMock.getFolderName()).thenReturn("folder");

        configureConnectionResources(connectionResourcesMock);
        connectionResourcesConstruction = mockConstruction(ConnectionResources.class, (mock, context) ->
                configureConnectionResources(mock));
        executorConstruction = mockConstruction(CustomExecutor.class, (mock, context) ->
                when(mock.isParallel()).thenReturn((Boolean) context.arguments().get(0)));
        vmUtilsConstruction = mockConstruction(VmUtils.class, (mock, context) -> {
            when(mock.getMorResourcePool(eq(RESOURCE_POOL_NAME), any(ConnectionResources.class)))
                    .thenReturn(resourcePoolMock);
            when(mock.getMorResourcePoolFromCluster(any(ConnectionResources.class), eq(clusterMock),
                    eq(RESOURCE_POOL_NAME)))
                    .thenReturn(resourcePoolMock);
            when(mock.getMorHost(anyString(), any(ConnectionResources.class), nullable(ManagedObjectReference.class)))
                    .thenReturn(hostMock);
            when(mock.getMorDataStore(anyString(), any(ConnectionResources.class), nullable(ManagedObjectReference.class),
                    any(VmInputs.class))).thenReturn(datastoreMock);
            when(mock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        });
        morObjectHandlerConstruction = mockConstruction(MorObjectHandler.class, (mock, context) ->
                when(mock.getSpecificMor(any(ConnectionResources.class), any(ManagedObjectReference.class),
                        anyString(), anyString())).thenReturn(clusterMock));

        ovfUtilsMock = mockStatic(OvfUtils.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        ovfUtilsMock.when(() -> OvfUtils.getHttpNfcLease(any(ConnectionResources.class), any(ImportSpec.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(leaseMock);
        ovfUtilsMock.when(() -> OvfUtils.getHttpNfcLeaseState(any(ConnectionResources.class),
                any(ManagedObjectReference.class))).thenReturn("ready");
        ovfUtilsMock.when(() -> OvfUtils.getHttpNfcLeaseInfo(any(ConnectionResources.class),
                any(ManagedObjectReference.class))).thenReturn(leaseInfoMock);

        when(vimPortMock.retrieveServiceContent(serviceInstanceMock)).thenReturn(serviceContentMock);
        when(serviceContentMock.getOvfManager()).thenReturn(ovfManagerMock);
        when(leaseInfoMock.getDeviceUrl()).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        ovfUtilsMock.close();
        morObjectHandlerConstruction.close();
        vmUtilsConstruction.close();
        executorConstruction.close();
        connectionResourcesConstruction.close();
    }

    @Test
    void testAsyncDeployOvfTemplate() throws Exception {
        Path templatePath = createTemplateFile();
        OvfCreateImportSpecResult importSpecResult = new OvfCreateImportSpecResult();
        importSpecResult.setImportSpec(importSpecMock);
        when(vimPortMock.createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class))).thenReturn(importSpecResult);

        new DeployOvfTemplateService(true).deployOvfTemplate(httpInputsMock, vmInputsMock,
                templatePath.toString(), Collections.emptyMap(), Collections.emptyMap());

        verifyDeployInvocation(true);
    }

    @Test
    void testSyncDeployOvfTemplate() throws Exception {
        Path templatePath = createTemplateFile();
        OvfCreateImportSpecResult importSpecResult = new OvfCreateImportSpecResult();
        importSpecResult.setImportSpec(importSpecMock);
        when(vimPortMock.createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class))).thenReturn(importSpecResult);

        new DeployOvfTemplateService(false).deployOvfTemplate(httpInputsMock, vmInputsMock,
                templatePath.toString(), Collections.emptyMap(), Collections.emptyMap());

        verifyDeployInvocation(false);
    }

    @Test
    void testCreateLeaseSetupWithClusterName() throws Exception {
        Path templatePath = createTemplateFile();
        OvfCreateImportSpecResult importSpecResult = new OvfCreateImportSpecResult();
        importSpecResult.setImportSpec(importSpecMock);
        when(vimPortMock.createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class))).thenReturn(importSpecResult);
        when(vmInputsMock.getClusterName()).thenReturn(CLUSTER_NAME);

        ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result =
                new DeployOvfTemplateService(false).createLeaseSetup(connectionResourcesMock, vmInputsMock,
                        templatePath.toString(), Collections.emptyMap(), Collections.emptyMap());

        assertEquals(leaseMock, result.getLeft());
        assertEquals(importSpecResult, result.getRight());
        verify(vmUtilsConstruction.constructed().get(0)).getMorResourcePoolFromCluster(connectionResourcesMock,
                clusterMock, RESOURCE_POOL_NAME);
        verify(vimPortMock).createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class));
    }

    @Test
    void testCreateLeaseSetupWithoutClusterName() throws Exception {
        Path templatePath = createTemplateFile();
        OvfCreateImportSpecResult importSpecResult = new OvfCreateImportSpecResult();
        importSpecResult.setImportSpec(importSpecMock);
        when(vimPortMock.createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class))).thenReturn(importSpecResult);
        when(vmInputsMock.getClusterName()).thenReturn("");

        ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result =
                new DeployOvfTemplateService(false).createLeaseSetup(connectionResourcesMock, vmInputsMock,
                        templatePath.toString(), Collections.emptyMap(), Collections.emptyMap());

        assertEquals(leaseMock, result.getLeft());
        assertEquals(importSpecResult, result.getRight());
        verify(vmUtilsConstruction.constructed().get(0)).getMorResourcePool(RESOURCE_POOL_NAME, connectionResourcesMock);
        verify(vimPortMock).createImportSpec(eq(ovfManagerMock), eq(OVF_TEMPLATE_AS_STRING), eq(resourcePoolMock),
                eq(datastoreMock), any(OvfCreateImportSpecParams.class));
    }

    private void configureConnectionResources(ConnectionResources resources) throws Exception {
        when(resources.getVimPortType()).thenReturn(vimPortMock);
        when(resources.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(resources.getMorRootFolder()).thenReturn(rootFolderMock);
    }

    private Path createTemplateFile() throws Exception {
        Path templatePath = tempDir.resolve("template.ovf");
        Files.writeString(templatePath, OVF_TEMPLATE_AS_STRING, StandardCharsets.UTF_8);
        return templatePath;
    }

    private void verifyDeployInvocation(boolean parallel) throws Exception {
        ConnectionResources resources = connectionResourcesConstruction.constructed().get(0);
        verify(resources, times(2)).getVimPortType();
        verify(leaseInfoMock).getDeviceUrl();
        CustomExecutor executor = executorConstruction.constructed().get(0);
        verify(executor).isParallel();
        ArgumentCaptor<Runnable> progressUpdaterCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(executor).execute(progressUpdaterCaptor.capture());
        if (parallel) {
            assertEquals(AsyncProgressUpdater.class, progressUpdaterCaptor.getValue().getClass());
        } else {
            assertEquals(SyncProgressUpdater.class, progressUpdaterCaptor.getValue().getClass());
        }
        verify(executor).shutdown();
    }
}
