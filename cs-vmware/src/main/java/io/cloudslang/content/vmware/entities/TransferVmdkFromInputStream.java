/*
 * Copyright 2019-2024 Open Text
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


package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.OvfUtils;

import java.io.InputStream;
import java.io.OutputStream;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

public class TransferVmdkFromInputStream implements ITransferVmdkFrom {

    private static final String INPUT_STREAM_MAY_NOT_BE_NULL = "Source input stream may not be null";
    private final InputStream inputStream;
    private final long length;

    public TransferVmdkFromInputStream(final InputStream inputStream, long length) {
        if (inputStream == null) {
            throw new IllegalArgumentException(INPUT_STREAM_MAY_NOT_BE_NULL);
        }
        this.inputStream = inputStream;
        this.length = length;
    }

    @Override
    public long uploadTo(final OutputStream outputStream, final ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        final byte[] buffer = new byte[SIZE_4K];
        try (InputStream is = inputStream) {
            while (length > bytesCopied) {
                final int bytesRead = is.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break;
                }
                bytesCopied = OvfUtils.writeToStream(outputStream, progressUpdater, bytesCopied, buffer, bytesRead);
            }
        }
        return bytesCopied;
    }
}
