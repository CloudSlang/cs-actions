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

package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * Created by vranau on 12/10/2014.
 */
public class CustomDatabaseTest {
    public static final String CUSTOM_CLASS_DRIVER = "org.h2.Driver";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUp() throws ClassNotFoundException {
        CustomDatabase customDatabase = new CustomDatabase();
        SQLInputs sqlInput = SQLInputs.builder().build();
        sqlInput.setDbClass(CUSTOM_CLASS_DRIVER);
        customDatabase.setUp(sqlInput);
    }

    @Test
    public void testSetUpNoClassName() throws ClassNotFoundException {

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("No db class name provided");
        CustomDatabase customDatabase = new CustomDatabase();
        SQLInputs sqlInput = SQLInputs.builder().build();
        sqlInput.setDbClass(EMPTY);
        customDatabase.setUp(sqlInput);
    }
}
