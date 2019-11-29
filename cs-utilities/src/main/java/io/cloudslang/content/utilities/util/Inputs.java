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


package io.cloudslang.content.utilities.util;

import io.cloudslang.content.constants.InputNames;

public final class Inputs extends InputNames {

    public static class Base64CoderInputs {
        public static final String FILE_PATH = "filePath";
        public static final String CONTENT_BYTES = "contentBytes";
    }

    public static class RandomPasswordGeneratorInputsNames {
        public static final String PASSWORD_LENGTH = "passwordLength";
        public static final String NUMBER_OF_LOWER_CASE_CHARACTERS = "numberOfLowerCaseCharacters";
        public static final String NUMBER_OF_UPPER_CASE_CHARACTERS = "numberOfUpperCaseCharacters";
        public static final String NUMBER_OF_NUMERICAL_CHARACTERS = "numberOfNumericalCharacters";
        public static final String NUMBER_OF_SPECIAL_CHARACTERS = "numberOfSpecialCharacters";
        public static final String FORBIDDEN_CHARACTERS = "forbiddenCharacters";
        public static final String SPECIFIC_FORMAT = "specificFormat";

    }

}

