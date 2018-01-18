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

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class TransferVmdkToUrl implements ITransferVmdkTo {

    private static final int CHUNK_LENGTH = 524288;

    private OutputStream outputStream;

    public TransferVmdkToUrl(final URL uploadURL, final boolean create) throws IOException {
        final HttpsURLConnection conn = Uploader.getHTTPSUploadConnection(uploadURL, CHUNK_LENGTH, 0, create);
        outputStream = conn.getOutputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
