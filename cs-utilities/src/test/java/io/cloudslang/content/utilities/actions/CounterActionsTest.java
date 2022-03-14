/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.junit.Assert.assertEquals;
@RunWith(PowerMockRunner.class)
@PrepareForTest({CounterActionsTest.class})
public class CounterActionsTest {

    @Test
    public void runCounter() {
        String from = "1";
        String to = "10";
        String incrementBy = "1";
        String reset = "false";

        GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Counter counter = new Counter();
        for (int i = Integer.parseInt(from); i < Integer.parseInt(to); i++) {
            Map<String, String> actualResult = counter.execute(from, to, incrementBy, reset, globalSessionObject);
            assertEquals(String.valueOf(i), actualResult.get("resultString"));
        }

    }

}
