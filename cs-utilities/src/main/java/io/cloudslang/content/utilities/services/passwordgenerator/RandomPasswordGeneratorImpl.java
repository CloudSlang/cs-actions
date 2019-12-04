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
package io.cloudslang.content.utilities.services.passwordgenerator;

import io.cloudslang.content.utilities.entities.RandomPasswordGeneratorInputs;
import io.cloudslang.content.utilities.util.randomPasswordGenerator.Characters;
import io.cloudslang.content.utilities.util.randomPasswordGenerator.SpecialCharacters;
import org.jetbrains.annotations.NotNull;
import org.passay.CharacterRule;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

public class RandomPasswordGeneratorImpl {

    @NotNull
    public static String generatePassword(RandomPasswordGeneratorInputs randomPasswordGeneratorInputs) throws Exception {
        List<CharacterRule> rules = generatePasswordRules(randomPasswordGeneratorInputs);
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        return passwordGenerator.generatePassword(Integer.parseInt(randomPasswordGeneratorInputs.getPasswordLength()), rules);
    }

    private static List<CharacterRule> generatePasswordRules(RandomPasswordGeneratorInputs randomPasswordGeneratorInputs) {
        List<CharacterRule> rules = new ArrayList<>();

        if (Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfLowerCaseCharacters()) > 0) {
            rules.add(new CharacterRule(Characters.LowerCase, Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfLowerCaseCharacters())));
        }
        if (Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfUpperCaseCharacters()) > 0) {
            rules.add(new CharacterRule(Characters.UpperCase, Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfUpperCaseCharacters())));
        }
        if (Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfNumericalCharacters()) > 0) {
            rules.add(new CharacterRule(Characters.Digit, Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfNumericalCharacters())));
        }
        if (Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfSpecialCharacters()) > 0) {
            if (randomPasswordGeneratorInputs.getForbiddenCharacters().isEmpty()) {
                rules.add(new CharacterRule(Characters.Special, Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfSpecialCharacters())));
            } else {
                rules.add(new CharacterRule(new SpecialCharacters(randomPasswordGeneratorInputs.getForbiddenCharacters()),
                        Integer.parseInt(randomPasswordGeneratorInputs.getNumberOfSpecialCharacters())));
            }
        }
        return rules;
    }

}