package io.cloudslang.content.database.utils;


import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by vranau on 12/2/2014.
 */
public class SQLUtils {
//
//    protected static final Log logger = LogFactory.getLog(SQLUtils.class);

    public static void processHostorTNS(String dbType, String dbServer, String tnsEntry) throws Exception {
        if (dbType == null) {
            return;
        }
        if (dbType.equalsIgnoreCase(Constants.ORACLE_DB_TYPE))//oracle
        {
            if ((dbServer == null || dbServer.length() == 0) &&
                    (tnsEntry == null || tnsEntry.length() == 0)
                    ) {
                SQLException sqlEx =
                        new SQLException("Failed to getConnection. Please provide DBServerName if you use JDBC. Or please provide TNSEntry if you use TNS");
//                logger.error(sqlEx); todo
                throw sqlEx;
            }
        } else//other db type
        {
            if (dbServer == null || dbServer.length() == 0) {
                SQLException sqlEx = new SQLException("Failed to getConnection. DBServerName is empty.");
//                logger.error(sqlEx); todo
                throw sqlEx;
            }
        }
    }

    public static boolean trimRowstat(String dbUrl, String trimRowstat) {
        //trim the last(ROWSTAT) column for MSSQL
        //for more info see OO bug #8886 and jTDS bug #1329765
        boolean trim = false;
        if (dbUrl == null) {
            return false;
        }
        if (dbUrl.trim().toLowerCase().startsWith(Constants.MSSQL_URL.toLowerCase())) {
            if ((trimRowstat == null) || (trimRowstat.trim().equalsIgnoreCase("true"))) {
                trim = true;
            }
        }
        return trim;
    }


    public static String processNullTerminatedString(String value) {
        String returnValue = value;
        if (StringUtils.isEmpty(value)) {
            return "null";
        }
        char[] charArray = value.toCharArray();
        if (charArray.length == 1 && (int) charArray[0] <= 0)
            return "null";
        else {
            if ((int) charArray[charArray.length - 1] <= 0) {
                returnValue = value.substring(0, value.length() - 1);
            }
        }

        return returnValue;
    }


    /**
     * Method returning the host surrounded by square brackets if 'dbServer' is IPv6 format.
     * Otherwise the returned string will be the same.
     *
     * @return
     */
    public static String getIPv4OrIPv6WithSquareBracketsHost(String dbServer) {
        Address address = new Address(dbServer);
        return address.getURIIPV6Literal();
    }
/*
    public static String toString(SQLException e) {
        String curr = com.opsware.pas.content.commons.util.StringUtils.toString(e) + "\nstate:" + e.getSQLState();
        while ((e = e.getNextException()) != null)
            curr += "\n\n" + com.opsware.pas.content.commons.util.StringUtils.toString(e) + "\nstate:" + e.getSQLState();
        return curr;
    }

    public static Map getParameters() {
        Map parameters = new Map();

        RASBinding arg0 = RASBindingFactory.createPromptBinding("HostName:", true, false);
        arg0.assignFrom = false;
        arg0.assignTo = false;

        RASBinding arg1 = RASBindingFactory.createPromptBinding("DBPort:", true, false);
        arg1.assignFrom = false;
        arg1.assignTo = false;

        RASBinding arg2 = RASBindingFactory.createPromptBinding("Database Name:", true, false);
        arg2.assignFrom = false;
        arg2.assignTo = false;

        RASBinding arg3 = RASBindingFactory.createPromptBinding("UserName:");
        arg3.assignFrom = false;
        arg3.assignTo = false;

        RASBinding arg4 = RASBindingFactory.createPromptBinding("Password:", false, true);
        arg4.assignFrom = false;
        arg4.assignTo = false;

        RASBinding arg5 = RASBindingFactory.createPromptBinding("SQL Command:", true);
        arg5.assignFrom = false;
        arg5.assignTo = false;

        RASBinding arg6 = RASBindingFactory.createPromptBinding("DBType:", true);
        arg6.assignFrom = false;
        arg6.assignTo = false;

        RASBinding arg7 = RASBindingFactory.createPromptBinding("Authentication Type:", false);
        arg7.assignFrom = false;
        arg7.assignTo = false;

        RASBinding arg8 = RASBindingFactory.createPromptBinding("dbClass:", false);
        arg8.assignFrom = false;
        arg8.assignTo = false;

        RASBinding arg9 = RASBindingFactory.createPromptBinding("dbURL:", false);
        arg9.assignFrom = false;
        arg9.assignTo = false;

        parameters.add(Constants.DBSERVERNAME, arg0);
        parameters.add(Constants.DBTYPE, arg6);
        parameters.add(Constants.DBPORT, arg1);
        parameters.add(Constants.DATABASENAME, arg2);
        parameters.add(Constants.COMMAND, arg5);
        parameters.add(Constants.USERNAME, arg3);
        parameters.add(Constants.PASSWORD, arg4);
        parameters.add(Constants.AUTH_TYPE, arg7);
        parameters.add(Constants.CUSTOM_DB_CLASS, arg8);
        parameters.add(Constants.DBURL, arg9);

        //add "instance" and "timeout" inputs in operation

        return parameters;
    }

    public static Map getResultFields() {
        Map resultFields = new Map();
        resultFields.add(Constants.RETURNRESULT, "");
        resultFields.add("queryCount", "");
        return resultFields;
    }

    public static Map getResponses() {
        Map parameters = new Map();
        parameters.add(Constants.RESPONSEPASSED, String.valueOf(Constants.PASSED));
        parameters.add(Constants.RESPONSEFAILED, String.valueOf(Constants.FAILED));
        return parameters;
    }
*/
    //compute session id for JDBC operations
    public static String computeSessionId(String aString) {
        if (aString != null) {
            MessageDigest digest;
            byte[] byteData = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
                digest.update(aString.getBytes());
                byteData = digest.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xFF & aByteData);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return "SQLQuery:" + sb.toString();
        } else {
            return null;
        }
    }


    /**
     * Some databases (Sybase) throw exceptions during a database dump. This function processes that exception, and if it is that type, builds up the output of the command
     *
     * @param sqlException The exception to analyze
     * @return The output of the dump command
     * @throws java.sql.SQLException If it was not a successful dump comamnd's exception.
     */
    public static String processDumpException(SQLException sqlException) throws SQLException {
        final String sqlState = sqlException.getSQLState();
        if (sqlState != null && sqlState.toLowerCase().equals("s1000")) {
            SQLException f = sqlException;
            StringBuilder s = new StringBuilder();
            s.append(f.getMessage());
            while ((f = f.getNextException()) != null)
                s.append("\n" + f.getMessage());
            String str = s.toString();
            if (str.toLowerCase().contains("dump is complete"))
                return str;
        }
        throw sqlException;
    }

    /**
     * Some databases (Sybase) throw exceptions during a database restore. This function processes that exception, and if it is that type, builds up the output of the command
     *
     * @param e The exception to analyze
     * @return The output of the dump command
     * @throws java.sql.SQLException If it was not a successful load command's exception.
     */
    public static String processLoadException(SQLException e) throws SQLException {
        final String sqlState = e.getSQLState();
        if (sqlState != null && e.getSQLState().toLowerCase().equals("s1000")) {
            SQLException f = e;
            StringBuilder s = new StringBuilder();
            s.append(f.getMessage());
            while ((f = f.getNextException()) != null)
                s.append("\n" + f.getMessage());
            String str = s.toString();
            if (str.toLowerCase().contains("load is complete"))
                return str;
        }
        throw e;
    }
/*
    public static String getActionDescription(String operationName) {
        return PropsLoader.ACTIONDESCRIPTIONS.getProperty(operationName);
    }
    */

}
