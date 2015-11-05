package io.cloudslang.content.services;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler;
import io.cloudslang.content.entities.PowerShellInputs;
//import io.cloudslang.content.utilities.utils.StringUtils;
import io.cloudslang.content.utils.Constants;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.xebialabs.overthere.ConnectionOptions.*;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.cifs.CifsConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.cifs.CifsConnectionType.WINRM_NATIVE;
import static com.xebialabs.overthere.util.CapturingOverthereExecutionOutputHandler.capturingHandler;

/**
 * User: bancl
 * Date: 10/9/2015
 */
public class PowerShellScriptServiceImpl implements PowerShellScriptService {

    @Override
    public Map<String, String> execute(PowerShellInputs inputs) {

        OverthereConnection connection = getOverthereConnection(inputs);
        String encodeBase64 = getBase64EncodedScript(inputs);

        CapturingOverthereExecutionOutputHandler outHandler;
        CapturingOverthereExecutionOutputHandler errHandler;
        connection.execute(outHandler = capturingHandler(), errHandler = capturingHandler(), CmdLine.build("powershell",
                "-NoProfile", "-NonInteractive", "-EncodedCommand", encodeBase64));

        return getReturnResult(outHandler, errHandler);
    }

    private Map<String, String> getReturnResult(CapturingOverthereExecutionOutputHandler outHandler,
                                                CapturingOverthereExecutionOutputHandler errHandler) {
        Map<String, String> returnResult = new HashMap<>();

        String outString = "";
        String errString = "";
        try {
            outString = readInputStream(new ByteArrayInputStream(outHandler.getOutput().getBytes(StandardCharsets.UTF_8)));
            errString = readInputStream(new ByteArrayInputStream(errHandler.getOutput().getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
//            returnResult.put(Constants.OutputNames.EXCEPTION, StringUtils.toString(e));
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        returnResult.put(Constants.OutputNames.RETURN_RESULT, outString);
        returnResult.put(Constants.OutputNames.EXCEPTION, errString);
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        return returnResult;
    }

    private String getBase64EncodedScript(PowerShellInputs inputs) {
        byte[] utf8Bytes = inputs.getScript().getBytes(StandardCharsets.UTF_16LE);
        return Base64.encodeBase64String(utf8Bytes);
    }

    private OverthereConnection getOverthereConnection(PowerShellInputs inputs) {
        ConnectionOptions options = new ConnectionOptions();
        options.set(ADDRESS, inputs.getHost());
        options.set(USERNAME, inputs.getUsername());
        options.set(PASSWORD, inputs.getPassword());
        options.set(OPERATING_SYSTEM, WINDOWS);
        options.set(CONNECTION_TYPE, WINRM_NATIVE);
        return Overthere.getConnection("cifs", options);
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }
}
