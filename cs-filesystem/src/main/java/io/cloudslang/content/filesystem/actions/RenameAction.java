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
package io.cloudslang.content.filesystem.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.filesystem.constants.InputNames;
import io.cloudslang.content.filesystem.constants.OutputNames;
import io.cloudslang.content.filesystem.entities.RenameInputs;
import io.cloudslang.content.filesystem.services.RenameService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;

public class RenameAction {

    private final RenameService service = new RenameService();


    /**
     * Renames a file or a directory.
     *
     * @param source    Absolute path to the file or directory that will be renamed.
     * @param newName   The new name for the file or directory.
     *                  The value of this input should contain only the name and extension of the file
     *                  and not the full path or parent folders names.
     *                  Example: "file.txt".
     * @param overwrite Optional. If set to "false" the operation will fail if "newName" exists.
     *                  Valid values: true, false.
     *                  Default value: false.
     * @return a map with following entries:
     * renamedPath: The absolute path of the renamed file if operation succeeded. Empty otherwise.
     * returnResult: A message describing the success or failure of the operation.
     * returnCode: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */
    @Action(name = "Rename",
            outputs = {
                    @Output(OutputNames.RENAMED_PATH),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_CODE),
                    @Output(io.cloudslang.content.constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE,
                            field = RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL,
                            isOnFail = true, isDefault = true)
            })
    public Map<String, String> execute(@Param(value = InputNames.SOURCE, required = true) String source,
                                       @Param(value = InputNames.NEW_NAME, required = true) String newName,
                                       @Param(value = InputNames.OVERWRITE) String overwrite) {
        try {
            RenameInputs inputs = new RenameInputs.Builder()
                    .source(source)
                    .newName(newName)
                    .overwrite(overwrite)
                    .build();
            return this.service.execute(inputs);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
