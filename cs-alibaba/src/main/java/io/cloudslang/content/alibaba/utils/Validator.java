/*
 * (c) Copyright 2018 Micro Focus
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
package io.cloudslang.content.alibaba.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

public class Validator {
    private final List<String> errorList;

    public Validator() {
        errorList = new ArrayList<>();
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public String getErrors() {
        return getErrors(System.lineSeparator());
    }

    public String getErrors(@NotNull final String delimiter) {
        return join(errorList, delimiter);
    }

    public Validator validatePort(@NotNull final String value, @NotNull final String inputName) {
        if (!isValidIpPort(value)) {
            errorList.add(format("[%s]: Invalid port value [%s]!", inputName, value));
        }
        return this;
    }
}
