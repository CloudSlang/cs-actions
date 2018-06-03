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
package io.cloudslang.content.amazon.utils;

import org.apache.commons.lang3.StringUtils;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

public class ParametersLine {
    private final String parametersLine;
    private final static int SPLIT_LIMIT  = 2;
    private final static int KEY_IDX      = 0;
    private final static int VAL_IDX = 1;

    public ParametersLine(String line) {
        parametersLine = line;
    }

    public boolean isValid() {
        if (StringUtils.isEmpty(parametersLine)) {
            return false;
        }

        String keyValueArr[] = parametersLine.split(EQUAL, SPLIT_LIMIT);
        return keyValueArr.length >= SPLIT_LIMIT &&
                (!StringUtils.isEmpty(keyValueArr[KEY_IDX])) &&
                (!StringUtils.isEmpty(keyValueArr[VAL_IDX]));
    }

    public String getKey() {
        if (StringUtils.isEmpty(parametersLine) ) {
            return StringUtils.EMPTY;
        }
        return parametersLine.split(EQUAL, SPLIT_LIMIT)[KEY_IDX];
    }

    public String getValue() {
        if (StringUtils.isEmpty(parametersLine) ) {
            return StringUtils.EMPTY;
        }

        return parametersLine.split(EQUAL, SPLIT_LIMIT)[VAL_IDX];
    }
}
