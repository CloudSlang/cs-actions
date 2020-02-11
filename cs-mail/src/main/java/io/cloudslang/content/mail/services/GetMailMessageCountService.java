package io.cloudslang.content.mail.services;

import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import io.cloudslang.content.mail.utils.Constants;

import javax.mail.*;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GetMailMessageCountService {

    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";

    private static final String SSLSOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

    protected GetMailMessageCountInput input;

    private Map<String,String> results = new HashMap<>();

    public Map<String, String> execute(GetMailMessageCountInput getMailMessageCountInput) {
        this.results = new HashMap<>();
        this.input = getMailMessageCountInput;
        try(Store store = createMessageStore()) {
            Folder folder = store.getFolder(input.getFolder());
            if(!folder.exists()){
                throw new Exception(Constants.ExceptionMsgs.THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
            }
            folder.open(Folder.READ_ONLY);
            results.put(Constants.Outputs.RETURN_RESULT, String.valueOf(folder.getMessageCount()));
        }catch (Throwable e) {
            e.printStackTrace();
            results.put(Constants.Outputs.EXCEPTION, e.toString());
            String resultMessage = e.getMessage();
            if (e.toString().contains("Unrecognized SSL message")) {
                resultMessage = "Unrecognized SSL message, plaintext connection?";
            }
            results.put(Constants.Outputs.RETURN_RESULT, resultMessage);
            results.put(Constants.Outputs.RETURN_CODE, Constants.ReturnCodes.FAILURE_RETURN_CODE);
        }

        return results;
    }

    protected Store createMessageStore() throws Exception {
        Properties props = new Properties();
        Authenticator auth = new SimpleAuthenticator(input.getUsername(), input.getPassword());
        Store store;
        if (input.isEnableTLS() || input.isEnableSSL()) {
            addSSLSettings(input.getPort(), input.isTrustAllRoots(), input.getKeystore(), input.getKeystorePassword(),
                    Constants.Strings.EMPTY, Constants.Strings.EMPTY);
        }
        if (input.isEnableTLS()) {
            store = tryTLSOtherwiseTrySSL(auth, props);
        } else if (input.isEnableSSL()) {
            store = connectUsingSSL(props, auth);
        } else {
            props.put(Constants.Props.MAIL + input.getProtocol() + Constants.Props.HOST, input.getHostname());
            props.put(Constants.Props.MAIL + input.getProtocol() + Constants.Props.PORT, input.getPort());
            Session s = Session.getInstance(props, auth);
            store = s.getStore(input.getProtocol());
            store.connect();
        }
        return store;
    }

    private Store connectUsingSSL(Properties props, Authenticator auth) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth);
        store.connect();
        return store;
    }


    protected Store configureStoreWithSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.SOCKET_FACTORY_CLASS, SSLSOCKET_FACTORY);
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.SOCKET_FACTORY_FALLBACK, Constants.Strings.FALSE);
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.PORT, String.valueOf(input.getPort()));
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), Constants.Strings.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }


    public void addSSLSettings(int port, boolean trustAllRoots, String keystore,
                               String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = System.getProperty(Constants.Props.FILE_SEPARATOR);
        String javaKeystore = System.getProperty(Constants.Props.JAVA_HOME) + separator + "lib" + separator + "security" + separator + "cacerts";
        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            keystorePassword = (storeExists) ? ((keystorePassword.equals(Constants.Strings.EMPTY)) ? "changeit" : keystorePassword) : null;

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
            trustPassword = (storeExists) ? ((trustPassword.equals(Constants.Strings.EMPTY)) ? "changeit" : trustPassword) : null;

            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(Constants.HTTP))
                    trustKeystore = Constants.FILE + trustKeystore;
                useTrustCert = true;
            }

        }

        SSLContext context = SSLContext.getInstance("SSL");
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
        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLContext.setDefault(context);
    }


    private Store tryTLSOtherwiseTrySSL(Authenticator auth, Properties props) throws MessagingException {
        Store store = configureStoreWithTLS(props, auth);
        try {
            store.connect(input.getHostname(), input.getUsername(), input.getPassword());
        } catch (Exception e) {
            if (input.isEnableSSL()) {
                clearTLSProperties(props);
                store = connectUsingSSL(props, auth);
            } else {
                throw e;
            }
        }
        return store;
    }


    protected Store configureStoreWithTLS(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.SSL_ENABLE, Constants.Strings.FALSE);
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.START_TLS_ENABLE, Constants.Strings.TRUE);
        props.setProperty(Constants.Props.MAIL + input.getProtocol() + Constants.Props.START_TLS_REQUIRED, Constants.Strings.TRUE);
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol() + SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }


    private void clearTLSProperties(Properties props) {
        props.remove(Constants.Props.MAIL + input.getProtocol() + Constants.Props.SSL_ENABLE);
        props.remove(Constants.Props.MAIL + input.getProtocol() + Constants.Props.START_TLS_ENABLE);
        props.remove(Constants.Props.MAIL + input.getProtocol() + Constants.Props.START_TLS_REQUIRED);
    }
}
