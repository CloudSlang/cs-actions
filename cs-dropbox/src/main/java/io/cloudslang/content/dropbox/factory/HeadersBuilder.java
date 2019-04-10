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



package io.cloudslang.content.dropbox.factory;

import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;

import static io.cloudslang.content.dropbox.entities.constants.Constants.Api.FOLDERS;
import static io.cloudslang.content.dropbox.entities.constants.Constants.ErrorMessages.UNKNOWN_DROPBOX_HEADER;
import static io.cloudslang.content.dropbox.factory.folders.FolderHeadersBuilder.setFolderHeaders;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class HeadersBuilder {
    private HeadersBuilder() {
        // prevent instantiation
    }

    public static void buildHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getApi()) {
            case FOLDERS:
                setFolderHeaders(wrapper);
                break;
            default:
                throw new RuntimeException(UNKNOWN_DROPBOX_HEADER);
        }
    }
}