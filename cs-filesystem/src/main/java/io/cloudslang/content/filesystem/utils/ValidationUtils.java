/*
 * Copyright 2020-2024 Open Text
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

package io.cloudslang.content.filesystem.utils;

import io.cloudslang.content.filesystem.constants.ExceptionMsgs;
import io.cloudslang.content.filesystem.constants.InputNames;
import io.cloudslang.content.filesystem.entities.RenameInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public final class ValidationUtils {

    public static void validate(@NotNull RenameInputs inputs) throws Exception {
        validateSource(inputs.getSource());
        validateNewName(inputs.getNewName());
    }


    private static void validateSource(Path source) throws Exception {
        if (source == null)
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, InputNames.SOURCE));
        File sourceFile = source.toFile();
        if (!sourceFile.isAbsolute())
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_ABSOLUTE_PATH);
        if (!sourceFile.getCanonicalPath().equals(sourceFile.getAbsolutePath()))
            throw new IllegalArgumentException(ExceptionMsgs.DIRECTORY_TRAVERSAL);
        if (!sourceFile.exists())
            throw new FileNotFoundException(String.format(ExceptionMsgs.DOES_NOT_EXIST, sourceFile.getAbsolutePath()));
    }


    private static void validateNewName(String newName) throws Exception {
        if (StringUtils.isBlank(newName))
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, InputNames.NEW_NAME));
        if (newName.contains("/") || newName.contains("\\"))
            throw new IllegalArgumentException(String.format(ExceptionMsgs.ILLEGAL_CHARACTERS, InputNames.NEW_NAME));
    }
}
