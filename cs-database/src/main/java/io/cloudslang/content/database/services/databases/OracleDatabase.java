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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class OracleDatabase implements SqlDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        if (((dbServer != null) && (!dbServer.equals(""))) ||
                ((dbPort != null) && (!dbPort.equals(""))) ||
                ((dbName != null) && (!dbName.equals("")))) {
            //Connect using the host, port and database info
            String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(dbServer);
            dbUrls.add("jdbc:oracle:thin:@//" + host + ":" + dbPort + dbName);
            dbUrls.add("jdbc:oracle:thin:@" + host + ":" + dbPort + ":" + dbName.substring(1));
        }
    }

    @Override
    public void setUp(@NotNull final SQLInputs sqlInputs) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        if (isNoneEmpty(sqlInputs.getDbServer()) ||
                isNoneEmpty(sqlInputs.getDbName())) {
            //Connect using the host, port and database info
            String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(sqlInputs.getDbServer());
            sqlInputs.getDbUrls().add("jdbc:oracle:thin:@//" + host + ":" + sqlInputs.getDbPort() + sqlInputs.getDbName());
            sqlInputs.getDbUrls().add("jdbc:oracle:thin:@" + host + ":" + sqlInputs.getDbPort() + ":" + sqlInputs.getDbName().substring(1));
        }

    }
}
