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



package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.cloudslang.content.database.constants.DBOtherValues.FORWARD_SLASH;
import static io.cloudslang.content.database.utils.Constants.COLON;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbUrls;
import static io.cloudslang.content.database.utils.SQLUtils.loadClassForName;

/**
 * Created by victor on 13.01.2017.
 */
public class DB2Database implements SqlDatabase {

    @Override
    public List<String> setUp(@NotNull final SQLInputs sqlInputs) {
        loadClassForName("com.ibm.db2.jcc.DB2Driver");

        final String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(sqlInputs.getDbServer());
        final StringBuilder connectionSb = new StringBuilder("jdbc:db2://")
                .append(host)
                .append(COLON)
                .append(sqlInputs.getDbPort());
        if (StringUtils.isNoneEmpty(sqlInputs.getDbName())) {
            connectionSb.append(FORWARD_SLASH)
                    .append(sqlInputs.getDbName());
        }

        final List<String> dbUrls = getDbUrls(sqlInputs.getDbUrl());
        dbUrls.add(connectionSb.toString());

        return dbUrls;

    }
}
