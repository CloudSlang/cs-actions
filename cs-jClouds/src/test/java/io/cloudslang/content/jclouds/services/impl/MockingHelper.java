package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.features.InstanceApi;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.util.Properties;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 6/23/2016.
 */
public class MockingHelper {
    private MockingHelper() {
    }

    public static void setExpectedExceptions(ExpectedException exception, Class<?> type, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }

    static void addCommonMocksForInitMethod(ContextBuilder contextBuilderMock, Properties propertiesMock) throws Exception {
        whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", "ec2");
        doReturn(contextBuilderMock).when(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        doReturn(contextBuilderMock).when(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
    }

    static void commonVerifiersForInitMethod(ContextBuilder contextBuilderMock, Properties propertiesMock) throws Exception {
        verifyNew(Properties.class).withNoArguments();
        verify(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        verify(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }

    // because the VolumeService and SnapshotService uses the same ElasticBlockStoreApi
    static <T> void addCommonMocksForMethods(AmazonVolumeServiceImpl volumeSpy, AmazonSnapshotServiceImpl snapshotSpy,
                                             EC2Api ec2ApiMock, Optional<? extends InstanceApi> optionalInstanceApiMock,
                                             ElasticBlockStoreApi ebsApiMock, Class<T> spy) {
        if (spy == AmazonVolumeServiceImpl.class) {
            doNothing().when(volumeSpy).lazyInit(anyString(), anyBoolean());
            doNothing().when(volumeSpy).init(anyBoolean());
            volumeSpy.ec2Api = ec2ApiMock;
        } else if (spy == AmazonSnapshotServiceImpl.class) {
            doNothing().when(snapshotSpy).lazyInit(anyString(), anyBoolean());
            doNothing().when(snapshotSpy).init(anyBoolean());
            snapshotSpy.ec2Api = ec2ApiMock;
        }
        doReturn(optionalInstanceApiMock).when(ec2ApiMock).getElasticBlockStoreApiForRegion(anyString());
        doReturn(optionalInstanceApiMock).when(ec2ApiMock).getElasticBlockStoreApi();
        doReturn(ebsApiMock).when(optionalInstanceApiMock).get();
    }

    static void commonVerifiersForMethods(Optional<? extends InstanceApi> optionalInstanceApiMock, ElasticBlockStoreApi ebsApiMock) {
        verify(optionalInstanceApiMock, times(1)).get();
        verifyNoMoreInteractions(ebsApiMock);
    }
}
