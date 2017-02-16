package io.cloudslang.content.amazon.entities.aws;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class Filter {
    private final String name;
    private final List<String> values;

    public Filter(String name, List<String> values) {
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
