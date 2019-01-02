/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.google-drive.entities.utils;

import io.cloudslang.content.constants.InputNames;

public final class Inputs extends InputNames {
    public static class CommonInputs {
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
    }

    public static class AuthorizationInputs {
        public static final String LOGIN_TYPE = "loginType";
        public static final String CLIENT_ID = "clientId";
        public static final String CLIENT_SECRET = "clientSecret";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGIN_AUTHORITY = "loginAuthority";
        public static final String RESOURCE = "resource";
    }

   public static class DeleteFileInputs {
       public static final String FILE_ID = "fileId";
       public static final String SUPPORTS_TEAM_DRIVES = "supportsTeamDrives";
   }

    public static class GetFileInputs {
        public static final String FILE_ID = "fileId";
        public static final String ACKNOWLEDGE_ABUSE = "acknowledgeAbuse";
        public static final String SUPPORTS_TEAM_DRIVES = "supportsTeamDrives";
    }

    public static class CreateFileInputs {
        public static final String FILE_ID = "fileId";
        public static final String UPLOAD_TYPE = "uploadType";
        public static final String IGNORE_DEFAULT_VISIILITY = "ignoreDefaultVisibility";
        public static final String KEEP_REVISION_FOREVER = "keepRevisionForever";
        public static final String OCR_LANGUAGE = "ocrLanguage";
        public static final String SUPPORTS_TEAM_DRIVES = "supportsTeamDrives";
        public static final String USE_CONTENT_AS_INDEXABLE_TEXT = "useContentAsIndexableText";
        public static final String APP_PROPERTIES = "appProperties";
        public static final String CONTENT_HINTS_INDEXABLE_TEXT = "contentHintsIndexableText";
        public static final String CONTENT_HINTS_THUMBNAIL_MIME_TYPE = "contentHintsThumbnailImage";
        public static final String CONTENT_HINTS_THUMBNAIL_MIME_TYPE = "contentHintsThumbnailMimeType";
        public static final String COPY_REQUIERS_WRITER_PERMISSION = "copyRequiresWriterPermission";
        public static final String CREATED_TIME = "createdTime";
        public static final String DESCRIPTION = "description";
        public static final String FOLDER_COLOR_RGB = "folderColorRgb";
        public static final String id = "id";
        public static final String MIME_TYPE = "mimeType";
        public static final String MODIFIED_TIME = "modifiedTime";
        public static final String NAME = "name";
        public static final String ORIGINAL_FILENAME = "originalFilename";
        public static final String PARENTS = "parents";
        public static final String PROPERTIES = "properties";
        public static final String STARRERD = "starred";
        public static final String VIEWED_BY_ME_TIME = "viewedByMeTime";
        public static final String WRITERS_CAN_SHARE = "writersCanShare";
    }
}

