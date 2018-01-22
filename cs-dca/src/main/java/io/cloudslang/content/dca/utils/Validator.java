/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.utils;

import io.cloudslang.content.utils.BooleanUtilities;
import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.dca.utils.Constants.HTTP;
import static io.cloudslang.content.dca.utils.Constants.HTTPS;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;

public class Validator {
    private final List<String> validationErrorList;

    public Validator() {
        validationErrorList = new ArrayList<>();
    }

    public List<String> getValidationErrorList() {
        return validationErrorList;
    }

    public void validatePort(@NotNull final String port) {
        if (!isValidIpPort(port)) {
            validationErrorList.add("Invalid port value!");
        }
    }

    public void validateProtocol(String protocol) {
        if (!(HTTPS.equalsIgnoreCase(protocol) || HTTP.equalsIgnoreCase(protocol))) {
            validationErrorList.add("Invalid protocol. Valid values: 'http' and 'https'.");
        }
    }

    public void validateInt(@NotNull final String intValue) {
        if (!NumberUtilities.isValidInt(intValue)) {
            validationErrorList.add("Invalid integer.");
        }
    }

    public void validateBoolean(@NotNull final String booleanValue) {
        if (!BooleanUtilities.isValid(booleanValue)) {
            validationErrorList.add("Invalid boolean.");
        }
    }

    @SafeVarargs
    public final void validateSameLength(List<String>... args) {
        if (args.length < 1) {
            return;
        }

        final List<String> firstList = args[0];
        for (final List<String> list : args) {
            if (list.size() != firstList.size()) {
                validationErrorList.add("Lists are of not equal size!");
            }
        }
    }
}
