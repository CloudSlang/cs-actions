/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.json.services;

import io.cloudslang.content.json.entities.GetArraySublistInput;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.json.utils.Constants.GetArraySublistAction.INVALID_FROM_INDEX_VALUE;
import static io.cloudslang.content.json.utils.Constants.GetArraySublistAction.INVALID_TO_INDEX_VALUE;
import static io.cloudslang.content.json.utils.Constants.InputNames.*;

public class GetArraySublistService {

    public @NotNull Map<String, String> execute(@NotNull GetArraySublistInput input) {

        List<String> outputArray = new ArrayList<>();

        if (Integer.parseInt(input.getfromIndex()) > input.getArray().size()) {
            return OutputUtilities.getFailureResultsMap(INVALID_FROM_INDEX_VALUE);
        }
        if (!StringUtils.isEmpty(input.gettoIndex()) && Integer.parseInt(input.gettoIndex()) > input.getArray().size()) {
            return OutputUtilities.getFailureResultsMap(INVALID_TO_INDEX_VALUE);
        }
        if (StringUtils.isEmpty(input.gettoIndex())) {
            for (int i = Integer.parseInt(input.getfromIndex()); i < input.getArray().size(); i++) {
                if (input.getArray().get(i).toString().startsWith(BRACKET) || input.getArray().get(i).toString().startsWith(SQUARE_BRACKET) || input.getArray().get(i).toString().startsWith(DOUBLE_QUOTES))
                    outputArray.add(input.getArray().get(i).toString());
                else
                    outputArray.add(DOUBLE_QUOTES + input.getArray().get(i).toString() + DOUBLE_QUOTES);
            }
        } else
            for (int i = Integer.parseInt(input.getfromIndex()); i < Integer.parseInt(input.gettoIndex()); i++) {
                if (input.getArray().get(i).toString().startsWith(BRACKET) || input.getArray().get(i).toString().startsWith(SQUARE_BRACKET) || input.getArray().get(i).toString().startsWith(DOUBLE_QUOTES))
                    outputArray.add(input.getArray().get(i).toString());
                else
                    outputArray.add(DOUBLE_QUOTES + input.getArray().get(i).toString() + DOUBLE_QUOTES);
            }
        return OutputUtilities.getSuccessResultsMap(outputArray.toString());
    }
}
