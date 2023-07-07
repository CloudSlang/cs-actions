/*
 * Copyright 2023 Open Text
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
package io.cloudslang.content.vmware.commons.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.vmware.commons.constants.Inputs.TIMEOUT;
import static io.cloudslang.content.vmware.commons.constants.Inputs.VM_IDENTIFIER_TYPE;
import static io.cloudslang.content.vmware.commons.utils.Constants.*;

public class InputsValidation {
    @NotNull
    public static List<String> verifyDeployTemplateFromLibraryInputs(@NotNull final String timeout,
                                                                     @NotNull final String vmIdentifierType) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyVmIdentifierType(exceptionMessages, vmIdentifierType, VM_IDENTIFIER_TYPE);
        addVerifyPositiveNumber(exceptionMessages, timeout, TIMEOUT);
        return exceptionMessages;
    }


    @NotNull
    public static List<String> addVerifyVmIdentifierType(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> identifierTypes = new ArrayList<>();
        identifierTypes.add(NAME);
        identifierTypes.add(VMID);
        if (!identifierTypes.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_VM_IDENTIFIER_TYPE, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyPositiveNumber(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        } else if (Integer.parseInt(input) < 0) {
            exceptions.add(String.format(EXCEPTION_NEGATIVE_INDEX, input, inputName));
        }
        return exceptions;
    }
}
