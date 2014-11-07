package com.hp.score.content.ssh.services.actions;

import junit.framework.TestCase;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


public class SSHShellAbstractTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddSecurityProvider() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {};
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.addSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertFalse(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
        //PowerMockito.verifyNew(BouncyCastleProvider.class).withNoArguments();
    }

    public void testRemoveSecurityProvider() throws Exception {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {};
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
    }

    public void testGetKeyFile() throws Exception {

    }

    public void testGetFromCache() throws Exception {

    }

    public void testGetFromCache1() throws Exception {

    }

    public void testSaveToCache() throws Exception {

    }

    public void testPopulateResult() throws Exception {

    }
}