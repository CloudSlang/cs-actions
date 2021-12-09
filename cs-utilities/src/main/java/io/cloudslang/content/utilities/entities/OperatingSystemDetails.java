/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.utilities.entities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public class OperatingSystemDetails {
    private String name;
    private String version;
    private String architecture;
    private String family;
    private Map<String, List<String>> commandsOutput;

    public OperatingSystemDetails() {
        name = "";
        version = "";
        architecture = "";
        family = "";
        commandsOutput = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFamily() {
       return family;
    }

    public Map<String, List<String>> getCommandsOutput() {
       return unmodifiableMap(commandsOutput);
    }

    public void addCommandOutput(String type, final List<String> cmdOutput) {
        commandsOutput.put(type, unmodifiableList(cmdOutput));
    }

    public void collectOsCommandOutputs(OperatingSystemDetails otherCommandOutputs) {
        commandsOutput.putAll(otherCommandOutputs.getCommandsOutput());
    }
}
