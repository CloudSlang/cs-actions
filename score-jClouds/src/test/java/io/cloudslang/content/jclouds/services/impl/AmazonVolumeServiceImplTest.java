package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.CreateVolumeOptions;
import org.jclouds.ec2.options.DescribeImagesOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 6/23/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonVolumeServiceImpl.class, ContextBuilder.class})
public class AmazonVolumeServiceImplTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private ElasticBlockStoreApi ebsApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApi;

    @Spy
    private AmazonVolumeServiceImpl volumeSpy = new AmazonVolumeServiceImpl("https://ec2.amazonaws.com",
            "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);

    private AmazonVolumeServiceImpl toTest;

    @Before
    public void init() throws Exception {
        mockStatic(ContextBuilder.class);
        addCommonMocksForMethods();

        toTest = new AmazonVolumeServiceImpl("https://ec2.amazonaws.com", "AKIAIQHVQ4UM7SO673TW",
                "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testInit() throws Exception {
        MockingHelper.addCommonMocksForInitMethod(contextBuilderMock, propertiesMock);

        toTest.init();

        MockingHelper.commonVerifiersForInitMethod(contextBuilderMock, propertiesMock);
        verifyNoMoreInteractions(propertiesMock);
    }

    @Test
    public void testLazyInit() throws Exception {
        MockingHelper.addCommonMocksForInitMethod(contextBuilderMock, propertiesMock);

        toTest.lazyInit("us-east-1");

        MockingHelper.commonVerifiersForInitMethod(contextBuilderMock, propertiesMock);
    }

    @Test
    public void createVolumeInAvailabilityZoneTest() throws Exception {
        volumeSpy.createVolumeInAvailabilityZone("some_region", "an_available_zone", "snap-c5920f60", "", 1024, 100, true);

        verify(volumeSpy, times(1)).lazyInit("some_region");
        verify(ebsApiMock, times(1)).createVolumeInAvailabilityZone(eq("an_available_zone"), any(CreateVolumeOptions.class));
        commonVerifiersForMethods();
    }

    @Test
    public void createVolumeInAvailabilityZoneValidationErrorNegativeSizeTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect provided value: -5 input. The value doesn't meet conditions for general purpose usage.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "", -5, 1111, false);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }


    @Test
    public void createVolumeInAvailabilityZoneValidationErrorLowerSizeTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The size [0] provided for [gp2] volumeType should be greater or equal than [1] GiBs value and smaller " +
                        "or equal than [16384] GiBs value.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "gp2", 0, 1111, false);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }

    @Test
    public void createVolumeInAvailabilityZoneValidationErrorHigherSizeTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The size [16385] provided for [standard] volumeType should be greater or equal than [1] GiBs value " +
                        "and smaller or equal than [1024] GiBs value.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "standard", 16385, 1111, false);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }

    @Test
    public void createVolumeInAvailabilityZoneValidationErrorIopsTest1() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The size [499] provided for [sc1] volumeType should be greater or equal than [500] GiBs value " +
                        "and smaller or equal than [16384] GiBs value.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "sc1", 499, 0, true);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }

    @Test
    public void createVolumeInAvailabilityZoneValidationErrorIopsTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The iops [10001] provided for [gp2] volumeType should be greater or equal than [100] IOPS value " +
                        "and smaller or equal than [10000] IOPS value.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "gp2", 3334, 10001, true);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }

    @Test
    public void createVolumeInAvailabilityZoneValidationErrorIopsTest2() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The iops [99] provided for [io1] volumeType should be greater or equal than [100] IOPS value " +
                        "and smaller or equal than [20000] IOPS value.");

        volumeSpy.createVolumeInAvailabilityZone("", "", "", "io1", 3333, 99, true);

        verify(volumeSpy, never()).lazyInit(anyString());
        verify(ebsApiMock, never()).createVolumeInAvailabilityZone(anyString(), any(CreateVolumeOptions.class));
    }

    private void addCommonMocksForMethods() {
        doNothing().when(volumeSpy).lazyInit(anyString());
        doNothing().when(volumeSpy).init();
        volumeSpy.ec2Api = ec2ApiMock;
        doReturn(optionalInstanceApi).when(ec2ApiMock).getElasticBlockStoreApiForRegion(anyString());
        doReturn(optionalInstanceApi).when(ec2ApiMock).getElasticBlockStoreApi();
        doReturn(ebsApiMock).when(optionalInstanceApi).get();
    }

    private void commonVerifiersForMethods() {
        verify(optionalInstanceApi, times(1)).get();
        verifyNoMoreInteractions(ebsApiMock);
    }
}