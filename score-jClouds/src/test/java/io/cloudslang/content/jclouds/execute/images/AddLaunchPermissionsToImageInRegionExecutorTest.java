package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 5/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddLaunchPermissionsToImageInRegionExecutor.class, ImageFactory.class})
public class AddLaunchPermissionsToImageInRegionExecutorTest {
    private AddLaunchPermissionsToImageInRegionExecutor toTest;

    @Mock
    private ImageService imageServiceMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init() {
        mockStatic(ImageFactory.class);

        toTest = new AddLaunchPermissionsToImageInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getPopulatedImageInputs());

        verify(imageServiceMock, times(1)).addLaunchPermissionsToImage(anyString(), anySetOf(String.class), anySetOf(String.class), anyString());

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    @Test
    public void testExecuteException() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("The [userIdsString] and [userGroupsString] inputs" +
                " cannot be both empty in order to add/remove permission launch on specified image.");

        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        toTest.execute(getCommonInputs(), getImageInputs());
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withProvider("amazon").build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.CustomInputsBuilder().withRegion("").withImageId("ami-4cfc1121").build();
    }

    private ImageInputs getImageInputs() {
        return new ImageInputs.ImageInputsBuilder().withCustomInputs(getCustomInputs()).build();
    }

    private ImageInputs getPopulatedImageInputs() {
        return new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withUserIdsString("firstId,secondId")
                .withUserGroupsString("firstGroup,secondGroup")
                .build();
    }
}