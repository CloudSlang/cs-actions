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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbUrls;

/**
 * Created by victor on 13.01.2017.
 */
public class MySqlDatabase implements SqlDatabase {
    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {

        if (dbName == null) {
            throw new SQLException("No database provided!");
        }

        Class.forName("com.mysql.jdbc.Driver");
        Address address = new Address(dbServer);
        if (address.isIPV6Literal()) {//the host is an IPv6 literal
            dbUrls.add("jdbc:mysql://address=(protocol=tcp)" + "(host=" + dbServer + ")(port="
                    + dbPort + ")" + dbName);
        } else {//the host is an IPv4 literal or a Host Name
            dbUrls.add("jdbc:mysql://" + dbServer + ":" + dbPort + dbName);
        }
    }

    @Override
    public List<String> setUp(@NotNull final SQLInputs sqlInputs) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        final String connectionString = getConnectionString(sqlInputs);

        sqlInputs.getDbUrls().add(connectionString);

        final List<String> dbUrls = getDbUrls(sqlInputs.getDbUrl());
        dbUrls.add(connectionString);

        return dbUrls;
    }

    private String getConnectionString(final SQLInputs sqlInputs) {
        final Address address = new Address(sqlInputs.getDbServer());
        if (address.isIPV6Literal()) {//the host is an IPv6 literal
            return String.format("jdbc:mysql://address=(protocol=tcp)(host=%s)(port=%d)%s",
                    sqlInputs.getDbServer(), sqlInputs.getDbPort(), sqlInputs.getDbName());
        }
        //the host is an IPv4 literal or a Host Name
        return String.format("jdbc:mysql://%s:%d%s", sqlInputs.getDbServer(), sqlInputs.getDbPort(), sqlInputs.getDbName());
    }
}
