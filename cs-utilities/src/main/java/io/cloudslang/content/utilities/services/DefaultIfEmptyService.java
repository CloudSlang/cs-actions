/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */

package io.cloudslang.content.utilities.services;

import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.utils.StringUtilities.defaultIfBlank;
import static io.cloudslang.content.utils.StringUtilities.defaultIfEmpty;

/**
 * Created by moldovai on 8/28/2017.
 */
public class DefaultIfEmptyService {

    public static String defaultIfBlankOrEmpty(final String initialValue, @NotNull final String defaultValue, boolean validTrim) {
        if (validTrim) {
            return defaultIfBlank(initialValue, defaultValue);
        }
        return defaultIfEmpty(initialValue, defaultValue);
    }
}