package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.features.InstanceApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Mihai Tusa.
 * 6/23/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonVolumeServiceImpl.class, ContextBuilder.class})
public class AmazonVolumeServiceImplTest {
    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private ElasticBlockStoreApi ebsApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApiMock;

    @Spy
    private AmazonVolumeServiceImpl volumeSpy = new AmazonVolumeServiceImpl("https://ec2.amazonaws.com",
            "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);

    private AmazonVolumeServiceImpl toTest;

    @Before
    public void init() throws Exception {
        mockStatic(ContextBuilder.class);
        MockingHelper.addCommonMocksForMethods(volumeSpy, null, ec2ApiMock, optionalInstanceApiMock, ebsApiMock,
                AmazonVolumeServiceImpl.class);

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

        toTest.init(true);

        MockingHelper.commonVerifiersForInitMethod(contextBuilderMock, propertiesMock);
        verifyNoMoreInteractions(propertiesMock);
    }

    @Test
    public void testLazyInit() throws Exception {
        MockingHelper.addCommonMocksForInitMethod(contextBuilderMock, propertiesMock);

        toTest.lazyInit("us-east-1", true);

        MockingHelper.commonVerifiersForInitMethod(contextBuilderMock, propertiesMock);
    }

    @Test
    public void deleteVolumeInRegion() {
        volumeSpy.deleteVolumeInRegion("some_region", "vol-b8d74e1c", false);

        verify(volumeSpy, times(1)).lazyInit("some_region", false);
        verify(ebsApiMock, times(1)).deleteVolumeInRegion(eq("some_region"), eq("vol-b8d74e1c"));
        MockingHelper.commonVerifiersForMethods(optionalInstanceApiMock, ebsApiMock);
    }
}