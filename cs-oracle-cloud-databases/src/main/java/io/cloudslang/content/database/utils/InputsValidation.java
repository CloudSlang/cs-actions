package io.cloudslang.content.database.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.Inputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    public static HashMap<String, Integer> concurValues = new HashMap<String, Integer>() {{
        put(CONCUR_READ_ONLY, ResultSet.CONCUR_READ_ONLY);
        put(CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
    }};

    public static HashMap<String, Integer> typeValues = new HashMap<String, Integer>() {{
        put(TYPE_FORWARD_ONLY, ResultSet.TYPE_FORWARD_ONLY);
        put(TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_INSENSITIVE);
        put(TYPE_SCROLL_SENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE);
    }};


    @NotNull
    public static List<String> verifySqlCommand(@NotNull final String walletPath,
                                                @NotNull final String trustore,
                                                @NotNull final String keystore,
                                                @NotNull final String overwrite,
                                                @NotNull final String timeout) {

        final List<String> exceptionMessages = new ArrayList<>();


        addVerifyFile(exceptionMessages, walletPath, WALLET_PATH);
        addVerifyFile(exceptionMessages, trustore, TRUST_STORE);
        addVerifyFile(exceptionMessages, keystore, KEYSTORE);
        addVerifyBoolean(exceptionMessages, overwrite, OVERWRITE);
        addVerifyPositiveNumber(exceptionMessages, timeout, EXECUTION_TIMEOUT);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifySqlQuery(@NotNull final String walletPath,
                                              @NotNull final String trustore,
                                              @NotNull final String keystore,
                                              @NotNull final String overwrite,
                                              @NotNull final String timeout,
                                              @NotNull final String resultSetConcurrency,
                                              @NotNull final String resultSetType) {

        final List<String> exceptionMessages = new ArrayList<>();


        addVerifyFile(exceptionMessages, walletPath, WALLET_PATH);
        addVerifyFile(exceptionMessages, trustore, TRUST_STORE);
        addVerifyFile(exceptionMessages, keystore, KEYSTORE);
        addVerifyBoolean(exceptionMessages, overwrite, OVERWRITE);
        addVerifyPositiveNumber(exceptionMessages, timeout, EXECUTION_TIMEOUT);
        validateResultSetConcurrency(resultSetConcurrency, exceptionMessages);
        validateResultSetType(resultSetType, exceptionMessages);


        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyPositiveNumber(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {

        if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        } else if (Integer.parseInt(input) < 0) {
            exceptions.add(String.format(EXCEPTION_NEGATIVE_VALUE, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (!isEmpty(filePath) && !isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_FILE, filePath, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }

    private static void validateResultSetConcurrency(final String resultSetConcurrency, final List<String> validationList) {
        if (!isValidResultSetConcurrency(resultSetConcurrency)) {
            validationList.add(INVALID_RESULT_SET_CONCURRENCY);
        }
    }

    private static void validateResultSetType(final String resultSetType, final List<String> validationList) {
        if (!isValidResultSetType(resultSetType)) {
            validationList.add(INVALID_RESULT_SET_TYPE);
        }
    }

    public static boolean isValidResultSetConcurrency(final String resultSetConcurrency) {
        return concurValues.containsKey(resultSetConcurrency);
    }

    public static boolean isValidResultSetType(final String resultSetType) {
        return typeValues.containsKey(resultSetType);
    }
}
