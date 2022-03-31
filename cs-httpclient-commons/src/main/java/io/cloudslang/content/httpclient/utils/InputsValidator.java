package io.cloudslang.content.httpclient.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.utils.Constants.*;
import static io.cloudslang.content.httpclient.utils.Inputs.HTTPInputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;


public class InputsValidator {

    private static List<String> authTypes = new ArrayList<String>() {{
        add(BASIC);
        add(NTLM);
        add(ANONYMOUS);
        add(DIGEST);
    }};

    private static List<String> x509HostnameVerifiers = new ArrayList<String>() {{
        add(STRICT);
        add(ALLOW_ALL);
    }};

    private static List<String> tlsVersions = new ArrayList<String>() {{
        add(TLSv12);
        add(TLSv13);
    }};

    @NotNull
    public static List<String> verifyHttpCommonInputs(@NotNull final String authType,
                                                      @NotNull final String preemptiveAuth,

                                                      @NotNull final String proxyPort,

                                                      @NotNull final String tlsVersion,
                                                      @NotNull final String trustAllRoots,
                                                      @NotNull final String x509HostnameVerifier,

                                                      @NotNull final String useCookies,
                                                      @NotNull final String keepAlive,
                                                      @NotNull final String connectionsMaxPerRoute,
                                                      @NotNull final String connectionsMaxTotal,
                                                      @NotNull final String followRedirects,

                                                      @NotNull final String connectTimeout,
                                                      @NotNull final String responseTimeout,
                                                      @NotNull final String executionTimeout) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyAuthType(exceptionMessages, authType, AUTH_TYPE);
        addVerifyBoolean(exceptionMessages, preemptiveAuth, PREEMPTIVE_AUTH);
        addVerifyPort(exceptionMessages, proxyPort, PROXY_PORT);

        addVerifyTlsVersion(exceptionMessages, tlsVersion, TLS_VERSION);
        addVerifyBoolean(exceptionMessages, trustAllRoots, TRUST_ALL_ROOTS);
        addVerifyx509HostnameVerifier(exceptionMessages, x509HostnameVerifier, X509_HOSTNAME_VERIFIER);
//        addVerifyFile(exceptionMessages, trustKeystore, TRUST_KEYSTORE);
//        addVerifyFile(exceptionMessages, keystore, KEYSTORE);

        addVerifyBoolean(exceptionMessages, useCookies, USE_COOKIES);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyBoolean(exceptionMessages, followRedirects, FOLLOW_REDIRECTS);
        addVerifyPositiveNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyPositiveNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        addVerifyIntegerNumber(exceptionMessages, executionTimeout, EXECUTION_TIMEOUT);
        addVerifyIntegerNumber(exceptionMessages, responseTimeout, RESPONSE_TIMEOUT);
        addVerifyIntegerNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyQueryParams(@NotNull final String queryParamsAreURLEncoded,
                                                 @NotNull final String queryParamsAreFormEncoded) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyBoolean(exceptionMessages, queryParamsAreURLEncoded, QUERY_PARAMS_ARE_URLENCODED);
        addVerifyBoolean(exceptionMessages, queryParamsAreFormEncoded, QUERY_PARAMS_ARE_FORM_ENCODED);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyPositiveNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        try {
            if (isEmpty(input))
                exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
            else if (Integer.parseInt(input) < 1)
                exceptions.add(String.format(EXCEPTION_NOT_POSITIVE_VALUE, input, inputName));
        } catch (Exception e) {
            exceptions.add(String.format(EXCEPTION_INVALID_VALUE, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyIntegerNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        try {
            if (isEmpty(input))
                exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
            else if (Integer.parseInt(input) < 0)
                exceptions.add(String.format(EXCEPTION_NEGATIVE_VALUE, input, inputName));
        } catch (Exception e) {
            exceptions.add(String.format(EXCEPTION_INVALID_VALUE, input, inputName));
        }
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

    @NotNull
    private static List<String> addVerifyAuthType(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (authTypes.stream().noneMatch(input::equalsIgnoreCase))
            exceptions.add(String.format(EXCEPTION_INVALID_AUTH_TYPE, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyx509HostnameVerifier(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (x509HostnameVerifiers.stream().noneMatch(input::equalsIgnoreCase))
            exceptions.add(String.format(EXCEPTION_INVALID_HOSTNAME_VERIFIER, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyPort(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PORT, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyTlsVersion(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (tlsVersions.stream().noneMatch(input::equalsIgnoreCase))
            exceptions.add(String.format(EXCEPTION_INVALID_TLS_VERSION, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (!isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PATH, filePath, inputName));
        }
        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }
}
