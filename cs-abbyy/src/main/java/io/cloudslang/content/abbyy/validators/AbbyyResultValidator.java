/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyRequest;
import org.jetbrains.annotations.NotNull;

public abstract class AbbyyResultValidator {
    public ValidationException validateBeforeDownload(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String url) throws Exception {
        try {
            validateBefore(abbyyInitialRequest, url);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    public ValidationException validateAfterDownload(@NotNull String result) throws Exception {
        try {
            validateAfter(result);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    abstract void validateBefore(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String url) throws Exception;

    abstract void validateAfter(@NotNull String result) throws Exception;
}
