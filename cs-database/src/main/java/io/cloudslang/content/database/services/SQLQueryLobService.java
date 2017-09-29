/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.services;

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import static org.apache.commons.io.FileUtils.*;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLQueryLobService {

    public static boolean executeSqlQueryLob(SQLInputs sqlInputs) throws Exception {
        if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
            throw new Exception("command input is empty.");
        }
        boolean isLOB = false;
        ConnectionService connectionService = new ConnectionService();
        try (final Connection connection = connectionService.setUpConnection(sqlInputs)) {

            StringBuilder strColumns = new StringBuilder(sqlInputs.getStrColumns());

            connection.setReadOnly(true);
            Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency());
            statement.setQueryTimeout(sqlInputs.getTimeout());

            ResultSet results = statement.executeQuery(sqlInputs.getSqlCommand());
            ResultSetMetaData mtd = results.getMetaData();
            int iNumCols = mtd.getColumnCount();
            for (int i = 1; i <= iNumCols; i++) {
                if (i > 1) strColumns.append(sqlInputs.getStrDelim());
                strColumns.append(mtd.getColumnLabel(i));
            }
            sqlInputs.setStrColumns(strColumns.toString());
            int nr = -1;
            while (results.next()) {
                nr++;
                final StringBuilder strRowHolder = new StringBuilder();
                for (int i = 1; i <= iNumCols; i++) {
                    if (i > 1) strRowHolder.append(sqlInputs.getStrDelim());
                    Object columnObject = results.getObject(i);
                    if (columnObject != null) {
                        String value;
                        if (columnObject instanceof java.sql.Clob) {
                            isLOB = true;
                            final File tmpFile = File.createTempFile("CLOB_" + mtd.getColumnLabel(i), ".txt");

                            copyInputStreamToFile(new ReaderInputStream(results.getCharacterStream(i), StandardCharsets.UTF_8), tmpFile);

                            if (sqlInputs.getLRowsFiles().size() == nr) {
                                sqlInputs.getLRowsFiles().add(nr, new ArrayList<String>());
                                sqlInputs.getLRowsNames().add(nr, new ArrayList<String>());
                            }
                            sqlInputs.getLRowsFiles().get(nr).add(tmpFile.getAbsolutePath());
                            sqlInputs.getLRowsNames().get(nr).add(mtd.getColumnLabel(i));
                            value = "(CLOB)...";

                        } else {
                            value = results.getString(i);
                            if (sqlInputs.isNetcool())
                                value = SQLUtils.processNullTerminatedString(value);
                        }
                        strRowHolder.append(value);
                    } else
                        strRowHolder.append("null");
                }
                sqlInputs.getLRows().add(strRowHolder.toString());
            }
        }

        return isLOB;
    }

}
