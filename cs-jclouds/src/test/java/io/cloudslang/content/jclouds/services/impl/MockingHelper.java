package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.ElasticBlockStoreApi;
import org.jclouds.ec2.features.InstanceApi;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.util.Properties;

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

    @SuppressWarnings("unchecked")
    public static void setExpectedExceptions(ExpectedException exception, Class<?> type, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }

    @SuppressWarnings("unchecked")
    static void addCommonMocksForInitMethod(ContextBuilder contextBuilderMock, Properties propertiesMock) throws Exception {
        whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", "ec2");
        doReturn(contextBuilderMock).when(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        doReturn(contextBuilderMock).when(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
    }

    @SuppressWarnings("unchecked")
    static void commonVerifiersForInitMethod(ContextBuilder contextBuilderMock, Properties propertiesMock) throws Exception {
        verifyNew(Properties.class).withNoArguments();
        verify(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        verify(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }
}