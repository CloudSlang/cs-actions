/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient.build.auth;

import com.hp.score.content.httpclient.build.auth.JCIFSEngine;
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
