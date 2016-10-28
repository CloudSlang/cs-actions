package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.REGION_NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_FILTERS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.REGIONS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_FILTERS_STRING;

/**
 * Created by TusaM
 * 10/14/2016.
 */
public class RegionUtils {
    public Map<String, String> getDescribeRegionsQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        String[] regionsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getCustomInputs().getRegionsString(),
                REGIONS_STRING, wrapper.getCommonInputs().getDelimiter());

        if (regionsArray != null && regionsArray.length > START_INDEX) {
            for (int index = START_INDEX; index < regionsArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(REGION_NAME, index), regionsArray[index]);
            }
        }

        setFilters(queryParamsMap, wrapper.getCustomInputs().getKeyFiltersString(), wrapper.getCustomInputs().getValueFiltersString(),
                wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    private void setFilters(Map<String, String> queryParamsMap, String keyFiltersString, String valueFiltersString, String delimiter) {
        String[] keyFiltersStringArray = InputsUtil.getStringsArray(keyFiltersString, EMPTY, delimiter);
        String[] valueFiltersStringArray = InputsUtil.getStringsArray(valueFiltersString, EMPTY, delimiter);
        InputsUtil.validateAgainstDifferentArraysLength(keyFiltersStringArray, valueFiltersStringArray, KEY_FILTERS_STRING,
                VALUE_FILTERS_STRING);

        if (keyFiltersStringArray != null && keyFiltersStringArray.length > START_INDEX
                && valueFiltersStringArray != null && valueFiltersStringArray.length > START_INDEX) {

            for (int index = START_INDEX; index < keyFiltersStringArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(NAME, index), keyFiltersStringArray[index]);
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(VALUES, index), valueFiltersStringArray[index]);
            }
        }
    }
}
