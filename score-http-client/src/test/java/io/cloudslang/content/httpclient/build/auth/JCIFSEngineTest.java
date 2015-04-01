/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package io.cloudslang.content.httpclient.build.auth;

import io.cloudslang.content.httpclient.build.auth.JCIFSEngine;
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
