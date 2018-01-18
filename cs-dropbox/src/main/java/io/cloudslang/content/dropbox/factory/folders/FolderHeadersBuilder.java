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

package io.cloudslang.content.dropbox.factory.folders;

import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;

import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.CREATE_FOLDER;
import static io.cloudslang.content.dropbox.entities.constants.Constants.FolderActions.DELETE_FILE_OR_FOLDER;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.AUTHORIZATION_HEADER_PREFIX;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Miscellaneous.BLANK_CHAR;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class FolderHeadersBuilder {
    private FolderHeadersBuilder() {
        // prevent instantiation
    }

    public static void setFolderHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_FOLDER:
                wrapper.getHttpClientInputs().setHeaders(AUTHORIZATION_HEADER_PREFIX + BLANK_CHAR + wrapper.getCommonInputs().getAccessToken());
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
                break;
            case DELETE_FILE_OR_FOLDER:
                wrapper.getHttpClientInputs().setHeaders(AUTHORIZATION_HEADER_PREFIX + BLANK_CHAR + wrapper.getCommonInputs().getAccessToken());
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
                break;
            default:
                break;
        }
    }
}