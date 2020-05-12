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
import com.google.gson.JsonParser;
import io.cloudslang.content.json.entities.RemoveArrayEntryInput;
import org.jetbrains.annotations.NotNull;

public class RemoveArrayEntryService {

    public String execute(@NotNull RemoveArrayEntryInput input) {
        JsonArray array = new JsonParser().parse(input.getArray()).getAsJsonArray();
        array.remove(input.getIndex() >= 0 ? input.getIndex() : array.size() + input.getIndex());
        return array.toString();
    }
}
