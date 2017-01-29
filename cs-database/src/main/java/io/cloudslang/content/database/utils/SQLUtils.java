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


import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLUtils {

    private final static Map<String, Integer> valueResultSet = getResultSetMap();

    public static int getResultSetValue(final String strResultSet) {
        if (valueResultSet.containsKey(strResultSet.toUpperCase())) {
            return valueResultSet.get(strResultSet.toUpperCase());
        }
        return -1000000;
    }

    @NotNull
    private static Map<String, Integer> getResultSetMap() {
        final Map<String, Integer> valueResultSet = new HashMap<>(5);
        valueResultSet.put("CONCUR_READ_ONLY", ResultSet.CONCUR_READ_ONLY);
        valueResultSet.put("CONCUR_UPDATABLE", ResultSet.CONCUR_UPDATABLE);
        valueResultSet.put("TYPE_FORWARD_ONLY", ResultSet.TYPE_FORWARD_ONLY);
        valueResultSet.put("TYPE_SCROLL_INSENSITIVE", ResultSet.TYPE_SCROLL_INSENSITIVE);
        valueResultSet.put("TYPE_SCROLL_SENSITIVE", ResultSet.TYPE_SCROLL_SENSITIVE);
        return valueResultSet;
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
        if (isEmpty(value)) {
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
                s.append("\n").append(f.getMessage());
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
                s.append("\n").append(f.getMessage());
            String str = s.toString();
            if (str.toLowerCase().contains("load is complete"))
                return str;
        }
        throw e;
    }

    public static List<String> readFromFile(String fileName) {
        final List<String> lines = new ArrayList<>();


        try (final FileInputStream fstream = new FileInputStream(new File(fileName));
             final DataInputStream in = new DataInputStream(fstream);
             final InputStreamReader inputStreamReader = new InputStreamReader(in);
             final BufferedReader br = new BufferedReader(inputStreamReader)) {

            String strLine;
            String aString = "";
            int i = 0;
            boolean youAreInAMultiLineComment = false;
            while ((strLine = br.readLine()) != null) {
                //ignore multi line comments
                if (youAreInAMultiLineComment) {
                    if (strLine.contains("*/")) {
                        int indx = strLine.indexOf("*/");
                        strLine = strLine.substring(indx + 2);
                        youAreInAMultiLineComment = false;
                    } else {
                        continue;
                    }
                }

                if (strLine.contains("/*")) {
                    int indx = strLine.indexOf("/*");
                    String firstPart = strLine.substring(0, indx);
                    String secondPart = strLine.substring(indx + 2);
                    strLine = firstPart;
                    youAreInAMultiLineComment = true;

                    if (secondPart.contains("*/")) {    //the comment starts and ends in the middle of the line
                        indx = secondPart.indexOf("*/");
                        secondPart = secondPart.substring(indx + 2);
                        youAreInAMultiLineComment = false;
                        strLine += secondPart;
                    }
                }

                //ignore one line comments
                if (strLine.contains("--")) {
                    int indx = strLine.indexOf("--");
                    strLine = strLine.substring(0, indx);
                }

                //ingnore empty lines
                if (0 == strLine.length()) {
                    continue;
                }

                //consider if a SQL statement is separated in different lines. eg,
                //create table employee(
                //  first varchar(15));
                //if a sql command finishes in one line, add in commands
                if (strLine.endsWith(";")) {
                    //get rid of ';',otherwise the operation will fail on Oracle database
                    int indx = strLine.indexOf(";", 0);
                    strLine = strLine.substring(0, indx);
                    //if the command has only one line
                    if (i == 0) {
                        lines.add(strLine);
                    }
                    //if the command has multiple lines
                    else {
                        aString += strLine;
                        lines.add(aString);
                        aString = "";
                        i = 0;
                    }
                }
                //if a line doesn't finish with a ';', it means the sql commands is not finished
                else {
                    aString = aString + strLine + " ";
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); //todo
            return Collections.emptyList();
        }
        return lines;
    }

}
