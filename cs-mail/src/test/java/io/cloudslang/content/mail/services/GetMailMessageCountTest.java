package io.cloudslang.content.mail.services;

import io.cloudslang.content.mail.constants.OutputNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.constants.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Folder;
import javax.mail.Store;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class GetMailMessageCountTest {

    @Spy
    private GetMailMessageCountService serviceSpy = new GetMailMessageCountService();
    @Mock
    private Folder folderMock;
    @Mock
    private Store storeMock;
    private GetMailMessageCountInput.Builder inputBuilder;

    @Before
    public void setUp(){
        inputBuilder = new GetMailMessageCountInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username("username");
        inputBuilder.password("password");
        inputBuilder.folder("folder");
    }

    @Test
    public void execute() throws Exception {
        doReturn(3).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        doNothing().when(folderMock).open(Matchers.anyInt());
        doReturn(folderMock).when(storeMock).getFolder(Matchers.anyString());
        doNothing().when(storeMock).connect();
        doReturn(storeMock).when(serviceSpy).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class));
        doReturn(storeMock).when(serviceSpy).configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class));
        doNothing().when(serviceSpy).addSSLSettings(anyInt(), anyBoolean(), anyString(), anyString(), anyString(), anyString());


        inputBuilder.enableTLS(Constants.Strings.TRUE);
        Map<String, String> results = serviceSpy.execute(inputBuilder.build());

        assertEquals(results.get(OutputNames.RETURN_RESULT), "3");
    }

}
