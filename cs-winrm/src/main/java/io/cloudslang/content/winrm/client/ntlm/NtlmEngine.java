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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.function.LongSupplier;

import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.concat;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.hmacMd5;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.readUint16;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.readUint32;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.uint32;

/**
 * NTLM (MS-NLMP) message engine producing NEGOTIATE / AUTHENTICATE messages and the exported session key
 * needed for WinRM message encryption.
 */
public final class NtlmEngine {

    public static final int NEGOTIATE_UNICODE = 0x00000001;
    public static final int NEGOTIATE_OEM = 0x00000002;
    public static final int REQUEST_TARGET = 0x00000004;
    public static final int NEGOTIATE_SIGN = 0x00000010;
    public static final int NEGOTIATE_SEAL = 0x00000020;
    public static final int NEGOTIATE_LM_KEY = 0x00000080;
    public static final int NEGOTIATE_NTLM = 0x00000200;
    public static final int NEGOTIATE_ALWAYS_SIGN = 0x00008000;
    public static final int NEGOTIATE_EXTENDED_SESSIONSECURITY = 0x00080000;
    public static final int NEGOTIATE_TARGET_INFO = 0x00800000;
    public static final int NEGOTIATE_VERSION = 0x02000000;
    public static final int NEGOTIATE_128 = 0x20000000;
    public static final int NEGOTIATE_KEY_EXCH = 0x40000000;
    public static final int NEGOTIATE_56 = 0x80000000;

    /**
     * Flags sent in the NEGOTIATE message.
     */
    public static final int DEFAULT_FLAGS = NEGOTIATE_NTLM
            | NEGOTIATE_EXTENDED_SESSIONSECURITY
            | NEGOTIATE_VERSION
            | NEGOTIATE_ALWAYS_SIGN
            | NEGOTIATE_128
            | NEGOTIATE_56
            | NEGOTIATE_UNICODE;

    /**
     * Additional flags requested when the WinRM payload must be signed and sealed (encrypted).
     */
    public static final int ENCRYPTION_FLAGS = NEGOTIATE_SIGN | NEGOTIATE_SEAL | NEGOTIATE_KEY_EXCH;

    private static final byte[] SIGNATURE = "NTLMSSP\0".getBytes(StandardCharsets.US_ASCII);
    private static final byte[] VERSION = {0x05, 0x01, 0x28, 0x0a, 0x00, 0x00, 0x00, 0x0f};
    private static final long WINDOWS_EPOCH_OFFSET_MILLIS = 11644473600000L;

    private final Random random;
    private final LongSupplier clock;

    public NtlmEngine() {
        this(new SecureRandom(), System::currentTimeMillis);
    }

    NtlmEngine(final Random random, final LongSupplier clock) {
        this.random = random;
        this.clock = clock;
    }

    public static byte[] createNegotiateMessage(final int flags) {
        final byte[] message = new byte[40];
        System.arraycopy(SIGNATURE, 0, message, 0, SIGNATURE.length);
        writeUint32(message, 8, 1);
        writeUint32(message, 12, flags);
        // empty domain and workstation security buffers, both pointing after the version field
        writeSecurityBuffer(message, 16, 0, 40);
        writeSecurityBuffer(message, 24, 0, 40);
        System.arraycopy(VERSION, 0, message, 32, VERSION.length);
        return message;
    }

    public static ChallengeMessage parseChallengeMessage(final byte[] message) {
        if (message.length < 32) {
            throw new IllegalArgumentException("NTLM challenge message is too short");
        }
        for (int i = 0; i < SIGNATURE.length; i++) {
            if (message[i] != SIGNATURE[i]) {
                throw new IllegalArgumentException("Invalid NTLM challenge message signature");
            }
        }
        if (readUint32(message, 8) != 2) {
            throw new IllegalArgumentException("Unexpected NTLM message type; expected a challenge message");
        }
        final int flags = (int) readUint32(message, 20);
        final byte[] serverChallenge = Arrays.copyOfRange(message, 24, 32);
        final byte[] targetName = readSecurityBuffer(message, 12);
        final byte[] targetInfo = message.length >= 48 ? readSecurityBuffer(message, 40) : new byte[0];
        return new ChallengeMessage(flags, serverChallenge,
                targetName.length == 0 ? null : new String(targetName, charset(flags)),
                targetInfo.length == 0 ? null : targetInfo);
    }

    /**
     * Builds the AUTHENTICATE message answering the given challenge.
     *
     * @param challenge parsed challenge (type 2) message
     * @param username  user name (without domain)
     * @param password  user password
     * @param domain    user domain, may be {@code null}
     */
    public AuthenticateMessage createAuthenticateMessage(final ChallengeMessage challenge, final String username,
                                                        final String password, final String domain) {
        final int flags = challenge.getFlags();
        final String userDomain = stripDotSuffix(domain == null ? null : domain.toUpperCase(Locale.ROOT));
        final byte[] ntHash = Md4.digest(password.getBytes(StandardCharsets.UTF_16LE));
        final byte[] serverChallenge = challenge.getServerChallenge();

        final byte[] lmResponse;
        final byte[] ntResponse;
        final byte[] sessionBaseKey;

        if ((flags & NEGOTIATE_TARGET_INFO) != 0 && challenge.getTargetInfo() != null && challenge.getTargetName() != null) {
            // NTLMv2
            final byte[] userBytes = username.toUpperCase(Locale.ROOT).getBytes(StandardCharsets.UTF_16LE);
            final byte[] domainBytes = userDomain == null ? new byte[0] : userDomain.getBytes(StandardCharsets.UTF_16LE);
            final byte[] ntlmV2Hash = hmacMd5(ntHash, userBytes, domainBytes);

            final byte[] blob = createBlob(randomBytes(8), challenge.getTargetInfo(), timestamp());
            final byte[] ntProofStr = hmacMd5(ntlmV2Hash, serverChallenge, blob);
            ntResponse = concat(ntProofStr, blob);

            final byte[] clientChallenge = randomBytes(8);
            lmResponse = concat(hmacMd5(ntlmV2Hash, serverChallenge, clientChallenge), clientChallenge);

            sessionBaseKey = hmacMd5(ntlmV2Hash, ntProofStr);
        } else if ((flags & NEGOTIATE_EXTENDED_SESSIONSECURITY) != 0) {
            // NTLMv1 with extended session security (NTLM2 session response)
            final byte[] clientChallenge = randomBytes(8);
            lmResponse = concat(clientChallenge, new byte[16]);
            final byte[] sessionHash = Arrays.copyOf(NtlmCrypto.md5(serverChallenge, clientChallenge), 8);
            ntResponse = NtlmCrypto.desLongResponse(ntHash, sessionHash);
            sessionBaseKey = hmacMd5(Md4.digest(ntHash), serverChallenge, clientChallenge);
        } else {
            throw new IllegalStateException("The server requested an NTLM authentication mode (NTLMv1 / LM) that is not supported. "
                    + "Enable NTLMv2 or NTLMv1 extended session security on the target host.");
        }

        final byte[] exportedSessionKey;
        final byte[] encryptedRandomSessionKey;
        if ((flags & NEGOTIATE_SIGN) != 0 || (flags & NEGOTIATE_SEAL) != 0) {
            if ((flags & NEGOTIATE_KEY_EXCH) != 0) {
                exportedSessionKey = randomBytes(16);
                encryptedRandomSessionKey = NtlmCrypto.rc4Once(sessionBaseKey, exportedSessionKey);
            } else {
                exportedSessionKey = sessionBaseKey;
                encryptedRandomSessionKey = sessionBaseKey;
            }
        } else {
            exportedSessionKey = null;
            encryptedRandomSessionKey = new byte[0];
        }

        final Charset charset = charset(flags);
        final byte[] domainField = userDomain == null ? new byte[0] : userDomain.getBytes(charset);
        final byte[] userField = username.getBytes(charset);
        final byte[] workstationField = new byte[0];

        final int lmOffset = 72;
        final int ntOffset = lmOffset + lmResponse.length;
        final int domainOffset = ntOffset + ntResponse.length;
        final int userOffset = domainOffset + domainField.length;
        final int workstationOffset = userOffset + userField.length;
        final int sessionKeyOffset = workstationOffset + workstationField.length;
        final int length = sessionKeyOffset + encryptedRandomSessionKey.length;

        final byte[] message = new byte[length];
        System.arraycopy(SIGNATURE, 0, message, 0, SIGNATURE.length);
        writeUint32(message, 8, 3);
        writeSecurityBuffer(message, 12, lmResponse.length, lmOffset);
        writeSecurityBuffer(message, 20, ntResponse.length, ntOffset);
        writeSecurityBuffer(message, 28, domainField.length, domainOffset);
        writeSecurityBuffer(message, 36, userField.length, userOffset);
        writeSecurityBuffer(message, 44, workstationField.length, workstationOffset);
        writeSecurityBuffer(message, 52, encryptedRandomSessionKey.length, sessionKeyOffset);
        writeUint32(message, 60, flags);
        System.arraycopy(VERSION, 0, message, 64, VERSION.length);
        System.arraycopy(lmResponse, 0, message, lmOffset, lmResponse.length);
        System.arraycopy(ntResponse, 0, message, ntOffset, ntResponse.length);
        System.arraycopy(domainField, 0, message, domainOffset, domainField.length);
        System.arraycopy(userField, 0, message, userOffset, userField.length);
        System.arraycopy(encryptedRandomSessionKey, 0, message, sessionKeyOffset, encryptedRandomSessionKey.length);

        return new AuthenticateMessage(message, exportedSessionKey, flags);
    }

    static byte[] createBlob(final byte[] clientChallenge, final byte[] targetInfo, final byte[] timestamp) {
        return concat(new byte[]{0x01, 0x01, 0x00, 0x00}, new byte[4], timestamp, clientChallenge, new byte[4], targetInfo, new byte[4]);
    }

    private byte[] timestamp() {
        long time = clock.getAsLong() + WINDOWS_EPOCH_OFFSET_MILLIS;
        time *= 10000;
        final byte[] timestamp = new byte[8];
        for (int i = 0; i < 8; i++) {
            timestamp[i] = (byte) time;
            time >>>= 8;
        }
        return timestamp;
    }

    private byte[] randomBytes(final int length) {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    private static Charset charset(final int flags) {
        return (flags & NEGOTIATE_UNICODE) != 0 ? StandardCharsets.UTF_16LE : StandardCharsets.US_ASCII;
    }

    private static String stripDotSuffix(final String value) {
        if (value == null) {
            return null;
        }
        final int index = value.indexOf('.');
        return index != -1 ? value.substring(0, index) : value;
    }

    private static byte[] readSecurityBuffer(final byte[] src, final int index) {
        final int length = readUint16(src, index);
        final int offset = (int) readUint32(src, index + 4);
        if (length == 0 || src.length < offset + length) {
            return new byte[0];
        }
        return Arrays.copyOfRange(src, offset, offset + length);
    }

    private static void writeSecurityBuffer(final byte[] dest, final int index, final int length, final int offset) {
        dest[index] = (byte) length;
        dest[index + 1] = (byte) (length >>> 8);
        dest[index + 2] = (byte) length;
        dest[index + 3] = (byte) (length >>> 8);
        writeUint32(dest, index + 4, offset);
    }

    private static void writeUint32(final byte[] dest, final int index, final long value) {
        System.arraycopy(uint32(value), 0, dest, index, 4);
    }

    public static final class ChallengeMessage {
        private final int flags;
        private final byte[] serverChallenge;
        private final String targetName;
        private final byte[] targetInfo;

        ChallengeMessage(final int flags, final byte[] serverChallenge, final String targetName, final byte[] targetInfo) {
            this.flags = flags;
            this.serverChallenge = serverChallenge;
            this.targetName = targetName;
            this.targetInfo = targetInfo;
        }

        public int getFlags() {
            return flags;
        }

        public byte[] getServerChallenge() {
            return serverChallenge;
        }

        public String getTargetName() {
            return targetName;
        }

        public byte[] getTargetInfo() {
            return targetInfo;
        }
    }

    public static final class AuthenticateMessage {
        private final byte[] message;
        private final byte[] exportedSessionKey;
        private final int flags;

        AuthenticateMessage(final byte[] message, final byte[] exportedSessionKey, final int flags) {
            this.message = message;
            this.exportedSessionKey = exportedSessionKey;
            this.flags = flags;
        }

        public byte[] getMessage() {
            return message;
        }

        /**
         * @return the session key used to derive signing and sealing keys, or {@code null} if signing was not negotiated
         */
        public byte[] getExportedSessionKey() {
            return exportedSessionKey;
        }

        public int getFlags() {
            return flags;
        }
    }
}
