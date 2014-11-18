package org.eclipse.score.content.ssh.services.actions;

import org.eclipse.score.content.ssh.entities.KeyFile;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class SSHShellAbstractTest {

    @Test
    public void testAddSecurityProvider() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.addSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertFalse(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
    }

    @Test
    public void testRemoveSecurityProvider() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
    }

    @Test
    public void testGetKeyFile() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        final String myPrivateKeyFileName = "myPrivateKeyFile";
        final String myPrivateKeyPassPhraseName = "myPrivateKeyPassPhrase";
        KeyFile keyFile = sshShellAbstract.getKeyFile(myPrivateKeyFileName, myPrivateKeyPassPhraseName);
        assertEquals(myPrivateKeyFileName, keyFile.getKeyFilePath());
        assertEquals(myPrivateKeyPassPhraseName, keyFile.getPassPhrase());

        keyFile = sshShellAbstract.getKeyFile(myPrivateKeyFileName, null);
        assertEquals(myPrivateKeyFileName, keyFile.getKeyFilePath());
        assertEquals(null, keyFile.getPassPhrase());

        keyFile = sshShellAbstract.getKeyFile(null, myPrivateKeyPassPhraseName);
        assertNull(keyFile);
    }

    @Test
    public void testGetFromCache() throws Exception {

    }

    @Test
    public void testGetFromCache1() throws Exception {

    }

    @Test
    public void testSaveToCache() throws Exception {

    }

    @Test
    public void testPopulateResult() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {};
        Map<String, String> returnResult = new HashMap<>();
        final String testExceptionMessage = "Test exception";
        Exception testException = new Exception(testExceptionMessage);
        sshShellAbstract.populateResult(returnResult, testException);

        assertEquals(returnResult.get("returnResult"), testExceptionMessage);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        testException.printStackTrace(pw);
        pw.close();

        assertEquals(returnResult.get("exception"), sw.toString());
        assertEquals(returnResult.get("returnCode"), "-1");
    }
}