/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.Filter;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.entities.validators.FilterValidator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sandorr
 * 2/15/2017.
 */
public class FilterUtils {
    private static final String EQUALS = "=";
    private static final String INVALID_INPUT_FOR_FILTER_TAG_FORMAT = "Invalid input [%s] for input filterTag.";
    private static final String TAG_NAME_FORMAT = "tag:%s";
    private static final String FILTER_NAME_FORMAT = "Filter.%d.Name";
    private static final String FILTER_VALUE_FORMAT = "Filter.%d.Value.%d";

    @NotNull
    public static List<String> stringToList(@NotNull final String string, @NotNull final String delimiter) {
        final String[] splitString = StringUtils.split(string, delimiter);
        return Arrays.asList(splitString);
    }

    @NotNull
    public static String getFilterNameKey(int index) {
        return String.format(FILTER_NAME_FORMAT, index + 1);
    }

    @NotNull
    public static String getFilterValueKey(int index, int counter) {
        return String.format(FILTER_VALUE_FORMAT, index + 1, counter + 1);
    }

    @NotNull
    public static Map<String, String> getFiltersQueryMap(@NotNull final FilterInputs filterInputs, @NotNull final FilterValidator validator) {
        final Map<String, String> filtersQueryMap = new HashMap<>();
        for (final Filter filter : filterInputs.getFilterList()) {
            final Map<String, String> filterQueryMap = getFilterQueryMap(filter,
                    filterInputs.getFilterList().indexOf(filter), validator);
            filtersQueryMap.putAll(filterQueryMap);
        }
        return filtersQueryMap;
    }

    @NotNull
    public static Map<String, String> getFilterQueryMap(@NotNull final Filter filter, int index, @NotNull final FilterValidator validator) {
        final Map<String, String> filterQueryMap = new HashMap<>();
        filterQueryMap.put(getFilterNameKey(index), filter.getName());
        for (final String value : filter.getValues()) {
            final String key = getFilterValueKey(index, filter.getValues().indexOf(value));
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
