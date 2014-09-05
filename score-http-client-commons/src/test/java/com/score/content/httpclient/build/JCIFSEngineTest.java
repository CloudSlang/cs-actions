package com.score.content.httpclient.build;

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
