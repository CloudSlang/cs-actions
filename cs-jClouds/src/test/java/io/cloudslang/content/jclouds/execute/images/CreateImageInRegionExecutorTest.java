package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
@PrepareForTest({CreateImageInRegionExecutor.class, ImageFactory.class})
public class CreateImageInRegionExecutorTest {
    private CreateImageInRegionExecutor toTest;

    @Mock
    private ImageService imageServiceMock;

    @Before
    public void init() {
        mockStatic(ImageFactory.class);

        toTest = new CreateImageInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getImageInputs());

        verify(imageServiceMock, times(1)).createImageInRegion(eq("some region"), eq("testName"), eq("i-abcdef12"),
                eq("anything in here"), eq(false), eq(false));

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withDebugMode("").build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.CustomInputsBuilder()
                .withRegion("some region")
                .withInstanceId("i-abcdef12")
                .build();
    }

    private ImageInputs getImageInputs() {
        return new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withImageName("testName")
                .withImageDescription("anything in here")
                .build();
    }
}
