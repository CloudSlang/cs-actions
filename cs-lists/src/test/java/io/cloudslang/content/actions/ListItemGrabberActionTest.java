/*
 * Copyright 2019-2023 Open Text
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




package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListItemGrabberActionTest {

    private static final String LIST = "Ana,Ion,Vasile,Maria,George";

    @Test
    public void testGetItem() {
        Map<String, String> result = new ListItemGrabberAction().grabItemFromList(LIST, ",", "1");
        assertEquals("success", result.get("response"));
        assertEquals("Ion", result.get("result"));
    }
}
