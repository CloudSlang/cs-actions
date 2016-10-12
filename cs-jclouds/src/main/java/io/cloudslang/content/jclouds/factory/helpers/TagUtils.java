package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.RESOURCE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;

/**
 * Created by TusaM
 * 10/12/2016.
 */
public class TagUtils {
    public Map<String, String> getCreateTagsQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        String[] resourceIdsArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getResourceIdsString(),
                EMPTY, wrapper.getCommonInputs().getDelimiter());
        InputsUtil.validateArrayAgainstDuplicateElements(resourceIdsArray, wrapper.getCustomInputs().getResourceIdsString(),
                wrapper.getCommonInputs().getDelimiter(), Inputs.CustomInputs.RESOURCE_IDS_STRING);

        if (resourceIdsArray != null && resourceIdsArray.length > START_INDEX) {
            for (int index = START_INDEX; index < resourceIdsArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(RESOURCE_ID, index), resourceIdsArray[index]);
            }
        }
        setResourcesTags(queryParamsMap, wrapper);

        return queryParamsMap;
    }

    private void setResourcesTags(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] keyTagsStringArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getKeyTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        String[] valueTagsStringArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getValueTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        InputsUtil.validateAgainstDifferentArraysLength(keyTagsStringArray, valueTagsStringArray, Inputs.CustomInputs.KEY_TAGS_STRING,
                Inputs.CustomInputs.VALUE_TAGS_STRING);


        if (keyTagsStringArray != null && keyTagsStringArray.length > START_INDEX
                && valueTagsStringArray != null && valueTagsStringArray.length > START_INDEX) {

            if (keyTagsStringArray.length > 50) {
                throw new RuntimeException("Resources: " + wrapper.getCustomInputs().getResourceIdsString() + " cannot " +
                        "be tagged with more than 50 tags!");
            }

            for (int index = START_INDEX; index < keyTagsStringArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(KEY, index), keyTagsStringArray[index]);
                String currentValue = NOT_RELEVANT.equalsIgnoreCase(valueTagsStringArray[index]) ?
                        EMPTY : valueTagsStringArray[index];
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(VALUE, index), currentValue);
            }
        }
    }
}