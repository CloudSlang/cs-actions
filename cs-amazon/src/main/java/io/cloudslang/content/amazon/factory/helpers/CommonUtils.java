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

import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsArray;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/23/2016.
 */
class CommonUtils {
    void setPrefixedAndSuffixedCommonQueryParams(Map<String, String> queryParamsMap, String inputString, String prefix,
                              String suffix, String delimiter) {
        if (isNotBlank(inputString)) {
            String[] securityGroupsRelatedArray = getStringsArray(inputString, EMPTY, delimiter);
            if (isNotEmpty(securityGroupsRelatedArray)) {
                for (int index = START_INDEX; index < securityGroupsRelatedArray.length; index++) {
                    queryParamsMap.put(prefix + DOT + String.valueOf(index + ONE) + suffix, securityGroupsRelatedArray[index]);
                }
            }
        }
    }
}
