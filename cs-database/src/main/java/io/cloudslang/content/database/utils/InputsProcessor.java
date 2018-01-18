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

package io.cloudslang.content.database.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 13.01.2017.
 */
public class InputsProcessor {

    /**
     * init all the non-static variables.
     *
     * @param sqlInputs
     */
    public static void init(SQLInputs sqlInputs) throws Exception {
        if (sqlInputs != null) {
            sqlInputs.setStrDelim(",");
            sqlInputs.setStrColumns("");
            sqlInputs.setLRows(new ArrayList<String>());
            sqlInputs.setIUpdateCount(0);
            sqlInputs.setSqlCommand(null);
            sqlInputs.setDbServer(null);
            sqlInputs.setDbName(null);
            sqlInputs.setDbType(null);
            sqlInputs.setUsername(null);
            sqlInputs.setPassword(null);
            sqlInputs.setAuthenticationType(null);
            sqlInputs.setDbUrl(null);
            sqlInputs.setDbClass(null);
            sqlInputs.setNetcool(false);
            sqlInputs.setLRowsFiles(new ArrayList<List<String>>());
            sqlInputs.setLRowsNames(new ArrayList<List<String>>());
            sqlInputs.setSkip(0L);
            sqlInputs.setInstance(null);
            sqlInputs.setTimeout(0);
            sqlInputs.setKey(null);
            sqlInputs.setIgnoreCase(false);
            sqlInputs.setResultSetConcurrency(-1000000);
            sqlInputs.setResultSetType(-1000000);
            sqlInputs.setTrustAllRoots(false);
            sqlInputs.setTrustStore("");
        } else throw new Exception("Cannot init null Sql inputs!");
    }
}
