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
package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.GetSizeInputs;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.filesystem.utils.Utils.createReturnResultGetSize;

public class GetSizeService {

    public @NotNull Map<String, String> execute(@NotNull GetSizeInputs input) throws Exception {
        Map<String, String> results = new HashMap<>();

        File f = new File(input.getSource());
        long size = f.length();
        String returnResult = createReturnResultGetSize(size,input.getThreshold());
        results.put("returnResult", returnResult);
        results.put("size", String.valueOf(size));
        return results;
    }
}


