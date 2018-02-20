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
package io.cloudslang.content.couchbase.validate;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.CONSTRAINS_ERROR_MESSAGE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.INPUTS_COMBINATION_ERROR_MESSAGE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.AT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.PORT_REGEX;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.COUCHBASE_DEFAULT_PROXY_PORT;
import static io.cloudslang.content.couchbase.entities.constants.Inputs.BucketInputs.SASL_PASSWORD;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getIntegerAboveMinimum;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getIntegerWithinValidRange;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getStringsArray;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Validators {
    private Validators() {
        // prevent instantiation
    }

    public static boolean areBothValuesPresent(String value1, String value2) {
        return isNotBlank(value1) && isNotBlank(value2);
    }

    public static int getValidPort(String input) {
        if (isBlank(input)) {
            return COUCHBASE_DEFAULT_PROXY_PORT;
        }

        if (!compile(PORT_REGEX).matcher(input).matches()) {
            throw new IllegalArgumentException(format("Incorrect provided value: %s input. %s",
                    input, CONSTRAINS_ERROR_MESSAGE));
        }

        return parseInt(input);
    }

    public static int getValidIntValue(String input, Integer minAllowed, Integer maxAllowed, Integer defaultValue) {
        return isBlank(input) ? defaultValue : maxAllowed == null ?
                getIntegerAboveMinimum(input, minAllowed) : getIntegerWithinValidRange(input, minAllowed, maxAllowed);
    }

    public static String getValidInternalNodeIpAddress(String input) {
        validateClusterInternalNodeFormat(input);

        return input;
    }

    public static void validateNotBothBlankInputs(String value1, String value2, String name1, String name2) {
        if (isBlank(value1) && isBlank(value2)) {
            throw new RuntimeException(format("The values: %s, %s provided for inputs: %s, %s cannot be both empty. " +
                    "Please provide values for at least one of them.", value1, value2, name1, name2));
        }
    }

    public static void validateRebalancingNodesPayloadInputs(String input, String delimiter) {
        String[] nodesArray = getStringsArray(input, delimiter);
        if (nodesArray != null) {
            for (String node : nodesArray) {
                validateClusterInternalNodeFormat(node);
            }
        }
    }

    public static void validateAuthType(Map<String, String> getPayloadMap, String authType) {
        if (AuthType.SASL.getValue().equals(authType) && !getPayloadMap.containsKey(SASL_PASSWORD)) {
            throw new RuntimeException(INPUTS_COMBINATION_ERROR_MESSAGE);
        }
    }

    public static String getValidOrDefaultValue(String input, String defaultValue, String[] validValues) {
        return isNotBlank(input) && stream(validValues).anyMatch(filter -> filter.contains(input)) ? input : defaultValue;
    }

    public static String getValidUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
    }

    private static void validateClusterInternalNodeFormat(String input) {
        if (!input.contains(AT)) {
            throw new RuntimeException(format("The provided value for: \"%s\" input must be a valid Couchbase internal node format.", input));
        }

        int indexOfAt = input.indexOf(AT);
        String ipv4Address = input.substring(indexOfAt + 1);

        if (!new InetAddressValidator().isValidInet4Address(ipv4Address)) {
            throw new RuntimeException(format("The value of: [%s] input as part of: [%s] input must be a valid IPv4 address.", ipv4Address, input));
        }
    }
}
