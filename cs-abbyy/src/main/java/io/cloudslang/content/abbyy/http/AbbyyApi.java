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
package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.HttpException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface AbbyyApi {

    String getLastStatusCode();

    AbbyyResponse postRequest(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String url) throws Exception;

    AbbyyResponse getTaskStatus(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String taskId) throws Exception;

    String getResult(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                     @Nullable String downloadPath, boolean useSpecificCharSet) throws AbbyySdkException, IOException, HttpException;

    long getResultSize(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat)
            throws AbbyySdkException, IOException, HttpException;

    String getResultChunk(@NotNull AbbyyRequest abbyyInitialRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                          int startByteIndex, int endByteIndex) throws AbbyySdkException, IOException, HttpException;
}
