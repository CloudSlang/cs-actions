/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

import com.vmware.vim25.HttpNfcLeaseDeviceUrl;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.ImportSpec;
import com.vmware.vim25.KeyValue;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OvfCreateImportSpecParams;
import com.vmware.vim25.OvfCreateImportSpecResult;
import com.vmware.vim25.OvfNetworkMapping;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.AsyncProgressUpdater;
import io.cloudslang.content.vmware.entities.CustomExecutor;
import io.cloudslang.content.vmware.entities.SyncProgressUpdater;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import io.cloudslang.content.vmware.utils.OvfUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DeployOvfTemplateService.class, OvfUtils.class})
public class DeployOvfTemplateServiceTest {

    private static final String PATH = "template\\path";
    private static final String TEST_CLUSTER = "test_cluster";
    private static final String TEST_RESOURCE_POOL = "test_resourcePool";
    private static final String EMPTY_STRING = "";
    private static final long DISK_SIZE = 1024;
    private ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> pair;
    private static final String OVF_TEMPLATE_AS_STRING = "template content";

    @Spy
    private DeployOvfTemplateService serviceSpy = new DeployOvfTemplateService(true);
    @Mock
    private HttpInputs httpInputsMock;
    @Mock
    private VmInputs vmInputsMock;
    @Mock
    private ConnectionResources connectionResourcesMock;
    @Mock
    private Map<String, String> ovfNetworkMapMock;
    @Mock
    private Map<String, String> ovfPropertyMapMock;
    @Mock
    private ManagedObjectReference httpNfcLeaseMock;
    @Mock
    private HttpNfcLeaseInfo httpNfcLeaseInfoMock;
    @Mock
    private List<HttpNfcLeaseDeviceUrl> deviceUrlsMock;
    @Mock
    private CustomExecutor executorMock;
    @Mock
    private AsyncProgressUpdater asyncProgressUpdaterMock;
    @Mock
    private SyncProgressUpdater syncProgressUpdaterMock;
    @Mock
    private ManagedObjectReference ovfManagerMock;
    @Mock
    private ManagedObjectReference resourcePoolMock;
    @Mock
    private ManagedObjectReference hostMorMock;
    @Mock
    private ManagedObjectReference datastoreMock;
    @Mock
    private ManagedObjectReference folderMock;
    @Mock
    private VmUtils vmUtilsMock;
    @Mock
    private List<OvfNetworkMapping> ovfNetworkMappingsMock;
    @Mock
    private List<KeyValue> ovfPropertyMappingsMock;
    @Mock
    private VimPortType vimPortTypeMock;
    @Mock
    private OvfCreateImportSpecResult ovfCreateImportSpecResultMock;
    @Mock
    private OvfCreateImportSpecParams ovfCreateImportSpecParamsMock;
    @Mock
    private ImportSpec importSpecMock;
    @Mock
    private ManagedObjectReference clusterMorMock;
    @Mock
    private MorObjectHandler morObjectHandlerMock;
    @Mock
    private ManagedObjectReference morRootFolderMock;

    @Before
    public void setUp() {
        pair = ImmutablePair.of(httpNfcLeaseMock, ovfCreateImportSpecResultMock);
    }

    @After
    public void tearDown() {
        httpInputsMock = null;
        httpNfcLeaseMock = null;
        httpNfcLeaseInfoMock = null;
        deviceUrlsMock = null;
        vmInputsMock = null;
        connectionResourcesMock = null;
        ovfNetworkMapMock = null;
        ovfPropertyMapMock = null;
        executorMock = null;
        pair = null;
        asyncProgressUpdaterMock = null;
        syncProgressUpdaterMock = null;
        ovfManagerMock = null;
        resourcePoolMock = null;
        hostMorMock = null;
        datastoreMock = null;
        folderMock = null;
        vmUtilsMock = null;
        ovfNetworkMappingsMock = null;
        ovfPropertyMappingsMock = null;
        vimPortTypeMock = null;
        ovfCreateImportSpecResultMock = null;
        ovfCreateImportSpecParamsMock = null;
        importSpecMock = null;
        clusterMorMock = null;
        morObjectHandlerMock = null;
        morRootFolderMock = null;
    }

    @Test
    public void testAsyncDeployOvfTemplate() throws Exception {
        prepareMocksForDeployOvfTemplate(true);

        serviceSpy.deployOvfTemplate(httpInputsMock, vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);

        verifyMockInvocationsForDeployOvfTemplate(true);
    }

    @Test
    public void testSyncDeployOvfTemplate() throws Exception {
        prepareMocksForDeployOvfTemplate(false);

        serviceSpy.deployOvfTemplate(httpInputsMock, vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);

        verifyMockInvocationsForDeployOvfTemplate(false);
    }

    @Test
    public void testCreateLeaseSetupWithClusterName() throws Exception {
        prepareMocksForCreateLeaseSetupTest(true);

        ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result
                = serviceSpy.createLeaseSetup(connectionResourcesMock, vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);

        verifyMockInvocationsForCreateLeaseSetupTest(result, true);
    }

    @Test
    public void testCreateLeaseSetupWithoutClusterName() throws Exception {
        prepareMocksForCreateLeaseSetupTest(false);

        ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result
                = serviceSpy.createLeaseSetup(connectionResourcesMock, vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);

        verifyMockInvocationsForCreateLeaseSetupTest(result, false);
    }

    private void verifyMockInvocationsForCreateLeaseSetupTest(ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result,
                                                              boolean withClustername) throws Exception {
        Assert.assertEquals(httpNfcLeaseMock, result.getLeft());
        Assert.assertEquals(ovfCreateImportSpecResultMock, result.getRight());
        verifyNew(VmUtils.class).withNoArguments();
        if (withClustername) {
            verify(vmInputsMock, times(2)).getClusterName();
            verify(vmUtilsMock, times(0)).getMorResourcePool(TEST_RESOURCE_POOL, connectionResourcesMock);
            verifyNew(MorObjectHandler.class).withNoArguments();
            verify(morObjectHandlerMock).getSpecificMor(connectionResourcesMock, morRootFolderMock, "ClusterComputeResource",
                    TEST_CLUSTER);
            verify(vmUtilsMock).getMorResourcePoolFromCluster(connectionResourcesMock, clusterMorMock, TEST_RESOURCE_POOL);
        } else {
            verify(vmInputsMock).getClusterName();
            verify(vmUtilsMock).getMorResourcePool(TEST_RESOURCE_POOL, connectionResourcesMock);
            verify(vmUtilsMock, times(0)).getMorResourcePoolFromCluster(connectionResourcesMock, clusterMorMock, TEST_RESOURCE_POOL);
        }
        verify(vmUtilsMock).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(vmUtilsMock).getMorDataStore(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class), any(VmInputs.class));
        verify(vmUtilsMock).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(connectionResourcesMock).getVimPortType();
        verify(vimPortTypeMock).createImportSpec(ovfManagerMock, OVF_TEMPLATE_AS_STRING, resourcePoolMock, datastoreMock,
                ovfCreateImportSpecParamsMock);
    }

    private void prepareMocksForCreateLeaseSetupTest(boolean withClustername) throws Exception {
        PowerMockito.doReturn(ovfManagerMock).when(serviceSpy, "getOvfManager", any(ConnectionResources.class));
        whenNew(VmUtils.class).withNoArguments().thenReturn(vmUtilsMock);
        doReturn(TEST_RESOURCE_POOL).when(vmInputsMock).getResourcePool();
        if (withClustername) {
            doReturn(TEST_CLUSTER).when(vmInputsMock).getClusterName();
            whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
            doReturn(morRootFolderMock).when(connectionResourcesMock).getMorRootFolder();
            doReturn(clusterMorMock).when(morObjectHandlerMock).getSpecificMor(connectionResourcesMock, morRootFolderMock,
                    "ClusterComputeResource", TEST_CLUSTER);
            doReturn(resourcePoolMock).when(vmUtilsMock).getMorResourcePoolFromCluster(connectionResourcesMock,
                    clusterMorMock, TEST_RESOURCE_POOL);
        } else {
            doReturn(EMPTY_STRING).when(vmInputsMock).getClusterName();
            doReturn(resourcePoolMock).when(vmUtilsMock).getMorResourcePool(TEST_RESOURCE_POOL, connectionResourcesMock);
        }
        doReturn(hostMorMock).when(vmUtilsMock).getMorHost(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class));
        doReturn(datastoreMock).when(vmUtilsMock).getMorDataStore(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class), any(VmInputs.class));
        doReturn(folderMock).when(vmUtilsMock).getMorFolder(anyString(), any(ConnectionResources.class));
        PowerMockito.doReturn(ovfNetworkMappingsMock).when(serviceSpy, "getOvfNetworkMappings",
                ovfNetworkMapMock, connectionResourcesMock);
        PowerMockito.doReturn(ovfPropertyMappingsMock).when(serviceSpy, "getOvfPropertyMappings", ovfPropertyMapMock);
        doReturn(vimPortTypeMock).when(connectionResourcesMock).getVimPortType();
        PowerMockito.doReturn(OVF_TEMPLATE_AS_STRING).when(serviceSpy, "getOvfTemplateAsString", PATH);
        PowerMockito.doReturn(ovfCreateImportSpecParamsMock).when(serviceSpy, "getOvfCreateImportSpecParams",
                vmInputsMock, hostMorMock, ovfNetworkMappingsMock, ovfPropertyMappingsMock);
        doReturn(ovfCreateImportSpecResultMock).when(vimPortTypeMock).createImportSpec(ovfManagerMock,
                OVF_TEMPLATE_AS_STRING, resourcePoolMock, datastoreMock, ovfCreateImportSpecParamsMock);

        PowerMockito.doNothing().when(serviceSpy, "checkImportSpecResultForErrors", ovfCreateImportSpecResultMock);
        PowerMockito.mockStatic(OvfUtils.class);
        PowerMockito.doReturn(httpNfcLeaseMock).when(OvfUtils.class);
        OvfUtils.getHttpNfcLease(any(ConnectionResources.class), any(ImportSpec.class), any(ManagedObjectReference.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class));
    }


    private void verifyMockInvocationsForDeployOvfTemplate(boolean parallel) throws Exception {
        verifyNew(ConnectionResources.class).withArguments(httpInputsMock, vmInputsMock);
        verify(httpNfcLeaseInfoMock).getDeviceUrl();
        verify(executorMock).isParallel();
        if (parallel) {
            verify(executorMock).execute(asyncProgressUpdaterMock);
        } else {
            verify(executorMock).execute(syncProgressUpdaterMock);
        }
        verify(executorMock).shutdown();
    }

    private void prepareMocksForDeployOvfTemplate(boolean parallel) throws Exception {
        whenNew(ConnectionResources.class)
                .withArguments(httpInputsMock, vmInputsMock)
                .thenReturn(connectionResourcesMock);
        PowerMockito.doReturn(pair).when(serviceSpy, "createLeaseSetup", connectionResourcesMock,
                vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);
        PowerMockito.doReturn(httpNfcLeaseInfoMock).when(serviceSpy, "getHttpNfcLeaseInfoWhenReady",
                any(ConnectionResources.class), any(ManagedObjectReference.class));
        doReturn(deviceUrlsMock).when(httpNfcLeaseInfoMock).getDeviceUrl();
        Whitebox.setInternalState(serviceSpy, "executor", executorMock);
        PowerMockito.doReturn(DISK_SIZE).when(serviceSpy, "getDisksTotalNoBytes", any(OvfCreateImportSpecResult.class));
        whenNew(AsyncProgressUpdater.class).withArguments(DISK_SIZE, httpNfcLeaseMock, connectionResourcesMock)
                .thenReturn(asyncProgressUpdaterMock);
        whenNew(SyncProgressUpdater.class).withArguments(DISK_SIZE, httpNfcLeaseMock, connectionResourcesMock)
                .thenReturn(syncProgressUpdaterMock);
        if (parallel) {
            doNothing().when(executorMock).execute(asyncProgressUpdaterMock);
            PowerMockito.doNothing().when(serviceSpy, "transferVmdkFiles", PATH, ovfCreateImportSpecResultMock, deviceUrlsMock,
                    asyncProgressUpdaterMock);
        } else {
            doNothing().when(executorMock).execute(syncProgressUpdaterMock);
            PowerMockito.doNothing().when(serviceSpy, "transferVmdkFiles", PATH, ovfCreateImportSpecResultMock, deviceUrlsMock,
                    syncProgressUpdaterMock);
        }
        doNothing().when(executorMock).shutdown();
        doReturn(parallel).when(executorMock).isParallel();
    }
}
