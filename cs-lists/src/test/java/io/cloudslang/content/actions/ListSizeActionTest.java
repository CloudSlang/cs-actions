/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
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

public class ListSizeActionTest {

    @Test
    public void testSize() {
        String list = "ana,ion,vasile,marian,george";
        Map<String, String> result = new ListSizeAction().getListSize(list, ",");
        assertEquals("5", result.get("result"));
    }
}
