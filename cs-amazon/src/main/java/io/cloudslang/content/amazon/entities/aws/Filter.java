/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class Filter {
    private final String name;
    private final List<String> values;

    public Filter(@NotNull final String name, @NotNull final List<String> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        return new EqualsBuilder()
                .append(name, filter.name)
                .append(values, filter.values)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(values)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Filter{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
