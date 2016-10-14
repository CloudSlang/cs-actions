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
import org.jclouds.http.HttpResponseException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Server;
import org.jclouds.openstack.nova.v2_0.extensions.ServerAdminApi;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
    private static final String REGION = "us-east-1";
    private static final String SERVER_ID = "i-578dde87";
    private static final String ENDPOINT = "https://ec2.amazonaws.com";
    private static final String IDENTITY = "AKIAIQHVQ4UM7SO673TW";
    private static final String PASSWORD = "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z";
    private static final String PROXY_HOST = "proxy.some.host.com";
    private static final String PROXY_PORT = "8080";
    private static final String AMAZON_PROVIDER = "ec2";
    private static final String PROPERTY_PROXY_HOST = "jclouds.proxy-host";
    private static final String PROPERTY_PROXY_PORT = "jclouds.proxy-port";
    private static final String PROPERTY_REGIONS = "jclouds.regions";
    private static final String CONNECTION_REFUSE_EXCEPTION_MESSAGE = "org.jclouds.http.HttpResponseException: Connection refused: connect connecting to POST http://11.11.11.11:5000/v2.0/tokens HTTP/1.1";

    private AmazonComputeServiceImpl toTest;

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
        toTest = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, PROXY_HOST, PROXY_PORT);

        addCommonMocksForInitMethod();
        doReturn(PROXY_HOST).when(propertiesMock).put(PROPERTY_PROXY_HOST, PROXY_HOST);
        doReturn(PROXY_PORT).when(propertiesMock).put(PROPERTY_PROXY_PORT, PROXY_PORT);
        doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
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
        toTest = new AmazonComputeServiceImpl(ENDPOINT, IDENTITY, PASSWORD, null, null);

        addCommonMocksForInitMethod();
        doReturn(REGION).when(propertiesMock).put(PROPERTY_REGIONS, REGION);

        toTest.region = REGION; //this may be or may not be set before init is called by lazyInit
        toTest.init(true);

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
     * test lazy init(region) when previous region was null.
     */
    @Test
    public void testLazyInitWithRegion() {
        doNothing().when(amazonComputeServiceImplSpy).init(anyBoolean());

        amazonComputeServiceImplSpy.lazyInit(REGION, false);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION, false);
        verify(amazonComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) when region parameter equals previous region parameter but with null NovaApi.
     * In this case init() method will be invoked although region didn't changed.
     */
    @Test
    public void testLazyInitWithSameRegionAndNullNovaApi() {
        doNothing().when(amazonComputeServiceImplSpy).init(anyBoolean());

        amazonComputeServiceImplSpy.region = REGION;
        amazonComputeServiceImplSpy.lazyInit(REGION, false);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION, false);
        verify(amazonComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
    }

    /**
     * test lazy init(region) with different region parameter than previous region parameter.
     * A new init will be invoked
     */
    @Test
    public void testLazyInitWithDifferentRegion() {
        doNothing().when(amazonComputeServiceImplSpy).init(anyBoolean());

        amazonComputeServiceImplSpy.region = REGION + " dasda";
        amazonComputeServiceImplSpy.lazyInit(REGION, false);

        verify(amazonComputeServiceImplSpy).lazyInit(REGION, false);
        verify(amazonComputeServiceImplSpy).init(false);
        verifyNoMoreInteractions(amazonComputeServiceImplSpy);
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

        amazonComputeServiceImplSpy.updateInstanceType("us-east-1", "", "", 20000, 20000, true);

        verifyMocksInteractionInstanceApi();
        verify(instanceApiMock, times(1)).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateInstanceType() throws Exception {
        addCommonMocksForInstanceApi();
        whenNew(AmazonComputeServiceHelper.class).withNoArguments().thenReturn(helperMock);
        doReturn(InstanceState.STOPPED).when(helperMock).getInstanceState(any(InstanceApi.class), anyString(), anyString());
        doNothing().when(instanceApiMock).setInstanceTypeForInstanceInRegion(anyString(), anyString(), anyString());

        String result = amazonComputeServiceImplSpy.updateInstanceType("us-east-1", "", "", 20000, 20000, true);

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
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Incorrect supplied values for: [keyTagsString] " +
                "and/or [valueTagsString] inputs. Number of tag keys should be the same with number of tag values.");
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
        verify(amazonComputeServiceImplSpy, times(1)).lazyInit(REGION, true);
        verify(optionalInstanceApi, times(1)).get();
    }

    private void addCommonMocksForInstanceApi() {
        amazonComputeServiceImplSpy.ec2Api = ec2ApiMock;

        doNothing().when(amazonComputeServiceImplSpy).init(anyBoolean());
        doNothing().when(amazonComputeServiceImplSpy).lazyInit(anyString(), anyBoolean());
        doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApiForRegion(REGION);
        doReturn(optionalInstanceApi).when(ec2ApiMock).getInstanceApi();
        doReturn(instanceApiMock).when(optionalInstanceApi).get();
    }

    private Set<InstanceStateChange> getInstanceStateChanges() {
        Set<InstanceStateChange> instanceStateChangeSet = new LinkedHashSet<>();
        InstanceStateChange instanceStateChange = new InstanceStateChange(REGION, SERVER_ID, InstanceState.STOPPED,
                InstanceState.RUNNING);
        instanceStateChangeSet.add(instanceStateChange);

        return instanceStateChangeSet;
    }

    private InstanceInputs getInstanceInputs(String tagKeys, String tagValues, String... filter) throws Exception {
        CustomInputs customInputs = new CustomInputs.Builder()
                .withRegion(REGION)
                .withKeyTagsString(tagKeys)
                .withValueTagsString(tagValues)
                .build();

        NetworkInputs networkInputs = (filter.length > 0) ?
                new NetworkInputs.Builder().withNetworkInterfacePublicIp(filter[0]).build() :
                new NetworkInputs.Builder().build();

        return new InstanceInputs.Builder()
                .withCustomInputs(customInputs)
                .withNetworkInputs(networkInputs)
                .build();
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.Builder().withDebugMode("TrUe").withDelimiter(",").build();
    }
}
