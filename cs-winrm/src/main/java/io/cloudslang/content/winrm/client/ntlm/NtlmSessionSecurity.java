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
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.CRC32;

import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.concat;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.hmacMd5;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.md5;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.rc4;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.readUint32;
import static io.cloudslang.content.winrm.client.ntlm.NtlmCrypto.uint32;

/**
 * NTLM session security (MS-NLMP 3.4) used to sign and seal WinRM messages, plus the
 * {@code multipart/encrypted} framing expected by the WinRM service (MS-WSMV 2.2.9.1).
 */
public final class NtlmSessionSecurity {

    public static final String ENCRYPTED_CONTENT_TYPE =
            "multipart/encrypted;protocol=\"application/HTTP-SPNEGO-session-encrypted\";boundary=\"Encrypted Boundary\"";

    private static final String BOUNDARY = "--Encrypted Boundary";
    private static final byte[] BOUNDARY_LINE = (BOUNDARY + "\r\n").getBytes(StandardCharsets.US_ASCII);
    private static final byte[] BOUNDARY_END = (BOUNDARY + "--\r\n").getBytes(StandardCharsets.US_ASCII);
    private static final byte[] OCTET_STREAM_HEADER = "\tContent-Type: application/octet-stream\r\n".getBytes(StandardCharsets.US_ASCII);

    private static final byte[] CLIENT_SIGNING = magic("session key to client-to-server signing key magic constant");
    private static final byte[] SERVER_SIGNING = magic("session key to server-to-client signing key magic constant");
    private static final byte[] CLIENT_SEALING = magic("session key to client-to-server sealing key magic constant");
    private static final byte[] SERVER_SEALING = magic("session key to server-to-client sealing key magic constant");

    private final int flags;
    private final byte[] clientSigningKey;
    private final byte[] serverSigningKey;
    private final Cipher clientSealingHandle;
    private final Cipher serverSealingHandle;
    private long outgoingSequenceNumber;

    public NtlmSessionSecurity(final byte[] exportedSessionKey, final int flags) {
        this.flags = flags;
        this.clientSigningKey = md5(exportedSessionKey, CLIENT_SIGNING);
        this.serverSigningKey = md5(exportedSessionKey, SERVER_SIGNING);
        this.clientSealingHandle = rc4(sealKey(exportedSessionKey, CLIENT_SEALING));
        this.serverSealingHandle = rc4(sealKey(exportedSessionKey, SERVER_SEALING));
    }

    /**
     * Encrypts and signs a SOAP message and wraps it in the WinRM multipart/encrypted body.
     */
    public synchronized byte[] encrypt(final byte[] message) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(BOUNDARY_LINE);
        out.writeBytes("\tContent-Type: application/HTTP-SPNEGO-session-encrypted\r\n".getBytes(StandardCharsets.US_ASCII));
        out.writeBytes(("\tOriginalContent: type=application/soap+xml;charset=UTF-8;Length=" + message.length + "\r\n")
                .getBytes(StandardCharsets.US_ASCII));
        out.writeBytes(BOUNDARY_LINE);
        out.writeBytes(OCTET_STREAM_HEADER);
        out.writeBytes(wrap(message));
        out.writeBytes(BOUNDARY_END);
        return out.toByteArray();
    }

    /**
     * Extracts, decrypts and verifies the payload of a WinRM multipart/encrypted body.
     */
    public synchronized byte[] decrypt(final byte[] body) {
        final int secondBoundary = indexOf(body, BOUNDARY_LINE, indexOf(body, BOUNDARY_LINE, 0) + BOUNDARY_LINE.length);
        if (secondBoundary < 0) {
            throw new IllegalStateException("Invalid encrypted response from server; missing payload boundary");
        }
        final int headerIndex = indexOf(body, OCTET_STREAM_HEADER, secondBoundary + BOUNDARY_LINE.length);
        if (headerIndex < 0) {
            throw new IllegalStateException("Invalid encrypted response from server; missing payload content type");
        }
        final int payloadStart = headerIndex + OCTET_STREAM_HEADER.length;
        final int payloadEnd = lastIndexOf(body, (BOUNDARY + "--").getBytes(StandardCharsets.US_ASCII));
        if (payloadEnd < payloadStart) {
            throw new IllegalStateException("Invalid encrypted response from server; missing closing boundary");
        }
        return unwrap(Arrays.copyOfRange(body, payloadStart, payloadEnd));
    }

    byte[] wrap(final byte[] message) {
        // seal before signing: both operations share the same RC4 handle
        final byte[] sealed = rc4(clientSealingHandle, message);
        final byte[] signature = signature(message, outgoingSequenceNumber, clientSigningKey, clientSealingHandle);
        outgoingSequenceNumber++;
        return concat(uint32(signature.length), signature, sealed);
    }

    byte[] unwrap(final byte[] payload) {
        if (payload.length < 4) {
            throw new IllegalStateException("Invalid encrypted payload from server");
        }
        final int signatureLength = (int) readUint32(payload, 0);
        if (signatureLength != 16 || payload.length < 4 + signatureLength) {
            throw new IllegalStateException("Invalid signature length in encrypted payload from server");
        }
        final byte[] signature = Arrays.copyOfRange(payload, 4, 4 + signatureLength);
        final byte[] sealed = Arrays.copyOfRange(payload, 4 + signatureLength, payload.length);
        final byte[] message = rc4(serverSealingHandle, sealed);

        final long sequenceNumber = readUint32(signature, 12);
        final byte[] expected = signature(message, sequenceNumber, serverSigningKey, serverSealingHandle);
        final int checksumOffset = hasFlag(NtlmEngine.NEGOTIATE_EXTENDED_SESSIONSECURITY) ? 4 : 8;
        if (!Arrays.equals(Arrays.copyOfRange(signature, checksumOffset, 12), Arrays.copyOfRange(expected, checksumOffset, 12))) {
            throw new IllegalStateException("Encrypted payload from server failed signature verification");
        }
        return message;
    }

    private byte[] signature(final byte[] message, final long sequenceNumber, final byte[] signingKey, final Cipher sealingHandle) {
        final byte[] version = {1, 0, 0, 0};
        if (hasFlag(NtlmEngine.NEGOTIATE_EXTENDED_SESSIONSECURITY)) {
            byte[] checksum = Arrays.copyOf(hmacMd5(signingKey, uint32(sequenceNumber), message), 8);
            if (hasFlag(NtlmEngine.NEGOTIATE_KEY_EXCH)) {
                checksum = rc4(sealingHandle, checksum);
            }
            return concat(version, checksum, uint32(sequenceNumber));
        }
        final CRC32 crc = new CRC32();
        crc.update(message);
        return concat(version,
                rc4(sealingHandle, uint32(0)),
                rc4(sealingHandle, uint32(crc.getValue())),
                rc4(sealingHandle, uint32(sequenceNumber)));
    }

    private byte[] sealKey(final byte[] exportedSessionKey, final byte[] magicConstant) {
        if (hasFlag(NtlmEngine.NEGOTIATE_EXTENDED_SESSIONSECURITY)) {
            final byte[] key;
            if (hasFlag(NtlmEngine.NEGOTIATE_128)) {
                key = exportedSessionKey;
            } else if (hasFlag(NtlmEngine.NEGOTIATE_56)) {
                key = Arrays.copyOf(exportedSessionKey, 7);
            } else {
                key = Arrays.copyOf(exportedSessionKey, 5);
            }
            return md5(key, magicConstant);
        }
        if (hasFlag(NtlmEngine.NEGOTIATE_LM_KEY)) {
            throw new UnsupportedOperationException("LM KEY negotiate mode not implemented; use extended session security instead");
        }
        return exportedSessionKey;
    }

    private boolean hasFlag(final int flag) {
        return (flags & flag) == flag;
    }

    private static byte[] magic(final String value) {
        return (value + "\0").getBytes(StandardCharsets.US_ASCII);
    }

    private static int indexOf(final byte[] data, final byte[] pattern, final int from) {
        outer:
        for (int i = Math.max(from, 0); i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    private static int lastIndexOf(final byte[] data, final byte[] pattern) {
        outer:
        for (int i = data.length - pattern.length; i >= 0; i--) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
