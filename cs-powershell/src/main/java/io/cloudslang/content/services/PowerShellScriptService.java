/*
 * Copyright 2019-2023 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.services;

import io.cloudslang.content.entities.PSEdition;
import io.cloudslang.content.entities.WSManRequestInputs;

import java.util.Map;

public class PowerShellScriptService {

    private final WSManRemoteShellService wsManService = new WSManRemoteShellService(PSEdition.WINDOWS);


    public Map<String, String> execute(WSManRequestInputs inputs) throws Exception {
        return this.wsManService.runCommand(inputs);
    }
}
