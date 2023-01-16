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


package io.cloudslang.content.office365.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Inputs.AddAttachment.CONTENT_BYTES;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class PopulateAttachmentBody {
    public static String populateAddAttachmentBody(@NotNull final String filePath,
                                                   @NotNull final String contentName,
                                                   @NotNull final String contentBytes) throws IOException {
        final JsonObject body = new JsonObject();

        body.addProperty(ODATA_TYPE, MICROSOFT_GRAPH_FILE_ATTACHMENT);

        if (isEmpty(contentName) || isEmpty(contentBytes)) {
            body.addProperty(NAME, getName(filePath));
            body.addProperty(CONTENT_BYTES, encodeBase64String(Files.readAllBytes(Paths.get(filePath))));
        } else {
            body.addProperty(NAME, contentName);
            body.addProperty(CONTENT_BYTES, contentBytes);
        }

        return new Gson().toJson(body);
    }

    public static String populateCreateUploadSessionBody(@NotNull final String filePath,
                                                         @NotNull final String fileName,
                                                         @NotNull final String fileType,
                                                         @NotNull long fileSize) throws IOException {
        final JsonObject body = new JsonObject();
        final JsonObject attachmentItem = new JsonObject();

        if (isEmpty(fileType))
            attachmentItem.addProperty(ATTACHMENT_TYPE, ATTACHMENT_TYPE_FILE);
        else
            attachmentItem.addProperty(ATTACHMENT_TYPE, fileType);

        if (isEmpty(fileName))
            attachmentItem.addProperty(ATTACHMENT_NAME, getName(filePath));
        else
            attachmentItem.addProperty(ATTACHMENT_NAME, fileName);

        attachmentItem.addProperty(ATTACHMENT_SIZE, fileSize);
        body.add(ATTACHMENT_ITEM, attachmentItem);

        return new Gson().toJson(body);
    }
}
