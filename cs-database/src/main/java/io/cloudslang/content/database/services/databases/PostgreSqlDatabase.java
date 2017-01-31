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

import io.cloudslang.content.database.utils.Address;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbUrls;
import static io.cloudslang.content.database.utils.SQLUtils.loadClassForName;

/**
 * Created by victor on 13.01.2017.
 */
public class PostgreSqlDatabase implements SqlDatabase {

    @Override
    public List<String> setUp(@NotNull final SQLInputs sqlInputs) {
        loadClassForName("org.postgresql.Driver");

        final String connectionString = getConnectionString(sqlInputs);
        sqlInputs.getDbUrls().add(connectionString);

        final List<String> dbUrls = getDbUrls(sqlInputs.getDbUrl());
        dbUrls.add(connectionString);

        return dbUrls;
    }

    private String getConnectionString(final SQLInputs sqlInputs) {
        final Address address = new Address(sqlInputs.getDbServer());
        if (address.isIPV6Literal()) {//the host is an IPv6 literal
            return String.format("jdbc:postgresql://[host=%s]:%d%s",
                    sqlInputs.getDbServer(), sqlInputs.getDbPort(), sqlInputs.getDbName());
        }
        //the host is an IPv4 literal or a Host Name
        return String.format("jdbc:postgresql://%s:%d%s", sqlInputs.getDbServer(), sqlInputs.getDbPort(), sqlInputs.getDbName());
    }
}
