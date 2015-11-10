package io.cloudslang.content.services;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler;
import io.cloudslang.content.entities.PowerShellActionInputs;
import io.cloudslang.content.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * User: bancl
 * Date: 11/9/2015
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PowerShellScriptServiceImpl.class, Overthere.class, CmdLine.class})
public class PowerShellScriptServiceImplTest {
    private PowerShellScriptServiceImpl powerShellScriptService;
    @Mock
    private PowerShellActionInputs powerShellActionInputs;
    @Mock
    private Map<String, String> result;
    @Mock
    private OverthereConnection overthereConnection;
    @Mock
    private CmdLine cmdLine;

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

        PowerMockito.when(powerShellActionInputs.getHost()).thenReturn("host");
        PowerMockito.when(powerShellActionInputs.getUsername()).thenReturn("username");
        PowerMockito.when(powerShellActionInputs.getPassword()).thenReturn("password");
        PowerMockito.when(powerShellActionInputs.getScript()).thenReturn("script");
        PowerMockito.when(powerShellActionInputs.getConnectionType()).thenReturn("WINRM_NATIVE");
    }

    @Test
    public void execute() {
        PowerMockito.when(powerShellActionInputs.getWinrmContext()).thenReturn("ctx");
        PowerMockito.when(powerShellActionInputs.getWinrmEnableHTTPS()).thenReturn("true");
        PowerMockito.when(powerShellActionInputs.getWinrmKerberosAddPortToSpn()).thenReturn("true");
        PowerMockito.when(powerShellActionInputs.getWinrmKerberosTicketCache()).thenReturn("true");
        PowerMockito.when(powerShellActionInputs.getWinrmKerberosUseHttpSpn()).thenReturn("true");
        PowerMockito.when(powerShellActionInputs.getWinrmLocale()).thenReturn("en-US");
        PowerMockito.when(powerShellActionInputs.getWinrmTimeout()).thenReturn("PT60.000S");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellActionInputs);
        assertEquals(Constants.ReturnCodes.RETURN_CODE_SUCCESS, result1.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void executeOptionalInputInteger() {
        PowerMockito.when(powerShellActionInputs.getWinrmEnvelopSize()).thenReturn("num");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellActionInputs);
        assertEquals(Constants.ReturnCodes.RETURN_CODE_FAILURE, result1.get(Constants.OutputNames.RETURN_CODE));
        assertEquals("For input string: \"num\"", result1.get(Constants.OutputNames.RETURN_RESULT));
        assertTrue(result1.get(Constants.OutputNames.EXCEPTION).contains(NumberFormatException.class.getName()));
    }

    @Test
    public void executeIncorrectConnectionType() {
        PowerMockito.when(powerShellActionInputs.getConnectionType()).thenReturn("2");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellActionInputs);
        assertEquals(Constants.ReturnCodes.RETURN_CODE_FAILURE, result1.get(Constants.OutputNames.RETURN_CODE));
        assertEquals("No enum constant com.xebialabs.overthere.cifs.CifsConnectionType.2",
                result1.get(Constants.OutputNames.RETURN_RESULT));
        assertTrue(result1.get(Constants.OutputNames.EXCEPTION).contains(IllegalArgumentException.class.getName()));
    }

    @Test
    public void executeOptionalInputWinrmHttpsCertificateTrustStrategy() {
        PowerMockito.when(powerShellActionInputs.getWinrmHttpsCertificateTrustStrategy()).thenReturn("2");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellActionInputs);
        assertEquals(Constants.ReturnCodes.RETURN_CODE_FAILURE, result1.get(Constants.OutputNames.RETURN_CODE));
        assertEquals("No enum constant com.xebialabs.overthere.cifs.WinrmHttpsCertificateTrustStrategy.2",
                result1.get(Constants.OutputNames.RETURN_RESULT));
        assertTrue(result1.get(Constants.OutputNames.EXCEPTION).contains(IllegalArgumentException.class.getName()));
    }

    @Test
    public void executeOptionalInputWinrmHttpsHostnameVerificationStrategy() {
        PowerMockito.when(powerShellActionInputs.getWinrmHttpsHostnameVerificationStrategy()).thenReturn("2");
        Map<String, String> result1 = powerShellScriptService.execute(powerShellActionInputs);
        assertEquals(Constants.ReturnCodes.RETURN_CODE_FAILURE, result1.get(Constants.OutputNames.RETURN_CODE));
        assertEquals("No enum constant com.xebialabs.overthere.cifs.WinrmHttpsHostnameVerificationStrategy.2",
                result1.get(Constants.OutputNames.RETURN_RESULT));
        assertTrue(result1.get(Constants.OutputNames.EXCEPTION).contains(IllegalArgumentException.class.getName()));
    }
}
