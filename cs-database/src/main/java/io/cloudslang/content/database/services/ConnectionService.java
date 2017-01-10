package io.cloudslang.content.database.services;

import com.iconclude.content.actions.sql.dbconnection.DBConnectionManager;
import com.iconclude.content.actions.sql.dbconnection.TotalMaxPoolSizeExceedException;
import com.iconclude.content.actions.sql.entities.SQLInputs;
import io.cloudslang.content.database.services.databases.*;
import com.iconclude.content.actions.sql.utils.Constants;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by vranau on 12/4/2014.
 */
public class ConnectionService {

    private DBConnectionManager dbConnectionManager = null;

    /**
     * get a pooled connection or a plain connection
     *
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public Connection setUpConnection(SQLInputs sqlInputs) throws ClassNotFoundException, SQLException {
        if (sqlInputs == null) {
            throw new SQLException("No connection inputs are provided!");
        }

        Properties databasePoolingProperties = sqlInputs.getDatabasePoolingProperties();

        dbConnectionManager = DBConnectionManager.getInstance();

        final String instance = sqlInputs.getInstance();
        final String tnsEntry = sqlInputs.getTnsEntry();
        final String windowsDomain = sqlInputs.getWindowsDomain();
        final String dbClass = sqlInputs.getDbClass();
        final List<String> dbUrls = sqlInputs.getDbUrls();
        final String dbType = sqlInputs.getDbType();
        final String dbServer = sqlInputs.getDbServer();
        final String dbName = sqlInputs.getDbName();
        final String dbPort = sqlInputs.getDbPort();
        final String tnsPath = sqlInputs.getTnsPath();
        final String authenticationType = sqlInputs.getAuthenticationType();

        if (dbUrls == null) {
            throw new SQLException("No database URL was provided");
        }

        //localDbName will be like "/localDbName"
        String localDbName =
                (dbName == null || dbName.length() == 0) ? "" : "/" + dbName;

        //db type if we use connection pooling
        DBConnectionManager.DBType enumDbType;
        String triedUrls = " ";

        //Oracle
        if (Constants.ORACLE_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.ORACLE;
            OracleDatabase oracleDatabase = new OracleDatabase();
            oracleDatabase.setUp(localDbName, dbServer, dbPort, dbUrls, tnsPath, tnsEntry);
        }
        //MySql
        else if (Constants.MYSQL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.MYSQL;
            MySqlDatabase mySqlDatabase = new MySqlDatabase();
            mySqlDatabase.setUp(localDbName, dbServer, dbPort, dbUrls);
        }
        //MSSQL
        else if (Constants.MSSQL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.MSSQL;
            MSSqlDatabase msSqlDatabase = new MSSqlDatabase();
            msSqlDatabase.setUp(localDbName, dbServer, dbPort, dbUrls, authenticationType, instance, windowsDomain);
        }
        //Sybase
        else if (Constants.SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.SYBASE;
            SybaseDatabase sybaseDatabase = new SybaseDatabase();
            sybaseDatabase.setUp(localDbName, dbServer, dbPort, dbUrls);
        }
        //NetCool
        else if (Constants.NETCOOL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.NETCOOL;
            NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
            netcoolDatabase.setUp(localDbName, dbServer, dbPort, dbUrls);
        }
        //Postgresql
        else if (Constants.POSTGRES_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.POSTGRESQL;
            PostgreSqlDatabase psDatabase = new PostgreSqlDatabase();
            psDatabase.setUp(localDbName, dbServer, dbPort, dbUrls);
        }
        //DB2
        else if (Constants.DB2_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.DB2;
            DB2Database db2Database = new DB2Database();
            db2Database.setUp(localDbName, dbServer, dbPort, dbUrls);
        } else if (Constants.CUSTOM_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.CUSTOM;
            CustomDatabase customDatabase = new CustomDatabase();
            customDatabase.setUp(dbClass);
            // dbUrl should already be defined!
        } else //check the dbtype
        {
            //if something other than allowed type or empty
            //we supply dbType to be default to Oracle if it is empty
            throw new SQLException("Invalid database type : " + dbType);
        }
        return obtainConnection(enumDbType, triedUrls, dbType, dbUrls, sqlInputs, databasePoolingProperties);
    }

    private Connection obtainConnection(DBConnectionManager.DBType enumDbType, String triedUrls, String dbType, List<String> dbUrls, SQLInputs sqlInputs, Properties properties) throws SQLException {
        //iterate through the dbUrls until we find one that we can connect
        //to the DB with and throw an error if no URL works
        Iterator<String> iter = dbUrls.iterator();

        Connection dbCon = null;
        boolean hasException = true;
        SQLException ex = new SQLException("No database URL was provided");
        while (hasException && iter.hasNext()) {
            String dbUrlTry = iter.next();

            //for logging, in case something goes wrong
            if (dbUrlTry != null && dbUrlTry.length() != 0) {
                triedUrls = triedUrls + dbUrlTry + " | ";

                if (logger.isDebugEnabled()) {
                    logger.debug("dbUrl so far: " + triedUrls);
                }
            } else {
                continue;//somehow the dbUrls might have empty string there
            }

            try {
                //Get the Connection, depends on what user set in
                //databasePooling.properties, this connection could be
                //pooled or not pooled
                dbCon =
                        dbConnectionManager.getConnection(enumDbType,
                                dbUrlTry,
                                sqlInputs.getUsername(),
                                sqlInputs.getPassword(),
                                properties);
                sqlInputs.setDbUrl(dbUrlTry);
                hasException = false;
            } catch (SQLException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e);
                }

                hasException = true;
                ex = e;

                if (e instanceof TotalMaxPoolSizeExceedException) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Get TotalMaxPoolSizeExceedException, will stop trying");
                    }
                    break;//don't try any more
                }
            }
        }
        if (hasException) {
            //have some logging
            String msg = "Failed to check out connection for dbType = "
                    + dbType + " username = " + sqlInputs.getUsername() + " tried dbUrls = " + triedUrls;
            logger.error(msg, ex);
            throw ex;
        }

        //for some reason it is till null, should not happen
        if (dbCon == null) {
            String msg = "Failed to check out connection. Connection is null. dbType = "
                    + dbType + " username = " + sqlInputs.getUsername() + " tried dbUrls = " + triedUrls;

            throw new SQLException(msg);
        }
        return dbCon;
    }

    /**
     * close a Connection. If the Connection is a pooled Connection, it will return
     * the connection to the pool
     *
     * @param connection a db Connection
     */
    public synchronized void closeConnection(Connection connection) {
        if (connection == null) {
            logger.warn("Try to close a null Connection, ignore");
            return;
        }

        try {
            connection.close();
            //tracing
            if (logger.isDebugEnabled()) {
                logger.debug("Closed connection");
            }
        } catch (SQLException e) {
            logger.warn("Failed to close a Connection", e);
        }
    }
}
