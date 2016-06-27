package io.cloudslang.content.jclouds.services.impl;

import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.util.Properties;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
}