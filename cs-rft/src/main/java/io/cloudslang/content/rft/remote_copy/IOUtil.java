/*
 * Copyright 2021-2024 Open Text
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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.cloudslang.content.rft.remote_copy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.apache.commons.io.IOUtils;

public class IOUtil {
    public IOUtil() {
    }

    public static void copyAll(InputStream in, OutputStream out, boolean closeInput, boolean closeOutput) throws IOException {
        try {
            if (in.available() > 2147483645) {
                IOUtils.copyLarge(in, out);
            } else {
                byte[] buffer = in.available() > 1024 ? new byte[in.available()] : new byte[1024];

                int read;
                while((read = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, read);
                }

                out.flush();
            }
        } finally {
            if (closeInput) {
                in.close();
            }

            if (closeOutput) {
                out.close();
            }

        }

    }

    public static String InputStreamToString(InputStream is) throws IOException {
        return InputStreamToString(is, Charset.defaultCharset());
    }

    public static String InputStreamToString(InputStream is, Charset charSet) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String line = null;

        try {
            br = new BufferedReader(new InputStreamReader(is, charSet));

            while(null != (line = br.readLine())) {
                sb.append(line + "\n");
            }

            br.close();
            return sb.toString();
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
            } catch (Exception var11) {
            }

        }
    }

    public static String InputStreamToString(InputStream is, String charSetName) throws IOException {
        return InputStreamToString(is, Charset.forName(charSetName));
    }

    public static ByteBuffer InputStreamToByteBuffer(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];

        while(is.available() > 0) {
            int read = is.read(buf);
            out.write(buf, 0, read);
        }

        ByteBuffer bb = ByteBuffer.wrap(out.toByteArray());
        return bb;
    }

    public static String read(InputStream in) throws IOException {
        StringBuffer readBuffer = new StringBuffer();

        while(in.available() > 0) {
            byte[] buff = new byte[in.available()];
            int charsRead = in.read(buff);
            if (charsRead > 0) {
                readBuffer.append(new String(buff, 0, charsRead));
            }
        }

        return readBuffer.toString();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        }
    }

    public static String decodeBytes(byte[] bb) throws IOException {
        if (bb == null) {
            throw new IllegalArgumentException("null byte buffer");
        } else {
            return decodeBytes(ByteBuffer.wrap(bb));
        }
    }

    public static String decodeBytes(ByteBuffer bb) throws IOException {
        return decodeBytes(bb, Charset.defaultCharset());
    }

    public static String decodeBytes(byte[] bb, Charset cset) throws IOException {
        if (bb == null) {
            throw new IllegalArgumentException("null byte buffer");
        } else {
            return decodeBytes(ByteBuffer.wrap(bb), cset);
        }
    }

    public static String decodeBytes(ByteBuffer bb, Charset cset) throws IOException {
        if (bb == null) {
            throw new IllegalArgumentException("null byte buffer");
        } else {
            StringBuilder buf = new StringBuilder(bb.remaining());
            decodeBytes(bb, cset, buf);
            return buf.toString();
        }
    }

    public static void decodeBytes(ByteBuffer bb, Charset cset, Appendable target) throws IOException {
        if (bb == null) {
            throw new IllegalArgumentException("null byte buffer");
        } else if (cset == null) {
            throw new IllegalArgumentException("null charset");
        } else if (target == null) {
            throw new IllegalArgumentException("null target");
        } else {
            CharsetDecoder decoder = cset.newDecoder();
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
            decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            CharBuffer cb = CharBuffer.allocate(1024);

            boolean eof;
            do {
                eof = !bb.hasRemaining();
                if (CoderResult.OVERFLOW == decoder.decode(bb, cb, eof)) {
                    drainCharBuf(cb, target);
                }
            } while(!eof);

            while(CoderResult.OVERFLOW == decoder.flush(cb)) {
                drainCharBuf(cb, target);
            }

            drainCharBuf(cb, target);
        }
    }

    private static void drainCharBuf(CharBuffer cb, Appendable target) throws IOException {
        cb.flip();
        if (cb.hasRemaining()) {
            target.append(cb.toString());
        }

        cb.clear();
    }
}
