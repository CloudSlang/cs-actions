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
package io.cloudslang.content.google-drive.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CreateFileInputs {
    private final String uploadType;
    private final String ignoreDefaultVisibility;
    private final String keepRevisionForever;
    private final String ocrLanguage;
    private final String supportsTeamDrives;
    private final String useContentAsIndexableText;
    private final String appProperties;
    private final String contentHintsIndexableText;
    private final String contentHintsThumbnailImage;
    private final String contentHintsThumbnailMimeType;
    private final String copyRequiresWriterPermission;
    private final String createdTime;
    private final String description;
    private final String folderColorRgb;
    private final String id;
    private final String mimeType;
    private final String modifiedTime;
    private final String name;
    private final String originalFilename;
    private final String parents;
    private final String properties;
    private final String starred;
    private final String viewedByMeTime;
    private final String writersCanShare;

    private final GoogleDriveCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"uploadType", "ignoreDefaultVisibility", "keepRevisionForever", "ocrLanguage", "supportsTeamDrives",
             "useContentAsIndexableText", "appProperties", "contentHintsIndexableText", "contentHintsThumbnailImage", "contentHintsThumbnailMimeType",
              "copyRequiresWriterPermission", "createdTime", "description", "folderColorRgb", "id", "mimeType", "modifiedTime", "name",
              "originalFilename", "parents", "properties", "starred", "viewedByMeTime", "writersCanShare"})

    public CreateFileInputs (String uploadType, String ignoreDefaultVisibility, String keepRevisionForever, String ocrLanguage, String supportsTeamDrives,
                             String useContentAsIndexableText, String appProperties, String contentHintsIndexableText, String contentHintsThumbnailImage,
                             String contentHintsThumbnailMimeType, String copyRequiresWriterPermission, String createdTime, String description,
                             String folderColorRgb, String id, String mimeType, String modifiedTime, String name, String originalFilename,
                             String parents, String properties, String starred, String viewedByMeTime, String writersCanShare){
        this.uploadType= uploadType;
        this.ignoreDefaultVisibility= ignoreDefaultVisibility;
        this.keepRevisionForever=keepRevisionForever;
        this.ocrLanguage= ocrLanguage;
        this.supportsTeamDrives= supportsTeamDrives;
        this.useContentAsIndexableText= useContentAsIndexableText;
        this.appProperties= appProperties;
        this.contentHintsIndexableText=contentHintsIndexableText;
        this.contentHintsThumbnailImage= contentHintsThumbnailImage;
        this.contentHintsThumbnailMimeType= contentHintsThumbnailMimeType;
        this.copyRequiresWriterPermission= copyRequiresWriterPermission;
        this.createdTime= createdTime;
        this.description= description;
        this.folderColorRgb= folderColorRgb;
        this.id= id;
        this.mimeType= mimeType;
        this.modifiedTime= modifiedTime;
        this.name= name;
        this.originalFilename= originalFilename;
        this.parents= parents;
        this.properties= properties;
        this.starred= starred;
        this.viewedByMeTime= viewedByMeTime;
        this.writersCanShare= writersCanShare;
    }

    @NotNull
    public static CreateFileInputsBuilder builder() {return new CreateFileInputsBuilder();}

    @NotNull
    public String uploadType {return uploadType;}

    @NotNull
    public String ignoreDefaultVisibility {return ignoreDefaultVisibility;}

    @NotNull
    public String keepRevisionForever {return keepRevisionForever;}

    @NotNull
    public String ocrLanguage {return ocrLanguage;}

    @NotNull
    public String supportsTeamDrives {return supportsTeamDrives;}

    @NotNull
    public String useContentAsIndexableText {return useContentAsIndexableText;}

    @NotNull
    public String appProperties {return appProperties;}

    @NotNull
    public String contentHintsIndexableText {return contentHintsIndexableText;}

    @NotNull
    public String contentHintsThumbnailImage {return contentHintsThumbnailImage;}

    @NotNull
    public String contentHintsThumbnailMimeType {return contentHintsThumbnailMimeType;}

    @NotNull
    public String copyRequiresWriterPermission {return copyRequiresWriterPermission;}

    @NotNull
    public String createdTime {return createdTime;}

    @NotNull
    public String description {return description;}

    @NotNull
    public String folderColorRgb {return folderColorRgb;}

    @NotNull
    public String id {return id;}

    @NotNull
    public String mimeType {return mimeType;}

    @NotNull
    public String ModifiedTime {return ModifiedTime;}

    @NotNull
    public String originalFilename {return originalFilename;}

    @NotNull
    public String name {return name;}

    @NotNull
    public String parents {return parents;}

    @NotNull
    public String properties {return properties;}

    @NotNull
    public String starred {return starred;}

    @NotNull
    public String viewedByMeTime {return viewedByMeTime;}

    @NotNull
    public String writersCanShare {return writersCanShare;}

    @NotNull
    public GoogleDriveCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class CreateFileInputsBuilder {
        private String uploadType = EMPTY;
        private String ignoreDefaultVisibility = EMPTY;
        private String keepRevisionForever = EMPTY;
        private String ocrLanguage = EMPTY;
        private String supportsTeamDrives = EMPTY;
        private String useContentAsIndexableText = EMPTY;
        private String appProperties = EMPTY;
        private String contentHintsIndexableText = EMPTY;
        private String contentHintsThumbnailImage = EMPTY;
        private String contentHintsThumbnailMimeType = EMPTY;
        private String copyRequiresWriterPermission = EMPTY;
        private String createdTime = EMPTY;
        private String description = EMPTY;
        private String folderColorRgb = EMPTY;
        private String id = EMPTY;
        private String mimeType = EMPTY;
        private String modifiedTime = EMPTY;
        private String name = EMPTY;
        private String originalFilename = EMPTY;
        private String parents = EMPTY;
        private String properties = EMPTY;
        private String starred = EMPTY;
        private String viewedByMeTime = EMPTY;
        private String writersCanShare = EMPTY;
        private GoogleDriveCommonInputs commonInputs;
        CreateFileInputsBuilder(){
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder uploadType(@NotNull final String uploadType) {
            this.uploadType = uploadType;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder ignoreDefaultVisibility(@NotNull final String ignoreDefaultVisibility) {
            this.ignoreDefaultVisibility = ignoreDefaultVisibility;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder keepRevisionForever(@NotNull final String keepRevisionForever) {
            this.keepRevisionForever = keepRevisionForever;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder ocrLanguage(@NotNull final String ocrLanguage) {
            this.ocrLanguage = ocrLanguage;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder supportsTeamDrives(@NotNull final String supportsTeamDrives) {
            this.supportsTeamDrives = supportsTeamDrives;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder useContentAsIndexableText(@NotNull final String useContentAsIndexableText) {
            this.useContentAsIndexableText = useContentAsIndexableText;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder appProperties(@NotNull final String appProperties) {
            this.appProperties = appProperties;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder contentHintsIndexableText(@NotNull final String contentHintsIndexableText) {
            this.contentHintsIndexableText = contentHintsIndexableText;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder contentHintsThumbnailImage(@NotNull final String contentHintsThumbnailImage) {
            this.contentHintsThumbnailImage = contentHintsThumbnailImage;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder contentHintsThumbnailMimeType(@NotNull final String contentHintsThumbnailMimeType) {
            this.contentHintsThumbnailMimeType = contentHintsThumbnailMimeType;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder copyRequiresWriterPermission(@NotNull final String copyRequiresWriterPermission) {
            this.copyRequiresWriterPermission = copyRequiresWriterPermission;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder createdTime(@NotNull final String createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder folderColorRgb(@NotNull final String folderColorRgb) {
            this.folderColorRgb = folderColorRgb;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder id(@NotNull final String id) {
            this.id = id;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder mimeType(@NotNull final String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder modifiedTime(@NotNull final String modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder name(@NotNull final String name) {
            this.name = name;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder originalFilename(@NotNull final String originalFilename) {
            this.originalFilename = originalFilename;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder parents(@NotNull final String parents) {
            this.parents = parents;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder properties(@NotNull final String properties) {
            this.properties = properties;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder starred(@NotNull final String starred) {
            this.starred = starred;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder viewedByMeTime(@NotNull final String viewedByMeTime) {
            this.viewedByMeTime = viewedByMeTime;
            return this;
        }

        public CreateFileInputs.CreateFileInputsBuilder commonInputs(@NotNull final GoogleDriveCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public CreateFileInputs.CreateFileInputsBuilder writersCanShare(@NotNull final String writersCanShare) {
            this.writersCanShare = writersCanShare;
            return this;
        }
    }
    public CreateFileInputs build() {
        return new CreateFileInputs(uploadType, ignoreDefaultVisibility, keepRevisionForever, ocrLanguage, supportsTeamDrives,
                useContentAsIndexableText, appProperties, contentHintsIndexableText, contentHintsThumbnailImage,
                contentHintsThumbnailMimeType, copyRequiresWriterPermission, createdTime, description, folderColorRgb,
                id, mimeType, modifiedTime, name, originalFilename, parents, properties, starred, viewedByMeTime, writersCanShare);
    }
}