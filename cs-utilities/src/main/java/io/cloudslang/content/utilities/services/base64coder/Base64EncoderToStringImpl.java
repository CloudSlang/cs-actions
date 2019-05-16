/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

import io.cloudslang.content.utilities.entities.Base64EncoderInputs;
import io.cloudslang.content.utilities.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64EncoderToStringImpl {
    /**
     * This method encodes the content of a file into base64
     *
     * @param base64EncoderInputs - required input fot the @Action (filepath)
     * @return - the encoded value
     * @throws Exception - specific error in case the file does not exist in the provided path
     */
    @NotNull
    public static String displayEncodedBytes(Base64EncoderInputs base64EncoderInputs) throws Exception {
        String stringValueBase64Encoded;
        File file = new File(base64EncoderInputs.getFilePath());
        if (file.exists()) {
            if (file.isFile()) {
                stringValueBase64Encoded = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(base64EncoderInputs.getFilePath())));
            } else {
                throw new Exception(Constants.ENCODE_IS_NO_FILE_EXCEPTION);
            }
        } else {
            throw new Exception(Constants.ENCODE_NO_FILE_EXCEPTION);
        }
        return stringValueBase64Encoded;
    }
}