package io.cloudslang.content.jclouds.execute.tags;

import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.factory.TagFactory;
import io.cloudslang.content.jclouds.services.TagService;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 7/21/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ApplyToResourcesExecutor.class, TagFactory.class})
public class ApplyToResourcesExecutorTest {
    private ApplyToResourcesExecutor toTest;

    @Mock
    private TagService tagServiceMock;

    @Before
    public void init() {
        mockStatic(TagFactory.class);

        toTest = new ApplyToResourcesExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
    }

    @Test
    public void testExecute() throws Exception {
        when(TagFactory.getTagService(any(CommonInputs.class))).thenReturn(tagServiceMock);

        Map<String, String> result = toTest.execute(getCommonInputs(), getCustomInputs());

        verify(tagServiceMock, times(1)).applyToResources(any(CustomInputs.class), eq(","));

        assertNotNull(result);
        assertEquals("0", result.get(Outputs.RETURN_CODE));
        assertEquals("Apply tags to resources process started successfully.", result.get("returnResult"));
    }

    private CommonInputs getCommonInputs() throws Exception {
        return new CommonInputs.CommonInputsBuilder().withDelimiter(",").build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder().build();
    }
}