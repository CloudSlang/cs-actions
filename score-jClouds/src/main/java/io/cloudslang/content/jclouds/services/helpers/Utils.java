package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ContextBuilder;

import java.io.Closeable;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public class Utils {
    private Utils() {
    }

    public static <T extends Closeable> T getApi(ContextBuilder contextBuilder, Class<T> valueType) {
        return contextBuilder.buildApi(valueType);
    }

    static void addFiltersMapRelevantEntry(Multimap<String, String> filtersMap, String filterKey, String filterValue) {
        if (StringUtils.isNotBlank(filterValue) && !Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(filterValue)) {
            filtersMap.put(filterKey, filterValue);
        }
    }

    static void updateFiltersMapEntry(Multimap<String, String> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }

    static void setTagFilters(Multimap<String, String> filtersMap, String[] tagKeys, String[] tagValues) {
        if (tagKeys != null && tagValues != null) {
            if (tagKeys.length != tagValues.length) {
                throw new RuntimeException(Constants.ErrorMessages.TAG_KEYS_TAG_VALUES_MISMATCH);
            }

            for (int counter = 0; counter < tagKeys.length - 1; counter++) {
                filtersMap.put(Constants.Miscellaneous.TAG, tagKeys[counter] + Constants.Miscellaneous.EQUAL + tagValues[counter]);
            }
        }
    }
}
