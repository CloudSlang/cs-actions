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
package io.cloudslang.content.winrm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
    public static final String REQUIRED_INPUT_MSG = "Input '%s' is required, but was not found.";
    public static final String XML_AMP = "&amp;";
    public static final String XML_QUOTE = "&quot;";
    public static final String XML_LESS = "&lt;";
    public static final String XML_GREATER = "&gt;";
    public static final String XML_LF = "\n";
    public static final String XML_CR = "\r";
    public static final String XML_TAB = "\t";

    public StringUtils() {
    }

    public static boolean isNull(String s) {
        return s == null || s.isEmpty();
    }


    public static String resolveRequiredString(java.util.Map<String, String> inputs, String key) throws Exception {
        String inputValue = (String)inputs.get(key);
        if (isNull(inputValue)) {
            throw new Exception("Input '" + key + "' is required, but was not found.");
        } else {
            return inputValue;
        }
    }



    public static String resolveString(java.util.Map<String, String> inputs, String key) {
        return resolve((String)inputs.get(key));
    }


    public static boolean resolveRequiredBoolean(java.util.Map<String, String> inputs, String inputName) throws IllegalArgumentException {
        return resolveRequiredBoolean(inputName, (String)inputs.get(inputName));
    }

    public static boolean resolveRequiredBoolean(String name, String value) throws IllegalArgumentException {
        if (isNull(value)) {
            throw new IllegalArgumentException(name + " input is required.  Valid values: true, false");
        } else if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid value for " + name + " input.  Valid values: true, false");
        }
    }


    public static int resolveRequiredInteger(java.util.Map<String, String> inputs, String inputName) throws IllegalArgumentException {
        return resolveRequiredInteger(inputName, (String)inputs.get(inputName), true);
    }

    public static int resolveRequiredInteger(String name, String value, boolean positiveOnly) throws IllegalArgumentException {
        if (isNull(value)) {
            if (positiveOnly) {
                throw new IllegalArgumentException(name + " input is required.  Valid values are positive integers.");
            } else {
                throw new IllegalArgumentException(name + " input is required.  Valid values are positive or negative integers.");
            }
        } else {
            try {
                int retVal = Integer.parseInt(value);
                if (positiveOnly && retVal < 0) {
                    throw new IllegalArgumentException("Input '" + name + "' must be an positive integer.");
                } else {
                    return retVal;
                }
            } catch (NumberFormatException var4) {
                if (positiveOnly) {
                    throw new IllegalArgumentException("Invalid value for " + name + " input.  Valid values are positive integers.", var4);
                } else {
                    throw new IllegalArgumentException("Invalid value for " + name + " input.  Valid values are positive and negative integers.", var4);
                }
            }
        }
    }

    public static long resolveRequiredLong(java.util.Map<String, String> inputs, String inputName) throws IllegalArgumentException {
        return resolveRequiredLong(inputName, (String)inputs.get(inputName), true);
    }

    public static long resolveRequiredLong(String name, String value, boolean positiveOnly) throws IllegalArgumentException {
        if (isNull(value)) {
            if (positiveOnly) {
                throw new IllegalArgumentException(name + " input is required.  Valid values are positive integers.");
            } else {
                throw new IllegalArgumentException(name + " input is required.  Valid values are positive or negative integers.");
            }
        } else {
            try {
                long retVal = Long.parseLong(value);
                if (positiveOnly && retVal < 0L) {
                    throw new IllegalArgumentException("Input '" + name + "' must be an positive integer.");
                } else {
                    return retVal;
                }
            } catch (NumberFormatException var5) {
                if (positiveOnly) {
                    throw new IllegalArgumentException("Invalid value for " + name + " input.  Valid values are positive integers.", var5);
                } else {
                    throw new IllegalArgumentException("Invalid value for " + name + " input.  Valid values are positive and negative integers.", var5);
                }
            }
        }
    }


    public static Boolean resolveOptionalBoolean(java.util.Map<String, String> inputs, String inputName, Boolean def) throws IllegalArgumentException {
        return resolveOptionalBoolean(inputName, (String)inputs.get(inputName), def);
    }

    public static Boolean resolveOptionalBoolean(String name, String value, Boolean def) throws IllegalArgumentException {
        if (isNull(value)) {
            return def;
        } else if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            throw new IllegalArgumentException("Invalid value for input '" + name + "'.  Valid values: true, false");
        }
    }

    public static Integer resolveOptionalInteger(java.util.Map<String, String> inputs, String inputName, Integer def) throws IllegalArgumentException {
        return resolveOptionalInteger(inputName, (String)inputs.get(inputName), def, true);
    }

    public static Integer resolveOptionalInteger(String name, String value, Integer def, boolean positiveOnly) throws IllegalArgumentException {
        if (isNull(value)) {
            return def;
        } else {
            try {
                Integer retVal = Integer.parseInt(value);
                if (positiveOnly && retVal < 0) {
                    throw new IllegalArgumentException("Input '" + name + "' must be an positive integer.");
                } else {
                    return retVal;
                }
            } catch (NumberFormatException var5) {
                if (positiveOnly) {
                    throw new IllegalArgumentException("Input '" + name + "' must be positive integer.", var5);
                } else {
                    throw new IllegalArgumentException("Input '" + name + "' must be an integer.", var5);
                }
            }
        }
    }

    public static Long resolveOptionalLong(java.util.Map<String, String> inputs, String inputName, Long def) throws IllegalArgumentException {
        return resolveOptionalLong(inputName, (String)inputs.get(inputName), def, true);
    }

    public static Long resolveOptionalLong(String name, String value, Long def, boolean positiveOnly) throws IllegalArgumentException {
        if (isNull(value)) {
            return def;
        } else {
            try {
                Long retVal = Long.parseLong(value);
                if (positiveOnly && retVal < 0L) {
                    throw new IllegalArgumentException("Input '" + name + "' must be an positive long.");
                } else {
                    return retVal;
                }
            } catch (NumberFormatException var5) {
                if (positiveOnly) {
                    throw new IllegalArgumentException("Input '" + name + "' must be positive long.", var5);
                } else {
                    throw new IllegalArgumentException("Input '" + name + "' must be a long.", var5);
                }
            }
        }
    }

    public static boolean resolve(String s, boolean def) {
        return isNull(s) ? def : Boolean.parseBoolean(s);
    }

    public static String resolve(String s, String def) {
        return isNull(s) ? def : s;
    }

    public static String resolve(String s) {
        return isNull(s) ? "" : s;
    }



    public static String resolve(java.util.Map<String, String> ar, String inputName, String def) {
        String result = resolveString(ar, inputName);
        if (isNull(result)) {
            result = def;
        }

        return result;
    }

    public static String toString(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString().replace("\u0000", "");
    }

    public static String replaceInvalidXMLCharacters(String s) {
        List<String> list = new ArrayList();
        list.add("\u0000");

        String c;
        for(Iterator var2 = list.iterator(); var2.hasNext(); s = s.replace(c, "")) {
            c = (String)var2.next();
        }

        return s;
    }

    public static String processNullTerminatedString(String s) {
        String returnValue = s;
        char[] charArray = s.toCharArray();
        if (charArray.length == 1 && charArray[0] <= 0) {
            return "null";
        } else {
            if (charArray[charArray.length - 1] <= 0) {
                returnValue = s.substring(0, s.length() - 1);
            }

            return returnValue;
        }
    }

    public static String match(String toProcess, StringUtils.MatchProcessor proc, Pattern pattern) {
        StringBuilder buff = new StringBuilder();
        Matcher matcher = pattern.matcher(toProcess);

        int nextChar;
        int end;
        for(nextChar = 0; matcher.find(); nextChar = end) {
            int start = matcher.start();
            end = matcher.end();
            buff.append(proc.processUnmatchedPortion(toProcess, nextChar, start));
            buff.append(proc.processMatchedPortion(toProcess, start, end));
        }

        if (nextChar != toProcess.length()) {
            buff.append(proc.processUnmatchedPortion(toProcess, nextChar, toProcess.length()));
        }

        return buff.toString();
    }

    public static String toXML(String val) {
        if (val != null && !val.isEmpty()) {
            int length = val.length();
            StringBuilder buf = new StringBuilder();

            for(int i = 0; i < length; ++i) {
                int codepoint = val.codePointAt(i);
                switch(codepoint) {
                    case 9:
                        buf.append("\t");
                        break;
                    case 10:
                        buf.append("\n");
                        break;
                    case 13:
                        buf.append("\r");
                        break;
                    case 34:
                        buf.append("&quot;");
                        break;
                    case 38:
                        buf.append("&amp;");
                        break;
                    case 60:
                        buf.append("&lt;");
                        break;
                    case 62:
                        buf.append("&gt;");
                        break;
                    default:
                        if (codepoint >= 32) {
                            if (codepoint > 127) {
                                buf.append("&#x");
                                buf.append(Integer.toHexString(codepoint).toUpperCase());
                                buf.append(";");
                                if (Character.isSupplementaryCodePoint(codepoint)) {
                                    ++i;
                                }
                            } else {
                                buf.append((char)codepoint);
                            }
                        }
                }
            }

            return buf.toString();
        } else {
            return val;
        }
    }

    public interface MatchProcessor {
        String processMatchedPortion(String var1, int var2, int var3);

        String processUnmatchedPortion(String var1, int var2, int var3);
    }
}

