/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
