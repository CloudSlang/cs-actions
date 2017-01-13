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

import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by victor on 13.01.2017.
 */
public class OracleDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls, final String tnsPath, final String tnsEntry) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        if (((dbServer != null) && (!dbServer.equals(""))) ||
                ((dbPort != null) && (!dbPort.equals(""))) ||
                ((dbName != null) && (!dbName.equals("")))) {
            //Connect using the host, port and database info
            String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(dbServer);
            dbUrls.add("jdbc:oracle:thin:@//" + host + ":" + dbPort + dbName);
            dbUrls.add("jdbc:oracle:thin:@" + host + ":" + dbPort + ":" + dbName.substring(1));
        } else
        //Connect using the TNS name file if none of the host/port/database are specified through inputs
        {
            //check tnsPath
            if (StringUtils.isEmpty(tnsPath)) {
                throw new SQLException("Empty TNSPath for Oracle. ");
            }

            //check the path is correct
            //the path should the be path which contains tnsnames.ora file
            File tnsFile = new File(tnsPath);
            if (!tnsFile.exists()) {
                throw new SQLException("Invalid TNSPath for Oracle : " + tnsPath);
            } else {
                //check if the path is a directory
                if (!tnsFile.isDirectory()) {
                    throw new SQLException("Invalid TNSPath for Oracle, TNSPath is not a directory: " + tnsPath);

                }
                //check if the file is there
                String oraFilePath = tnsPath + File.separator + "tnsnames.ora";
                File oraFile = new File(oraFilePath);
                if (!oraFile.exists()) {
                    throw new SQLException("Failed to find tnsnames.ora file from :" + tnsPath);
                }
            }

            System.setProperty("oracle.net.tns_admin", tnsPath);
            dbUrls.add("jdbc:oracle:thin:@" + tnsEntry);
        }
    }
}
