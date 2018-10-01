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

package io.cloudslang.content.dropbox.factory;

import io.cloudslang.content.dropbox.entities.inputs.CommonInputs;
import io.cloudslang.content.dropbox.entities.inputs.FolderInputs;
import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.dropbox.entities.constants.Constants.ErrorMessages.UNKNOWN_BUILDER_TYPE;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Values.INIT_INDEX;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public class InputsWrapperBuilder {
    private InputsWrapperBuilder() {
        // prevent instantiation
    }

    @SafeVarargs
    public static <T> InputsWrapper buildWrapper(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders) {
        InputsWrapper wrapper = new InputsWrapper.Builder()
                .withHttpClientInputs(httpClientInputs)
                .withCommonInputs(commonInputs)
                .build();

        if (builders.length > INIT_INDEX) {
            for (T builder : builders) {
                if (builder instanceof FolderInputs) {
                    wrapper.setFolderInputs((FolderInputs) builder);
                } else {
                    throw new RuntimeException(UNKNOWN_BUILDER_TYPE);
                }
            }
        }

        return wrapper;
    }
}