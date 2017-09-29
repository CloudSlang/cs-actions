/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.entities.validators;

import io.cloudslang.content.amazon.entities.aws.ResourceType;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.amazon.entities.aws.TagFilter.RESOURCE_TYPE;

/**
 * Created by Tirla Alin
 * 2/16/2017.
 */
public class TagFilterValidator implements FilterValidator {
    @Override
    @NotNull
    public String getFilterValue(@NotNull final String filterName, @NotNull final String filterValue) {
        if(RESOURCE_TYPE.equals(filterName)) {
            return ResourceType.getValue(filterValue);
        } else {
            return filterValue;
        }
    }
}
