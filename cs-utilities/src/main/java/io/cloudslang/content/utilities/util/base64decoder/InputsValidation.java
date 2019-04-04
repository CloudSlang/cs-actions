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
package io.cloudslang.content.utilities.util.base64decoder;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utilities.util.base64decoder.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {

    @NotNull
    public static List<String> verifyBase64DecoderInputs(
            @Nullable final String filePath,
            @Nullable final String contentBytes) {

        return verifyBase64DecoderBytesAndPath(filePath, contentBytes);
    }

    @NotNull
    private static List<String> verifyBase64DecoderBytesAndPath(@Nullable final String filePath, @Nullable final String contentBytes) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (isEmpty(contentBytes)) {
            exceptionMessages.add(EXCEPTION_EMPTY_CONTENT_BYTES);
        }
        if (isEmpty(filePath)) {
            exceptionMessages.add(EXCEPTION_EMPTY_PATH);
        } else if (!isValidPath(filePath)) {
            exceptionMessages.add(EXCEPTION_VALID_PATH);
        }
        return exceptionMessages;
    }

    private static boolean isValidPath(@NotNull final String path) {
        File file = new File(path);
        return new File(FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath())).exists();
    }

}

