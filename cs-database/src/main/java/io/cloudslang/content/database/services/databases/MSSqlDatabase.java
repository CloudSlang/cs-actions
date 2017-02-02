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
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.database.constants.DBExceptionValues.INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL;
import static io.cloudslang.content.database.constants.DBInputNames.INSTANCE;
import static io.cloudslang.content.database.constants.DBOtherValues.DATABASE_NAME_CAP;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.isValidAuthType;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class MSSqlDatabase implements SqlDatabase {

    private List<String> supportedJdbcDrivers;

    public static String addSslEncryptionToConnection(boolean trustAllRoots, String trustStore, String trustStorePassword, String dbUrlMSSQL) {
        final StringBuilder dbUrlBuilder = new StringBuilder(dbUrlMSSQL);
        dbUrlBuilder.append(SEMI_COLON + ENCRYPT + EQUALS)
                .append(TRUE)
                .append(SEMI_COLON)
                .append(TRUST_SERVER_CERTIFICATE)
                .append(EQUALS);
        if (trustAllRoots) {
            dbUrlBuilder.append(TRUE);
        } else {
            dbUrlBuilder.append(FALSE)
                    .append(String.format(TRUSTORE_PARAMS, trustStore, trustStorePassword));
        }
        return dbUrlBuilder.toString();
    }

    private void initializeJdbcDrivers() {
        supportedJdbcDrivers = Arrays.asList(SQLSERVER_JDBC_DRIVER, JTDS_JDBC_DRIVER);
    }

    private void loadJdbcDriver(String dbClass) throws ClassNotFoundException {
        boolean driverFound = false;
        initializeJdbcDrivers();
        for (String driver : supportedJdbcDrivers) {
            if (driver.equals(dbClass)) {
                driverFound = true;
            }
        }
        if (driverFound) {
            Class.forName(dbClass);
        } else {
            throw new RuntimeException("The driver provided is not supported.");
        }
    }

    @Override
    public List<String> setUp(@NotNull final SQLInputs sqlInputs) {
        try {
            final List<String> dbUrls = new ArrayList<>();
            // todo ask eugen if need to check class
            if (sqlInputs.getDbClass() != null && sqlInputs.getDbClass().equals(SQLSERVER_JDBC_DRIVER)) {
                if (isNoneEmpty(sqlInputs.getDbUrl())) {
                    final String dbUrl = MSSqlDatabase.addSslEncryptionToConnection(sqlInputs.getTrustAllRoots(),
                            sqlInputs.getTrustStore(), sqlInputs.getTrustStorePassword(), sqlInputs.getDbUrl());
                    dbUrls.add(dbUrl);

                }
            }


            loadJdbcDriver(sqlInputs.getDbClass());

            String host;

            //compute the host value that will be used in the url
            String[] serverInstanceComponents = null;
            if (sqlInputs.getDbServer().contains(Constants.ESCAPED_BACKSLASH)) { //instance is included in the dbServer value
                serverInstanceComponents = sqlInputs.getDbServer().split("\\\\");
                host = serverInstanceComponents[0];
            } else {
                host = sqlInputs.getDbServer();
            }
            final Address address = new Address(host);
            host = address.getURIIPV6Literal();


            //instance is included in the host name

            if (isValidAuthType(sqlInputs.getAuthenticationType())) {
                final StringBuilder dbUrlMSSQL = new StringBuilder(Constants.MSSQL_URL + host + COLON + sqlInputs.getDbPort()
                        + SEMI_COLON + DATABASE_NAME_CAP + EQUALS + sqlInputs.getDbName());

                if (serverInstanceComponents != null) {
                    dbUrlMSSQL.append(SEMI_COLON + INSTANCE + EQUALS)
                            .append(serverInstanceComponents[1]);
                } else if (isNoneEmpty(sqlInputs.getInstance())) {
                    dbUrlMSSQL.append(SEMI_COLON + INSTANCE + EQUALS)
                            .append(sqlInputs.getInstance());
                }
                if (AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType())) {
                    dbUrlMSSQL.append(SEMI_COLON + INTEGRATED_SECURITY + EQUALS + TRUE);
                }
                final String connectionString = addSslEncryptionToConnection(sqlInputs.getTrustAllRoots(), sqlInputs.getTrustStore(), sqlInputs.getTrustStorePassword(), dbUrlMSSQL.toString());
//                sqlInputs.getDbUrls().add(connectionString);

                dbUrls.add(connectionString);

            } else {
                throw new SQLException(INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL + sqlInputs.getAuthenticationType());
            }

            return dbUrls;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}
