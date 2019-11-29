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
package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.entities.RandomPasswordGeneratorInputs;
import io.cloudslang.content.utilities.services.passwordgenerator.RandomPasswordGeneratorImpl;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.util.Constants.*;
import static io.cloudslang.content.utilities.util.Descriptions.RandomPasswordGeneratorDescriptions.*;
import static io.cloudslang.content.utilities.util.Inputs.RandomPasswordGeneratorInputsNames.*;
import static io.cloudslang.content.utilities.util.InputsValidation.verifyGenerateRandomPasswordInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class RandomPasswordGenerator {
    @Action(name = RANDOM_PASSWORD_GENERATOR,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = PASSWORD_LENGTH, description = PASSWORD_LENGTH_DESC) String passwordLength,
                                       @Param(value = NUMBER_OF_LOWER_CASE_CHARACTERS, description = NUMBER_OF_LOWER_CASE_CHARACTERS_DESC) String numberOfLowerCaseCharacters,
                                       @Param(value = NUMBER_OF_UPPER_CASE_CHARACTERS, description = NUMBER_OF_UPPER_CASE_CHARACTERS_DESC) String numberOfUpperCaseCharacters,
                                       @Param(value = NUMBER_OF_NUMERICAL_CHARACTERS, description = NUMBER_OF_NUMERICAL_CHARACTERS_DESC) String numberOfNumericalCharacters,
                                       @Param(value = NUMBER_OF_SPECIAL_CHARACTERS, description = NUMBER_OF_SPECIAL_CHARACTERS_DESC) String numberOfSpecialCharacters,
                                       @Param(value = FORBIDDEN_CHARACTERS, description = FORBIDDEN_CHARACTERS_DESC) String forbiddenCharacters,
                                       @Param(value = SPECIFIC_FORMAT, description = SPECIFIC_FORMAT_DESC) String specificFormat) {


        passwordLength = defaultIfEmpty(passwordLength, DEFAULT_PASSWORD_LENGTH);
        numberOfLowerCaseCharacters = defaultIfEmpty(numberOfLowerCaseCharacters, ONE);
        numberOfUpperCaseCharacters = defaultIfEmpty(numberOfUpperCaseCharacters, ONE);
        numberOfNumericalCharacters = defaultIfEmpty(numberOfNumericalCharacters, ONE);
        numberOfSpecialCharacters = defaultIfEmpty(numberOfSpecialCharacters, ONE);
        forbiddenCharacters = defaultIfEmpty(forbiddenCharacters, EMPTY);
        specificFormat = defaultIfEmpty(specificFormat, EMPTY);

        try {

            final List<String> exceptionMessages = verifyGenerateRandomPasswordInputs(passwordLength, numberOfLowerCaseCharacters,
                    numberOfUpperCaseCharacters, numberOfNumericalCharacters, numberOfSpecialCharacters);

            if (!exceptionMessages.isEmpty()) {
                return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
            }

            final RandomPasswordGeneratorInputs randomPasswordGeneratorInputs = RandomPasswordGeneratorInputs.builder()
                    .passwordLength(passwordLength)
                    .numberOfLowerCaseCharacters(numberOfLowerCaseCharacters)
                    .numberOfUpperCaseCharacters(numberOfUpperCaseCharacters)
                    .numberOfNumericalCharacters(numberOfNumericalCharacters)
                    .numberOfSpecialCharacters(numberOfSpecialCharacters)
                    .forbiddenCharacters(forbiddenCharacters)
                    .specificFormat(specificFormat)
                    .build();

            final String resultPassword = RandomPasswordGeneratorImpl.generatePassword(randomPasswordGeneratorInputs);

            return getSuccessResultsMap(resultPassword);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
