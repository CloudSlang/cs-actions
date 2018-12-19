package io.cloudslang.content.office365.services;

import io.cloudslang.content.office365.entities.GetMessageInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailServiceImpl.class)
public class EmailServiceImplTest {
    private final GetMessageInputs invalidGetMessageInputs = GetMessageInputs.builder()
            .messageId("")
            .folderId("")
            .oDataQuery("")
            .commonInputs(Office365CommonInputs.builder()
                    .authToken("")
                    .connectionsMaxPerRoute("")
                    .connectionsMaxTotal("")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .connectionsMaxTotal("")
                    .connectionsMaxPerRoute("")
                    .keepAlive("")
                    .responseCharacterSet("")
                    .connectTimeout("")
                    .trustAllRoots("")
                    .userId("")
                    .userPrincipalName("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .build())
            .build();

    @Test(expected = IllegalArgumentException.class)
    public void getMessageThrows() throws Exception {
        EmailServiceImpl.getMessage(invalidGetMessageInputs);
    }
}

