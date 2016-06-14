package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Sets;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.entities.inputs.NetworkInputs;
import io.cloudslang.content.jclouds.services.helpers.AmazonComputeServiceHelper;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by persdana on 6/19/2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonComputeServiceImpl.class, ContextBuilder.class})
public class AmazonComputeServiceImplTest {
    private AmazonComputeServiceImpl toTest;

    private static final String REGION = "us-east-1";
    private static final String SERVER_ID = "i-578dde87";
    private static final String INVALID_SERVER_ID = "i-578dde88";
    private static final String ENDPOINT = "https://ec2.amazonaws.com";
    private static final String IDENTITY = "AKIAIQHVQ4UM7SO673TW";
    private static final String PASSWORD = "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z";
    private static final String PROXY_HOST = "proxy.some.host.com";
    private static final String PROXY_PORT = "8080";

    private static final String AMAZON_PROVIDER = "ec2";
    private static final String PROPERTY_PROXY_HOST = "jclouds.proxy-host";
    private static final String PROPERTY_PROXY_PORT = "jclouds.proxy-port";
    private static final String PROPERTY_REGIONS = "jclouds.regions";

    private static final String SERVER_STOP_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=running, instanceId=i-578dde87, previousState=stopped, region=us-east-1]]";
    private static final String SERVER_START_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=stopped, instanceId=i-578dde87, previousState=running, region=us-east-1]]";
    private static final String REMOVE_SERVER_SUCCESS_MESSAGE = "[InstanceStateChange [currentState=terminated, instanceId=i-578dde87, previousState=stopped, region=us-east-1]]";

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
    private AMIApi amiApiMock;

    @Mock
    private InstanceApi instanceApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApi;

    @Mock
    private Optional<? extends AMIApi> optionalAmiApiMock;

    @Mock
    private Optional<ServerAdminApi> optionalServerAdminApiMock;

    @Mock
    private ServerAdminApi serverAdminApiMock;

    @Mock
    private Set<RunningInstance> instancesInRegion;

    @Mock
    private Iterator<RunningInstance> iterableWithMarkerIteratorMock;

    @Mock
    private Iterator<Image> imageIteratorMock;

    @Mock
    private IterableWithMarker<Server> iterableWithMarkerServersMock;

    @Mock
    private Iterator<Server> serverIteratorMock;

    @Mock
    private Reservation<RunningInstance> serverMock;

    @Mock
    private Reservation<RunningInstance> serverCreatedMock;

    @Mock
    private Set<Image> imagesSetMock;

    @Mock
    private Image imageMock;

    @Mock
    private AmazonComputeServiceHelper helperMock;

    @Spy
    private AmazonComputeServiceImpl amazonComputeServiceImplSpy = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, null, null);

    /**
     * Tests the init method.
     *
     * @throws Exception
     */
    @Test
    public void testInit() throws Exception {
        toTest = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, null, null);
        addCommonMocksForInitMethod();

        toTest.init();

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
        toTest = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);

        addCommonMocksForInitMethod();
        doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
        toTest.init();

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
        toTest = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, null, null);

        addCommonMocksForInitMethod();
        doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
        toTest.init();

        PowerMockito.verifyNew(Properties.class).withNoArguments();
        verify(propertiesMock, times(1)).setProperty(eq(PROPERTY_REGIONS), eq(REGION));
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
        doNothing().when(amazonComputeServiceImplSpy).init();

        amazonComputeServiceImplSpy.lazyInit();

        verify(amazonComputeServiceImplSpy).lazyInit();
        verify(amazonComputeServiceImplSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init when novaApi is not null.
     */
    @Test
    public void testLazyInitNotNullNovaApi() {
        amazonComputeServiceImplSpy.ec2Api = ec2ApiMock;
        amazonComputeServiceImplSpy.lazyInit();

        verify(amazonComputeServiceImplSpy).lazyInit();
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when previous region was null.
     */
    @Test
    public void testLazyInitWithRegion() {
        doNothing().when(amazonComputeServiceImplSpy).init();

        amazonComputeServiceImplSpy.lazyInit(REGION);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION);
        verify(amazonComputeServiceImplSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter.
     * In this case no need to invoke init()
     */
    @Test
    public void testLazyInitWithSameRegion() {
        amazonComputeServiceImplSpy.ec2Api = ec2ApiMock;
        amazonComputeServiceImplSpy.region = REGION;
        amazonComputeServiceImplSpy.lazyInit(REGION);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION);
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null NovaApi.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullNovaApi() {
        doNothing().when(amazonComputeServiceImplSpy).init();

        amazonComputeServiceImplSpy.region = REGION;
        amazonComputeServiceImplSpy.lazyInit(REGION);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION);
        verify(amazonComputeServiceImplSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(amazonComputeServiceImplSpy).init();

        amazonComputeServiceImplSpy.region = REGION + " dasda";
        amazonComputeServiceImplSpy.lazyInit(REGION);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION);
        verify(amazonComputeServiceImplSpy).init();
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * Test startInstances server method. Positive scenario.
     */
    @Test
    public void testStartInstances() {
        addCommonMocksForInstanceApi();

        Set<InstanceStateChange> instanceStateChangeSet = getInstanceStateChanges();
        doReturn(instanceStateChangeSet).when(instanceApiMock).startInstancesInRegion(REGION, SERVER_ID);

        String result = amazonComputeServiceImplSpy.startInstances(REGION, SERVER_ID);

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock, times(1)).startInstancesInRegion(REGION, SERVER_ID);

        assertEquals(SERVER_START_SUCCESS_MESSAGE, result);
    }


    /**
     * Test startInstances server method with invalid server id.
     */
    @Test
    public void testStartInstancesWithInvalidServerId() {
        setExpectedExceptions(ResourceNotFoundException.class, INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        addCommonMocksForInstanceApi();

        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doThrow(toThrow).when(instanceApiMock).startInstancesInRegion(REGION, SERVER_ID);

        amazonComputeServiceImplSpy.startInstances(REGION, SERVER_ID);
    }

    /**
     * Test stopInstances server method. Positive scenario.
     */
    @Test
    public void testStopInstances() {
        addCommonMocksForInstanceApi();

        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.RUNNING, InstanceState.STOPPED);
        instanceStateChangeSet.add(instanceStateChange);
        doReturn(instanceStateChangeSet).when(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);

        String result = amazonComputeServiceImplSpy.stopInstances(REGION, SERVER_ID);

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);

        assertEquals(SERVER_STOP_SUCCESS_MESSAGE, result);
    }

    /**
     * Test stopInstances server method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testStopInstancesWithInvalidServerId() {
        setExpectedExceptions(ResourceNotFoundException.class, INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        addCommonMocksForInstanceApi();

        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doThrow(toThrow).when(instanceApiMock).stopInstancesInRegion(REGION, false, SERVER_ID);

        amazonComputeServiceImplSpy.stopInstances(REGION, SERVER_ID);
    }

    /**
     * Test soft reboot server method. Positive scenario.
     */
    @Test
    public void testRebootInstances() {
        addCommonMocksForInstanceApi();
        doNothing().when(instanceApiMock).rebootInstancesInRegion(REGION, SERVER_ID);

        amazonComputeServiceImplSpy.rebootInstances(REGION, SERVER_ID);

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock).rebootInstancesInRegion(REGION, SERVER_ID);
    }

    /**
     * Test soft reboot method with invalid server id.
     * this should throw an "org.jclouds.rest.ResourceNotFoundException"
     * with the message "{"itemNotFound": {"message": "Instance not found", "code": 404}}"
     */
    @Test
    public void testRebootInstancesWithInvalidServerId() {
        setExpectedExceptions(ResourceNotFoundException.class, INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        addCommonMocksForInstanceApi();

        ResourceNotFoundException toThrow = new ResourceNotFoundException(INVALID_SERVER_ID_EXCEPTION_MESSAGE);
        doThrow(toThrow).when(instanceApiMock).rebootInstancesInRegion(REGION, INVALID_SERVER_ID);

        amazonComputeServiceImplSpy.rebootInstances(REGION, INVALID_SERVER_ID);
    }

    /**
     * Test remove server method. Positive scenario.
     */
    @Test
    public void testTerminateInstances() {
        addCommonMocksForInstanceApi();

        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.TERMINATED, InstanceState.STOPPED);
        instanceStateChangeSet.add(instanceStateChange);

        doReturn(instanceStateChangeSet).when(instanceApiMock).terminateInstancesInRegion(REGION, SERVER_ID);

        String result = amazonComputeServiceImplSpy.terminateInstances(REGION, SERVER_ID);

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock).terminateInstancesInRegion(REGION, SERVER_ID);

        assertEquals(REMOVE_SERVER_SUCCESS_MESSAGE, result);
    }

    /**
     * Test describeRegions method. Positive scenario.
     */
    @Test
    public void testListRegions() {
        addCommonMocksForInstanceApi();

        Set<String> regions = Sets.newIdentityHashSet();
        regions.add(REGION);

        doReturn(regions).when(ec2ApiMock).getConfiguredRegions();

        Set<String> returnedRegions = amazonComputeServiceImplSpy.describeRegions();

        assertTrue(returnedRegions.contains(REGION));
        verify(amazonComputeServiceImplSpy).lazyInit();
        verify(ec2ApiMock, times(1)).getConfiguredRegions();

        assertEquals(1, returnedRegions.size());
    }

    /**
     * Test describeRegions method with invalid endpoint set in init().
     * This scenario is equivalent to invalid credentials.
     */
    @Test
    public void testListRegionsOnInvalidEndpoint() {
        setExpectedExceptions(HttpResponseException.class, CONNECTION_REFUSE_EXCEPTION_MESSAGE);
        addCommonMocksForInstanceApi();

        HttpResponseException toThrow = new HttpResponseException(CONNECTION_REFUSE_EXCEPTION_MESSAGE, null, null);
        doThrow(toThrow).when(ec2ApiMock).getConfiguredRegions();

        amazonComputeServiceImplSpy.describeRegions();
    }

    /**
     * Test runInstancesInRegion method. Positive scenario.
     */
    @Test
    public void testRunInstancesInRegion() throws Exception {
        addCommonMocksForInstanceApi();
        doReturn(serverCreatedMock).when(instanceApiMock)
                .runInstancesInRegion(anyString(), anyString(), anyString(), anyInt(), anyInt(), any(RunInstancesOptions.class));

        amazonComputeServiceImplSpy.runInstancesInRegion("us-east-1", "", "", 1, 1, new RunInstancesOptions());

        verifyMocksInteractionInstanceApi();
        verify(instanceApiMock, times(1))
                .runInstancesInRegion(anyString(), anyString(), anyString(), anyInt(), anyInt(), any(RunInstancesOptions.class));
    }

    /**
     * Test updateServer method. Positive scenario.
     */
    @Test
    public void testUpdateInstanceTypeRunning() throws Exception {
        addCommonMocksForInstanceApi();
        whenNew(AmazonComputeServiceHelper.class).withNoArguments().thenReturn(helperMock);
        doReturn(InstanceState.RUNNING).when(helperMock).getInstanceState(any(InstanceApi.class), anyString(), anyString());
        doNothing().when(helperMock)
                .stopAndWaitToStopInstance(any(InstanceApi.class), eq(InstanceState.RUNNING), anyString(), anyString(),
                        anyLong(), anyLong());
        doNothing().when(instanceApiMock).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());

        Set<InstanceStateChange> instanceStateChangeSet = getInstanceStateChanges();

        doReturn(instanceStateChangeSet).when(instanceApiMock).startInstancesInRegion(anyString(), anyString());

        amazonComputeServiceImplSpy.updateInstanceType("us-east-1", "", "", 20000, 20000);

        verifyMocksInteractionInstanceApi();
        verify(instanceApiMock, times(1)).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateInstanceType() throws Exception {
        addCommonMocksForInstanceApi();
        whenNew(AmazonComputeServiceHelper.class).withNoArguments().thenReturn(helperMock);
        doReturn(InstanceState.STOPPED).when(helperMock).getInstanceState(any(InstanceApi.class), anyString(), anyString());
        doNothing().when(instanceApiMock).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());

        String result = amazonComputeServiceImplSpy.updateInstanceType("us-east-1", "", "", 20000, 20000);

        verifyMocksInteractionInstanceApi();
        verify(instanceApiMock, times(1)).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());

        assertEquals("Instance successfully updated.", result);
    }

    @Test
    public void testDescribeInstancesInRegion() throws Exception {
        addCommonMocksForInstanceApi();

        amazonComputeServiceImplSpy.describeInstancesInRegion(getCommonInputs(), getInstanceInputs("", ""));

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock, times(1)).describeInstancesInRegion(eq(REGION));
    }

    @Test
    public void testDescribeInstancesInRegionWithFiltersNoTags() throws Exception {
        addCommonMocksForInstanceApi();

        amazonComputeServiceImplSpy.describeInstancesInRegion(getCommonInputs(), getInstanceInputs("", "", "1.1.1.1"));

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock, times(1)).describeInstancesInRegionWithFilter(eq(REGION), any(ArrayListMultimap.class));
    }

    @Test
    public void testDescribeInstancesInRegionWithFiltersWithTags() throws Exception {
        addCommonMocksForInstanceApi();

        amazonComputeServiceImplSpy.describeInstancesInRegion(getCommonInputs(), getInstanceInputs("a,b,c", "x,y,z", "1.1.1.1"));

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock, times(1)).describeInstancesInRegionWithFilter(eq(REGION), any(ArrayListMultimap.class));
    }

    @Test
    public void testDescribeInstancesInRegionWithFiltersInvalidTags() throws Exception {
        setExpectedExceptions(RuntimeException.class, "Incorrect supplied values for: [keyTagsString] and/or " +
                "[valueTagsString] inputs. Number of tag keys should be the same with number of tag values.");
        addCommonMocksForInstanceApi();

        amazonComputeServiceImplSpy.describeInstancesInRegion(getCommonInputs(), getInstanceInputs("d,e,f,g", "1,2", "1.1.1.1"));

        verifyMocksInteractionInstanceApiForRegion();
        verify(instanceApiMock, times(1)).describeInstancesInRegionWithFilter(eq(REGION), any(ArrayListMultimap.class));
    }

    /**
     * Add common mocks for all tests on init() method.
     *
     * @throws Exception
     */
    private void addCommonMocksForInitMethod() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.mockStatic(ContextBuilder.class);
        PowerMockito.doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", AMAZON_PROVIDER);
        doReturn(contextBuilderMock).when(contextBuilderMock).endpoint(ENDPOINT);
        doReturn(contextBuilderMock).when(contextBuilderMock).credentials(IDENTITY, PASSWORD);
        doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
        doReturn(ec2ApiMock).when(contextBuilderMock).buildApi(NovaApi.class);
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
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    private void verifyMocksInteractionInstanceApi() {
        verifyCommonMocksInteraction();
        verify(ec2ApiMock, times(1)).getInstanceApi();
    }

    private void verifyMocksInteractionInstanceApiForRegion() {
        verifyCommonMocksInteraction();
        verify(ec2ApiMock, times(1)).getInstanceApiForRegion(REGION);
    }

    private void verifyCommonMocksInteraction() {
        verify(amazonComputeServiceImplSpy, times(1)).lazyInit(REGION);
        verify(optionalInstanceApi, times(1)).get();
    }

    private void addCommonMocksForInstanceApi() {
        amazonComputeServiceImplSpy.ec2Api = ec2ApiMock;

        doNothing().when(amazonComputeServiceImplSpy).lazyInit(anyString());
        doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApi();
        doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        doReturn(instanceApiMock).when(optionalInstanceApi).get();
    }

    private void setExpectedExceptions(Class<?> type, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }

    private Set<InstanceStateChange> getInstanceStateChanges() {
        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.STOPPED,
                InstanceState.RUNNING);
        instanceStateChangeSet.add(instanceStateChange);

        return instanceStateChangeSet;
    }

    private InstanceInputs getInstanceInputs(String tagKeys, String tagValues, String... filter) throws Exception {
        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder().withRegion(REGION).build();
        NetworkInputs networkInputs = (filter.length > 0) ?
                new NetworkInputs.NetworkInputsBuilder().withNetworkInterfacePublicIp(filter[0]).build() :
                new NetworkInputs.NetworkInputsBuilder().build();

        return new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(customInputs)
                .withNetworkInputs(networkInputs)
                .withKeyTagsString(tagKeys)
                .withValueTagsString(tagValues)
                .build();
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withDelimiter(",").build();
    }
}