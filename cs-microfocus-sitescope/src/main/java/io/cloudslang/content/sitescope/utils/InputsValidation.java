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

package io.cloudslang.content.sitescope.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.sitescope.constants.ExceptionMsgs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.ChangeMonitorGroupStatusInputs.STATUS;
import static io.cloudslang.content.sitescope.constants.Inputs.ChangeMonitorStatusInputs.MONITOR_ID;
import static io.cloudslang.content.sitescope.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.sitescope.constants.Inputs.DeleteMonitorGroupInputs.EXTERNAL_ID;
import static io.cloudslang.content.sitescope.constants.Inputs.DeployTemplate.*;
import static io.cloudslang.content.sitescope.constants.Inputs.UpdateTemplate.FULL_PATH_TO_TEMPLATE;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {


    @NotNull
    public static List<String> verifyGetGroupPropertiesInputs(@Nullable final String fullPathToGroup) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToGroup)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, FULL_PATH_TO_GROUP));
        }

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyChangeMonitorGroupStatusInputs(@Nullable final String fullPathToGroup,
                                                                    @Nullable final String status) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToGroup)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, FULL_PATH_TO_GROUP));
        }

        addVerifyStatus(exceptionMessages, status, STATUS);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeleteMonitorGroupInputs(@Nullable final String fullPathToGroup,
                                                              @Nullable final String externalId) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToGroup) && StringUtils.isEmpty(externalId)) {
            exceptionMessages.add(String.format(EXCEPTION_AT_LEAST_ONE_OF_INPUTS, FULL_PATH_TO_GROUP + ", " + EXTERNAL_ID));
        }

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeleteMonitorInputs(@Nullable final String fullPathToMonitor) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToMonitor)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, FULL_PATH_TO_MONITOR));
        }

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String port,
                                                  @Nullable final String proxyPort,
                                                  @Nullable final String trustAllRoots,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyPort(exceptionMessages, port, PORT);
        addVerifyPort(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trustAllRoots, TRUST_ALL_ROOTS);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeleteRemoteServerInputs(@NotNull final String platform,
                                                              @Nullable final String remoteName) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(remoteName)) {
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, remoteName));
        }
        if (!platform.equalsIgnoreCase("windows")) {
            if (!platform.equalsIgnoreCase("unix"))
                exceptionMessages.add(String.format(EXCEPTION_INVALID_PLATFORM, platform));
        }
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
    private static List<String> addVerifyStatus(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {

        String inputStrip = input.replaceAll("\\s+","");
        if (isEmpty(input)) {
            System.out.print(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        else if (!inputStrip.toLowerCase().matches("enabled|disabled")) {
            System.out.print(String.format(EXCEPTION_INVALID_STATUS, input, inputName));
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

    @NotNull
    public static List<String> verifyChangeMonitorStatusInputs(@Nullable final String fullPathToMonitor,
                                                               @Nullable final String monitorId,
                                                               @Nullable final String status) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToMonitor) && StringUtils.isEmpty(monitorId)) {
            exceptionMessages.add(String.format(EXCEPTION_AT_LEAST_ONE_OF_INPUTS, FULL_PATH_TO_MONITOR + ", " + MONITOR_ID));
        }

        addVerifyStatus(exceptionMessages, status, STATUS);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeployTemplateInputs(@Nullable final String pathToTemplate,
                                                          @Nullable final String pathToTargetGroup,
                                                          @Nullable final String connectToServer,
                                                          @Nullable final String testRemotes) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (isEmpty(pathToTemplate))
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, PATH_TO_TEMPLATE));
        if (isEmpty(pathToTargetGroup))
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, PATH_TO_TARGET_GROUP));
        addVerifyBoolean(exceptionMessages, connectToServer, CONNECT_TO_SERVER);
        addVerifyBoolean(exceptionMessages, testRemotes, TEST_REMOTES);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyUpdateTemplateInputs(@Nullable final String fullPathToTemplate) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (isEmpty(fullPathToTemplate))
            exceptionMessages.add(String.format(EXCEPTION_NULL_EMPTY, FULL_PATH_TO_TEMPLATE));

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyRunMonitorInputs(@Nullable final String fullPathToMonitor,
                                                      @Nullable final String monitorId) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(fullPathToMonitor) && StringUtils.isEmpty(monitorId))
            exceptionMessages.add(String.format(EXCEPTION_AT_LEAST_ONE_OF_INPUTS, FULL_PATH_TO_MONITOR + ", " + MONITOR_ID));

        return exceptionMessages;
    }
}
