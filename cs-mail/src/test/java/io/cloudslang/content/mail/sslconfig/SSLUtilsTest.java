package io.cloudslang.content.mail.sslconfig;

import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.mockito.Mockito.*;

/**
 * Created by persdana on 11/7/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLUtils.class, KeyStore.class, KeyManagerFactory.class, TrustManagerFactory.class})
public class SSLUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private static final String NULL_URL_EXCEPTION_MESSAGE = "Keystore url may not be null";
    private static final String NULL_KEYSTORE_EXCEPTION_MESSAGE = "Keystore may not be null";
    private String password = "";

    @Mock
    private InputStream isMock;
    @Mock
    private KeyStore keystoreMock;
    @Mock
    private URL urlMock;
    @Mock
    private KeyManagerFactory keyManagerFactoryMock;
    @Mock
    private TrustManagerFactory trustManagerFactoryMock;

    @Test
    public void testCreateKeyStore() throws Exception {
        when(urlMock.openStream()).thenReturn(isMock);

        PowerMockito.mockStatic(KeyStore.class);
        PowerMockito.doReturn(keystoreMock).when(KeyStore.class, "getInstance", "jks");

//        Mockito.doNothing().when(isMock).close(); //error

        KeyStore result = SSLUtils.createKeyStore(urlMock, password);

        Assert.assertNull(result);
//        Mockito.verify(isMock).close();   //error
//        Mockito.verify(urlMock).openStream(); //error
        //Mockito.verify(keystoreMock).load(isMock, password != null ? password.toCharArray() : null); //cannot verify/stub final method
    }

    @Test
    public void testCreateKeyStoreWithNullURL() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        final URL url = null;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(NULL_URL_EXCEPTION_MESSAGE);

        SSLUtils.createKeyStore(url, password);
    }

    @Test
    public void testCreateKeyManagers() throws Exception {
        PowerMockito.mockStatic(KeyManagerFactory.class);
        PowerMockito.doReturn(keyManagerFactoryMock).when(KeyManagerFactory.class, "getInstance", anyString());


        KeyManager[] result = SSLUtils.createKeyManagers(keystoreMock, "");

        verify(keyManagerFactoryMock).init(keystoreMock, password != null ? password.toCharArray() : null);
        Assert.assertEquals(keyManagerFactoryMock.getKeyManagers(), result);
    }

    @Test
    public void testCreateKeyManagersWithNullKeyStore() throws Exception {
        final KeyStore keyStore = null;
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(NULL_KEYSTORE_EXCEPTION_MESSAGE);

        SSLUtils.createKeyManagers(keyStore, "");
    }

    @Test
    public void testCreateAuthTrustManagers() throws Exception {
        TrustManager[] trustManagers = new TrustManager[3];
        X509TrustManager tm = Mockito.mock(X509TrustManager.class);
        trustManagers[1] = tm;

        PowerMockito.mockStatic(TrustManagerFactory.class);
        PowerMockito.doReturn(trustManagerFactoryMock).when(TrustManagerFactory.class, "getInstance", anyObject());
        Mockito.doReturn(trustManagers).when(trustManagerFactoryMock).getTrustManagers();

        TrustManager[] result = SSLUtils.createAuthTrustManagers(keystoreMock);

        Assert.assertEquals(trustManagers, result);
        verify(trustManagerFactoryMock).init(keystoreMock);
        Assert.assertTrue(result[1] instanceof AuthSSLX509TrustManager);
    }
}
