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

import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by victor on 13.01.2017.
 */
public class SybaseDatabase {
    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {
        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        dbUrls.add("jdbc:jtds:sybase://" + SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(dbServer) + ":" + dbPort + dbName + ";prepareSQL=1;useLOBs=false;TDS=4.2;");
    }
}
