/*
 * (c) Copyright 2021 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.rft.services;

import com.opsware.pas.content.commons.util.StringUtils;
import io.cloudslang.content.rft.remote_copy.CopierFactory;
import io.cloudslang.content.rft.remote_copy.CopierFactory.copiers;
import io.cloudslang.content.rft.remote_copy.ICopier;
import io.cloudslang.content.rft.remote_copy.RemoteCopyInputs;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Inputs.RemoteCopyInputs.DEST_CHARACTER_SET;
import static io.cloudslang.content.rft.utils.Inputs.RemoteCopyInputs.SRC_CHARACTER_SET;

public class RemoteCopyService {

    private static final String BACK_SLASH = "/";

    public Map<String, String> execute(RemoteCopyInputs inputs) {

        Map<String, String> results = new HashMap();

        try {
            String srcProtocol = inputs.getSourceProtocol().toLowerCase().trim();
            String destProtocol = inputs.getDestinationProtocol().toLowerCase().trim();

            ICopier src = CopierFactory.getExecutor(srcProtocol);
            src.setProtocol(srcProtocol);
            ICopier dest = CopierFactory.getExecutor(destProtocol);
            dest.setProtocol(destProtocol);

            src.setVersion(VERSION);
            dest.setVersion(VERSION);

            checkOptions(inputs.getSourceProtocol(), inputs.getSourceHost());
            checkOptions(inputs.getDestinationProtocol(), inputs.getDestinationHost());

            setCredentials(src, inputs.getSourceHost(), inputs.getSourcePort(), inputs.getSourceUsername(), inputs.getSourcePassword(),
                    inputs.getSourcePrivateKeyFile());
            setCredentials(dest, inputs.getDestinationHost(), inputs.getDestinationPort(), inputs.getDestinationUsername(),
                    inputs.getDestinationPassword(), inputs.getDestinationPrivateKeyFile());

            setAndValidateCharacterSet(src, inputs.getSourceCharacterSet(), SRC_CHARACTER_SET);
            setAndValidateCharacterSet(dest, inputs.getDestinationCharacterSet(), DEST_CHARACTER_SET);

            setConnectionTimeout(src, inputs.getConnectionTimeout());
            setConnectionTimeout(dest, inputs.getConnectionTimeout());
            setExecutionTimeout(src, inputs.getExecutionTimeout());
            setExecutionTimeout(dest, inputs.getExecutionTimeout());

            src.copyTo(dest, BACK_SLASH + inputs.getSourcePath(), BACK_SLASH + inputs.getDestinationPath());
            results.put(RETURN_RESULT, SUCCESS_RESULT);
            results.put(RETURN_CODE, SUCCESS_RETURN_CODE);

        } catch (Exception e) {
            results.put(EXCEPTION, String.valueOf(e));
            results.put(RETURN_RESULT, (e.getMessage()));
            results.put(RETURN_CODE, FAILURE_RETURN_CODE);
        }
        return results;

    }

    public static void setConnectionTimeout(ICopier src, String connectionTimeout) {
        if ((connectionTimeout != null) && (connectionTimeout.length() > 0)) {
            src.setConnectionTimeout(Integer.parseInt(connectionTimeout));
        }
    }

    public static void setExecutionTimeout(ICopier src, String executionTimeout) {
        if ((executionTimeout != null) && (executionTimeout.length() > 0)) {
            src.setExecutionTimeout(Integer.parseInt(executionTimeout));
        }
    }

    public static void checkOptions(String copier, String host) throws Exception {
        switch (copiers.valueOf(copier)) {
            case local:
                if (host == null || !host.trim().equalsIgnoreCase("localhost")) {
                    throw new Exception("When the protocol is local, the host must be localhost!\n");
                }
                break;
            case scp:
                break;
            case sftp:
                break;
            default:
                break;
        }
    }

    public static void setCredentials(ICopier copier, String host, String portString, String username, String password,
                                      String privateKeyFile) {
        int port = -1;
        if (portString != null && !portString.isEmpty())
            port = Integer.parseInt(portString);
        if (copiers.valueOf(copier.getProtocolName()) == copiers.local) {
            host = "";
        }
        if (privateKeyFile != null && privateKeyFile.length() > 0) {
            copier.setCredentials(host, port, username, password, privateKeyFile);
        } else {
            copier.setCredentials(host, port, username, password);
        }
    }

    public static boolean setCharacterSet(ICopier copier, String characterSetName) {
        if (copiers.valueOf(copier.getProtocolName()) == copiers.sftp) {
            if (!StringUtils.isNull(characterSetName)) {
                if (!Charset.isSupported(characterSetName)) {
                    return false;
                }
            }
            copier.setCustomArgument(ICopier.simpleArgument.characterSet, characterSetName);
        }
        return true;
    }

    private void setAndValidateCharacterSet(ICopier copier, String characterSet, String source) throws Exception {
        try {
            if (!setCharacterSet(copier, characterSet)) {
                throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
            }
        } catch (IllegalCharsetNameException icne) {
            throw new Exception(source + " input: " + characterSet + " is not a valid character set name");
        }
    }

    public enum protocols {local, scp, sftp, smb3, LOCAL, SCP, SFTP, SMB3}

}
