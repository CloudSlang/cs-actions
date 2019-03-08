package io.cloudslang.content.tesseract.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_EMPTY_FILE;
import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_INVALID_BOOLEAN;
import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_INVALID_DATA_PATH;
import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_INVALID_FILE;
import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_INVALID_NUMBER;
import static io.cloudslang.content.tesseract.utils.Constants.EXCEPTION_NULL_EMPTY;
import static io.cloudslang.content.tesseract.utils.Inputs.DATA_PATH;
import static io.cloudslang.content.tesseract.utils.Inputs.DESKEW;
import static io.cloudslang.content.tesseract.utils.Inputs.DPI;
import static io.cloudslang.content.tesseract.utils.Inputs.FILE_PATH;
import static io.cloudslang.content.tesseract.utils.Inputs.TEXT_BLOCKS;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    @NotNull
    public static List<String> verifyExtractTextInputs(@Nullable final String filePath,
                                                       @Nullable final String dataPath,
                                                       @Nullable final String textBlocks,
                                                       @Nullable final String deskew) {

       return verifyCommonInputs(filePath, dataPath, textBlocks, deskew);
    }

    @NotNull
    public static List<String> verifyExtractTextFromPDF(@Nullable final String filePath,
                                                        @Nullable final String dataPath,
                                                        @Nullable final String dpi,
                                                        @Nullable final String textBlocks,
                                                        @Nullable final String deskew) {

        final List<String> exceptionMessages = verifyCommonInputs(filePath, dataPath, textBlocks, deskew);
        addVerifyNumber(exceptionMessages, dpi, DPI);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> verifyCommonInputs(@Nullable final String filePath,
                                                  @Nullable final String dataPath,
                                                  @Nullable final String textBlocks,
                                                  @Nullable final String deskew) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyBoolean(exceptionMessages, textBlocks, TEXT_BLOCKS);
        addVerifyBoolean(exceptionMessages, deskew, DESKEW);
        addVerifyFilePath(exceptionMessages, filePath, FILE_PATH);
        addVerifyDataPath(exceptionMessages, dataPath, DATA_PATH);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyFilePath(@NotNull List<String> exceptions, @Nullable final String filePath, @NotNull final String inputName) {

        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE);
        } else if (!isEmpty(filePath) && !isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_FILE, filePath, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyDataPath(@NotNull List<String> exceptions, @Nullable final String dataPath, @NotNull final String inputName) {

        if (!isEmpty(dataPath) && !isValidFile(dataPath))
            exceptions.add(String.format(EXCEPTION_INVALID_DATA_PATH, dataPath, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }
}
