/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.winrm.client.ntlm;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;

final class NtlmCrypto {

    private NtlmCrypto() {
    }

    static byte[] md5(final byte[]... parts) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            for (byte[] part : parts) {
                md5.update(part);
            }
            return md5.digest();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    static byte[] hmacMd5(final byte[] key, final byte[]... parts) {
        try {
            final Mac mac = Mac.getInstance("HmacMD5");
            mac.init(new SecretKeySpec(key, "HmacMD5"));
            for (byte[] part : parts) {
                mac.update(part);
            }
            return mac.doFinal();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Creates a stateful RC4 cipher; NTLM session security keeps the RC4 state across messages.
     */
    static Cipher rc4(final byte[] key) {
        try {
            final Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "RC4"));
            return rc4;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    static byte[] rc4(final Cipher cipher, final byte[] data) {
        if (data.length == 0) {
            return new byte[0];
        }
        final byte[] out = cipher.update(data);
        return out == null ? new byte[0] : out;
    }

    static byte[] rc4Once(final byte[] key, final byte[] data) {
        return rc4(rc4(key), data);
    }

    /**
     * DES based response used by the NTLM2 session response (NTLMv1 with extended session security).
     */
    static byte[] desLongResponse(final byte[] hash16, final byte[] challenge8) {
        try {
            final byte[] keyBytes = new byte[21];
            System.arraycopy(hash16, 0, keyBytes, 0, 16);
            final byte[] response = new byte[24];
            final Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
            for (int i = 0; i < 3; i++) {
                des.init(Cipher.ENCRYPT_MODE, createDesKey(keyBytes, i * 7));
                System.arraycopy(des.doFinal(challenge8), 0, response, i * 8, 8);
            }
            return response;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Key createDesKey(final byte[] bytes, final int offset) {
        final byte[] material = new byte[7];
        System.arraycopy(bytes, offset, material, 0, 7);
        final byte[] key = new byte[8];
        key[0] = material[0];
        key[1] = (byte) (material[0] << 7 | (material[1] & 0xff) >>> 1);
        key[2] = (byte) (material[1] << 6 | (material[2] & 0xff) >>> 2);
        key[3] = (byte) (material[2] << 5 | (material[3] & 0xff) >>> 3);
        key[4] = (byte) (material[3] << 4 | (material[4] & 0xff) >>> 4);
        key[5] = (byte) (material[4] << 3 | (material[5] & 0xff) >>> 5);
        key[6] = (byte) (material[5] << 2 | (material[6] & 0xff) >>> 6);
        key[7] = (byte) (material[6] << 1);
        for (int i = 0; i < 8; i++) {
            final int b = key[i];
            final boolean needsParity = (((b >>> 7) ^ (b >>> 6) ^ (b >>> 5) ^ (b >>> 4) ^ (b >>> 3) ^ (b >>> 2) ^ (b >>> 1)) & 0x01) == 0;
            key[i] = needsParity ? (byte) (b | 0x01) : (byte) (b & 0xfe);
        }
        return new SecretKeySpec(key, "DES");
    }

    static byte[] concat(final byte[]... parts) {
        int length = 0;
        for (byte[] part : parts) {
            length += part.length;
        }
        final byte[] out = new byte[length];
        int offset = 0;
        for (byte[] part : parts) {
            System.arraycopy(part, 0, out, offset, part.length);
            offset += part.length;
        }
        return out;
    }

    static byte[] uint32(final long value) {
        return new byte[]{(byte) value, (byte) (value >>> 8), (byte) (value >>> 16), (byte) (value >>> 24)};
    }

    static long readUint32(final byte[] src, final int index) {
        return (src[index] & 0xffL) | ((src[index + 1] & 0xffL) << 8)
                | ((src[index + 2] & 0xffL) << 16) | ((src[index + 3] & 0xffL) << 24);
    }

    static int readUint16(final byte[] src, final int index) {
        return (src[index] & 0xff) | ((src[index + 1] & 0xff) << 8);
    }
}
