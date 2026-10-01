/*
 * Copyright 2022-2025 Open Text
 * This program and the accompanying materials
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
/*
 * Copyright 2026 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.httpclient.utils;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputsValidatorTest {

    @Test
    public void addVerifyDestinationFileAllowsAnOmittedDestinationFile() {
        List<String> exceptions = new ArrayList<>();

        InputsValidator.addVerifyDestinationFile(exceptions, null, "destinationFile");
        InputsValidator.addVerifyDestinationFile(exceptions, "", "destinationFile");

        assertTrue(exceptions.isEmpty());
    }

    @Test
    public void addVerifyDestinationFileRejectsAnInvalidDestinationFile() {
        List<String> exceptions = new ArrayList<>();
        String destinationFile = new File("nonexistent-directory", "response.txt").getPath();

        InputsValidator.addVerifyDestinationFile(exceptions, destinationFile, "destinationFile");

        assertFalse(exceptions.isEmpty());
    }
}
