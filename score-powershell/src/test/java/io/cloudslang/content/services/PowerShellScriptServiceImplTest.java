package io.cloudslang.content.services;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler;
import io.cloudslang.content.entities.PowerShellInputs;
import io.cloudslang.content.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * User: bancl
 * Date: 11/9/2015
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PowerShellScriptServiceImpl.class, Overthere.class, CmdLine.class, BufferedReader.class})
public class PowerShellScriptServiceImplTest {
    private PowerShellScriptServiceImpl powerShellScriptService;
    @Mock
    private PowerShellInputs powerShellInputs;
    @Mock
    private Map<String, String> result;
    @Mock
    private OverthereConnection overthereConnection;
    @Mock
    private CmdLine cmdLine;
    @Mock
    private BufferedReader bufferedReader;

    @Before
    public void setUp() throws Exception {
        powerShellScriptService = new PowerShellScriptServiceImpl();

        mockStatic(Overthere.class);
        mockStatic(CmdLine.class);
        PowerMockito.when(Overthere.getConnection(eq(CifsConnectionBuilder.CIFS_PROTOCOL),
                any(ConnectionOptions.class))).thenReturn(overthereConnection);
        PowerMockito.when(CmdLine.build(eq("powershell"), eq("-NoProfile"), eq("-NonInteractive"),
                eq("-EncodedCommand"), any(String.class))).thenReturn(cmdLine);
        PowerMockito.when(overthereConnection.execute(any(CapturingOverthereExecutionOutputHandler.class),
                any(CapturingOverthereExecutionOutputHandler.class), any(CmdLine.class))).thenReturn(0);

        PowerMockito.whenNew(BufferedReader.class)
                .withArguments(any(InputStreamReader.class)).thenReturn(bufferedReader);
        PowerMockito.when(bufferedReader.readLine()).thenReturn("first line")
                .thenReturn(" second line").thenReturn(null).thenReturn("first line exception")
                .thenReturn(" second line exception").thenReturn(null);

        PowerMockito.when(powerShellInputs.getHost()).thenReturn("host");
        PowerMockito.when(powerShellInputs.getUsername()).thenReturn("username");
        PowerMockito.when(powerShellInputs.getPassword()).thenReturn("password");
        PowerMockito.when(powerShellInputs.getScript()).thenReturn("script");
        PowerMockito.when(powerShellInputs.getConnectionType()).thenReturn("WINRM_NATIVE");
    }

    @Test
    public void execute() {
        PowerMockito.when(powerShellInputs.getWinrmContext()).thenReturn("ctx");
        PowerMockito.when(powerShellInputs.getWinrmEnableHTTPS()).thenReturn("true");
        PowerMockito.when(powerShellInputs.getWinrmKerberosAddPortToSpn()).thenReturn("true");
        PowerMockito.when(powerShellInputs.getWinrmKerberosTicketCache()).thenReturn("true");
        PowerMockito.when(powerShellInputs.getWinrmKerberosUseHttpSpn()).thenReturn("true");
        PowerMockito.when(powerShellInputs.getWinrmLocale()).thenReturn("en-US");
        PowerMockito.when(powerShellInputs.getWinrmTimeout()).thenReturn("PT60.000S");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellInputs);
        assertEquals("0", result1.get(Constants.OutputNames.RETURN_CODE));
        assertEquals("first line second line", result1.get(Constants.OutputNames.RETURN_RESULT));
        assertEquals("first line exception second line exception", result1.get(Constants.OutputNames.EXCEPTION));
    }

    @Test(expected = NumberFormatException.class)
    public void executeOptionalInputInteger() {
        PowerMockito.when(powerShellInputs.getWinrmEnvelopSize()).thenReturn("num");
        powerShellScriptService.execute(powerShellInputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeOptionalInputWinrmHttpsCertificateTrustStrategy() {
        PowerMockito.when(powerShellInputs.getWinrmHttpsCertificateTrustStrategy()).thenReturn("2");
        powerShellScriptService.execute(powerShellInputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeOptionalInputWinrmHttpsHostnameVerificationStrategy() {
        PowerMockito.when(powerShellInputs.getWinrmHttpsHostnameVerificationStrategy()).thenReturn("2");
        powerShellScriptService.execute(powerShellInputs);
    }
}
