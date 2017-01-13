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

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by victor on 13.01.2017.
 */
public class NetcoolDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {


        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        //Attempt to load jconn3 driver first, then jconn2 driver
        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");

        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
            } catch (ClassNotFoundException ex) {
                throw new ClassNotFoundException
                        ("Could not locate either jconn2.jar or jconn3.jar file in the classpath!");
            }
        }
        dbUrls.add("jdbc:sybase:Tds:" + dbServer + ":" + dbPort + dbName);
    }
}
