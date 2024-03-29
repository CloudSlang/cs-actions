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

import com.vmware.vim25.ClusterHostGroup;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ListHostGroups.class)
public class ListHostGroupTest {

    private ListHostGroups listHostGroups;

    @Before
    public void init() {
        listHostGroups = new ListHostGroups();
    }

    @After
    public void tearDown() {
        listHostGroups = null;
    }

    @Mock
    private ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testListGroupVms() throws Exception {
        String expectedReturnResult = new String();

        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(clusterComputeResourceServiceMock);
        when(clusterComputeResourceServiceMock.listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class))).thenReturn(expectedReturnResult);

        Map<String, String> actualResultMap = listHostGroups.listHostGroups("", "", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, times(1)).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class));

        assertNotNull(actualResultMap);
        assertEquals(expectedReturnResult, actualResultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testListGroupVmsProtocolException() throws Exception {
        Map<String, String> resultMap = listHostGroups.listHostGroups("", "", "myProtocol", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testListGroupVmsPortException() throws Exception {
        Map<String, String> resultMap = listHostGroups.listHostGroups("", "myPort", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }
}
