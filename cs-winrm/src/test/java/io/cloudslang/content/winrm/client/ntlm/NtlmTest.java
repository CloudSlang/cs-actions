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

import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests based on the NTLMv2 authentication examples of [MS-NLMP] section 4.2.4.
 */
public class NtlmTest {

    private static final HexFormat HEX = HexFormat.of();
    private static final int SPEC_FLAGS = 0xe28a8233;
    private static final byte[] SERVER_CHALLENGE = HEX.parseHex("0123456789abcdef");
    private static final byte[] CLIENT_CHALLENGE = HEX.parseHex("aaaaaaaaaaaaaaaa");
    private static final byte[] RANDOM_SESSION_KEY = HEX.parseHex("55555555555555555555555555555555");
    private static final byte[] TARGET_INFO = HEX.parseHex(
            "02000c0044006f006d00610069006e00" + "01000c00530065007200760065007200" + "00000000");

    @Test
    public void md4() {
        assertEquals("31d6cfe0d16ae931b73c59d7e0c089c0", HEX.formatHex(Md4.digest(new byte[0])));
        assertEquals("a448017aaf21d8525fc10ae87aa6729d", HEX.formatHex(Md4.digest(ascii("abc"))));
        assertEquals("d9130a8164549fe818874806e1c7014b", HEX.formatHex(Md4.digest(ascii("message digest"))));
        assertEquals("e33b4ddc9c38f2199c3e7b164fcc0536", HEX.formatHex(Md4.digest(ascii(
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890"))));
        assertEquals("a4f49c406510bdcab6824ee7c30fd852", HEX.formatHex(Md4.digest(utf16("Password"))));
    }

    @Test
    public void ntlmV2SpecVectors() {
        final byte[] ntOwfV2 = NtlmCrypto.hmacMd5(Md4.digest(utf16("Password")), utf16("USER"), utf16("Domain"));
        assertEquals("0c868a403bfd7a93a3001ef22ef02e3f", HEX.formatHex(ntOwfV2));

        final byte[] blob = NtlmEngine.createBlob(CLIENT_CHALLENGE, TARGET_INFO, new byte[8]);
        final byte[] ntProofStr = NtlmCrypto.hmacMd5(ntOwfV2, SERVER_CHALLENGE, blob);
        assertEquals("68cd0ab851e51c96aabc927bebef6a1c", HEX.formatHex(ntProofStr));

        final byte[] sessionBaseKey = NtlmCrypto.hmacMd5(ntOwfV2, ntProofStr);
        assertEquals("8de40ccadbc14a82f15cb0ad0de95ca3", HEX.formatHex(sessionBaseKey));
        assertEquals("c5dad2544fc9799094ce1ce90bc9d03e", HEX.formatHex(NtlmCrypto.rc4Once(sessionBaseKey, RANDOM_SESSION_KEY)));
    }

    @Test
    public void sealingSpecVector() {
        final NtlmSessionSecurity security = new NtlmSessionSecurity(RANDOM_SESSION_KEY, SPEC_FLAGS);
        final byte[] wrapped = security.wrap(utf16("Plaintext"));
        assertEquals("10000000" + "01000000" + "7fb38ec5c55d4976" + "00000000" + "54e50165bf1936dc996020c1811b0f06fb5f",
                HEX.formatHex(wrapped));
    }

    @Test
    public void negotiateMessage() {
        final byte[] message = NtlmEngine.createNegotiateMessage(NtlmEngine.DEFAULT_FLAGS | NtlmEngine.ENCRYPTION_FLAGS);
        assertEquals(40, message.length);
        assertEquals("4e544c4d5353500001000000", HEX.formatHex(Arrays.copyOf(message, 12)));
        assertEquals(NtlmEngine.DEFAULT_FLAGS | NtlmEngine.ENCRYPTION_FLAGS, (int) NtlmCrypto.readUint32(message, 12));
    }

    @Test
    public void authenticateMessageIsVerifiableByServer() throws Exception {
        final NtlmEngine engine = new NtlmEngine(new SpecRandom(), () -> -11644473600000L);
        final NtlmEngine.ChallengeMessage challenge = NtlmEngine.parseChallengeMessage(challengeMessage(SPEC_FLAGS));
        assertEquals("Domain", challenge.getTargetName());
        assertArrayEquals(SERVER_CHALLENGE, challenge.getServerChallenge());

        final NtlmEngine.AuthenticateMessage authenticate = engine.createAuthenticateMessage(challenge, "User", "Password", "Domain");
        final byte[] message = authenticate.getMessage();
        assertEquals(3, NtlmCrypto.readUint32(message, 8));
        assertEquals(SPEC_FLAGS, authenticate.getFlags());
        assertArrayEquals(RANDOM_SESSION_KEY, authenticate.getExportedSessionKey());

        // domain names are upper cased, as done by Apache HttpClient 4 (previously used through winrm4j)
        assertEquals("DOMAIN", new String(securityBuffer(message, 28), StandardCharsets.UTF_16LE));
        assertEquals("User", new String(securityBuffer(message, 36), StandardCharsets.UTF_16LE));

        // independent server side verification
        final byte[] ntResponse = securityBuffer(message, 20);
        final byte[] ntProofStr = Arrays.copyOf(ntResponse, 16);
        final byte[] blob = Arrays.copyOfRange(ntResponse, 16, ntResponse.length);
        assertArrayEquals(NtlmEngine.createBlob(CLIENT_CHALLENGE, TARGET_INFO, new byte[8]), blob);

        final byte[] ntOwfV2 = hmac(HEX.parseHex("a4f49c406510bdcab6824ee7c30fd852"), concat(utf16("USER"), utf16("DOMAIN")));
        assertArrayEquals(hmac(ntOwfV2, concat(SERVER_CHALLENGE, blob)), ntProofStr);
        final byte[] lmResponse = securityBuffer(message, 12);
        assertArrayEquals(concat(hmac(ntOwfV2, concat(SERVER_CHALLENGE, CLIENT_CHALLENGE)), CLIENT_CHALLENGE), lmResponse);

        final byte[] sessionBaseKey = hmac(ntOwfV2, ntProofStr);
        final Cipher rc4 = Cipher.getInstance("ARCFOUR");
        rc4.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sessionBaseKey, "ARCFOUR"));
        assertArrayEquals(RANDOM_SESSION_KEY, rc4.doFinal(securityBuffer(message, 52)));
    }

    @Test
    public void unsupportedNtlmV1IsRejected() {
        final int flags = NtlmEngine.NEGOTIATE_UNICODE | NtlmEngine.NEGOTIATE_NTLM;
        final NtlmEngine.ChallengeMessage challenge = NtlmEngine.parseChallengeMessage(challengeMessage(flags));
        try {
            new NtlmEngine().createAuthenticateMessage(challenge, "User", "Password", null);
            fail("NTLMv1 without extended session security must be rejected");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("not supported"));
        }
    }

    @Test
    public void noSessionKeyWithoutSignOrSeal() {
        final int flags = NtlmEngine.DEFAULT_FLAGS | NtlmEngine.NEGOTIATE_TARGET_INFO;
        final NtlmEngine.ChallengeMessage challenge = NtlmEngine.parseChallengeMessage(challengeMessage(flags));
        final NtlmEngine.AuthenticateMessage authenticate = new NtlmEngine().createAuthenticateMessage(challenge, "User", "Password", null);
        assertNull(authenticate.getExportedSessionKey());
        assertEquals(0, securityBuffer(authenticate.getMessage(), 52).length);
    }

    @Test
    public void encryptAndDecryptMultipartMessages() throws Exception {
        final byte[] key = HEX.parseHex("000102030405060708090a0b0c0d0e0f");
        final NtlmSessionSecurity client = new NtlmSessionSecurity(key, SPEC_FLAGS);
        final ServerSide server = new ServerSide(key);

        for (int i = 0; i < 3; i++) {
            final byte[] request = ("<request number=\"" + i + "\"/>").getBytes(StandardCharsets.UTF_8);
            final byte[] encrypted = client.encrypt(request);
            final String text = new String(encrypted, StandardCharsets.ISO_8859_1);
            assertTrue(text.startsWith("--Encrypted Boundary\r\n\tContent-Type: application/HTTP-SPNEGO-session-encrypted\r\n"));
            assertTrue(text.contains("\tOriginalContent: type=application/soap+xml;charset=UTF-8;Length=" + request.length + "\r\n"));
            assertTrue(text.endsWith("--Encrypted Boundary--\r\n"));
            assertArrayEquals(request, server.decrypt(encrypted, i));

            final byte[] response = ("<response number=\"" + i + "\">\u00e9</response>").getBytes(StandardCharsets.UTF_8);
            assertArrayEquals(response, client.decrypt(server.encrypt(response, i)));
        }
    }

    @Test
    public void tamperedResponseIsRejected() throws Exception {
        final byte[] key = HEX.parseHex("000102030405060708090a0b0c0d0e0f");
        final NtlmSessionSecurity client = new NtlmSessionSecurity(key, SPEC_FLAGS);
        final byte[] encrypted = new ServerSide(key).encrypt("<response/>".getBytes(StandardCharsets.UTF_8), 0);
        encrypted[encrypted.length - 30] ^= 1;
        assertThrows(IllegalStateException.class, () -> client.decrypt(encrypted));
    }

    /**
     * Server side of the NTLM session security, written independently of the production code.
     */
    public static final class ServerSide {
        private final byte[] clientSigningKey;
        private final byte[] serverSigningKey;
        private final Cipher clientSealing;
        private final Cipher serverSealing;

        public ServerSide(final byte[] exportedSessionKey) throws Exception {
            clientSigningKey = md5(exportedSessionKey, "session key to client-to-server signing key magic constant");
            serverSigningKey = md5(exportedSessionKey, "session key to server-to-client signing key magic constant");
            clientSealing = rc4(md5(exportedSessionKey, "session key to client-to-server sealing key magic constant"));
            serverSealing = rc4(md5(exportedSessionKey, "session key to server-to-client sealing key magic constant"));
        }

        public byte[] decrypt(final byte[] body, final int sequenceNumber) throws Exception {
            final String text = new String(body, StandardCharsets.ISO_8859_1);
            final String marker = "\tContent-Type: application/octet-stream\r\n";
            final int start = text.indexOf(marker) + marker.length();
            final int end = text.lastIndexOf("--Encrypted Boundary--");
            final byte[] payload = Arrays.copyOfRange(body, start, end);
            assertEquals(16, readUint32(payload, 0));
            final byte[] signature = Arrays.copyOfRange(payload, 4, 20);
            final byte[] message = clientSealing.update(Arrays.copyOfRange(payload, 20, payload.length));
            final byte[] checksum = clientSealing.update(Arrays.copyOf(hmac(clientSigningKey, concat(uint32(sequenceNumber), message)), 8));
            assertArrayEquals(concat(new byte[]{1, 0, 0, 0}, checksum, uint32(sequenceNumber)), signature);
            return message;
        }

        public byte[] encrypt(final byte[] message, final int sequenceNumber) throws Exception {
            final byte[] sealed = serverSealing.update(message);
            final byte[] checksum = serverSealing.update(Arrays.copyOf(hmac(serverSigningKey, concat(uint32(sequenceNumber), message)), 8));
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.writeBytes(ascii("--Encrypted Boundary\r\n\tContent-Type: application/HTTP-SPNEGO-session-encrypted\r\n" +
                    "\tOriginalContent: type=application/soap+xml;charset=UTF-8;Length=" + message.length + "\r\n" +
                    "--Encrypted Boundary\r\n\tContent-Type: application/octet-stream\r\n"));
            out.writeBytes(uint32(16));
            out.writeBytes(concat(new byte[]{1, 0, 0, 0}, checksum, uint32(sequenceNumber)));
            out.writeBytes(sealed);
            out.writeBytes(ascii("--Encrypted Boundary--\r\n"));
            return out.toByteArray();
        }

        private static byte[] md5(final byte[] key, final String magic) throws Exception {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key);
            md5.update(ascii(magic + "\0"));
            return md5.digest();
        }

        private static Cipher rc4(final byte[] key) throws Exception {
            final Cipher cipher = Cipher.getInstance("ARCFOUR");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "ARCFOUR"));
            return cipher;
        }
    }

    public static byte[] challengeMessage(final int flags) {
        final byte[] targetName = utf16("Domain");
        final int targetNameOffset = 56;
        final int targetInfoOffset = targetNameOffset + targetName.length;
        final byte[] message = new byte[targetInfoOffset + TARGET_INFO.length];
        System.arraycopy(ascii("NTLMSSP\0"), 0, message, 0, 8);
        System.arraycopy(uint32(2), 0, message, 8, 4);
        writeSecurityBuffer(message, 12, targetName.length, targetNameOffset);
        System.arraycopy(uint32(flags), 0, message, 20, 4);
        System.arraycopy(SERVER_CHALLENGE, 0, message, 24, 8);
        writeSecurityBuffer(message, 40, TARGET_INFO.length, targetInfoOffset);
        System.arraycopy(HEX.parseHex("0601b11d0000000f"), 0, message, 48, 8);
        System.arraycopy(targetName, 0, message, targetNameOffset, targetName.length);
        System.arraycopy(TARGET_INFO, 0, message, targetInfoOffset, TARGET_INFO.length);
        return message;
    }

    public static byte[] serverChallenge() {
        return SERVER_CHALLENGE.clone();
    }

    private static void writeSecurityBuffer(final byte[] message, final int index, final int length, final int offset) {
        message[index] = (byte) length;
        message[index + 1] = (byte) (length >> 8);
        message[index + 2] = (byte) length;
        message[index + 3] = (byte) (length >> 8);
        System.arraycopy(uint32(offset), 0, message, index + 4, 4);
    }

    public static byte[] securityBuffer(final byte[] message, final int index) {
        final int length = (message[index] & 0xff) | (message[index + 1] & 0xff) << 8;
        final int offset = readUint32(message, index + 4);
        return Arrays.copyOfRange(message, offset, offset + length);
    }

    public static int readUint32(final byte[] data, final int index) {
        return (data[index] & 0xff) | (data[index + 1] & 0xff) << 8 | (data[index + 2] & 0xff) << 16 | (data[index + 3] & 0xff) << 24;
    }

    public static byte[] hmac(final byte[] key, final byte[] data) throws Exception {
        final Mac mac = Mac.getInstance("HmacMD5");
        mac.init(new SecretKeySpec(key, "HmacMD5"));
        return mac.doFinal(data);
    }

    public static byte[] concat(final byte[]... parts) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (byte[] part : parts) {
            out.writeBytes(part);
        }
        return out.toByteArray();
    }

    private static byte[] uint32(final long value) {
        return new byte[]{(byte) value, (byte) (value >> 8), (byte) (value >> 16), (byte) (value >> 24)};
    }

    private static byte[] ascii(final String value) {
        return value.getBytes(StandardCharsets.US_ASCII);
    }

    private static byte[] utf16(final String value) {
        return value.getBytes(StandardCharsets.UTF_16LE);
    }

    /**
     * Returns the [MS-NLMP] example client challenge for 8 byte requests and the random session key for 16 byte requests.
     */
    private static final class SpecRandom extends Random {
        @Override
        public void nextBytes(final byte[] bytes) {
            Arrays.fill(bytes, (byte) (bytes.length == 16 ? 0x55 : 0xaa));
        }
    }
}
