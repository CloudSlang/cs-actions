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
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.sitescope.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.cloudslang.content.utils.NumberUtilities;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.sitescope.constants.ExceptionMsgs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.DeleteMonitorGroupInputs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.EnableMonitorGroupInputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {

    @NotNull
    public static List<String> verifyGetGroupPropertiesInputs(@Nullable final String fullPathToGroup) {
        final List<String> exceptionMessages = new ArrayList<>();

        if(StringUtils.isEmpty(fullPathToGroup)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY));
        }

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyEnableMonitorGroupInputs(@Nullable final String fullPathToGroup,
                                                              @Nullable final String enable,
                                                              @Nullable final String timePeriod,
                                                              @Nullable final String fromTime,
                                                              @Nullable final String toTime) {
        final List<String> exceptionMessages = new ArrayList<>();

        if(StringUtils.isEmpty(fullPathToGroup)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY));
        }

        addVerifyBoolean(exceptionMessages, enable, ENABLE);
        addVerifyNumber(exceptionMessages, timePeriod, TIME_PERIOD);
        addVerifyNumber(exceptionMessages, fromTime, FROM_TIME);
        addVerifyNumber(exceptionMessages, toTime, TO_TIME);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeleteMonitorGroupInputs(@Nullable final String fullPathToGroup,
                                                              @Nullable final String externalId) {
        final List<String> exceptionMessages = new ArrayList<>();

        if(StringUtils.isEmpty(fullPathToGroup) && StringUtils.isEmpty(externalId)) {
            exceptionMessages.add(String.format(EXCEPTION_AT_LEAST_ONE_OF_INPUTS, FULL_PATH_TO_GROUP + ", " + EXTERNAL_ID));
        }

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String port,
                                                  @Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyPort(exceptionMessages, port, PORT);
        addVerifyPort(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyPort(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PORT, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

}

