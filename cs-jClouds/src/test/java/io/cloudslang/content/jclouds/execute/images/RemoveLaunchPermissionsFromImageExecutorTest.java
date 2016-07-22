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
 * 5/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RemoveLaunchPermissionsFromImageExecutor.class, ImageFactory.class})
public class RemoveLaunchPermissionsFromImageExecutorTest {
    private RemoveLaunchPermissionsFromImageExecutor toTest;

    @Mock
    private ImageService imageServiceMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init() {
        mockStatic(ImageFactory.class);

        toTest = new RemoveLaunchPermissionsFromImageExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getPopulatedImageInputs(true));

        verify(imageServiceMock, times(1)).removeLaunchPermissionsFromImage(eq("some region"), anySetOf(String.class),
                anySetOf(String.class), eq("ami-abcdef12"), eq(false));

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    @Test
    public void testExecuteException() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("The [userIdsString] and [userGroupsString] inputs" +
                " cannot be both empty in order to add/remove permission launch on specified image.");

        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        toTest.execute(getCommonInputs(), getPopulatedImageInputs(false));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withProvider("amazon").withExecutionLogs("anything in here").build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.CustomInputsBuilder().withRegion("some region").withImageId("ami-abcdef12").build();
    }

    private ImageInputs getPopulatedImageInputs(boolean withUser) {
        if (withUser) {
            return new ImageInputs.ImageInputsBuilder()
                    .withCustomInputs(getCustomInputs())
                    .withUserIdsString("firstId,secondId")
                    .withUserGroupsString("firstGroup,secondGroup")
                    .build();
        }

        return new ImageInputs.ImageInputsBuilder().withCustomInputs(getCustomInputs()).build();
    }
}
