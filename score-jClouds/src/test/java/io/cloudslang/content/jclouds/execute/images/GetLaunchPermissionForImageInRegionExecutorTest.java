package io.cloudslang.content.jclouds.execute.images;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.ImageFactory;
import io.cloudslang.content.jclouds.services.ImageService;
import org.jclouds.ec2.domain.Permission;
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
 * 5/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetLaunchPermissionForImageInRegionExecutor.class, ImageFactory.class})
public class GetLaunchPermissionForImageInRegionExecutorTest {
    private GetLaunchPermissionForImageInRegionExecutor toTest;

    @Mock
    private ImageService imageServiceMock;

    @Mock
    private Permission permissionsMock;

    @Before
    public void init() {
        mockStatic(ImageFactory.class);

        toTest = new GetLaunchPermissionForImageInRegionExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(ImageFactory.getImageService(any(CommonInputs.class))).thenReturn(imageServiceMock);
        when(imageServiceMock.getLaunchPermissionForImage(anyString(), anyString())).thenReturn(permissionsMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getCustomInputs());

        verify(imageServiceMock, times(1)).getLaunchPermissionForImage(anyString(), anyString());

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().build();
    }

    private CustomInputs getCustomInputs() {
        return new CustomInputs.CustomInputsBuilder().build();
    }
}