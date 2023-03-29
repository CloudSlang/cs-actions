/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Utils {
    public static String getQueryParamsString(Map<String, String> queryParams) {

        String result = EMPTY;

        for (String key : queryParams.keySet())
            if (!StringUtils.isEmpty(queryParams.get(key)))
                result += key + "=" + queryParams.get(key) + "&";

        return StringUtils.isEmpty(result) ? result : (result.substring(0, result.length() - 1));
    }

    public static String getFirstAvailableFileName(String path) {

        String newPath = path;
        int number = 2;

        while (new File(newPath).exists()) {
            newPath = FilenameUtils.getFullPath(path) + FilenameUtils.getBaseName(path) + " (" + number + ")." + FilenameUtils.getExtension(path);
            number++;
        }

        return newPath;
    }
}
