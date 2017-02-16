/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.stringToList;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class FilterInputs {
    private final List<Filter> filterMap;

    private FilterInputs(@NotNull final Builder builder) {
        filterMap = new ArrayList<>(builder.filterMap);
    }

    public List<Filter> getFilterList() {
        return filterMap;
    }

    public static class Builder {
        private List<Filter> filterMap;
        private String delimiter;

        public Builder() {
            filterMap = new ArrayList<>();
        }

        @NotNull
        public Builder withFilter(@NotNull final Filter filter) {
            filterMap.add(filter);
            return this;
        }

        @NotNull
        public Builder withNewFilter(@NotNull final String filterName, @NotNull final String filterValues) {
            final Filter filter = new Filter(filterName, stringToList(filterValues, delimiter));
            return withFilter(filter);
        }

        @NotNull
        public FilterInputs build() {
            return new FilterInputs(this);
        }

        public Builder withDelimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }
    }
}
