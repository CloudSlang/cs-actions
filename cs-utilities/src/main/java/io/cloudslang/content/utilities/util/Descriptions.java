/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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

public class Descriptions {

    public static class ConvertBytesToFile {
        public static final String BASE64_DECODER_TO_FILE = "Base64 Decoder to File";
        public static final String CONTENT_BYTES_DESC = "The representation in bytes of the file that will be decoded";
        public static final String FILE_PATH_DESC = "The absolute path with file name and extension that will be converted";
        public static final String SUCCESS_DESC = "The file was decoded successfully";

        public static final String FAILURE_DESC = "There was an error while trying to decode the file";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while decoding the file";

        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise";
        public static final String RETURN_PATH_DESC = "The path to the decoded file";
        public static final String RETURN_RESULT_DESC = "The path of the decoded file or error message in case of failure";

    }

    public static class EncodeFileToStringOutput {
        public static final String BASE64_ENCODER_FROM_FILE = "Base64 Encoder from File.";
        public static final String BASE64_ENCODER_FROM_FILE_DESC = "This operation encodes a file to base64.";
        public static final String FILE_PATH_DESC = "The absolute path with file name and extension that will be read.";

        public static final String EXCEPTION_DESC = "An error message in case there was an error while encoding the value read from file.";
        public static final String FAILURE_DESC = "An error occurred while trying to encode the value from a file.";

        public static final String SUCCESS_DESC = "The file was read and encoded successfully.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String RETURN_RESULT_DESC = "The encoded value or error message in case of failure.";
        public static final String RETURN_VALUE_DESC = "The returned value after the encode.";

    }

    public static class RandomPasswordGeneratorDescriptions{
        public static final String RANDOM_PASSWORD_GENERATOR = "Random Password Generator";
        public static final String SUCCESS_DESC = "The password was generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to generate the password.";

        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the password.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String RETURN_RESULT_DESC = "The generated password or error message in case of failure.";

        public static final String PASSWORD_LENGTH_DESC = "The length of the generated password. It must be a positive integer. Default: 10.";
        public static final String NUMBER_OF_LOWER_CASE_CHARACTERS_DESC = "The minimum number of lower case characters that the password should contain. If 0 it won't contain this type of character. Default: 1.";
        public static final String NUMBER_OF_UPPER_CASE_CHARACTERS_DESC = "The minimum number of upper case characters that the password should contain. If 0 it won't contain this type of character. Default: 1.";
        public static final String NUMBER_OF_NUMERICAL_CHARACTERS_DESC = "The minimum number of numerical characters that the password should contain. If 0 it won't contain this type of character. Default: 1.";
        public static final String NUMBER_OF_SPECIAL_CHARACTERS_DESC = "The minimum number of special characters that the password should contain. If 0 it won't contain this " +
                "type of character. Default: 1. Used special characters are: -=[];,.~!@#$%&*()_+{}|:<>?";
        public static final String FORBIDDEN_CHARACTERS_DESC = "A list of characters that the password should not contain. Example: []{}.";
    }

    public static class StringEqualsDescriptions{
        public static final String FIRST_STRING_DESC = "First string to compare.";
        public static final String SECOND_STRING_DESC = "Second string to compare.";
        public static final String IGNORE_CASE_DESC = "If set to 'true', then the comparison ignores case considerations. The two strings are\n" +
                "considered equal ignoring case if they are of the same length and corresponding characters in the\n" +
                "two strings are equal ignoring case. If set to any value other than 'true', then the strings must\n" +
                "match exactly to be considered equal";
    }

    public static class IsTrueDescriptions{
        public static final String BOOL_VALUE_DESC = "Boolean value to check.";
    }

}