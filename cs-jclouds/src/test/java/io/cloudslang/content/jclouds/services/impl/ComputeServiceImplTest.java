package io.cloudslang.content.jclouds.services.impl;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.domain.Location;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.rest.ResourceNotFoundException;
import org.junit.Rule;
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
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 7/3/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComputeServiceImpl.class, ContextBuilder.class, ComputeService.class})
public class ComputeServiceImplTest {
    private static final String REGION = "RegionOne";
    private static final String SERVER_ID = "3d95572b-b237-48e9-968c-a8d51ef1d9e5";
    private static final String INVALID_SERVER_ID = "3d95572b-b237-48e9-968c-a8d51ef1d9e6";
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
    private static final String INVALID_SERVER_ID_EXCEPTION_MESSAGE = "{\"itemNotFound\": {\"message\": \"Instance not found\", \"code\": 404}}";
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' providers in the provider input";

    private ComputeServiceImpl toTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private SLF4JLoggingModule loggingModuleMock;

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private ComputeServiceContext computeServiceContextMock;

    @Mock
    private ComputeService computeServiceMock;

    @Mock
    private Iterator<Location> locationIteratorMock;

    @Mock
    private Set<? extends Location> locationsSetMock;

    @Mock
    private Location locationMock;

    @Mock
    private Set<ComputeMetadata> computeMetadataSetMock;

    @Mock
    private Iterator<ComputeMetadata> computeMetadataIteratorMock;

    @Mock
    private ComputeMetadata computeMetadataMock;

    @Mock
    private TemplateBuilder templateBuilder;

    @Mock
    private Template template;

    @Spy
    private ComputeServiceImpl ComputeServiceImplSpy = new ComputeServiceImpl(OPENSTACK_PROVIDER, ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);

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
        Mockito.doReturn(computeServiceContextMock).when(contextBuilderMock).buildView(ComputeServiceContext.class);
        Mockito.doReturn(computeServiceMock).when(computeServiceContextMock).getComputeService();
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
        verify(contextBuilderMock).buildView(ComputeServiceContext.class);
        verifyNoMoreInteractions(contextBuilderMock);
        verify(computeServiceContextMock).getComputeService();
        verifyNoMoreInteractions(computeServiceContextMock);
    }

    /**
     * Tests the init method.
     *
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        toTest = new ComputeServiceImpl(OPENSTACK_PROVIDER, ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
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
        toTest = new ComputeServiceImpl(OPENSTACK_PROVIDER, ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        Mockito.doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be setted before init is called by lazyInit
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
        toTest = new ComputeServiceImpl(OPENSTACK_PROVIDER, ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be setted before init is called by lazyInit
        toTest.init(true);

        commonVerifiersForInitMethod();
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(propertiesMock).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
        verifyNoMoreInteractions(contextBuilderMock);
    }

    /**
     * test lazy init when computeService is null.
     */
    @Test
    public void testLazyInit() {
        doNothing().when(ComputeServiceImplSpy).init(false);

        ComputeServiceImplSpy.lazyInit(false);

        verify(ComputeServiceImplSpy).lazyInit(false);
        verify(ComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * test lazy init when computeService is not null.
     */
    @Test
    public void testLazyInitNotNullComputeService() {
        ComputeServiceImplSpy.computeService = computeServiceMock;

        ComputeServiceImplSpy.lazyInit(false);

        verify(ComputeServiceImplSpy).lazyInit(false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when previous region was null.
     */
    @Test
    public void testLazyInitWithRegion() {
        doNothing().when(ComputeServiceImplSpy).init(false);

        ComputeServiceImplSpy.lazyInit(REGION, false);

        verify(ComputeServiceImplSpy).lazyInit(REGION, false);
        verify(ComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter.
     * In this case no need to invoke init()
     */
    @Test
    public void testLazyInitWithSameRegion() {
        ComputeServiceImplSpy.computeService = computeServiceMock;
        ComputeServiceImplSpy.region = REGION;

        ComputeServiceImplSpy.lazyInit(REGION, false);

        verify(ComputeServiceImplSpy).lazyInit(REGION, false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null computeService.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullComputeService() {
        doNothing().when(ComputeServiceImplSpy).init(false);
        ComputeServiceImplSpy.region = REGION;

        ComputeServiceImplSpy.lazyInit(REGION, false);

        verify(ComputeServiceImplSpy).lazyInit(REGION, false);
        verify(ComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(ComputeServiceImplSpy).init(false);
        ComputeServiceImplSpy.region = REGION + " dasda";

        ComputeServiceImplSpy.lazyInit(REGION, false);

        verify(ComputeServiceImplSpy).lazyInit(REGION, false);
        verify(ComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(ComputeServiceImplSpy);
    }

    /**
     * Test describeRegions method. Positive scenario.
     */
    @Test
    public void testListRegions() {
        doNothing().when(ComputeServiceImplSpy).lazyInit(false);
        ComputeServiceImplSpy.computeService = computeServiceMock;
        Mockito.doReturn(locationsSetMock).when(computeServiceMock).listAssignableLocations();
        Mockito.doReturn(locationIteratorMock).when(locationsSetMock).iterator();
        Mockito.doReturn(true).doReturn(false).when(locationIteratorMock).hasNext();
        Mockito.doReturn(locationMock).when(locationIteratorMock).next();
        Mockito.doReturn("locationDrescription").when(locationMock).getDescription();

        Set<String> result = ComputeServiceImplSpy.describeRegions(false);

        assertEquals("[locationDrescription]", result.toString());
        verify(ComputeServiceImplSpy).lazyInit(false);
        verifyNoMoreInteractions(computeServiceContextMock);
        verify(computeServiceMock).listAssignableLocations();
        verifyNoMoreInteractions(computeServiceMock);
        verify(locationsSetMock).iterator();
        verifyNoMoreInteractions(locationsSetMock);
        verify(locationIteratorMock, times(2)).hasNext();
        verify(locationIteratorMock).next();
        verify(locationMock).getDescription();
    }

    /**
     * Test describeRegions method with invalid endpoint set in init().
     * This scenario is equivalent to invalid credentials.
     */
    @Test
    public void testListRegionsOnInvalidEndpoint() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doNothing().when(ComputeServiceImplSpy).lazyInit(REGION, false);
        ComputeServiceImplSpy.computeService = computeServiceMock;
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(computeServiceMock).listAssignableLocations();

        ComputeServiceImplSpy.describeRegions(false);
    }
}
