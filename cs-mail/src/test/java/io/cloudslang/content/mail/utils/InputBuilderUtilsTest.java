package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.CipherSuites;
import io.cloudslang.content.mail.constants.TlsVersions;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({InputBuilderUtils.class})
public class InputBuilderUtilsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void buildPort_PortIsNullAndMandatory_Exception() throws Exception {
        final String port = null;
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNullAndNotMandatory_NullReturned() throws Exception {
        final String port = null;
        final boolean mandatory = false;

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertNull(result);
    }


    @Test
    public void buildPort_PortIsEmptyAndMandatory_Exception() throws Exception {
        final String port = StringUtils.EMPTY;
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsEmptyAndNotMandatory_NullReturned() throws Exception {
        final String port = StringUtils.EMPTY;
        final boolean mandatory = false;

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertNull(result);
    }


    @Test
    public void buildPort_PortIsInvalidAndMandatory_NumberFormatException() throws Exception {
        final String port = "asd";
        final boolean mandatory = true;
        exception.expect(NumberFormatException.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsInvalidAndNotMandatory_NumberFormatException() throws Exception {
        final String port = "asd";
        final boolean mandatory = false;
        exception.expect(NumberFormatException.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNotInRangeAndMandatory_Exception() throws Exception {
        final String port = "0";
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNotInRangeAndNotMandatory_Exception() throws Exception {
        final String port = "0";
        final boolean mandatory = false;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsValid_ValidResult() throws Exception {
        final String port = "1";
        final boolean mandatory = true;
        final Short expectedResult = Short.parseShort(port);

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertEquals(expectedResult, result);
    }


    @Test
    public void buildTlsVersions_TlsVersionIsNull_EmptyResult() throws Exception {
        final String tlsVersion = null;

        List<String> result = InputBuilderUtils.buildTlsVersions(tlsVersion);

        assertTrue(result.isEmpty());
    }


    @Test
    public void buildTlsVersions_TlsVersionIsEmpty_CodeDefault() throws Exception {
        final String tlsVersion = StringUtils.EMPTY;

        List<String> result = InputBuilderUtils.buildTlsVersions(tlsVersion);

        assertTrue(result.isEmpty());
    }


    @Test
    public void buildTlsVersions_IllegalTlsVersion_Exception() throws Exception {
        final String tlsVersion = TlsVersions.SSLv3 + ", asd";
        exception.expect(IllegalArgumentException.class);

        InputBuilderUtils.buildTlsVersions(tlsVersion);
    }


    @Test
    public void buildTlsVersions_TlsVersionIsValid_ValidResult() throws Exception {
        final String tlsVersion = TlsVersions.SSLv3 + ", \n\t" + TlsVersions.TLSv1_2;

        List<String> result = InputBuilderUtils.buildTlsVersions(tlsVersion);

        assertEquals(TlsVersions.SSLv3, result.get(0));
        assertEquals(TlsVersions.TLSv1_2, result.get(1));
    }


    @Test
    public void buildCipherSuites_CipherSuiteIsNull_EmptyList() {
        final String cipherSuite = null;

        List<String> result = InputBuilderUtils.buildAllowedCiphers(cipherSuite);

        assertTrue(result.isEmpty());
    }


    @Test
    public void buildCipherSuites_CipherSuiteIsEmpty_EmptyList() {
        final String cipherSuite = StringUtils.EMPTY;

        List<String> result = InputBuilderUtils.buildAllowedCiphers(cipherSuite);

        assertTrue(result.isEmpty());
    }


    @Test
    public void buildCipherSuites_CipherSuiteIsMicroFocusAccepted_ValidResult() {
        final String cipherSuite = CipherSuites.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 + ", \n\t" +
                CipherSuites.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256;

        List<String> result = InputBuilderUtils.buildAllowedCiphers(cipherSuite);

        assertEquals(CipherSuites.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, result.get(0));
        assertEquals(CipherSuites.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, result.get(1));
    }


    @Test
    public void buildCipherSuites_CipherSuiteContainsNonMicrofocusAccepted_ValidResult() {
        final String unknownCipher = "asd";
        final String cipherSuite = CipherSuites.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 + ", \n\t" + unknownCipher;

        List<String> result = InputBuilderUtils.buildAllowedCiphers(cipherSuite);

        assertEquals(CipherSuites.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, result.get(0));
        assertEquals(unknownCipher, result.get(1));
    }
}