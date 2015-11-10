package io.cloudslang.content.services;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.cifs.CifsConnectionBuilder;
import com.xebialabs.overthere.cifs.CifsConnectionType;
import com.xebialabs.overthere.cifs.WinrmHttpsCertificateTrustStrategy;
import com.xebialabs.overthere.cifs.WinrmHttpsHostnameVerificationStrategy;
import com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler;
import io.cloudslang.content.entities.PowerShellInputs;
import io.cloudslang.content.utils.Constants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.*;
import static com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler.capturingHandler;

/**
 * User: bancl
 * Date: 10/9/2015
 */
public class PowerShellScriptServiceImpl implements PowerShellScriptService {

    @Override
    public Map<String, String> execute(PowerShellInputs inputs) {

        OverthereConnection connection = getOverthereConnection(inputs);
        String encodeBase64 = getBase64EncodedScript(inputs.getScript());

        CapturingOverthereExecutionOutputHandler outHandler = capturingHandler();
        CapturingOverthereExecutionOutputHandler errHandler = capturingHandler();
        CmdLine cmdLine = CmdLine.build("powershell",
                "-NoProfile", "-NonInteractive", "-EncodedCommand", encodeBase64);
        connection.execute(outHandler, errHandler, cmdLine);

        return getReturnResult(outHandler, errHandler);
    }

    private Map<String, String> getReturnResult(CapturingOverthereExecutionOutputHandler outHandler,
                                                CapturingOverthereExecutionOutputHandler errHandler) {
        Map<String, String> returnResult = new HashMap<>();

        returnResult.put(Constants.OutputNames.RETURN_RESULT, outHandler.getOutput());
        returnResult.put(Constants.OutputNames.EXCEPTION, errHandler.getOutput());
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);

        return returnResult;
    }

    private String getBase64EncodedScript(String script) {
        byte[] utf8Bytes = script.getBytes(StandardCharsets.UTF_16LE);
        return Base64.encodeBase64String(utf8Bytes);
    }

    private OverthereConnection getOverthereConnection(PowerShellInputs inputs) {
        ConnectionOptions options = new ConnectionOptions();
        options.set(ADDRESS, inputs.getHost());
        options.set(USERNAME, inputs.getUsername());
        options.set(PASSWORD, inputs.getPassword());
        options.set(OPERATING_SYSTEM, WINDOWS);
        options.set(CONNECTION_TYPE, CifsConnectionType.valueOf(inputs.getConnectionType()));

        setOptionalInput(options, WINRM_CONTEXT, inputs.getWinrmContext());
        setOptionalInput(options, WINRM_ENABLE_HTTPS, inputs.getWinrmEnableHTTPS());
        setOptionalInput(options, WINRM_ENVELOP_SIZE, inputs.getWinrmEnvelopSize());
        setOptionalInput(options, WINRM_HTTPS_CERTIFICATE_TRUST_STRATEGY,
                inputs.getWinrmHttpsCertificateTrustStrategy());
        setOptionalInput(options, WINRM_HTTPS_HOSTNAME_VERIFICATION_STRATEGY,
                inputs.getWinrmHttpsHostnameVerificationStrategy());
        setOptionalInput(options, WINRM_KERBEROS_ADD_PORT_TO_SPN, inputs.getWinrmKerberosAddPortToSpn());
        setOptionalInput(options, WINRM_KERBEROS_TICKET_CACHE, inputs.getWinrmKerberosTicketCache());
        setOptionalInput(options, WINRM_KERBEROS_USE_HTTP_SPN, inputs.getWinrmKerberosUseHttpSpn());
        setOptionalInput(options, WINRM_LOCALE, inputs.getWinrmLocale());
        setOptionalInput(options, WINRM_TIMEMOUT, inputs.getWinrmTimeout());

        return Overthere.getConnection(CifsConnectionBuilder.CIFS_PROTOCOL, options);
    }

    private void setOptionalInput(ConnectionOptions options, String key, String value) {
        if (StringUtils.isNotBlank(value))
            switch (key) {
                case WINRM_ENVELOP_SIZE:
                    options.set(key, Integer.parseInt(value));
                    break;
                case WINRM_HTTPS_CERTIFICATE_TRUST_STRATEGY:
                    options.set(key, WinrmHttpsCertificateTrustStrategy.valueOf(value));
                    break;
                case WINRM_HTTPS_HOSTNAME_VERIFICATION_STRATEGY:
                    options.set(key, WinrmHttpsHostnameVerificationStrategy.valueOf(value));
                    break;
                case WINRM_ENABLE_HTTPS:
                case WINRM_KERBEROS_ADD_PORT_TO_SPN:
                case WINRM_KERBEROS_TICKET_CACHE:
                case WINRM_KERBEROS_USE_HTTP_SPN:
                    options.set(key, Boolean.parseBoolean(value));
                    break;
                default:
                    options.set(key, value);
            }
    }
}
