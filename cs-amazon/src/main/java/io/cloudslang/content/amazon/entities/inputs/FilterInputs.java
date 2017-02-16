package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.Filter;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.stringToList;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class FilterInputs {
    private final List<Filter> filterMap;

    private FilterInputs(final Builder builder) {
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

        public Builder withFilter(final Filter filter) {
            filterMap.add(filter);
            return this;
        }

        public Builder withNewFilter(final String filterName, final String filterValues) {
            final Filter filter = new Filter(filterName, stringToList(filterValues, delimiter));
            return withFilter(filter);
        }

        public FilterInputs build() {
            return new FilterInputs(this);
        }

        public Builder withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }
    }
}
