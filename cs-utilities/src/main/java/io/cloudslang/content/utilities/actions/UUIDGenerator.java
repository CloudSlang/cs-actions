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
package io.cloudslang.content.utilities.actions;

import com.fasterxml.uuid.Generators;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;
import java.util.UUID;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.NAME_VALUE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.VERSION_VALUE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OperationDescription.GENERATE_UUID_BY_VERSION;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Inputs.NAME;
import static io.cloudslang.content.utilities.entities.constants.Inputs.VERSION;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class UUIDGenerator {

    @Action(name = "Generate UUID",
            description = GENERATE_UUID_BY_VERSION,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESCRIPTION),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = NAME, description = NAME_VALUE_DESC) String name,
            @Param(value = VERSION, description = VERSION_VALUE_DESC) String version) {
        UUID uuid;
        final String nameValue = defaultIfEmpty(name, EMPTY);
        final String versionValue = defaultIfEmpty(version, "1");

        switch (versionValue) {
            case "1":
                uuid = Generators.timeBasedGenerator().generate();
                return getSuccessResultsMap(uuid.toString());
            case "3":
                uuid = Generators.nameBasedGenerator().generate(nameValue);
                return getSuccessResultsMap(uuid.toString());
            case "4":
                uuid = Generators.randomBasedGenerator().generate();
                return getSuccessResultsMap(uuid.toString());
            case "5":
                uuid = Generators.randomBasedGenerator().generate();
                return getSuccessResultsMap(uuid.toString());
            default:
                return getFailureResultsMap("Invalid UUID version");
        }
    }
}



