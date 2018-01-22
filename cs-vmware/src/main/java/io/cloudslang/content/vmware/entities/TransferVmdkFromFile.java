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

package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.OvfUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

public class TransferVmdkFromFile implements ITransferVmdkFrom {

    private FileInputStream fis;

    public TransferVmdkFromFile(final File vmdkFile) throws IOException {
        fis = new FileInputStream(vmdkFile);
    }

    @Override
    public long uploadTo(final OutputStream outputStream, final ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        final byte[] buffer = new byte[SIZE_4K];
        int read;
        try (final InputStream is = fis) {
            while (0 <= (read = is.read(buffer))) {
                bytesCopied = OvfUtils.writeToStream(outputStream, progressUpdater, bytesCopied, buffer, read);
            }
        }
        return bytesCopied;
    }
}
