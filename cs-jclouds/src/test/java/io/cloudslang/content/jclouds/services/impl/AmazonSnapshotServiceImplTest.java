package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.CreateSnapshotOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Mihai Tusa.
 * 6/30/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonSnapshotServiceImpl.class, ContextBuilder.class})
public class AmazonSnapshotServiceImplTest {
    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApiMock;

    @Mock
    private ElasticBlockStoreApi ebsApiMock;

    @Spy
    private AmazonSnapshotServiceImpl snapshotSpy = new AmazonSnapshotServiceImpl("https://ec2.amazonaws.com",
            "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);

    private AmazonSnapshotServiceImpl toTest;

    @Before
    public void init() throws Exception {
        mockStatic(ContextBuilder.class);
        MockingHelper.addCommonMocksForMethods(snapshotSpy, ec2ApiMock, optionalInstanceApiMock, ebsApiMock,
                AmazonSnapshotServiceImpl.class);

        toTest = new AmazonSnapshotServiceImpl("https://ec2.amazonaws.com", "AKIAIQHVQ4UM7SO673TW",
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
    public void createSnapshotInRegionWithoutDescriptionTest() throws Exception {
        snapshotSpy.createSnapshotInRegion("some_region", "vol-abcdef16", "", false);

        verify(snapshotSpy, times(1)).lazyInit("some_region", false);
        verify(ebsApiMock, times(1)).createSnapshotInRegion(eq("some_region"), eq("vol-abcdef16"));
        MockingHelper.commonVerifiersForMethods(optionalInstanceApiMock, ebsApiMock);
    }

    @Test
    public void createSnapshotInRegionWithDescriptionTest() throws Exception {
        snapshotSpy.createSnapshotInRegion("some_region", "vol-abcdef16", "anything in here", false);

        verify(snapshotSpy, times(1)).lazyInit("some_region", false);
        verify(ebsApiMock, times(1))
                .createSnapshotInRegion(eq("some_region"), eq("vol-abcdef16"), any(CreateSnapshotOptions.class));
        MockingHelper.commonVerifiersForMethods(optionalInstanceApiMock, ebsApiMock);
    }

    @Test
    public void deleteSnapshotInRegionTest() throws Exception {
        snapshotSpy.deleteSnapshotInRegion("some_region", "snap-abcdef16", false);

        verify(snapshotSpy, times(1)).lazyInit("some_region", false);
        verify(ebsApiMock, times(1)).deleteSnapshotInRegion(eq("some_region"), eq("snap-abcdef16"));
        MockingHelper.commonVerifiersForMethods(optionalInstanceApiMock, ebsApiMock);
    }
}
