/*
 * Copyright 2024-2025 Open Text
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


package io.cloudslang.content.sharepoint.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

import static io.cloudslang.content.sharepoint.utils.Constants.*;
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

    public static StringBuilder buildPermissionsURL(String siteId, String driveId, String itemId) {

        StringBuilder urlBuilder = new StringBuilder(GRAPH_API_ENDPOINT);
        if (StringUtils.isEmpty(siteId) && StringUtils.isEmpty(driveId)) {
            urlBuilder.append(CURRENT_USER_ENDPOINT);
        } else {
            if (!StringUtils.isEmpty(siteId))
                urlBuilder.append(SITES_ENDPOINT).append(siteId);

            if (StringUtils.isEmpty(driveId))
                urlBuilder.append(DRIVE_ENDPOINT);
            else
                urlBuilder.append(DRIVES_ENDPOINT).append(driveId);
        }
        return urlBuilder.append(ITEMS_ENDPOINT).append(itemId);
    }

    //GET /drive/items/{item-id}/permissions
    //GET /drives/{drive-id}/items/{item-id}/permissions
    //GET /sites/{siteId}/drive/items/{itemId}/permissions
    //GET /sites/{siteId}/drives/{driveId}/items/{itemId}/permissions
    //GET /me/drive/items/{item-id}/permissions
    public static String buildListPermissionsURL(String siteId, String driveId, String itemId) {
        return buildPermissionsURL(siteId, driveId, itemId).append(PERMISSIONS_ENDPOINT).toString();
    }

    public static String buildAddPermissionsURL(String siteId, String driveId, String itemId) {
        return buildPermissionsURL(siteId, driveId, itemId).append(INVITE_ENDPOINT).toString();
    }

    public static String buildDeleteUpdatePermissionsURL(String siteId, String driveId, String itemId, String permId) {
        return buildPermissionsURL(siteId, driveId, itemId).append(PERMISSIONS_ENDPOINT).append(SLASH).append(permId).toString();
    }
    public static String buildMoveItemURL(String siteId, String driveId, String itemId) {
        return buildPermissionsURL(siteId, driveId, itemId).toString();
    }

    public static String buildCopyItemURL(String siteId, String driveId, String itemId) {
        return buildPermissionsURL(siteId, driveId, itemId).append(COPY_ENDPOINT).toString();
    }

    public static class HostException extends Exception {
        public HostException(String errorMessage) {
            super(errorMessage);
        }
    }
}
