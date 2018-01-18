/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

package io.cloudslang.content.vmware.actions.vm;

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 2/2/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UpdateVM.class)
public class UpdateVMTest {
    private UpdateVM updateVM;

    @Before
    public void init() {
        updateVM = new UpdateVM();
    }

    @After
    public void tearDown() {
        updateVM = null;
    }

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testUpdateVM() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.updateVM(any(HttpInputs.class), any(VmInputs.class))).thenReturn(resultMap);

        resultMap = updateVM.updateVM("", "", "", "", "", "", "", "", "update", "cpu", "low", "", "", null);

        verify(vmServiceMock, times(1)).updateVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
    }

    @Test
    public void testUpdateVMProtocolException() throws Exception {
        Map<String, String> resultMap = updateVM.updateVM("", "", "myProtocol", "", "", "", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, never()).deleteVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }

    @Test
    public void testUpdateVMOperationException() throws Exception {
        Map<String, String> resultMap = updateVM.updateVM("", "", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, never()).updateVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported operation value: []. Valid values are: create, add, remove, update.", resultMap.get("returnResult"));
    }
}
