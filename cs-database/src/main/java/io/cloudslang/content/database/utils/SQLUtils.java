/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLUtils {

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

    public static String exceptionToString(Throwable e) {
        // Print the stack trace into an in memory string
        StringWriter writer = new StringWriter();
        e.printStackTrace(new java.io.PrintWriter(writer));

        // Process the stack trace, remove the FIRST null character
        return writer.toString().replace("" + (char) 0x00, "");
    }

    public static String toString(SQLException e) {
        String curr = exceptionToString(e) + "\nstate:" + e.getSQLState();
        while ((e = e.getNextException()) != null)
            curr += "\n\n" + exceptionToString(e) + "\nstate:" + e.getSQLState();
        return curr;
    }

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
}
