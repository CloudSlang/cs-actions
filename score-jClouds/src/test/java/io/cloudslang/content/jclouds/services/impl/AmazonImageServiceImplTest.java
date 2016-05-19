package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.AMIApi;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.options.CreateImageOptions;
import org.jclouds.ec2.options.DescribeImagesOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Properties;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anySetOf;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 5/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonImageServiceImpl.class, ContextBuilder.class})
public class AmazonImageServiceImplTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private AMIApi amiApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApi;

    @Spy
    private AmazonImageServiceImpl imageSpy = new AmazonImageServiceImpl("https://ec2.amazonaws.com", "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);

    private AmazonImageServiceImpl toTest;

    @Before
    public void init() throws Exception {
        mockStatic(ContextBuilder.class);
        addCommonMocksForMethods();

        toTest = new AmazonImageServiceImpl("https://ec2.amazonaws.com", "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testInit() throws Exception {
        addCommonMocksForInitMethod();

        toTest.init();

        commonVerifiersForInitMethod();
        verifyNoMoreInteractions(propertiesMock);
    }

    @Test
    public void testLazyInit() throws Exception {
        addCommonMocksForInitMethod();

        toTest.lazyInit("us-east-1");

        commonVerifiersForInitMethod();
    }

    @Test
    public void createImageInRegionTest() {
        imageSpy.createImageInRegion("", "", "", "", true);

        verify(imageSpy, times(1)).lazyInit(eq(""));
        verify(amiApiMock, times(1)).createImageInRegion(anyString(), anyString(), anyString(), any(CreateImageOptions.class));
        commonVerifiersForMethods();
    }

    @Test
    public void deregisterImageInRegionTest() {
        imageSpy.deregisterImageInRegion("", "");

        verify(imageSpy, times(1)).lazyInit(eq(""));
        verify(amiApiMock, times(1)).deregisterImageInRegion(eq(""), eq(""));
        commonVerifiersForMethods();
    }

    @Test
    public void describeImagesInRegionNoIdentityIdTest() {
        imageSpy.describeImagesInRegion("", "", null, null);

        verify(imageSpy, times(1)).lazyInit(eq(""));
        verify(amiApiMock, times(1)).describeImagesInRegion(eq(""), any(DescribeImagesOptions.class));
        commonVerifiersForMethods();
    }

    @Test
    public void describeImagesInRegionWithIdentityIdTest() {
        imageSpy.describeImagesInRegion("", "123456789", null, null);

        verify(imageSpy, times(1)).lazyInit(eq(""));
        verify(amiApiMock, times(1)).describeImagesInRegion(eq(""), any(DescribeImagesOptions.class));
        commonVerifiersForMethods();
    }

    @Test
    public void describeImagesInRegionTest() {
        String[] imageIds = InputsUtil.getStringsArray("firstId|secondId|thirdId", "", ",");
        String[] owners = InputsUtil.getStringsArray("firstGroup|secondGroup|thirdGroup", "", ",");

        imageSpy.describeImagesInRegion("us-east-1", "123456789", imageIds, owners);

        verify(imageSpy, times(1)).lazyInit(eq("us-east-1"));
        verify(amiApiMock, times(1)).describeImagesInRegion(eq("us-east-1"), any(DescribeImagesOptions.class));
        commonVerifiersForMethods();
    }

    @Test
    public void getLaunchPermissionForImageTest() {
        imageSpy.getLaunchPermissionForImage("us-east-1", "ami-abcdef16");

        verify(imageSpy, times(1)).lazyInit(eq("us-east-1"));
        verify(amiApiMock, times(1)).getLaunchPermissionForImageInRegion(eq("us-east-1"), eq("ami-abcdef16"));
        commonVerifiersForMethods();
    }

    @Test
    public void addLaunchPermissionsToImageTest() {
        Set<String> userIds = InputsUtil.getStringsSet("firstId|secondId|thirdId", "|");
        Set<String> userGroups = InputsUtil.getStringsSet("firstGroup,secondGroup,thirdGroup", ",");

        imageSpy.addLaunchPermissionsToImage("some_region", userIds, userGroups, "ami-abcdef16");

        verify(imageSpy, times(1)).lazyInit(eq("some_region"));
        verify(amiApiMock, times(1)).addLaunchPermissionsToImageInRegion(eq("some_region"), anySetOf(String.class), anySetOf(String.class), eq("ami-abcdef16"));
        commonVerifiersForMethods();
    }

    @Test
    public void removeLaunchPermissionsFromImageTest() {
        Set<String> userIds = InputsUtil.getStringsSet("firstId|secondId", "|");
        Set<String> userGroups = InputsUtil.getStringsSet("firstGroup,secondGroup", "");

        imageSpy.removeLaunchPermissionsFromImage("some_region", userIds, userGroups, "ami-abcdef16");

        verify(imageSpy, times(1)).lazyInit(eq("some_region"));
        verify(amiApiMock, times(1)).removeLaunchPermissionsFromImageInRegion(eq("some_region"), anySetOf(String.class), anySetOf(String.class), eq("ami-abcdef16"));
        commonVerifiersForMethods();
    }

    @Test
    public void resetLaunchPermissionsOnImageTest() {
        imageSpy.resetLaunchPermissionsOnImage("some_region", "ami-abcdef16");

        verify(imageSpy, times(1)).lazyInit(eq("some_region"));
        verify(amiApiMock, times(1)).resetLaunchPermissionsOnImageInRegion(eq("some_region"), eq("ami-abcdef16"));
        commonVerifiersForMethods();
    }

    private void commonVerifiersForMethods() {
        verify(optionalInstanceApi, times(1)).get();
        verifyNoMoreInteractions(amiApiMock);
    }

    private void addCommonMocksForMethods() {
        doNothing().when(imageSpy).lazyInit(anyString());
        doNothing().when(imageSpy).init();
        imageSpy.ec2Api = ec2ApiMock;
        doReturn(optionalInstanceApi).when(ec2ApiMock).getAMIApiForRegion(anyString());
        doReturn(optionalInstanceApi).when(ec2ApiMock).getAMIApi();
        doReturn(amiApiMock).when(optionalInstanceApi).get();
    }

    private void addCommonMocksForInitMethod() throws Exception {
        whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        doReturn(contextBuilderMock).when(ContextBuilder.class, "newBuilder", "ec2");
        doReturn(contextBuilderMock).when(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        doReturn(contextBuilderMock).when(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        doReturn(contextBuilderMock).when(contextBuilderMock).overrides(propertiesMock);
        doReturn(contextBuilderMock).when(contextBuilderMock).modules(Matchers.<Iterable>any());
    }

    private void commonVerifiersForInitMethod() throws Exception {
        verifyNew(Properties.class).withNoArguments();
        verify(contextBuilderMock).endpoint("https://ec2.amazonaws.com");
        verify(contextBuilderMock).credentials("AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z");
        verify(contextBuilderMock).overrides(propertiesMock);
        verify(contextBuilderMock).modules(Matchers.<Iterable>any());
        verify(contextBuilderMock).buildApi(EC2Api.class);
        verifyNoMoreInteractions(contextBuilderMock);
    }
}