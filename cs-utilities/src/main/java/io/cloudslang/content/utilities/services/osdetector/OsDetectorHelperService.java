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
/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.net.InetAddresses.isInetAddress;
import static com.google.common.net.InternetDomainName.isValid;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.ALLOWED_CHARACTERS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_NMAP_PATH;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DETECTION;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.EARLY_MAC_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.HOST;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.ILLEGAL_CHARACTERS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_ARCHITECTURE_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_NAME_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_VERSION_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_ARGUMENTS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_PATH;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_VALIDATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.PERMISSIVE_NMAP_VALIDATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.QUOTES;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.RESTRICTIVE_NMAP_VALIDATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.UNIX_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.UNIX_OS_FAMILY_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_ARCHITECTURE_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_NAME_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_VERSION_KEY;
import static io.cloudslang.content.utils.Constants.OutputNames.SCRIPT_EXIT_CODE;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.indexOfIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.strip;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.math.NumberUtils.max;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class OsDetectorHelperService {
    private static final String MAC_OS_NAME_KEY = "ProductName:";
    private static final String MAC_OS_VERSION_KEY = "ProductVersion:";

    public String formatOsCommandsOutput(Map<String, List<String>> commandsOutput) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : commandsOutput.entrySet()) {
            stringBuilder.append(entry.getKey()).append(DETECTION).append(lineSeparator());
            for (String cmdOutput : entry.getValue()) {
                stringBuilder.append(cmdOutput).append(lineSeparator());
            }
        }
        return stringBuilder.toString();
    }

    public void validateNmapInputs(OsDetectorInputs osDetectorInputs, NmapOsDetectorService nmapOsDetectorService) {
        String nmapPath = osDetectorInputs.getNmapPath();
        if (!StringUtils.equals(nmapPath, DEFAULT_NMAP_PATH) && !new File(nmapPath).isAbsolute()) {
            throw new IllegalArgumentException(format("The '%s' input must be an absolute path or the string '%s'.", NMAP_PATH, DEFAULT_NMAP_PATH));
        }

        String host = osDetectorInputs.getHost();
        if (!equalsIgnoreCase("localhost", host) && !isInetAddress(host) && !isValid(host)) {
            throw new IllegalArgumentException(format("The '%s' input must be an must be localhost or an internet domain name or an internet address.", HOST));
        }

        String nmapArguments = osDetectorInputs.getNmapArguments();
        String nmapValidator = osDetectorInputs.getNmapValidator();
        if (equalsIgnoreCase(nmapValidator, RESTRICTIVE_NMAP_VALIDATOR)) {
            if (!ALLOWED_CHARACTERS.matcher(nmapArguments).matches()) {
                throw new IllegalArgumentException(format("The '%s' input contains illegal characters. To perform a weaker validation set the value '%s' for the input '%s'.",
                        NMAP_ARGUMENTS, PERMISSIVE_NMAP_VALIDATOR, NMAP_VALIDATOR));
            }
        } else if (equalsIgnoreCase(nmapValidator, PERMISSIVE_NMAP_VALIDATOR)) {
            StringBuilder argumentViolation = new StringBuilder();
            for (char illegalCharacter : ILLEGAL_CHARACTERS) {
                if (contains(nmapArguments, illegalCharacter)) {
                    argumentViolation.append(illegalCharacter);
                }
            }
            if (argumentViolation.length() != 0) {
                throw new IllegalArgumentException(format("The '%s' input contains the following illegal characters: %s.",
                        NMAP_ARGUMENTS, argumentViolation.toString()));
            }
        } else {
            throw new IllegalArgumentException(format("The value provided for '%s' in invalid. Valid values are: %s, %s.",
                    NMAP_VALIDATOR, RESTRICTIVE_NMAP_VALIDATOR, PERMISSIVE_NMAP_VALIDATOR));
        }


        String proxyHost = osDetectorInputs.getProxyHost();
        String proxyPort = osDetectorInputs.getProxyPort();
        if (!contains(nmapArguments, "--proxies") && !contains(nmapArguments, "--proxy") && isNotEmpty(proxyHost)) {
            // just for early exit in case of invalid input
            nmapOsDetectorService.appendProxyArgument("", proxyHost, proxyPort);
        }
    }

    public String cropValue(String stdOut, String begin, String end) {
        if (containsIgnoreCase(stdOut, begin)) {
            return trimToEmpty(substring(stdOut, indexOfIgnoreCase(stdOut, begin) + length(begin), max(0, indexOfIgnoreCase(stdOut, end, indexOfIgnoreCase(stdOut, begin) + length(begin)))));
        } else {
            return "";
        }
    }

    public boolean foundOperatingSystem(OperatingSystemDetails os) {
        return isNotBlank(os.getFamily()) || isNotBlank(os.getName());
    }

    public OperatingSystemDetails processOutput(OperatingSystemDetails operatingSystemDetails, Map<String, String> execute, String detectOption) {
        String resultString  = execute.get(RETURN_RESULT);
        operatingSystemDetails.addCommandOutput(detectOption, singletonList(resultString));

        String lineSeparator = contains(resultString, WINDOWS_LINE_SEPARATOR) ? WINDOWS_LINE_SEPARATOR
                : contains(resultString, UNIX_LINE_SEPARATOR) ? UNIX_LINE_SEPARATOR : EARLY_MAC_LINE_SEPARATOR;

        if (isWindows(execute)) {
            operatingSystemDetails.setFamily(WINDOWS);
            operatingSystemDetails.setName(strip(cropValue(resultString, WINDOWS_OS_NAME_KEY, lineSeparator), QUOTES));
            operatingSystemDetails.setVersion(strip(cropValue(resultString, WINDOWS_OS_VERSION_KEY, lineSeparator), QUOTES));
            operatingSystemDetails.setArchitecture(strip(cropValue(resultString, WINDOWS_OS_ARCHITECTURE_KEY, lineSeparator), QUOTES));
            return operatingSystemDetails;
        } else if (isUnix(execute)){
            String osFamily = strip(cropValue(resultString, UNIX_OS_FAMILY_KEY, lineSeparator), QUOTES);

            operatingSystemDetails.setFamily(osFamily);
            operatingSystemDetails.setArchitecture(strip(cropValue(resultString, LINUX_OS_ARCHITECTURE_KEY, lineSeparator), QUOTES));
            if (equalsIgnoreCase(osFamily, "darwin")) {
                operatingSystemDetails.setName(strip(cropValue(resultString, MAC_OS_NAME_KEY, lineSeparator), QUOTES));
                operatingSystemDetails.setVersion(strip(cropValue(resultString, MAC_OS_VERSION_KEY, lineSeparator), QUOTES));
            } else {
                // linux processing
                operatingSystemDetails.setName(strip(cropValue(resultString, LINUX_OS_NAME_KEY, lineSeparator), QUOTES));
                operatingSystemDetails.setVersion(strip(cropValue(resultString, LINUX_OS_VERSION_KEY, lineSeparator), QUOTES));
            }
            return operatingSystemDetails;

        }
        return operatingSystemDetails;
    }

    public String resolveOsFamily(String osName) {
        if (isEmpty(osName)) {
            return "";
        }

        Map<String, String> osFamilyResolverMap = new HashMap<String, String>() {{
            put("windows", "Windows");
            put("microsoft", "Windows");

            put("linux", "Linux");
            put("ubuntu", "Linux");
            put("red hat", "Linux");
            put("redhat", "Linux");
            put("rhel", "Linux");
            put("debian", "Linux");
            put("suse", "Linux");
            put("android", "Linux");
            put("centos", "Linux");
            put("fedora", "Linux");
            put("gentoo", "Linux");
            put("ultrasparc", "Linux");
            put("ultra sparc", "Linux");

            put("darwin", "Darwin");
            put("mac", "Darwin");
            put("os x", "Darwin");

            put("sunos", "SunOS");
            put("solaris", "SunOS");
            put("openindiana", "SunOS");
            put("open indiana", "SunOS");
            put("smartos", "SunOS");
            put("smart os", "SunOS");
            put("omnios", "SunOS");
            put("omni os", "SunOS");

            put("dragonfly", "DragonFly");
            put("dragon fly", "DragonFly");

            put("freebsd", "FreeBSD");
            put("free bsd", "FreeBSD");

            put("hp-ux", "HP-UX");
        }};

        for (Map.Entry<String, String> osFamilyResolver : osFamilyResolverMap.entrySet()) {
            if (containsIgnoreCase(osName, osFamilyResolver.getKey())) {
                return osFamilyResolver.getValue();
            }
        }
        return "Other";
    }

    private boolean isUnix(Map<String, String> execute) {
        return (StringUtils.equals(SUCCESS, execute.get(RETURN_CODE)) || StringUtils.equals(SUCCESS, execute.get(SCRIPT_EXIT_CODE)))
                && containsIgnoreCase(execute.get(RETURN_RESULT), UNIX_OS_FAMILY_KEY);
    }

    private boolean isWindows(Map<String, String> execute) {
        return (StringUtils.equals(SUCCESS, execute.get(RETURN_CODE)) || StringUtils.equals(SUCCESS, execute.get(SCRIPT_EXIT_CODE)))
                && containsIgnoreCase(execute.get(RETURN_RESULT), WINDOWS);
    }
}
