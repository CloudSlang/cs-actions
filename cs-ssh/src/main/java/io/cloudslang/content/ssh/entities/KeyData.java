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

package io.cloudslang.content.ssh.entities;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * User: sacalosb
 * Date: 07.01.2016
 */
public class KeyData extends IdentityKey {
    public static final String MD_5 = "MD5";
    public static final int SIGNUM_POSITIVE = 1;
    public static final int RADIX_HEXA = 16;
    private byte[] privateKeyData;
    private String keyName;

    public KeyData(String privateKeyData) {
        this.setPrivateKeyData(privateKeyData);
        this.passPhrase = null;
        this.setKeyName();
    }

    public KeyData(String privateKeyData, String passPhrase) {
        this.setPrivateKeyData(privateKeyData);
        this.setPassPhrase(passPhrase);
        this.setKeyName();
    }

    public byte[] getPrivateKeyData() {
        return (privateKeyData == null) ? null : Arrays.copyOf(privateKeyData, privateKeyData.length);
    }

    private void setPrivateKeyData(String privateKeyData) {
        this.privateKeyData = privateKeyData.getBytes(KEY_ENCODING);
    }

    public String getKeyName() {
        return keyName;
    }

    private void setKeyName() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD_5);
            keyName = new BigInteger(SIGNUM_POSITIVE, messageDigest.digest(privateKeyData)).toString(RADIX_HEXA);
        } catch (NoSuchAlgorithmException e) {
            keyName = Integer.toHexString(Arrays.hashCode(privateKeyData));
        }
    }
}
