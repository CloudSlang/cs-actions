package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.Filter;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.entities.validators.FilterValidator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class FilterUtils {
    public static final String EQUALS = "=";
    public static final String INVALID_INPUT_FOR_FILTER_TAG_FORMAT = "Invalid input [%s] for input filterTag.";
    public static final String TAG_NAME_FORMAT = "tag:%s";

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
            if (Objects.equals(filter.getName(), "tag")) {
                System.out.println(filter.getValues());
            }
            filterQueryMap.put(key, validator.getFilterValue(filter.getName(), value));
        }
        return filterQueryMap;
    }

    public static void processTagFilter(@NotNull final String inputTag, @NotNull final String delimiter,
                                                        @NotNull final FilterInputs.Builder builder) {
        final List<String> tagPairs = stringToList(inputTag, delimiter);
        for (final String tagPair : tagPairs) {
            final List<String> tag = stringToList(tagPair, EQUALS);
            if (tag.size() != 2) {
                throw new RuntimeException(String.format(INVALID_INPUT_FOR_FILTER_TAG_FORMAT, inputTag));
            }
            final String filterKey = String.format(TAG_NAME_FORMAT, tag.get(0));
            builder.withNewFilter(filterKey, tag.get(1));
        }
    }
}
