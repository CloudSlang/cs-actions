/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.utils;

import org.junit.Test;

import java.util.List;

import static io.cloudslang.content.rft.utils.Constants.ASCII_FILE_TYPE;
import static io.cloudslang.content.rft.utils.Constants.CHARACTER_SET_LATIN1;
import static org.junit.Assert.assertEquals;


public class FTPInputsValidationTest {

    @Test
    public void verifyValidFTPInputs() {
        List<String> exceptions = InputsValidation.verifyInputsFTP("someHost",
                "21",
                "exitingFile.txt",
                "existingRemoteFile.txt",
                "user",
                "password",
                ASCII_FILE_TYPE,
                "TRuE",
                CHARACTER_SET_LATIN1);
        int numberOfExceptions = exceptions.size();
        assertEquals(numberOfExceptions,0);
    }

    @Test
    public void verifyInvalidFTPInputs() {
        List<String> exceptions = InputsValidation.verifyInputsFTP(null,
                "wrongPort",
                "",
                "",
                "",
                "",
                null,
                "wrongboolean",
                "wrongCharSet");
        int numberOfExceptions = exceptions.size();
        assertEquals(numberOfExceptions,9);
    }


}