package io.cloudslang.content.httpclient.utils;

import io.cloudslang.content.httpclient.entities.*;
import org.apache.commons.lang3.*;

import java.util.*;

import static io.cloudslang.content.httpclient.utils.Constants.*;


public class HttpValidator {

    public static List<String> validateInputs(HttpClientInputs clientInputs) {

        List<String> exceptions = new ArrayList<>();

        validateHttpAuth(exceptions,clientInputs.getAuthType());

        try {
            if (Integer.parseInt(clientInputs.getExecutionTimeout()) < 0)
                exceptions.add(String.format(EXCEPTION_NEGATIVE_VALUE, clientInputs.getExecutionTimeout()));
        } catch (Exception e) {
            exceptions.add(String.format(EXCEPTION_INVALID_VALUE, clientInputs.getExecutionTimeout()));
        }

        try {
            if (Integer.parseInt(clientInputs.getResponseTimeout()) < 0)
                exceptions.add(String.format(EXCEPTION_NEGATIVE_VALUE, clientInputs.getResponseTimeout()));
        } catch (Exception e) {
            exceptions.add(String.format(EXCEPTION_INVALID_VALUE, clientInputs.getResponseTimeout()));
        }

        final int portNumber;
        final StringBuilder exceptionMessageBuilder = new StringBuilder();
        exceptionMessageBuilder.append("Invalid value '").append(clientInputs.getProxyPort())
                .append("' for input '").append(Inputs.HTTPInputs.PROXY_PORT)
                .append("'. Valid Values: -1 and integer values greater than 0. ");

        try {
            portNumber = Integer.parseInt(clientInputs.getProxyPort());
            if (portNumber <= 0) {
                throw new IllegalArgumentException(exceptionMessageBuilder.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(exceptionMessageBuilder.toString(), e);
        }

        return exceptions;
    }

    public static void validateHttpAuth(List<String> exceptions, String authType){
        Set<String> supportedAuthTypes = new HashSet<>();
        Set<String> authTypes = new HashSet();

        supportedAuthTypes.add(BASIC);
        supportedAuthTypes.add(DIGEST);
        supportedAuthTypes.add(NTLM);
        supportedAuthTypes.add(ANONYMOUS);

        if (authType.equalsIgnoreCase(ANY)) {
            authTypes.addAll(supportedAuthTypes);
        } else {
            String[] toks = authType.split(",");
            for (String tok : toks) {
                if (supportedAuthTypes.contains(tok.toUpperCase())) {
                    authTypes.add(tok);
                } else {
                    throw new IllegalArgumentException("unsupported authType in \"" + authType + "\"");
                }
            }
        }

    }
}
