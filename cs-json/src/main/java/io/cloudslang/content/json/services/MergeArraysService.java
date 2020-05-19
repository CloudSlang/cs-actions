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

import com.google.gson.JsonArray;
import io.cloudslang.content.json.entities.MergeArraysInput;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

public class MergeArraysService {
    public Map<String, String> execute(MergeArraysInput input) {
        JsonArray mergedArray = new JsonArray();
        mergedArray.addAll(input.getArray1());
        mergedArray.addAll(input.getArray2());

        String returnResult = mergedArray.toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
