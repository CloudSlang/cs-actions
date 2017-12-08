package io.cloudslang.content.utilities.util;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.EARLY_MAC_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_ARCHITECTURE_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_NAME_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.LINUX_OS_VERSION_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.QUOTES;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.UNIX_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.UNIX_OS_FAMILY_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_LINE_SEPARATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_ARCHITECTURE_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_NAME_KEY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.WINDOWS_OS_VERSION_KEY;
import static io.cloudslang.content.utils.Constants.OutputNames.SCRIPT_EXIT_CODE;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.indexOfIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.strip;
import static org.apache.commons.lang3.StringUtils.substring;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class OsDetectorUtils {
    private static final String MAC_OS_NAME_KEY = "ProductName:";
    private static final String MAC_OS_VERSION_KEY = "ProductVersion:";

    public String cropValue(String stdOut, String begin, String end) {
        if (contains(stdOut, begin)) {
            return trimToEmpty(substring(stdOut, indexOfIgnoreCase(stdOut, begin) + length(begin), indexOfIgnoreCase(stdOut, end, indexOfIgnoreCase(stdOut, begin) + length(begin))));
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
