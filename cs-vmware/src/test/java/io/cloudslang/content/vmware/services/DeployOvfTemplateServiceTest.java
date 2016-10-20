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
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
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
    private OvfCreateImportSpecResult importSpecResultMock;
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

    private long DISK_SIZE = 1024;
    private ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> pair;
    private String ovfTemplateAsString = "template content";

    @Before
    public void setUp() {
        pair = ImmutablePair.of(httpNfcLeaseMock, importSpecResultMock);
    }

    @After
    public void tearDown() {
        httpInputsMock = null;
        httpNfcLeaseMock = null;
        importSpecResultMock = null;
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
    public void testCreateLeaseSetup() throws Exception {
        prepareMocksForCreateLeaseSetupTest();

        ImmutablePair<ManagedObjectReference, OvfCreateImportSpecResult> result
                = serviceSpy.createLeaseSetup(connectionResourcesMock, vmInputsMock, PATH, ovfNetworkMapMock, ovfPropertyMapMock);

        Assert.assertEquals(httpNfcLeaseMock, result.getLeft());
        Assert.assertEquals(ovfCreateImportSpecResultMock, result.getRight());
        verifyNew(VmUtils.class).withNoArguments();
        verify(vmInputsMock).getClusterName();
        verify(vmUtilsMock).getMorResourcePool(TEST_RESOURCE_POOL, connectionResourcesMock);
        verify(vmUtilsMock).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(vmUtilsMock).getMorDataStore(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class), any(VmInputs.class));
        verify(vmUtilsMock).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(connectionResourcesMock).getVimPortType();
        verify(vimPortTypeMock).createImportSpec(ovfManagerMock, ovfTemplateAsString, resourcePoolMock, datastoreMock,
                ovfCreateImportSpecParamsMock);
    }

    private void prepareMocksForCreateLeaseSetupTest() throws Exception {
        PowerMockito.doReturn(ovfManagerMock).when(serviceSpy, "getOvfManager", any(ConnectionResources.class));
        whenNew(VmUtils.class).withNoArguments().thenReturn(vmUtilsMock);
        doReturn(EMPTY_STRING).when(vmInputsMock).getClusterName();
        doReturn(TEST_RESOURCE_POOL).when(vmInputsMock).getResourcePool();
        doReturn(resourcePoolMock).when(vmUtilsMock).getMorResourcePool(TEST_RESOURCE_POOL, connectionResourcesMock);
        doReturn(hostMorMock).when(vmUtilsMock).getMorHost(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class));
        doReturn(datastoreMock).when(vmUtilsMock).getMorDataStore(anyString(), any(ConnectionResources.class),
                any(ManagedObjectReference.class), any(VmInputs.class));
        doReturn(folderMock).when(vmUtilsMock).getMorFolder(anyString(), any(ConnectionResources.class));
        PowerMockito.doReturn(ovfNetworkMappingsMock).when(serviceSpy, "getOvfNetworkMappings",
                ovfNetworkMapMock, connectionResourcesMock);
        PowerMockito.doReturn(ovfPropertyMappingsMock).when(serviceSpy, "getOvfPropertyMappings", ovfPropertyMapMock);
        doReturn(vimPortTypeMock).when(connectionResourcesMock).getVimPortType();
        PowerMockito.doReturn(ovfTemplateAsString).when(serviceSpy, "getOvfTemplateAsString", PATH);
        PowerMockito.doReturn(ovfCreateImportSpecParamsMock).when(serviceSpy, "getOvfCreateImportSpecParams",
                vmInputsMock, hostMorMock, ovfNetworkMappingsMock, ovfPropertyMappingsMock);
        doReturn(ovfCreateImportSpecResultMock).when(vimPortTypeMock).createImportSpec(ovfManagerMock,
                ovfTemplateAsString, resourcePoolMock, datastoreMock, ovfCreateImportSpecParamsMock);
        PowerMockito.doNothing().when(serviceSpy, "checkImportSpecResultForErrors", importSpecResultMock);
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
            PowerMockito.doNothing().when(serviceSpy, "transferVmdkFiles", PATH, importSpecResultMock, deviceUrlsMock,
                    asyncProgressUpdaterMock);
        } else {
            doNothing().when(executorMock).execute(syncProgressUpdaterMock);
            PowerMockito.doNothing().when(serviceSpy, "transferVmdkFiles", PATH, importSpecResultMock, deviceUrlsMock,
                    syncProgressUpdaterMock);
        }
        doNothing().when(executorMock).shutdown();
        doReturn(parallel).when(executorMock).isParallel();
    }
}
