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
package io.cloudslang.content.utilities.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RandomPasswordGeneratorInputs {

    private final String passwordLength;
    private final String numberOfLowerCaseCharacters;
    private final String numberOfUpperCaseCharacters;
    private final String numberOfNumericalCharacters;
    private final String numberOfSpecialCharacters;
    private final String forbiddenCharacters;

    @NotNull
    public String getPasswordLength() {
        return passwordLength;
    }

    @NotNull
    public String getNumberOfLowerCaseCharacters() {
        return numberOfLowerCaseCharacters;
    }

    @NotNull
    public String getNumberOfUpperCaseCharacters() {
        return numberOfUpperCaseCharacters;
    }

    @NotNull
    public String getNumberOfNumericalCharacters() {
        return numberOfNumericalCharacters;
    }

    @NotNull
    public String getNumberOfSpecialCharacters() {
        return numberOfSpecialCharacters;
    }

    @NotNull
    public String getForbiddenCharacters() {
        return forbiddenCharacters;
    }


    @java.beans.ConstructorProperties({"passwordLength", "numberOfLowerCaseCharacters","numberOfUpperCaseCharacters","numberOfNumericalCharacters"
            ,"numberOfSpecialCharacters","forbiddenCharacters"})
    private RandomPasswordGeneratorInputs(String passwordLength, String numberOfLowerCaseCharacters, String numberOfUpperCaseCharacters,
                                          String numberOfNumericalCharacters, String numberOfSpecialCharacters, String forbiddenCharacters) {
        this.passwordLength = passwordLength;
        this.numberOfLowerCaseCharacters = numberOfLowerCaseCharacters;
        this.numberOfUpperCaseCharacters = numberOfUpperCaseCharacters;
        this.numberOfNumericalCharacters = numberOfNumericalCharacters;
        this.numberOfSpecialCharacters = numberOfSpecialCharacters;
        this.forbiddenCharacters = forbiddenCharacters;

    }

    @NotNull
    public static RandomPasswordGeneratorInputsBuilder builder() {
        return new RandomPasswordGeneratorInputsBuilder();
    }

    public static class RandomPasswordGeneratorInputsBuilder {

        String passwordLength = EMPTY;
        String numberOfLowerCaseCharacters = EMPTY;
        String numberOfUpperCaseCharacters = EMPTY;
        String numberOfNumericalCharacters = EMPTY;
        String numberOfSpecialCharacters = EMPTY;
        String forbiddenCharacters = EMPTY;



        RandomPasswordGeneratorInputsBuilder() {
        }

        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder passwordLength(@NotNull final String passwordLength) {
            this.passwordLength = passwordLength;
            return this;
        }


        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder numberOfLowerCaseCharacters(@NotNull final String numberOfLowerCaseCharacters) {
            this.numberOfLowerCaseCharacters = numberOfLowerCaseCharacters;
            return this;
        }

        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder numberOfUpperCaseCharacters(@NotNull final String numberOfUpperCaseCharacters) {
            this.numberOfUpperCaseCharacters = numberOfUpperCaseCharacters;
            return this;
        }

        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder numberOfNumericalCharacters(@NotNull final String numberOfNumericalCharacters) {
            this.numberOfNumericalCharacters = numberOfNumericalCharacters;
            return this;
        }

        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder numberOfSpecialCharacters(@NotNull final String numberOfSpecialCharacters) {
            this.numberOfSpecialCharacters = numberOfSpecialCharacters;
            return this;
        }

        @NotNull
        public RandomPasswordGeneratorInputs.RandomPasswordGeneratorInputsBuilder forbiddenCharacters(@NotNull final String forbiddenCharacters) {
            this.forbiddenCharacters = forbiddenCharacters;
            return this;
        }

        public RandomPasswordGeneratorInputs build() {
            return new RandomPasswordGeneratorInputs(passwordLength, numberOfLowerCaseCharacters, numberOfUpperCaseCharacters,
                    numberOfNumericalCharacters, numberOfSpecialCharacters, forbiddenCharacters);
        }
    }

}

