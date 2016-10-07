package io.cloudslang.content.jclouds.services.impl;

import com.google.common.base.Optional;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.features.TagApi;
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

import java.util.HashMap;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 7/21/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AmazonTagServiceImpl.class, ContextBuilder.class})
public class AmazonTagServiceImplTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private Properties propertiesMock;

    @Mock
    private ContextBuilder contextBuilderMock;

    @Mock
    private EC2Api ec2ApiMock;

    @Mock
    private TagApi tagApiMock;

    @Mock
    private Optional<? extends InstanceApi> optionalInstanceApi;

    @Spy
    private AmazonTagServiceImpl tagSpy = new AmazonTagServiceImpl("https://ec2.amazonaws.com",
            "AKIAIQHVQ4UM7SO673TW", "R1ZRPK4HPXU6cyBi1XY/IkYqQ+qR4Nfohkcd384Z", null, null);

    private AmazonTagServiceImpl toTest;

    @Before
    public void init() throws Exception {
        mockStatic(ContextBuilder.class);
        addCommonMocksForMethods();

        toTest = new AmazonTagServiceImpl("https://ec2.amazonaws.com", "AKIAIQHVQ4UM7SO673TW",
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
    public void applyToResourcesTest() throws Exception {
        tagSpy.applyToResources(getCommonInputs(), getCustomInputs("positive"));

        verify(tagSpy, times(1)).lazyInit(eq("some region"), eq(true));
        verify(tagApiMock, times(1)).applyToResources(any(HashMap.class), anySetOf(String.class));
        commonVerifiersForMethods();
    }

    @Test
    public void applyToResourcesLimitExceededTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect supplied values for: [keyTagsString] and/or [valueTagsString] inputs. A resource can have " +
                        "a maximum of 10 tags.");

        tagSpy.applyToResources(getCommonInputs(), getCustomInputs("limit"));

        verify(tagSpy, never()).lazyInit(anyString(), anyBoolean());
        verify(tagApiMock, never()).applyToResources(any(HashMap.class), anySetOf(String.class));
    }

    @Test
    public void applyToResourcesLimitNoIdsTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Value for supplied for: [resourceIdsString] input cannot be empty. Please provide at least one resource " +
                        "id to apply tags to.");

        tagSpy.applyToResources(getCommonInputs(), getCustomInputs("anything but positive or limit"));

        verify(tagSpy, never()).lazyInit(anyString(), anyBoolean());
        verify(tagApiMock, never()).applyToResources(any(HashMap.class), anySetOf(String.class));
    }

    private void addCommonMocksForMethods() {
        doNothing().when(tagSpy).lazyInit(anyString(), anyBoolean());
        doNothing().when(tagSpy).init(anyBoolean());
        tagSpy.ec2Api = ec2ApiMock;
        doReturn(optionalInstanceApi).when(ec2ApiMock).getTagApiForRegion(anyString());
        doReturn(optionalInstanceApi).when(ec2ApiMock).getTagApi();
        doReturn(tagApiMock).when(optionalInstanceApi).get();
    }

    private void commonVerifiersForMethods() {
        verify(optionalInstanceApi, times(1)).get();
        verifyNoMoreInteractions(tagApiMock);
    }

    private CommonInputs getCommonInputs() {
        return new CommonInputs.Builder().withDebugMode("tRuE").withDelimiter(",").build();
    }

    private CustomInputs getCustomInputs(String condition) throws Exception {
        if ("positive".equalsIgnoreCase(condition)) {
            return new CustomInputs.Builder()
                    .withRegion("some region")
                    .withKeyTagsString("firstKey,secondKey,thirdKey")
                    .withValueTagsString("firstValue,secondValue,thirdValue")
                    .withResourceIdsString("i-abcdef12,ami-abcdef12")
                    .build();
        } else if ("limit".equalsIgnoreCase(condition)) {
            return new CustomInputs.Builder()
                    .withRegion("some region")
                    .withKeyTagsString("1,2,3,4,5,6,7,8,9,10,11")
                    .withValueTagsString("a,b,c,d,e,f,g,h,i,j,k")
                    .withResourceIdsString("snap-abcdef12")
                    .build();
        } else {
            return new CustomInputs.Builder()
                    .withRegion("some region")
                    .withKeyTagsString("1")
                    .withValueTagsString("a")
                    .withResourceIdsString("")
                    .build();
        }
    }
}