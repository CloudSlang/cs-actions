/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.utilities.services.base64coder;

import io.cloudslang.content.utilities.entities.Base64DecoderToFileInputs;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64DecoderToFileImpl {

    @NotNull
    public static String writeBytesToFile(Base64DecoderToFileInputs base64DecoderToFileInputs) throws IOException {
        byte[] data = Base64.decodeBase64(base64DecoderToFileInputs.getContentBytes());
        try (OutputStream stream = new FileOutputStream(base64DecoderToFileInputs.getFilePath())) {
            stream.write(data);
        }
        return base64DecoderToFileInputs.getFilePath();
    }
}
