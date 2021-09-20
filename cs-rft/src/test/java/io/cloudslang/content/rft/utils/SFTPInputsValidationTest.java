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

import static org.junit.Assert.assertEquals;

public class SFTPInputsValidationTest {

    @Test
    public void verifyValidSFTPPInputs() {
        List<String> exceptions = InputsValidation.verifyInputsSFTP(
                "someHost",
                "21",
                "username",
                "password",
                "8080",
                "UTF-8",
                "true",
                SFTPOperation.PUT,
                "specificinput.txt",
                "specificinput2.txt",
                "60",
                "60");
        int numberOfExceptions = exceptions.size();
        assertEquals(numberOfExceptions, 0);
    }

    @Test
    public void verifyInvalidSFTPInputs() {
        List<String> exceptions = InputsValidation.verifyInputsSFTP(
                null,
                "212222",
                null,
                "",
                "",
                "",
                "",
                SFTPOperation.GET,
                "",
                "",
                "invalid",
                "");
        int numberOfExceptions = exceptions.size();
        assertEquals(numberOfExceptions, 10);
    }

}
