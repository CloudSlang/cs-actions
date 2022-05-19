/*
 * (c) Copyright 2022 Micro Focus
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


package io.cloudslang.content.cyberark.utils;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CyberarkUtils {

    public static void removeUnusedHttpResults(Map<String, String> httpResults) {
        httpResults.remove("responseHeaders");
        httpResults.remove("reasonPhrase");
        httpResults.remove("finalLocation");
        httpResults.remove("protocolVersion");
    }

    public static String getQueryParamsString(Map<String, String> queryParams) {

        String result = EMPTY;

        for (String key : queryParams.keySet())
            if (!StringUtils.isEmpty(queryParams.get(key)))
                result += key + "=" + queryParams.get(key) + "&";

        return StringUtils.isEmpty(result) ? result : (result.substring(0, result.length() - 1));
    }

    public static void validateProtocol(String protocol) throws Exception {
        if (protocol.toLowerCase() != "http" && protocol.toLowerCase() != "https")
            throw new Exception("Invalid protocol: " + protocol);
    }
}
