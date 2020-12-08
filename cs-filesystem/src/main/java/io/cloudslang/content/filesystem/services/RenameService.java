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
package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.constants.Constants;
import io.cloudslang.content.filesystem.constants.OutputNames;
import io.cloudslang.content.filesystem.entities.RenameInputs;
import io.cloudslang.content.filesystem.utils.ValidationUtils;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;

public class RenameService {
    public @NotNull Map<String, String> execute(@NotNull RenameInputs inputs) throws Exception {
        ValidationUtils.validate(inputs);

        Path source = inputs.getSource();
        Path destination = inputs.getSource().resolveSibling(inputs.getNewName());
        if (Files.exists(destination, LinkOption.NOFOLLOW_LINKS) && inputs.isOverwrite()) {
            if (Files.isDirectory(destination, LinkOption.NOFOLLOW_LINKS)) {
                FileUtils.deleteDirectory(destination.toFile());
            } else {
                Files.delete(destination);
            }
        }
        Files.move(source, destination);

        Map<String, String> results = OutputUtilities.getSuccessResultsMap(Constants.RENAME_OPERATION_SUCCEEDED);
        results.put(OutputNames.RENAMED_PATH, destination.toString());
        return results;
    }
}
