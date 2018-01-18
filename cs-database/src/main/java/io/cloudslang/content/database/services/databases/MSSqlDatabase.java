/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.Address;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.SQLInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.database.constants.DBExceptionValues.INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL;
import static io.cloudslang.content.database.constants.DBInputNames.INSTANCE;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.isValidAuthType;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Created by victor on 13.01.2017.
 */
public class MSSqlDatabase implements SqlDatabase {
    private List<String> supportedJdbcDrivers;

    private static String addSslEncryptionToConnection(boolean trustAllRoots, String trustStore, String trustStorePassword, String dbUrlMSSQL) {
        final StringBuilder dbUrlBuilder = new StringBuilder(dbUrlMSSQL);
        dbUrlBuilder.append(SEMI_COLON)
                .append(ENCRYPT)
                .append(EQUALS)
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

    private static void loadWindowsAuthentication(String sqlJdbcAuthFilePath) {
        validateLibraryPath(sqlJdbcAuthFilePath);
        setJavaLibraryPath(sqlJdbcAuthFilePath);
    }

    private static void setJavaLibraryPath(String sqlJdbcAuthFilePath) {
        String javaLibraryPath = System.getProperty(JAVA_LIBRARY_PATH);

        if (StringUtils.isEmpty(javaLibraryPath)) {
            javaLibraryPath = sqlJdbcAuthFilePath;
        } else {
            javaLibraryPath = javaLibraryPath.substring(0, javaLibraryPath.length() - 1) + sqlJdbcAuthFilePath + System.getProperty(PATH_SEPARATOR) + CURRENT_DIRECTORY_NOTATION;
        }
        System.setProperty(JAVA_LIBRARY_PATH, javaLibraryPath);

        try {
            Field sysPathsField = ClassLoader.class.getDeclaredField(SYS_PATHS);
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(INACCESSIBLE_OR_INEXISTENT_SYS_PATHS_FIELD_EXCEPTION);
        }
    }

    private static void validateLibraryPath(String sqlJdbcAuthLibraryPath) {
        final List<String> exceptions = new ArrayList<>();

        if (StringUtils.isEmpty(sqlJdbcAuthLibraryPath)) {
            throw new RuntimeException(EMPTY_DRIVER_PATH_EXCEPTION);
        }

        final Path libraryPath = Paths.get(sqlJdbcAuthLibraryPath);

        try {
            if (!libraryPath.equals(libraryPath.toRealPath())) {
                exceptions.add(SYMBOLIC_PATH_EXCEPTION);
            }
            if (!Files.isDirectory(libraryPath)) {
                exceptions.add(INVALID_DIRECTORY_PATH_EXCEPTION);
            }
            if (!libraryPath.isAbsolute()) {
                exceptions.add(DRIVER_PATH_NOT_ABSOLUTE_EXCEPTION);
            }
            if (!libraryPath.equals(libraryPath.normalize())) {
                exceptions.add(NOT_THE_SHORTEST_PATH_EXCEPTION);
            }
        } catch (IOException e) {
            exceptions.add(INVALID_PATH);
        } finally {
            if (exceptions.size() != 0) {
                throw new RuntimeException(join(exceptions, NEW_LINE));
            }
        }
    }

    private void initializeJdbcDrivers() {
        supportedJdbcDrivers = Arrays.asList(SQLSERVER_JDBC_DRIVER, JTDS_JDBC_DRIVER);
    }

    private void loadJdbcDriver(String dbClass, final String authenticationType, final String sqlJdbcAuthFilePath) throws ClassNotFoundException {
        boolean driverFound = false;
        initializeJdbcDrivers();
        for (String driver : supportedJdbcDrivers) {
            if (driver.equals(dbClass)) {
                driverFound = true;
            }
        }
        if (driverFound) {
            Class.forName(dbClass);
            if (AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
                loadWindowsAuthentication(sqlJdbcAuthFilePath);
            }
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
                    final String dbUrl = MSSqlDatabase.addSslEncryptionToConnection(sqlInputs.isTrustAllRoots(),
                            sqlInputs.getTrustStore(), sqlInputs.getTrustStorePassword(), sqlInputs.getDbUrl());
                    dbUrls.add(dbUrl);

                }
            }


            loadJdbcDriver(sqlInputs.getDbClass(), sqlInputs.getAuthenticationType(), sqlInputs.getAuthLibraryPath());

            String host;

            //compute the host value that will be used in the url
            String[] serverInstanceComponents = null;
            if (sqlInputs.getDbServer().contains(BACK_SLASH)) { //instance is included in the dbServer value
                serverInstanceComponents = sqlInputs.getDbServer().split("\\\\");
                host = serverInstanceComponents[0];
            } else {
                host = sqlInputs.getDbServer();
            }
            final Address address = new Address(host);
            host = address.getURIIPV6Literal();


            //instance is included in the host name
            //todo check if mssql dbName can be null, the other operation "supported" it
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
                    dbUrlMSSQL.append(SEMI_COLON + INTEGRATED_SECURITY + EQUALS).append(TRUE);
                }
                final String connectionString = addSslEncryptionToConnection(sqlInputs.isTrustAllRoots(), sqlInputs.getTrustStore(), sqlInputs.getTrustStorePassword(), dbUrlMSSQL.toString());
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
