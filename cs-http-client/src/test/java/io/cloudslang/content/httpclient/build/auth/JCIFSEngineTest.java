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

package io.cloudslang.content.httpclient.build.auth;

import org.apache.http.impl.auth.NTLMEngineException;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
public class JCIFSEngineTest {

    @Test
    public void generateType1Msg() throws NTLMEngineException {
        JCIFSEngine engine = new JCIFSEngine();
        String type1Msg = engine.generateType1Msg("DOMAIN", "16.77.60");
        assertEquals("TlRMTVNTUAABAAAABbIIoAYABgAgAAAACAAIACYAAABET01BSU4xNi43Ny42MA==", type1Msg);
    }
}
