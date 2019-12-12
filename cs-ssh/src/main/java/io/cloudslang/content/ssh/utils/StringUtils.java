/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.ssh.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 * @author octavian-h
 */
public class StringUtils {

    public static final String DEFAULT_DELIMITER = ",";
    public static final String DIGITS_REGEX = "((\\d)|,)*";

    public static boolean toBoolean(String value, boolean defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Boolean.valueOf(value);
    }

    public static int toInt(String value, int defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Integer.valueOf(value);
    }

    public static String toNotEmptyString(String value, String defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return value;
    }

    public static String toNewline(String value) {
        value = toNotEmptyString(value, Constants.DEFAULT_NEWLINE);
        char[] chars;
        if (!value.matches(DIGITS_REGEX)) {
            String[] split = value.split("\\\\");
            chars = new char[split.length - 1];
            for (int count = 0; count < split.length; count++)
                if (split[count].equalsIgnoreCase("n"))
                    chars[count - 1] = '\n';
                else if (split[count].equalsIgnoreCase("r"))
                    chars[count - 1] = '\r';
                else if (split[count].length() != 0) {
                    throw new RuntimeException("Unable to parse sequence of newline characters. Please specify this input as a comma delimited list of base-10 ASCII character codes.");
                }
        } else {
            String[] split = value.split(DEFAULT_DELIMITER);
            chars = new char[split.length];
            for (int count = 0; count < split.length; count++)
                chars[count] = (char) Integer.parseInt(split[count]);
        }
        return new String(chars);
    }

    public static Path toPath(String value, Path defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Paths.get(value);
    }

    public static String getStackTraceAsString(Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            final String stackTraceAsString = sw.toString();
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.close();
            return stackTraceAsString;
        } else {
            return Constants.EMPTY_STRING;
        }
    }

    /**
     * Checks if a given value represents a valid port number and returns an int value representing that port number otherwise throws an exception when an invalid port value is provided.
     * Valid port values: -1 and integer numbers greater than 0.
     * Although network specifications state that port values need to be 16-bit unsigned integers, the value '-1' is considered valid by some party components.
     *
     * @param portStringValue String value representing the port number;
     * @return int value representing a valid port number
     */
    public static int validatePortNumber(String portStringValue, String portInputName) {
        final int portNumber;
        final StringBuilder exceptionMessageBuilder = new StringBuilder();
        exceptionMessageBuilder.append("Invalid value '").append(portStringValue)
                .append("' for input '").append(portInputName)
                .append("'. Valid Values: -1 and integer values greater than 0. ");

        try {
            portNumber = Integer.parseInt(portStringValue);
            if ((portNumber < 1) && (portNumber != -1)) {
                throw new IllegalArgumentException(exceptionMessageBuilder.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(exceptionMessageBuilder.toString(), e);
        }
        return portNumber;
    }
}
