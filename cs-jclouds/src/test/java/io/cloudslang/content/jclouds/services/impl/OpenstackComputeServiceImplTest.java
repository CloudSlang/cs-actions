package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import org.jclouds.ContextBuilder;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 6/23/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OpenstackComputeServiceImpl.class, ContextBuilder.class})
public class OpenstackComputeServiceImplTest {
    private static final String REGION = "RegionOne";
    private static final String ENDPOINT = "http://11.11.11.11:5000/v2.0";
    private static final String IDENTITY = "anyAlias:actulUsername";
    private static final String PASSWORD = "secretpasswrord";
    private static final String NULL_PROXY_HOST = null;
    private static final String NULL_PROXY_PORT = null;
    private static final String PROXY_HOST = "some.host";
    private static final String PROXY_PORT = "8888";
    private static final String OPENSTACK_PROVIDER = "openstack-nova";
    private static final String PROPERTY_PROXY_HOST = "jclouds.proxy-host";
    private static final String PROPERTY_PROXY_PORT = "jclouds.proxy-port";
    private static final String PROPERTY_REGIONS = "jclouds.regions";

    private OpenstackComputeServiceImpl toTest;

    @Mock
    private SLF4JLoggingModule loggingModuleMock;

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private NovaApi novaApiMock;

    @Mock
    private ServerApi serverApiMock;

    @Mock
    private Optional<ServerAdminApi> optionalServerAdminApiMock;

    @Mock
    private ServerAdminApi serverAdminApiMock;

    @Mock
    private PagedIterable<Server> pagedIterableServersMock;

    @Mock
    private Iterator<IterableWithMarker<Server>> iterableWithMarkerIteratorMock;

    @Mock
    private IterableWithMarker<Server> iterableWithMarkerServersMock;

    @Mock
    private Iterator<Server> serverIteratorMock;

    @Mock
    private Server serverMock;

    @Mock
    private ServerCreated serverCreatedMock;

    @Spy
    private OpenstackComputeServiceImpl openstackComputeServiceImplSpy = new OpenstackComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);

    /**
     * Add common mocks for all tests on init() method.
     *
     * @throws Exception
     */
    private void addCommonMocksForInitMethod() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.mockStatic(ContextBuilder.class);
        PowerMockito.doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", OPENSTACK_PROVIDER);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).endpoint(ENDPOINT);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
        Mockito.doReturn(novaApiMock).when(contextBuilderMock).buildApi(NovaApi.class);
    }

    /**
     * Add common verifiers for tests on init() method.
     *
     * @throws Exception
     */
    private void commonVerifiersForInitMethod() throws Exception {
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(contextBuilderMock).endpoint(ENDPOINT);
        verify(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(NovaApi.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    /**
     * Tests the init method.
     *
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        toTest = new OpenstackComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();

        toTest.init(true);

        commonVerifiersForInitMethod();
        verifyNoMoreInteractions(propertiesMock);
    }

    /**
     * Test init method when proxy and region are not null.
     *
     * @throws Exception
     */
    @Test
    public void testInitWithProxyAndRegionSet() throws Exception {
        toTest = new OpenstackComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        Mockito.doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.setRegion(REGION);  //this may be or may not be set before init is called by lazyInit
        toTest.init(true);

        commonVerifiersForInitMethod();
        verify(propertiesMock).setProperty(eq(PROPERTY_PROXY_HOST), eq(PROXY_HOST));
        verify(propertiesMock).setProperty(eq(PROPERTY_PROXY_PORT), eq(PROXY_PORT));
        verify(propertiesMock).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
        verifyNoMoreInteractions(propertiesMock);
    }

    /**
     * Test init method with null proxy parameters and not null region.
     *
     * @throws Exception
     */
    @Test
    public void testInitWithNoProxyAndWithRegionSet() throws Exception {
        toTest = new OpenstackComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.setRegion(REGION); //this may be or may not be set before init is called by lazyInit
        toTest.init(true);

        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(propertiesMock).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
        verifyNoMoreInteractions(propertiesMock);
        verify(contextBuilderMock).endpoint(ENDPOINT);
        verify(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(NovaApi.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    /**
     * test lazy init when novaApi is not null.
     */
    @Test
    public void testLazyInitNotNullNovaApi() {
        openstackComputeServiceImplSpy.novaApi = novaApiMock;

        openstackComputeServiceImplSpy.lazyInit(false);

        verify(openstackComputeServiceImplSpy).lazyInit(false);
        verifyNoMoreInteractions(openstackComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter.
     * In this case no need to invoke init()
     */
    @Test
    public void testLazyInitWithSameRegion() {
        openstackComputeServiceImplSpy.novaApi = novaApiMock;
        openstackComputeServiceImplSpy.setRegion(REGION);

        openstackComputeServiceImplSpy.lazyInit(REGION, false);

        verify(openstackComputeServiceImplSpy).lazyInit(REGION, false);
        verify(openstackComputeServiceImplSpy).setRegion(REGION);
        verifyNoMoreInteractions(openstackComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null NovaApi.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullNovaApi() {
        doNothing().when(openstackComputeServiceImplSpy).init(anyBoolean());
        openstackComputeServiceImplSpy.setRegion(REGION);

        openstackComputeServiceImplSpy.lazyInit(REGION, false);

        verify(openstackComputeServiceImplSpy).lazyInit(REGION, false);
        verify(openstackComputeServiceImplSpy).init(false);
        verify(openstackComputeServiceImplSpy).setRegion(REGION);
        verifyNoMoreInteractions(openstackComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(openstackComputeServiceImplSpy).init(anyBoolean());
        openstackComputeServiceImplSpy.setRegion(REGION + " dasda");

        openstackComputeServiceImplSpy.lazyInit(REGION, false);

        verify(openstackComputeServiceImplSpy).lazyInit(REGION, false);
        verify(openstackComputeServiceImplSpy).setRegion(REGION + " dasda");
        verify(openstackComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(openstackComputeServiceImplSpy);
    }

    /**
     * Test runInstancesInRegion method. Positive scenario.
     */
    @Test
    public void testRunServer() {
        String exceptedResult = "server created, details: ...";
        doNothing().when(openstackComputeServiceImplSpy).lazyInit(REGION, false);
        openstackComputeServiceImplSpy.novaApi = novaApiMock; //this would be set by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doReturn(serverCreatedMock).when(serverApiMock).create("name", "imageRef", "flavorRef");
        Mockito.doReturn(exceptedResult).when(serverCreatedMock).toString();

        String result = openstackComputeServiceImplSpy.createServer(REGION, "name", "imageRef", "flavorRef", false);

        assertEquals(exceptedResult, result);
        verify(openstackComputeServiceImplSpy).lazyInit(REGION, false);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).create("name", "imageRef", "flavorRef");
        verifyNoMoreInteractions(serverApiMock);
    }
}
