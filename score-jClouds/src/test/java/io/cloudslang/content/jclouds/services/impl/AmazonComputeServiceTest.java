package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.jclouds.ContextBuilder;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.domain.*;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.RunInstancesOptions;
import org.jclouds.http.HttpResponseException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
import org.jclouds.rest.ResourceNotFoundException;
import org.junit.Before;
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
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by persdana on 6/19/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonComputeService.class, ContextBuilder.class})
public class AmazonComputeServiceTest {

    private AmazonComputeService toTest;

    private static final String REGION = "us-east-1";
    private static final String INVALID_REGION = "randomRegion";
    private static final String SERVER_ID = "i-578dde87";
    private static final String INVALID_SERVER_ID = "i-578dde88";
    private static final String ENDPOINT = "https://ec2.amazonaws.com";
    private static final String IDENTITY = "AKIAIQHVQ4UM7SO673TW";
    private static final String PASSWORD = "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z";
    private static final String NULL_PROXY_HOST = null;
    private static final String NULL_PROXY_PORT = null;
    private static final String PROXY_HOST = "proxy.some.host.com";
    private static final String PROXY_PORT = "8080";

    private static final String AMAZON_PROVIDER = "ec2";
    private static final String PROPERTY_PROXY_HOST = "jclouds.proxy-host";
    private static final String PROPERTY_PROXY_PORT = "jclouds.proxy-port";
    private static final String PROPERTY_REGIONS = "jclouds.regions";

    private static final String SERVER_STOP_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=running, instanceId=i-578dde87, previousState=stopped, region=us-east-1]]";
    private static final String SERVER_START_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=stopped, instanceId=i-578dde87, previousState=running, region=us-east-1]]";
    private static final String REMOVE_SERVER_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=terminated, instanceId=i-578dde87, previousState=stopped, region=us-east-1]]";

    private static final String INVALID_REGION_EXCEPTION_MESSAGE = "requested location RegionOneTwo, which is not a configured region: {RegionOne=Suppliers.ofInstance(http://11.11.11.11:8774/v2/462822a54b064729b26d41a5027002cd)}";
    private static final String CONNECTION_REFUSE_EXCEPTION_MESSAGE = "org.jclouds.http.HttpResponseException: Connection refused: connect connecting to POST http://11.11.11.11:5000/v2.0/tokens HTTP/1.1";
    private static final String INVALID_SERVER_ID_EXCEPTION_MESSAGE = "The instance ID 'i-a7be737' does not exist";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private SLF4JLoggingModule loggingModuleMock;

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApi;

    @Mock
    Optional<? extends AMIApi> optionalAmiApiMock;

    @Mock
    AMIApi amiApiMock;

    @Mock
    private InstanceApi instanceApiMock;

    @Mock
    Optional<ServerAdminApi> optionalServerAdminApiMock;

    @Mock
    ServerAdminApi serverAdminApiMock;

    @Mock
    java.util.Set<org.jclouds.ec2.domain.RunningInstance> instancesInRegion;

    @Mock
    Iterator<org.jclouds.ec2.domain.RunningInstance> iterableWithMarkerIteratorMock;

    @Mock
    Iterator<Image> imageIteratorMock;

    @Mock
    IterableWithMarker<Server> iterableWithMarkerServersMock;

    @Mock
    Iterator<Server> serverIteratorMock;

    @Mock
    org.jclouds.ec2.domain.Reservation<org.jclouds.ec2.domain.RunningInstance> serverMock;

    @Mock
    Reservation<RunningInstance> serverCreatedMock;

    @Mock
    Set<Image> imagesSetMock;

    @Mock
    Image imageMock;

    @Spy
    private AmazonComputeService amazonComputeServiceSpy = new AmazonComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);

    @Before
    public void setUp() throws Exception {

    }

    /**
     * Add common mocks for all tests on init() method.
     * @throws Exception
     */
    private void addCommonMocksForInitMethod() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.mockStatic(ContextBuilder.class);
        PowerMockito.doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", AMAZON_PROVIDER);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).endpoint(ENDPOINT);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        Mockito.doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
        Mockito.doReturn(ec2ApiMock).when(contextBuilderMock).buildApi(NovaApi.class);
    }

    /**
     * Add common verifiers for tests on init() method.
     * @throws Exception
     */
    private void commonVerifiersFirInitMethod() throws Exception {
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(contextBuilderMock).endpoint(ENDPOINT);
        verify(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    /**
     * Tests the init method.
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        toTest = new AmazonComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();

        toTest.init();

        commonVerifiersFirInitMethod();
        verifyNoMoreInteractions(propertiesMock);
    }

    /**
     * Test init method when proxy and region are not null.
     * @throws Exception
     */
    @Test
    public void testInitWithProxyAndRegionSet() throws Exception {
        toTest = new AmazonComputeService(ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        Mockito.doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
        toTest.init();

        commonVerifiersFirInitMethod();
        verify(propertiesMock).setProperty(eq(PROPERTY_PROXY_HOST), eq(PROXY_HOST));
        verify(propertiesMock).setProperty(eq(PROPERTY_PROXY_PORT), eq(PROXY_PORT));
        verify(propertiesMock).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
        verifyNoMoreInteractions(propertiesMock);
    }

    /**
     * Test init method with null proxy parameters and not null region.
     * @throws Exception
     */
    @Test
    public void testInitWithNoProxyAndWithRegionSet() throws Exception {
        toTest = new AmazonComputeService(ENDPOINT, IDENTITY, PASSWORD, NULL_PROXY_HOST, NULL_PROXY_PORT);
        addCommonMocksForInitMethod();
        Mockito.doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
        toTest.init();

        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(propertiesMock).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
        verifyNoMoreInteractions(propertiesMock);
        verify(contextBuilderMock).endpoint(ENDPOINT);
        verify(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    /**
     * test lazy init when ec2Api is null.
     */
    @Test
    public void testLazyInit() {
        doNothing().when(amazonComputeServiceSpy).init();

        amazonComputeServiceSpy.lazyInit();

        verify(amazonComputeServiceSpy).lazyInit();
        verify(amazonComputeServiceSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }

    /**
     * test lazy init when novaApi is not null.
     */
    @Test
    public void testLazyInitNotNullNovaApi() {
        amazonComputeServiceSpy.ec2Api = ec2ApiMock;

        amazonComputeServiceSpy.lazyInit();

        verify(amazonComputeServiceSpy).lazyInit();
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }

    /**
     * test lazy init(region) when previous region was null.
     */
    @Test
    public void testLazyInitWithRegion() {
        doNothing().when(amazonComputeServiceSpy).init();

        amazonComputeServiceSpy.lazyInit(REGION);

        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(amazonComputeServiceSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter.
     * In this case no need to invoke init()
     */
    @Test
    public void testLazyInitWithSameRegion() {
        amazonComputeServiceSpy.ec2Api = ec2ApiMock;
        amazonComputeServiceSpy.region = REGION;

        amazonComputeServiceSpy.lazyInit(REGION);

        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null NovaApi.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullNovaApi() {
        doNothing().when(amazonComputeServiceSpy).init();
        amazonComputeServiceSpy.region = REGION;

        amazonComputeServiceSpy.lazyInit(REGION);

        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(amazonComputeServiceSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(amazonComputeServiceSpy).init();
        amazonComputeServiceSpy.region = REGION + " dasda";

        amazonComputeServiceSpy.lazyInit(REGION);

        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(amazonComputeServiceSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceSpy);
    }


    /**
     * Test start server method. Positive scenario.
     */
    @Test
    public void testStart() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.STOPPED, InstanceState.RUNNING);
        instanceStateChangeSet.add(instanceStateChange);
        Mockito.doReturn(instanceStateChangeSet).when(instanceApiMock).startInstancesInRegion(REGION, SERVER_ID);

        String result = amazonComputeServiceSpy.start(REGION, SERVER_ID);

        assertEquals(SERVER_START_SUCCESS_MESSAGE, result);
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApiForRegion(REGION);
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).startInstancesInRegion(REGION, SERVER_ID);
        verifyNoMoreInteractions(optionalInstanceApi);
    }

    /**
     * Test start server method with invalid server id.
     */
    @Test
    public void testStartWithInvalidServerId() {
        exception.expect(org.jclouds.rest.ResourceNotFoundException.class);
        exception.expectMessage(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(instanceApiMock).startInstancesInRegion(REGION, SERVER_ID);

        amazonComputeServiceSpy.start(REGION, SERVER_ID);
    }

    /**
     * Test stop server method. Positive scenario.
     */
    @Test
    public void testStop() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.RUNNING, InstanceState.STOPPED);
        instanceStateChangeSet.add(instanceStateChange);
        Mockito.doReturn(instanceStateChangeSet).when(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);

        String result = amazonComputeServiceSpy.stop(REGION, SERVER_ID);

        assertEquals(SERVER_STOP_SUCCESS_MESSAGE, result);
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApiForRegion(REGION);
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);
        verifyNoMoreInteractions(optionalInstanceApi);
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

        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);

        amazonComputeServiceSpy.stop(REGION, SERVER_ID);
    }

    /**
     * Test soft reboot server method. Positive scenario.
     */
    @Test
    public void testSoftReboot() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Mockito.doNothing().when(instanceApiMock).rebootInstancesInRegion(REGION, SERVER_ID);

        amazonComputeServiceSpy.softReboot(REGION, SERVER_ID);

        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApiForRegion(REGION);
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).rebootInstancesInRegion(REGION, SERVER_ID);
        verifyNoMoreInteractions(optionalInstanceApi);
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

        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        Mockito.doThrow(toThrow).when(instanceApiMock).rebootInstancesInRegion(REGION, INVALID_SERVER_ID);

        amazonComputeServiceSpy.softReboot(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test hard reboot server method. This method always throw exception because hard reboot is not available for amazon.
     */
    @Test
    public void testHardReboot() throws Exception {
        exception.expect(java.lang.Exception.class);
        exception.expectMessage("Use soft reboot and if a Linux/UNIX instance does not cleanly shut down within four minutes, Amazon EC2 will perform a hard reboot\n");

        amazonComputeServiceSpy.hardReboot(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test suspend server method. This method always throw exception because suspend is not available for amazon.
     */
    @Test
    public void testSuspend() throws Exception {
        exception.expect(java.lang.Exception.class);
        exception.expectMessage("Use stop server operation to suspend an amazon Instance");

        amazonComputeServiceSpy.suspend(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test resume server method. This method always throw exception because resume is not available for amazon.
     */
    @Test
    public void testResume() throws Exception {
        exception.expect(java.lang.Exception.class);
        exception.expectMessage("Resume is not supported on Amazon. Use start server operation to resume an amazon Instance");

        amazonComputeServiceSpy.resume(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test remove server method. Positive scenario.
     */
    @Test
    public void testRemoveServer() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.TERMINATED, InstanceState.STOPPED);
        instanceStateChangeSet.add(instanceStateChange);
        Mockito.doReturn(instanceStateChangeSet).when(instanceApiMock).terminateInstancesInRegion(REGION, SERVER_ID);

        String result = amazonComputeServiceSpy.removeServer(REGION, SERVER_ID);

        assertEquals(REMOVE_SERVER_SUCCESS_MESSAGE, result);
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApiForRegion(REGION);
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).terminateInstancesInRegion(REGION, SERVER_ID);
        verifyNoMoreInteractions(optionalInstanceApi);
    }

    /**
     * Test listRegions method. Positive scenario.
     */
    @Test
    public void testListRegions() {
        doNothing().when(amazonComputeServiceSpy).lazyInit();
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Set<String> regions = Sets.newIdentityHashSet();
        regions.add(REGION);
        Mockito.doReturn(regions).when(ec2ApiMock).getConfiguredRegions();

        Set<String> returnedRegions = amazonComputeServiceSpy.listRegions();

        assertEquals(1, returnedRegions.size());
        assertTrue(returnedRegions.contains(REGION));
        verify(amazonComputeServiceSpy).lazyInit();
        verify(ec2ApiMock).getConfiguredRegions();
        verifyNoMoreInteractions(ec2ApiMock);
    }

    /**
     * Test listRegions method with invalid endpoint set in init().
     * This scenario is equivalent to invalid credentials.
     */
    @Test
    public void testListRegionsOnInvalidEndpoint() {
        exception.expect(HttpResponseException.class);
        exception.expectMessage(CONNECTION_REFUSE_EXCEPTION_MESSAGE);
        doNothing().when(amazonComputeServiceSpy).lazyInit();
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        HttpResponseException toThrow = new HttpResponseException(CONNECTION_REFUSE_EXCEPTION_MESSAGE, null, null);
        Mockito.doThrow(toThrow).when(ec2ApiMock).getConfiguredRegions();

        amazonComputeServiceSpy.listRegions();
    }

    /**
     * Test list nodes method. Positive scenario. 1 node returned.
     */
    @Test
    public void testListNodes() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApi();
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Mockito.doReturn(instancesInRegion).when(instanceApiMock).describeInstancesInRegion(REGION);
        Mockito.doReturn(iterableWithMarkerIteratorMock).when(instancesInRegion).iterator();
        Mockito.doReturn(true).doReturn(false).when(iterableWithMarkerIteratorMock).hasNext();
        Mockito.doReturn(serverMock).when(iterableWithMarkerIteratorMock).next();

        Set<String> res = amazonComputeServiceSpy.listNodes(REGION);

        assertEquals(1, res.size());
        assertTrue(res.contains("reservation"));
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApi();
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).describeInstancesInRegion(REGION);
        verifyNoMoreInteractions(instanceApiMock);
        verify(instancesInRegion).iterator();
        verifyNoMoreInteractions(instancesInRegion);
        verify(iterableWithMarkerIteratorMock, Mockito.times(2)).hasNext();
        verify(iterableWithMarkerIteratorMock).next();
    }

    /**
     * Test createServer method. Positive scenario.
     */
    @Test
    public void testCreateServer() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        Mockito.doReturn(instanceApiMock).when(optionalInstanceApi).get();
        Mockito.doReturn(serverCreatedMock).when(instanceApiMock).runInstancesInRegion(REGION, null, "imageRef", 1, 1, RunInstancesOptions.NONE);
        String exceptedResult = "server created";
        Mockito.doReturn(exceptedResult).when(serverCreatedMock).toString();

        String result = amazonComputeServiceSpy.createServer(REGION, null, "imageRef", "flavorRef");

        assertEquals(exceptedResult, result);
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getInstanceApiForRegion(REGION);
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalInstanceApi).get();
        verifyNoMoreInteractions(optionalInstanceApi);
        verify(instanceApiMock).runInstancesInRegion(REGION, null, "imageRef", 1, 1, RunInstancesOptions.NONE);
        verifyNoMoreInteractions(instanceApiMock);
    }


    /**
     * Test list images in region method. Positive scenario.
     */
    @Test
    public void testListImages() {
        doNothing().when(amazonComputeServiceSpy).lazyInit(REGION);
        amazonComputeServiceSpy.ec2Api = ec2ApiMock; //this wold be set by lazyInit
        Mockito.doReturn(optionalAmiApiMock).when(ec2ApiMock).getAMIApi();
        Mockito.doReturn(amiApiMock).when(optionalAmiApiMock).get();
        Mockito.doReturn(imagesSetMock).when(amiApiMock).describeImagesInRegion(REGION);
        Mockito.doReturn(imageIteratorMock).when(imagesSetMock).iterator();
        Mockito.doReturn(true).doReturn(false).when(imageIteratorMock).hasNext();
        Mockito.doReturn(imageMock).when(imageIteratorMock).next();

        Set<String> result = amazonComputeServiceSpy.listImagesInRegion(REGION);

        assertEquals("[image]", result.toString());
        verify(amazonComputeServiceSpy).lazyInit(REGION);
        verify(ec2ApiMock).getAMIApi();
        verifyNoMoreInteractions(ec2ApiMock);
        verify(optionalAmiApiMock).get();
        verifyNoMoreInteractions(optionalAmiApiMock);
        verify(amiApiMock).describeImagesInRegion(REGION);
        verifyNoMoreInteractions(instanceApiMock);
        verify(imagesSetMock).iterator();
        verifyNoMoreInteractions(instancesInRegion);
        verify(imageIteratorMock, Mockito.times(2)).hasNext();
        verify(imageIteratorMock).next();
    }


}
