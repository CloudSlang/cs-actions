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


package io.cloudslang.content.vmware.actions.vm;

/**
 * Created by Mihai Tusa.
 * 1/14/2016.
 */

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListVMsAndTemplates.class)
public class ListVMsAndTemplatesTest {
    private ListVMsAndTemplates listVMsAndTemplates;

    @Before
    public void init() {
        listVMsAndTemplates = new ListVMsAndTemplates();
    }

    @After
    public void tearDown() {
        listVMsAndTemplates = null;
    }

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testSuccessfullyListVMsAndTemplates() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);

        when(vmServiceMock.listVMsAndTemplates(any(HttpInputs.class), any(VmInputs.class), anyString())).thenReturn(resultMap);

        resultMap = listVMsAndTemplates.listVMsAndTemplates("", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, times(1)).listVMsAndTemplates(any(HttpInputs.class), any(VmInputs.class), anyString());

        assertNotNull(resultMap);
    }

    @Test
    public void testListVMsAndTemplatesProtocolException() throws Exception {
        Map<String, String> resultMap = listVMsAndTemplates.listVMsAndTemplates("", "", "myProtocol", "", "", "", "", "", null);

        verify(vmServiceMock, never()).listVMsAndTemplates(any(HttpInputs.class), any(VmInputs.class), anyString());

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
