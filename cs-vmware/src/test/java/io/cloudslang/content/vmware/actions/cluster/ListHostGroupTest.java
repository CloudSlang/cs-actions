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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by pinteae on 10/12/2016.
 */
@ExtendWith(MockitoExtension.class)
public class ListHostGroupTest {
    @Test
    public void testListGroupVms() throws Exception {
        String expectedReturnResult = new String();

        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> when(mock.listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class))).thenReturn(expectedReturnResult))) {
            Map<String, String> actualResultMap = new ListHostGroups().listHostGroups("", "", "", "", "", "", "", "", "", null);
            verify(construction.constructed().get(0), times(1)).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterHostGroup.class));
            assertNotNull(actualResultMap);
            assertEquals(expectedReturnResult, actualResultMap.get(OutputNames.RETURN_RESULT));
        }
    }

    @Test
    public void testListGroupVmsProtocolException() throws Exception {
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class)) {
            Map<String, String> resultMap = new ListHostGroups().listHostGroups("", "", "myProtocol", "", "", "", "", "", "", null);
            assertTrue(construction.constructed().isEmpty());
            assertNotNull(resultMap);
            assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
            assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
        }
    }

    @Test
    public void testListGroupVmsPortException() throws Exception {
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class)) {
            Map<String, String> resultMap = new ListHostGroups().listHostGroups("", "myPort", "", "", "", "", "", "", "", null);
            assertTrue(construction.constructed().isEmpty());
            assertNotNull(resultMap);
            assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
            assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
        }
    }
}
