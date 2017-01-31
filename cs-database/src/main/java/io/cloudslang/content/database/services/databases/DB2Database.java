/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

        final String connectionString = String.format("jdbc:db2://%s:%d%s",
                host, sqlInputs.getDbPort(), sqlInputs.getDbName());

        sqlInputs.getDbUrls().add(connectionString);

        final List<String> dbUrls = getDbUrls(sqlInputs.getDbUrl());
        dbUrls.add(connectionString);

        return dbUrls;

    }
}
