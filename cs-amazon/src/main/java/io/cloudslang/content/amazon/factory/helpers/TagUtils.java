package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.RESOURCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RESOURCE_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;

/**
 * Created by TusaM
 * 10/12/2016.
 */
public class TagUtils {
    private static final String MORE_THAN_50_TAGS_ERROR_MESSAGE = "The resources cannot be tagged with more than 50 tags!";

    private static final int MAXIMUM_TAGS_ALLOWED = 50;

    public Map<String, String> getCreateTagsQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        String[] resourceIdsArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getResourceIdsString(),
                EMPTY, wrapper.getCommonInputs().getDelimiter());
        InputsUtil.validateArrayAgainstDuplicateElements(resourceIdsArray, wrapper.getCustomInputs().getResourceIdsString(),
                wrapper.getCommonInputs().getDelimiter(), RESOURCE_IDS_STRING);

        if (resourceIdsArray != null && resourceIdsArray.length > START_INDEX) {
            for (int index = START_INDEX; index < resourceIdsArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(RESOURCE_ID, index), resourceIdsArray[index]);
            }
        }
        setResourcesTags(queryParamsMap, wrapper.getCustomInputs().getKeyTagsString(), wrapper.getCustomInputs().getValueTagsString(),
                wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    private void setResourcesTags(Map<String, String> queryParamsMap, String keyTagsString, String valueTagsString, String delimiter) {
        String[] keyTagsStringArray = InputsUtil.getStringsArray(keyTagsString, EMPTY, delimiter);
        String[] valueTagsStringArray = InputsUtil.getStringsArray(valueTagsString, EMPTY, delimiter);
        InputsUtil.validateAgainstDifferentArraysLength(keyTagsStringArray, valueTagsStringArray, KEY_TAGS_STRING, VALUE_TAGS_STRING);

        if (keyTagsStringArray != null && keyTagsStringArray.length > START_INDEX
                && valueTagsStringArray != null && valueTagsStringArray.length > START_INDEX) {

            if (keyTagsStringArray.length > MAXIMUM_TAGS_ALLOWED) {
                throw new RuntimeException(MORE_THAN_50_TAGS_ERROR_MESSAGE);
            }

            for (int index = START_INDEX; index < keyTagsStringArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(KEY, index), InputsUtil.getValidKeyOrValueTag(keyTagsStringArray[index], true));
                String currentValue = NOT_RELEVANT.equalsIgnoreCase(valueTagsStringArray[index]) ? EMPTY : valueTagsStringArray[index];
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(VALUE, index), InputsUtil.getValidKeyOrValueTag(currentValue, false));
            }
        }
    }
}