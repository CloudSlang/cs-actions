package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.Filter;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.entities.validators.FilterValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class FilterUtils {
    public static List<String> stringToList(final String string, final String delimiter) {
        final String[] splitString = StringUtils.split(string, delimiter);
        return Arrays.asList(splitString);
    }

    public static String getFilterNameKey(int index) {
        return String.format("Filter.%d.Name", index + 1);
    }

    public static String getFilterValueKey(int index, int counter) {
        return String.format("Filter.%d.Value.%d", index + 1, counter + 1);
    }


    public static Map<String, String> getFiltersQueryMap(final FilterInputs filterInputs, final FilterValidator validator) {
        final Map<String, String> filtersQueryMap = new HashMap<>();
        for (final Filter filter : filterInputs.getFilterList()) {
            final Map<String, String> filterQueryMap = getFilterQueryMap(filter,
                    filterInputs.getFilterList().indexOf(filter), validator);
            filtersQueryMap.putAll(filterQueryMap);
        }
        return filtersQueryMap;
    }

    public static Map<String, String> getFilterQueryMap(final Filter filter, int index, final FilterValidator validator) {
        final Map<String, String> filterQueryMap = new HashMap<>();
        filterQueryMap.put(getFilterNameKey(index), filter.getName());
        for (final String value : filter.getValues()) {
            final String key = getFilterValueKey(index, filter.getValues().indexOf(value));
            filterQueryMap.put(key, validator.getFilterValue(filter.getName(), value));
        }
        return filterQueryMap;
    }
}
