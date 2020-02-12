package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.entities.GetMailBaseInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class MessageStoreUtils {

    public static Store createMessageStore(GetMailBaseInput input) throws Exception {
        Properties props = new Properties();
        Authenticator auth = new SimpleAuthenticator(input.getUsername(), input.getPassword());
        Store store;
        if (input.isEnableTLS() || input.isEnableSSL()) {
            addSSLSettings(input.isTrustAllRoots(), input.getKeystore(), input.getKeystorePassword(),
                    StringUtils.EMPTY, StringUtils.EMPTY);
        }
        if (input.isEnableTLS()) {
            store = tryTLSOtherwiseTrySSL(auth, props, input);
        } else if (input.isEnableSSL()) {
            store = connectUsingSSL(props, auth, input);
        } else {
            props.put(PropNames.MAIL + input.getProtocol() + PropNames.HOST, input.getHostname());
            props.put(PropNames.MAIL + input.getProtocol() + PropNames.PORT, input.getPort());
            Session s = Session.getInstance(props, auth);
            store = s.getStore(input.getProtocol());
            store.connect();
        }
        return store;
    }


    public static Store connectUsingSSL(Properties props, Authenticator auth, GetMailBaseInput input) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth, input);
        store.connect();
        return store;
    }


    public static Store configureStoreWithSSL(Properties props, Authenticator auth, GetMailBaseInput input) throws NoSuchProviderException {
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.PORT, String.valueOf(input.getPort()));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), StringUtils.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }


    public static void addSSLSettings(boolean trustAllRoots, String keystore,
                               String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = System.getProperty(PropNames.FILE_SEPARATOR);
        String javaKeystore = System.getProperty(PropNames.JAVA_HOME) + separator + "lib" + separator + "security" + separator + "cacerts";

        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            if (null != keystorePassword) {
                if (StringUtils.EMPTY.equals(keystorePassword)) {
                    keystorePassword = SecurityConstants.DEFAULT_PASSWORD_FOR_STORE;
                }
            }
            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith(Constants.HTTP))
                    keystore = Constants.FILE + keystore;
                useClientCert = true;
            }
        }

        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            if (storeExists) {
                if (isEmpty(trustPassword)) {
                    trustPassword = SecurityConstants.DEFAULT_PASSWORD_FOR_STORE;
                }
            } else {
                trustPassword = null;
            }
            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(Constants.HTTP))
                    trustKeystore = Constants.FILE + trustKeystore;
                useTrustCert = true;
            }

        }

        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager()};
        }

        if (useTrustCert) {
            KeyStore trustKeyStore = SSLUtils.createKeyStore(new URL(trustKeystore), trustPassword);
            trustManagers = SSLUtils.createAuthTrustManagers(trustKeyStore);
        }
        if (useClientCert) {
            KeyStore clientKeyStore = SSLUtils.createKeyStore(new URL(keystore), keystorePassword);
            keyManagers = SSLUtils.createKeyManagers(clientKeyStore, keystorePassword);
        }

        SSLContext context = SSLContext.getInstance(SecurityConstants.SSL);
        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLContext.setDefault(context);
    }


    public static Store tryTLSOtherwiseTrySSL(Authenticator auth, Properties props, GetMailBaseInput input) throws MessagingException {
        Store store = configureStoreWithTLS(props, auth, input);
        try {
            store.connect(input.getHostname(), input.getUsername(), input.getPassword());
        } catch (Exception e) {
            if (input.isEnableSSL()) {
                clearTLSProperties(props, input);
                store = connectUsingSSL(props, auth, input);
            } else {
                throw e;
            }
        }
        return store;
    }


    public static Store configureStoreWithTLS(Properties props, Authenticator auth, GetMailBaseInput input) throws NoSuchProviderException {
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SSL_ENABLE, String.valueOf(false));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_ENABLE, String.valueOf(true));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_REQUIRED, String.valueOf(true));
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol() + SecurityConstants.SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }


    public static void clearTLSProperties(Properties props, GetMailBaseInput input) {
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.SSL_ENABLE);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_ENABLE);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_REQUIRED);
    }


    public static Store configureStoreWithoutSSL(Properties props, Authenticator auth, GetMailBaseInput input) throws NoSuchProviderException {
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.HOST, input.getHostname());
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.PORT, input.getPort());
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol());
    }
}
