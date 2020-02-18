package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

public final class InputBuilderUtils {

    public static String buildHostname(String hostname) throws Exception {
        if (isEmpty(hostname)) {
            throw new Exception(ExceptionMsgs.HOST_NOT_SPECIFIED);
        }
        return hostname.trim();
    }


    public static Short buildPort(String port, boolean mandatory) throws Exception {
        if (StringUtils.isEmpty(port)) {
            if (!mandatory) {
                return null;
            } else {
                throw new Exception(ExceptionMsgs.PORT_NOT_SPECIFIED);
            }
        } else {
            Short p = Short.parseShort(port);
            if (p < 1) {
                throw new Exception(ExceptionMsgs.INVALID_PORT_NUMBER);
            }
            return p;
        }
    }


    public static Map<String, Object> buildPortAndProtocol(String protocol, String port) throws Exception {
        Map<String, Object> result = new HashMap<>();
        final String protocolKey = "protocol";
        final String portKey = "port";

        result.put(protocolKey, protocol);
        if (isEmpty(protocol) && isEmpty(port)) {
            throw new Exception(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
        } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP)) &&
                (!protocol.equalsIgnoreCase(PopPropNames.POP3)) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP4)) &&
                isEmpty(port)) {
            throw new Exception(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
        } else if (isEmpty(protocol) && !isEmpty(port) &&
                (!port.equalsIgnoreCase(ImapPropNames.PORT)) && (!port.equalsIgnoreCase(PopPropNames.POP3_PORT))) {
            throw new Exception(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
        } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(ImapPropNames.PORT)) {
            result.put(protocolKey, ImapPropNames.IMAP);
        } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(PopPropNames.POP3_PORT)) {
            result.put(protocolKey, PopPropNames.POP3);
        } else if (protocol.trim().equalsIgnoreCase(PopPropNames.POP3) && isEmpty(port)) {
            result.put(portKey, Short.parseShort(PopPropNames.POP3_PORT));
        } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP) && isEmpty(port)) {
            result.put(portKey, Short.parseShort(ImapPropNames.PORT));
        } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP4) && (isEmpty(port))) {
            result.put(portKey, Short.parseShort(ImapPropNames.PORT));
        }
        //The protocol should be given in lowercase to be recognised.
        result.put(protocolKey, result.get(protocolKey).toString().toLowerCase());
        if (result.get(protocolKey).toString().trim().equalsIgnoreCase(ImapPropNames.IMAP4)) {
            result.put(protocolKey, ImapPropNames.IMAP);
        }

        return result;
    }


    public static String buildUsername(String username, boolean mandatory) throws Exception {
        if (isEmpty(username) && mandatory) {
            throw new Exception(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
        }
        return username;
    }


    public static String buildPassword(String password) {
        return (password == null) ? StringUtils.EMPTY : password;
    }


    public static boolean buildTrustAllRoots(String trustAllRoots) {
        return StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);
    }


    public static boolean buildEnableTLS(String enableTLS) {
        try {
            return Boolean.parseBoolean(enableTLS);
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean buildEnableSSL(String enableSSL) {
        try {
            return Boolean.parseBoolean(enableSSL);
        } catch (Exception e) {
            return false;
        }
    }


    public static String buildKeystore(String keystore) {
        return defaultIfEmpty(keystore, SecurityConstants.DEFAULT_JAVA_KEYSTORE);
    }


    public static int buildTimeout(String timeout) throws Exception {
        if (isNotEmpty(timeout)) {
            Integer t = Integer.parseInt(timeout);
            if (t <= 0) {
                throw new Exception(ExceptionMsgs.TIMEOUT_MUST_BE_POSITIVE);
            }
            return Constants.ONE_SECOND; //timeouts in seconds
        }
        return -1;
    }


    public static Map<String, String> buildDecryptionKeystore(String decryptionKeystore, String decryptionKeyAlias,
                                                              String decryptionKeystorePassword) {
        final String decryptionKeystoreKey = "decryptionKeystore";
        final String decryptionKeyAliasKey = "decryptionKeyAlias";
        final String decryptionKeystorePasswordKey = "decryptionKeystorePassword";
        Map<String, String> result = new HashMap<>();

        result.put(decryptionKeystoreKey, decryptionKeystore);
        if (StringUtils.isNotEmpty(decryptionKeystore)) {
            if (!decryptionKeystore.startsWith(Constants.HTTP)) {
                result.put(decryptionKeystoreKey, Constants.FILE + decryptionKeystore);
            }

            result.put(decryptionKeyAliasKey, StringUtils.defaultString(decryptionKeyAlias));

            result.put(decryptionKeystorePasswordKey, StringUtils.defaultString(decryptionKeystorePassword));
        }

        return result;
    }


    public static List<String> buildTlsVersions(String tlsVersionInput) throws Exception {
        if (StringUtils.isEmpty(tlsVersionInput)) {
            return Collections.emptyList();
        }

        String[] tlsVersionsArray = tlsVersionInput.replaceAll("\\s+", StringUtils.EMPTY).split(",");
        List<String> tlsVersions = new ArrayList<>();
        for (String tlsVersion : tlsVersionsArray) {
            if (!TlsVersions.validate(tlsVersion)) {
                throw new IllegalArgumentException("Illegal value of input '" + InputNames.TLS_VERSION + "'");
            }
            tlsVersions.add(tlsVersion);
        }
        return tlsVersions;
    }


    public static List<String> buildAllowedCiphers(String allowedCiphersInput) throws Exception {
        if (StringUtils.isEmpty(allowedCiphersInput)) {
            return Collections.emptyList();
        }

        String[] cipherSuites = allowedCiphersInput.replaceAll("\\s+", StringUtils.EMPTY).split(",");
        List<String> allowedCiphers = Arrays.asList(cipherSuites);

        for(String cipher : allowedCiphers) {
            if(!CipherSuites.validate(cipher)) {
                throw new IllegalArgumentException("Illegal value of input '" + InputNames.ENCRYPTION_ALGORITHM + "'");
            }
        }

        return allowedCiphers;
    }
}