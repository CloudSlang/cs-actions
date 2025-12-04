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
package io.cloudslang.content.mail.handlers;

import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minimal DataContentHandler for image/* types that streams bytes directly.
 * This serves as a fallback when com.sun.mail handlers are not present at runtime.
 */
public class ImageDataContentHandler implements DataContentHandler {

    private static final DataFlavor BYTE_ARRAY_FLAVOR = new DataFlavor(byte[].class, "application/octet-stream");

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{BYTE_ARRAY_FLAVOR};
    }

    @Override
    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        if (BYTE_ARRAY_FLAVOR.equals(df)) {
            return getContent(ds);
        }
        return null;
    }

    @Override
    public Object getContent(DataSource ds) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = ds.getInputStream();
            byte[] tmp = new byte[8192];
            int read;
            while ((read = is.read(tmp)) != -1) {
                buffer.write(tmp, 0, read);
            }
            return buffer.toByteArray();
        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
    }

    @Override
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (obj instanceof byte[]) {
            os.write((byte[]) obj);
            return;
        }
        // Fallback: stream from data source if possible
        if (obj instanceof DataSource) {
            InputStream is = null;
            try {
                is = ((DataSource) obj).getInputStream();
                byte[] tmp = new byte[8192];
                int read;
                while ((read = is.read(tmp)) != -1) {
                    os.write(tmp, 0, read);
                }
            } finally {
                if (is != null) {
                    try { is.close(); } catch (IOException ignored) {}
                }
            }
            return;
        }
        throw new IOException("Unsupported object type for ImageDataContentHandler: " + (obj != null ? obj.getClass() : "null"));
    }
}
