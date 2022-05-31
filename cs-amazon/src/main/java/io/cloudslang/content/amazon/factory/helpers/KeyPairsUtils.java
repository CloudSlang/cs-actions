/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

import io.cloudslang.content.amazon.entities.aws.AvailabilityZoneState;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_FILTERS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_FILTERS_STRING;
import static io.cloudslang.content.amazon.utils.InputsUtil.*;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

public class KeyPairsUtils {
    private static final String STATE = "state";

    @NotNull
    public Map<String, String> getDescribeKeyPairsQueryParamsMap(@NotNull InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());


        setFilters(queryParamsMap, wrapper.getCustomInputs().getKeyFiltersString(), wrapper.getCustomInputs().getValueFiltersString(),
                wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    private void setSpecificQueryParamsMap(Map<String, String> queryParamsMap, String[] inputArray, String specificString) {
        if (isNotEmpty(inputArray)) {
            for (int index = START_INDEX; index < inputArray.length; index++) {
                queryParamsMap.put(getQueryParamsSpecificString(specificString, index), inputArray[index]);
            }
        }
    }


    private void setFilters(Map<String, String> queryParamsMap, String keyFiltersString, String valueFiltersString, String delimiter) {
        String[] keyFiltersStringArray = getStringsArray(keyFiltersString, EMPTY, delimiter);
        String[] valueFiltersStringArray = getStringsArray(valueFiltersString, EMPTY, delimiter);
        validateAgainstDifferentArraysLength(keyFiltersStringArray, valueFiltersStringArray, KEY_FILTERS_STRING, VALUE_FILTERS_STRING);

        if (isNotEmpty(keyFiltersStringArray) && isNotEmpty(valueFiltersStringArray)) {
            for (int index = START_INDEX; index < keyFiltersStringArray.length; index++) {
                queryParamsMap.put(getQueryParamsSpecificString(NAME, index), keyFiltersStringArray[index]);
                String paramValue = STATE.equals(keyFiltersStringArray[index]) ?
                        AvailabilityZoneState.getValue(valueFiltersStringArray[index]) : valueFiltersStringArray[index];
                queryParamsMap.put(getQueryParamsSpecificString(VALUES, index), paramValue);
            }
        }
    }
}
