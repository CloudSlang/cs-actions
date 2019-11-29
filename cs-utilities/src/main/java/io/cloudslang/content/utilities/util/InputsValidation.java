/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.utilities.util;

import io.cloudslang.content.utilities.util.randomPasswordGenerator.SpecificPasswordFormat;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class InputsValidation {

    @NotNull
    public static List<String> verifyBase64DecoderToFileInputs(
            @Nullable final String filePath,
            @Nullable final String contentBytes) {

        return verifyBase64DecoderToFileBytesAndPath(filePath, contentBytes);
    }

    @NotNull
    private static List<String> verifyBase64DecoderToFileBytesAndPath(@Nullable final String filePath, @Nullable final String contentBytes) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(contentBytes)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_CONTENT_BYTES);
        }
        if (StringUtils.isEmpty(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_PATH);
        } else if (!isValidPath(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_VALID_PATH);
        }
        return exceptionMessages;
    }

    /**
     * This method verifies the inputs for the encode operation
     *
     * @param filePath - the absolute filepath to the file that need to be encoded
     * @return - returns an error message in case of not meeting the requirements
     */
    @NotNull
    public static List<String> verifyBase64EncoderInputs(@Nullable final String filePath) {
        final List<String> exceptionMessages = new ArrayList<>();

        if (StringUtils.isEmpty(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_EMPTY_PATH);
        } else if (!isValidPath(filePath)) {
            exceptionMessages.add(Constants.EXCEPTION_VALID_PATH);
        }
        return exceptionMessages;
    }

    /**
     * This method verifies if the path is valid and if the specified file exists
     *
     * @param path - the absolute filepath to the file
     * @return - returns a boolean response
     */
    private static boolean isValidPath(@NotNull final String path) {
        final File file = new File(path);
        return new File(FilenameUtils.getFullPathNoEndSeparator(file.getAbsolutePath())).exists();
    }

    @NotNull
    public static List<String> verifyGenerateRandomPasswordInputs(@NotNull final String passwordLength, @NotNull final String numberOfLowerCaseCharacters,
                                                                  @NotNull final String numberOfUpperCaseCharacters, @NotNull final String numberOfNumericalCharacters,
                                                                  @NotNull final String numberOfSpecialCharacters) throws Exception {
        final List<String> exceptionMessages = new ArrayList<>();
        try {
            if (!isValidLength(passwordLength) || !isValidPasswordLength(passwordLength, numberOfLowerCaseCharacters,
                    numberOfUpperCaseCharacters, numberOfNumericalCharacters, numberOfSpecialCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_PASSWORD_LENGTH);
            }
            if (!isValidLength(numberOfLowerCaseCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfUpperCaseCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfNumericalCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            if (!isValidLength(numberOfSpecialCharacters)) {
                exceptionMessages.add(Constants.EXCEPTION_LENGTH);
            }
            return exceptionMessages;
        }catch (Exception exception){
            throw new Exception(Constants.EXCEPTION_NUMBER_FORMAT);
        }
    }

    private static boolean isValidLength(@NotNull final String passwordLength) {
        int length = Integer.parseInt(passwordLength);
        return length > -1;
    }

    private static boolean isValidPasswordLength(@NotNull final String passwordLength, @NotNull final String numberOfLowerCaseCharacters,
                                                 @NotNull final String numberOfUpperCaseCharacters, @NotNull final String numberOfNumericalCharacters,
                                                 @NotNull final String numberOfSpecialCharacters) {
        return Integer.parseInt(passwordLength) >= Integer.parseInt(numberOfLowerCaseCharacters) + Integer.parseInt(numberOfUpperCaseCharacters) + Integer.parseInt(numberOfNumericalCharacters)
                + Integer.parseInt(numberOfSpecialCharacters);
    }

}

