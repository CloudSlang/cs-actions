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

/**
 * MD4 message digest (RFC 1320), required to compute the NT hash of a password.
 * The JDK does not expose MD4 through a public provider, hence this small implementation.
 */
final class Md4 {

    private Md4() {
    }

    static byte[] digest(final byte[] input) {
        final long bitLength = (long) input.length * 8;
        final int paddedLength = ((input.length + 8) / 64 + 1) * 64;
        final byte[] message = new byte[paddedLength];
        System.arraycopy(input, 0, message, 0, input.length);
        message[input.length] = (byte) 0x80;
        for (int i = 0; i < 8; i++) {
            message[paddedLength - 8 + i] = (byte) (bitLength >>> (8 * i));
        }

        int a = 0x67452301;
        int b = 0xefcdab89;
        int c = 0x98badcfe;
        int d = 0x10325476;

        final int[] x = new int[16];
        for (int block = 0; block < paddedLength; block += 64) {
            for (int i = 0; i < 16; i++) {
                final int off = block + i * 4;
                x[i] = (message[off] & 0xff) | ((message[off + 1] & 0xff) << 8)
                        | ((message[off + 2] & 0xff) << 16) | ((message[off + 3] & 0xff) << 24);
            }
            final int aa = a;
            final int bb = b;
            final int cc = c;
            final int dd = d;

            // Round 1
            a = round1(a, b, c, d, x[0], 3);
            d = round1(d, a, b, c, x[1], 7);
            c = round1(c, d, a, b, x[2], 11);
            b = round1(b, c, d, a, x[3], 19);
            a = round1(a, b, c, d, x[4], 3);
            d = round1(d, a, b, c, x[5], 7);
            c = round1(c, d, a, b, x[6], 11);
            b = round1(b, c, d, a, x[7], 19);
            a = round1(a, b, c, d, x[8], 3);
            d = round1(d, a, b, c, x[9], 7);
            c = round1(c, d, a, b, x[10], 11);
            b = round1(b, c, d, a, x[11], 19);
            a = round1(a, b, c, d, x[12], 3);
            d = round1(d, a, b, c, x[13], 7);
            c = round1(c, d, a, b, x[14], 11);
            b = round1(b, c, d, a, x[15], 19);

            // Round 2
            a = round2(a, b, c, d, x[0], 3);
            d = round2(d, a, b, c, x[4], 5);
            c = round2(c, d, a, b, x[8], 9);
            b = round2(b, c, d, a, x[12], 13);
            a = round2(a, b, c, d, x[1], 3);
            d = round2(d, a, b, c, x[5], 5);
            c = round2(c, d, a, b, x[9], 9);
            b = round2(b, c, d, a, x[13], 13);
            a = round2(a, b, c, d, x[2], 3);
            d = round2(d, a, b, c, x[6], 5);
            c = round2(c, d, a, b, x[10], 9);
            b = round2(b, c, d, a, x[14], 13);
            a = round2(a, b, c, d, x[3], 3);
            d = round2(d, a, b, c, x[7], 5);
            c = round2(c, d, a, b, x[11], 9);
            b = round2(b, c, d, a, x[15], 13);

            // Round 3
            a = round3(a, b, c, d, x[0], 3);
            d = round3(d, a, b, c, x[8], 9);
            c = round3(c, d, a, b, x[4], 11);
            b = round3(b, c, d, a, x[12], 15);
            a = round3(a, b, c, d, x[2], 3);
            d = round3(d, a, b, c, x[10], 9);
            c = round3(c, d, a, b, x[6], 11);
            b = round3(b, c, d, a, x[14], 15);
            a = round3(a, b, c, d, x[1], 3);
            d = round3(d, a, b, c, x[9], 9);
            c = round3(c, d, a, b, x[5], 11);
            b = round3(b, c, d, a, x[13], 15);
            a = round3(a, b, c, d, x[3], 3);
            d = round3(d, a, b, c, x[11], 9);
            c = round3(c, d, a, b, x[7], 11);
            b = round3(b, c, d, a, x[15], 15);

            a += aa;
            b += bb;
            c += cc;
            d += dd;
        }

        final byte[] out = new byte[16];
        final int[] state = {a, b, c, d};
        for (int i = 0; i < 4; i++) {
            out[i * 4] = (byte) state[i];
            out[i * 4 + 1] = (byte) (state[i] >>> 8);
            out[i * 4 + 2] = (byte) (state[i] >>> 16);
            out[i * 4 + 3] = (byte) (state[i] >>> 24);
        }
        return out;
    }

    private static int round1(int a, int b, int c, int d, int x, int s) {
        return Integer.rotateLeft(a + ((b & c) | (~b & d)) + x, s);
    }

    private static int round2(int a, int b, int c, int d, int x, int s) {
        return Integer.rotateLeft(a + ((b & c) | (b & d) | (c & d)) + x + 0x5a827999, s);
    }

    private static int round3(int a, int b, int c, int d, int x, int s) {
        return Integer.rotateLeft(a + (b ^ c ^ d) + x + 0x6ed9eba1, s);
    }
}
