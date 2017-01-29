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
import java.util.List;

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
    public void setUp(@NotNull final SQLInputs sqlInputs) {
//        if (sqlInputs.getDbName() == null) {
//            throw new RuntimeException("No database provided!");
//        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        Address address = new Address(sqlInputs.getDbServer());
        if (address.isIPV6Literal()) {//the host is an IPv6 literal
            sqlInputs.getDbUrls().add("jdbc:mysql://address=(protocol=tcp)" + "(host=" + sqlInputs.getDbServer() + ")(port="
                    + sqlInputs.getDbPort() + ")" + sqlInputs.getDbName());
        } else {//the host is an IPv4 literal or a Host Name
            sqlInputs.getDbUrls().add("jdbc:mysql://" + sqlInputs.getDbServer() + ":" + sqlInputs.getDbPort() + sqlInputs.getDbName());
        }

    }
}
