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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.cloudslang.content.database.constants.DBOtherValues.FORWARD_SLASH;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbUrls;

/**
 * Created by victor on 13.01.2017.
 */
public class NetcoolDatabase implements SqlDatabase {

    @Override
    public List<String> setUp(@NotNull final SQLInputs sqlInputs) {
        //Attempt to load jconn3 driver first, then jconn2 driver
        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
        } catch (Exception e) {
            try {
                Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Could not locate either jconn2.jar or jconn3.jar file in the classpath!");
            }
        }
        final StringBuilder connectionSb = new StringBuilder(String.format("jdbc:sybase:Tds:%s:%d",
                sqlInputs.getDbServer(), sqlInputs.getDbPort()));
        if (StringUtils.isNoneEmpty(sqlInputs.getDbName())) {
            connectionSb.append(FORWARD_SLASH)
                    .append(sqlInputs.getDbName());
        }
        final List<String> dbUrls = getDbUrls(sqlInputs.getDbUrl());
        dbUrls.add(connectionSb.toString());

        return dbUrls;
    }
}
