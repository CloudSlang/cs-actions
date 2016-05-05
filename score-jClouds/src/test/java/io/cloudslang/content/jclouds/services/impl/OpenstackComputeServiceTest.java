package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.jclouds.ContextBuilder;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.PagedIterable;
import org.jclouds.http.HttpResponseException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.RebootType;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.domain.ServerCreated;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
import org.jclouds.openstack.nova.v2_0.features.ServerApi;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 6/23/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OpenstackComputeService.class, ContextBuilder.class})
public class OpenstackComputeServiceTest {
    private OpenstackComputeService toTest;
    private static final String REGION = "RegionOne";
    private static final String INVALID_REGION = "RegionOneTwo";
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

    private static final String SERVER_STOP_SUCCESS_MESSAGE = "The server is stopping";
    private static final String SERVER_START_SUCCESS_MESSAGE = "Server is Starting";
    private static final String SUSPEND_SERVER_SUCCESS_MESSAGE = "OpenStack instance is suspending";
    private static final String SUSPEND_SERVER_FAIL_MESSAGE = "Can't suspend instance";

    private static final String INVALID_REGION_EXCEPTION_MESSAGE = "requested location RegionOneTwo, which is not a configured region: {RegionOne=Suppliers.ofInstance(http://11.11.11.11:8774/v2/462822a54b064729b26d41a5027002cd)}";
    private static final String CONNECTION_REFUSE_EXCEPTION_MESSAGE = "org.jclouds.http.HttpResponseException: Connection refused: connect connecting to POST http://11.11.11.11:5000/v2.0/tokens HTTP/1.1";
    private static final String INVALID_SERVER_ID_EXCEPTION_MESSAGE = "{\"itemNotFound\": {\"message\": \"Instance not found\", \"code\": 404}}";

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    private OpenstackComputeService openstackComputeServiceSpy = new OpenstackComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);

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
    private void commonVerifiersFirInitMethod() throws Exception {
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
        toTest = new OpenstackComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();

        toTest.init();

        commonVerifiersFirInitMethod();
        verifyNoMoreInteractions(propertiesMock);
    }

    /**
     * Test init method when proxy and region are not null.
     *
     * @throws Exception
     */
    @Test
    public void testInitWithProxyAndRegionSet() throws Exception {
        toTest = new OpenstackComputeService(ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        Mockito.doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.setRegion(REGION);  //this may be or may not be setted before init is called by lazyInit
        toTest.init();

        commonVerifiersFirInitMethod();
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
        toTest = new OpenstackComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.setRegion(REGION); //this may be or may not be setted before init is called by lazyInit
        toTest.init();

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
     * test lazy init when novaApi is null.
     */
    @Test
    public void testLazyInit() {
        doNothing().when(openstackComputeServiceSpy).init();

        openstackComputeServiceSpy.lazyInit();

        verify(openstackComputeServiceSpy).lazyInit();
        verify(openstackComputeServiceSpy).init();
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }

    /**
     * test lazy init when novaApi is not null.
     */
    @Test
    public void testLazyInitNotNullNovaApi() {
        openstackComputeServiceSpy.novaApi = novaApiMock;

        openstackComputeServiceSpy.lazyInit();

        verify(openstackComputeServiceSpy).lazyInit();
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }

    /**
     * test lazy init(region) when previous region was null.
     */
    @Test
    public void testLazyInitWithRegion() {
        doNothing().when(openstackComputeServiceSpy).init();

        openstackComputeServiceSpy.lazyInit(REGION);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(openstackComputeServiceSpy).init();
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter.
     * In this case no need to invoke init()
     */
    @Test
    public void testLazyInitWithSameRegion() {
        openstackComputeServiceSpy.novaApi = novaApiMock;
        openstackComputeServiceSpy.setRegion(REGION);

        openstackComputeServiceSpy.lazyInit(REGION);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(openstackComputeServiceSpy).setRegion(REGION);
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null NovaApi.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullNovaApi() {
        doNothing().when(openstackComputeServiceSpy).init();
        openstackComputeServiceSpy.setRegion(REGION);

        openstackComputeServiceSpy.lazyInit(REGION);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(openstackComputeServiceSpy).init();
        verify(openstackComputeServiceSpy).setRegion(REGION);
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(openstackComputeServiceSpy).init();
        openstackComputeServiceSpy.setRegion(REGION + " dasda");

        openstackComputeServiceSpy.lazyInit(REGION);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(openstackComputeServiceSpy).setRegion(REGION + " dasda");
        verify(openstackComputeServiceSpy).init();
        verifyNoMoreInteractions(openstackComputeServiceSpy);
    }


    /**
     * Test start server method. Positive scenario.
     */
    @Test
    public void testStart() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doNothing().when(serverApiMock).start(SERVER_ID);

        String result = openstackComputeServiceSpy.start(REGION, SERVER_ID);

        assertEquals(SERVER_START_SUCCESS_MESSAGE, result);
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).start(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test start server method with invalid server id.
     */
    @Test
    public void testStartWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).start(INVALID_SERVER_ID);

        openstackComputeServiceSpy.start(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test stop server method. Positive scenario.
     */
    @Test
    public void testStop() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doNothing().when(serverApiMock).stop(SERVER_ID);

        String result = openstackComputeServiceSpy.stop(REGION, SERVER_ID);

        assertEquals(SERVER_STOP_SUCCESS_MESSAGE, result);
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).stop(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test stop server method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testStopWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).stop(INVALID_SERVER_ID);

        openstackComputeServiceSpy.stop(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test soft reboot server method. Positive scenario.
     */
    @Test
    public void testSoftReboot() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doNothing().when(serverApiMock).reboot(SERVER_ID, RebootType.SOFT);

        openstackComputeServiceSpy.softReboot(REGION, SERVER_ID);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).reboot(SERVER_ID, RebootType.SOFT);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test soft reboot method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testSoftRebootWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).reboot(INVALID_SERVER_ID, RebootType.SOFT);

        openstackComputeServiceSpy.softReboot(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test hard reboot server method. Positive scenario.
     */
    @Test
    public void testHardReboot() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doNothing().when(serverApiMock).reboot(SERVER_ID, RebootType.HARD);

        openstackComputeServiceSpy.hardReboot(REGION, SERVER_ID);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).reboot(SERVER_ID, RebootType.HARD);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test hard reboot method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testHardRebootWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).reboot(INVALID_SERVER_ID, RebootType.HARD);

        openstackComputeServiceSpy.hardReboot(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test suspend server method. Positive scenario.
     */
    @Test
    public void testSuspend() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(optionalServerAdminApiMock).when(novaApiMock).getServerAdminApi(REGION);
        Mockito.doReturn(serverAdminApiMock).when(optionalServerAdminApiMock).get();
        Mockito.doReturn(true).when(serverAdminApiMock).suspend(SERVER_ID);

        String result = openstackComputeServiceSpy.suspend(REGION, SERVER_ID);

        assertEquals(SUSPEND_SERVER_SUCCESS_MESSAGE, result);
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerAdminApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(optionalServerAdminApiMock).get();
        verifyNoMoreInteractions(optionalServerAdminApiMock);
        verify(serverAdminApiMock).suspend(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test suspend server method. Negative scenario (api call fails).
     */
    @Test
    public void testSuspendNegativeScenario() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(optionalServerAdminApiMock).when(novaApiMock).getServerAdminApi(REGION);
        Mockito.doReturn(serverAdminApiMock).when(optionalServerAdminApiMock).get();
        Mockito.doReturn(false).when(serverAdminApiMock).suspend(SERVER_ID);

        String result = openstackComputeServiceSpy.suspend(REGION, SERVER_ID);

        assertEquals(SUSPEND_SERVER_FAIL_MESSAGE, result);
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerAdminApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(optionalServerAdminApiMock).get();
        verifyNoMoreInteractions(optionalServerAdminApiMock);
        verify(serverAdminApiMock).suspend(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test suspende server method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testSuspendWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(optionalServerAdminApiMock).when(novaApiMock).getServerAdminApi(REGION);
        Mockito.doReturn(serverAdminApiMock).when(optionalServerAdminApiMock).get();
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverAdminApiMock).suspend(INVALID_SERVER_ID);

        openstackComputeServiceSpy.suspend(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test resume server method. Positive scenario.
     */
    @Test
    public void testResume() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(optionalServerAdminApiMock).when(novaApiMock).getServerAdminApi(REGION);
        Mockito.doReturn(serverAdminApiMock).when(optionalServerAdminApiMock).get();
        Mockito.doReturn(true).when(serverAdminApiMock).resume(SERVER_ID);

        openstackComputeServiceSpy.resume(REGION, SERVER_ID);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerAdminApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(optionalServerAdminApiMock).get();
        verifyNoMoreInteractions(optionalServerAdminApiMock);
        verify(serverAdminApiMock).resume(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test resume server method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testResumeWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(optionalServerAdminApiMock).when(novaApiMock).getServerAdminApi(REGION);
        Mockito.doReturn(serverAdminApiMock).when(optionalServerAdminApiMock).get();
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverAdminApiMock).resume(INVALID_SERVER_ID);

        openstackComputeServiceSpy.resume(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test remove server method. Positive scenario.
     */
    @Test
    public void testRemoveServer() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doReturn(true).when(serverApiMock).delete(SERVER_ID);

        openstackComputeServiceSpy.removeServer(REGION, SERVER_ID);

        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).delete(SERVER_ID);
        verifyNoMoreInteractions(serverApiMock);
    }

    /**
     * Test removeServer method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testRemoveServerWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).delete(INVALID_SERVER_ID);

        openstackComputeServiceSpy.removeServer(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test listRegions method. Positive scenario.
     */
    @Test
    public void testListRegions() {
        doNothing().when(openstackComputeServiceSpy).lazyInit();
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Set<String> regions = Sets.newIdentityHashSet();
        regions.add(REGION);
        Mockito.doReturn(regions).when(novaApiMock).getConfiguredRegions();

        Set<String> returnedRegions = openstackComputeServiceSpy.listRegions();

        assertEquals(1, returnedRegions.size());
        assertTrue(returnedRegions.contains(REGION));
        verify(openstackComputeServiceSpy).lazyInit();
        verify(novaApiMock).getConfiguredRegions();
        verifyNoMoreInteractions(novaApiMock);
    }

    /**
     * Test listRegions method with invalid endpoint set in init().
     * This scenario is equivalent to invalid credentials.
     */
    @Test
    public void testListRegionsOnInvalidEndpoint() {
        exception.expect(HttpResponseException.class);
        exception.expectMessage(CONNECTION_REFUSE_EXCEPTION_MESSAGE);
        doNothing().when(openstackComputeServiceSpy).lazyInit();
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        HttpResponseException toThrow = new HttpResponseException(CONNECTION_REFUSE_EXCEPTION_MESSAGE, null, null);
        Mockito.doThrow(toThrow).when(novaApiMock).getConfiguredRegions();

        openstackComputeServiceSpy.listRegions();
    }

    /**
     * Test list nodes method. Positive scenario. 1 node returned.
     */
    @Test
    public void testListNodes() {
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doReturn(pagedIterableServersMock).when(serverApiMock).listInDetail();
        Mockito.doReturn(iterableWithMarkerIteratorMock).when(pagedIterableServersMock).iterator();
        Mockito.doReturn(true).doReturn(false).when(iterableWithMarkerIteratorMock).hasNext();
        Mockito.doReturn(iterableWithMarkerServersMock).when(iterableWithMarkerIteratorMock).next();
        Mockito.doReturn(serverIteratorMock).when(iterableWithMarkerServersMock).iterator();
        Mockito.doReturn(true).doReturn(false).when(serverIteratorMock).hasNext();
        Mockito.doReturn(serverMock).when(serverIteratorMock).next();

        Set<String> res = openstackComputeServiceSpy.listNodes(REGION);

        assertEquals(1, res.size());
        assertTrue(res.contains("server"));
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).listInDetail();
        verifyNoMoreInteractions(serverApiMock);
        verify(pagedIterableServersMock).iterator();
        verifyNoMoreInteractions(pagedIterableServersMock);
        verify(iterableWithMarkerIteratorMock, Mockito.times(2)).hasNext();
        verify(iterableWithMarkerIteratorMock).next();
        verify(iterableWithMarkerServersMock).iterator();
        verify(serverIteratorMock, Mockito.times(2)).hasNext();
        verify(serverIteratorMock).next();
    }

    /**
     * Test list nodes method with invalid region.
     */
    @Test
    public void testListNodesWithInvalidRegion() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(INVALID_REGION_EXCEPTION_MESSAGE);

        doNothing().when(openstackComputeServiceSpy).lazyInit(INVALID_REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(INVALID_REGION);
        IllegalArgumentException toThrow = new IllegalArgumentException(INVALID_REGION_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(serverApiMock).listInDetail();

        openstackComputeServiceSpy.listNodes(INVALID_REGION);
    }

    /**
     * Test runInstancesInRegion method. Positive scenario.
     */
    @Test
    public void testRunServer() {
        String exceptedResult = "server created, details: ...";
        doNothing().when(openstackComputeServiceSpy).lazyInit(REGION);
        openstackComputeServiceSpy.novaApi = novaApiMock; //this wold be setted by lazyInit
        Mockito.doReturn(serverApiMock).when(novaApiMock).getServerApi(REGION);
        Mockito.doReturn(serverCreatedMock).when(serverApiMock).create("name", "imageRef", "flavorRef");
        Mockito.doReturn(exceptedResult).when(serverCreatedMock).toString();

        String result = openstackComputeServiceSpy.createServer(REGION, "name", "imageRef", "flavorRef");

        assertEquals(exceptedResult, result);
        verify(openstackComputeServiceSpy).lazyInit(REGION);
        verify(novaApiMock).getServerApi(REGION);
        verifyNoMoreInteractions(novaApiMock);
        verify(serverApiMock).create("name", "imageRef", "flavorRef");
        verifyNoMoreInteractions(serverApiMock);
    }
}
