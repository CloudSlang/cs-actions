/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * An extension of apaches StringUtils to enforce an unique dependency to the library in cs-actions
 * Created by victor on 31.08.2016.
 */
public final class StringUtilities extends StringUtils {
    private StringUtilities() {
    }
}
