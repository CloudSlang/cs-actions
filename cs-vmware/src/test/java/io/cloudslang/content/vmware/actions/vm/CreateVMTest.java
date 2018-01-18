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
 * 1/11/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreateVM.class)
public class CreateVMTest {
    private CreateVM createVM;

    @Before
    public void init() {
        createVM = new CreateVM();
    }

    @After
    public void tearDown() {
        createVM = null;
    }

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testCreatesVM() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.createVM(any(HttpInputs.class), any(VmInputs.class))).thenReturn(resultMap);

        resultMap = createVM.createVM("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, times(1)).createVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
    }

    @Test
    public void testCreatesVMProtocolException() throws Exception {
        Map<String, String> resultMap = createVM.createVM("", "", "myProtocol", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, never()).createVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }

    @Test
    public void testCreatesVMIntException() throws Exception {
        Map<String, String> resultMap = createVM.createVM("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "2147483648", "", "", null);

        verify(vmServiceMock, never()).createVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("The input value must be 0 or positive number.", resultMap.get("returnResult"));
    }
}
