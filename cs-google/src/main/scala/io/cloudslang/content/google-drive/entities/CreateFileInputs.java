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
        this.description=description;
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





}