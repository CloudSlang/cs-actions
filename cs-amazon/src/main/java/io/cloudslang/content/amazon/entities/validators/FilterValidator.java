package io.cloudslang.content.amazon.entities.validators;

/**
 * Created by sandorr
 * 2/16/2017.
 */
public interface FilterValidator {
    String getFilterValue(final String filterName, final String filterValue);
}
