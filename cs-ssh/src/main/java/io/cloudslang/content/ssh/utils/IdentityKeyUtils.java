/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.ssh.utils;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import io.cloudslang.content.ssh.entities.IdentityKey;
import io.cloudslang.content.ssh.entities.KeyData;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.exceptions.SSHException;
import io.cloudslang.content.utils.StringUtilities;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public class IdentityKeyUtils {
    private static final String KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
    private static final String TYPE_HEADER = "Proc-Type: ";
    private static final String DEK_HEADER = "DEK-Info: ";
    private static final String NEW_LINE = System.lineSeparator();
    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";

    public static IdentityKey getIdentityKey(String privateKeyFile, String privateKeyString, String privateKeyPassphrase) {
        IdentityKey identityKey = null;
        if (StringUtilities.isNotEmpty(privateKeyFile)) {
            identityKey = new KeyFile(privateKeyFile);
        }
        if (StringUtilities.isNotEmpty(privateKeyString)) {
            if (identityKey != null) {
                throw new IllegalArgumentException(String.format("%s and %s inputs are mutually exclusive. They can't be both set at the same time.",
                        Constants.PRIVATE_KEY_FILE, Constants.PRIVATE_KEY_DATA));
            }
            identityKey = new KeyData(privateKeyString);
        }
        if (identityKey != null && StringUtilities.isNotEmpty(privateKeyPassphrase)) {
            identityKey.setPassPhrase(privateKeyPassphrase);
        }
        return identityKey;
    }

    public static void setIdentity(JSch jsch, IdentityKey identityKey) throws SSHException {
        byte[] passPhrase = identityKey.getPassPhrase();
        if (identityKey instanceof KeyFile) {
            try {
                jsch.addIdentity(((KeyFile) identityKey).getKeyFilePath(), passPhrase);
            } catch (JSchException e) {
                throw new SSHException("The keyFilePath is invalid.", e);
            }
        } else {
            try {
                jsch.addIdentity(((KeyData) identityKey).getKeyName(), ((KeyData) identityKey).getPrivateKeyData(), null, passPhrase);
            } catch (JSchException e) {
                throw new SSHException("The " + Constants.PRIVATE_KEY_DATA + " is invalid.", e);
            }
        }
    }
}
