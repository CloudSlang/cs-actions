/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.cloudslang.content.vmware.actions.cluster;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_AFFINE_OR_ANTI_AFFINE_HOST_GROUP;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreateAffinityRule.class)
public class CreateAffinityRuleTest {
    private CreateAffinityRule createAffinityRule;

    @Before
    public void init() {
        createAffinityRule = new CreateAffinityRule();
    }

    @After
    public void tearDown() {
        createAffinityRule = null;
    }

    @Mock
    private ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testCreateAffinityRule() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();

        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(clusterComputeResourceServiceMock);
        when(clusterComputeResourceServiceMock.createAffinityRule(any(HttpInputs.class), any(VmInputs.class), any(String.class), any(String.class))).thenReturn(expectedResultMap);

        Map<String, String> actualResultMap = createAffinityRule.createAffinityRule("", "", "", "", "", "", "", "", "", "", "affineHostGroup", "", null);

        verify(clusterComputeResourceServiceMock, times(1)).createAffinityRule(any(HttpInputs.class), any(VmInputs.class), any(String.class), any(String.class));

        assertNotNull(actualResultMap);
        assertEquals(expectedResultMap, actualResultMap);
    }

    @Test
    public void testCreateAffinityRuleHostGroupsNotProvidedException() throws Exception {
        Map<String, String> resultMap = createAffinityRule.createAffinityRule("", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).createAffinityRule(any(HttpInputs.class), any(VmInputs.class), any(String.class), any(String.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(PROVIDE_AFFINE_OR_ANTI_AFFINE_HOST_GROUP, resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testCreateAffinityRuleProtocolException() throws Exception {
        Map<String, String> resultMap = createAffinityRule.createAffinityRule("", "", "myProtocol", "", "", "", "", "", "", "", "affineHostGroup", "", null);

        verify(clusterComputeResourceServiceMock, never()).createAffinityRule(any(HttpInputs.class), any(VmInputs.class), any(String.class), any(String.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testCreateAffinityRulePortException() throws Exception {
        Map<String, String> resultMap = createAffinityRule.createAffinityRule("", "myPort", "", "", "", "", "", "", "", "", "affineHostGroup", "", null);

        verify(clusterComputeResourceServiceMock, never()).createAffinityRule(any(HttpInputs.class), any(VmInputs.class), any(String.class), any(String.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }
}
