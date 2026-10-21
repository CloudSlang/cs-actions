/*
 * Copyright 2022-2024 Open Text
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

package io.cloudslang.content.utilities;

import io.cloudslang.content.utilities.actions.RandomPasswordGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomPasswordGeneratorTest {

    @Test
    void generatesPasswordUsingPassayTwoRules() {
        Map<String, String> result = new RandomPasswordGenerator().execute("12", "2", "2", "2", "2", "!");

        assertEquals(SUCCESS, result.get(RETURN_CODE));
        String password = result.get(RETURN_RESULT);
        assertEquals(12, password.length());
        assertTrue(countMatches(password, c -> c >= 'a' && c <= 'z') >= 2);
        assertTrue(countMatches(password, c -> c >= 'A' && c <= 'Z') >= 2);
        assertTrue(countMatches(password, c -> c >= '0' && c <= '9') >= 2);
        assertTrue(countMatches(password, c -> !Character.isLetterOrDigit(c)) >= 2);
        assertFalse(password.contains("!"));
    }

    private long countMatches(String value, java.util.function.IntPredicate predicate) {
        return value.chars().filter(predicate).count();
    }
}
