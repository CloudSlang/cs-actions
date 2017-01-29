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

import java.sql.SQLException;
import java.util.List;

/**
 * Created by victor on 13.01.2017.
 */
public class DB2Database implements SqlDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {
        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        dbUrls.add("jdbc:db2://" + SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(dbServer) + ":" + dbPort + dbName);
    }

    @Override
    public void setUp(@NotNull final SQLInputs sqlInputs) {
//        if (sqlInputs.getDbName() == null) {
//            throw new RuntimeException("No database provided!");
//        }
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        sqlInputs.getDbUrls().add("jdbc:db2://" + SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(sqlInputs.getDbServer()) + ":" + sqlInputs.getDbPort() + sqlInputs.getDbName());

    }
}
