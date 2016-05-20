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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 5/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DescribeImagesInRegionExecutor.class, ImageFactory.class})
public class DescribeImagesInRegionExecutorTest {
    private DescribeImagesInRegionExecutor toTest;

    @Mock
    private ImageService imageServiceMock;

    @Before
    public void init() {
        mockStatic(ImageFactory.class);

        toTest = new DescribeImagesInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getImageInputs());

        verify(imageServiceMock, times(1)).describeImagesInRegion(anyString(), anyString(), any(String[].class), any(String[].class));

        assertNotNull(result);
        assertNotNull(result.get(Outputs.RETURN_RESULT));
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.CustomInputsBuilder().build();
    }

    private ImageInputs getImageInputs() {
        return new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(getCustomInputs())
                .withImageIdsString("")
                .withOwnersString("")
                .build();
    }
}