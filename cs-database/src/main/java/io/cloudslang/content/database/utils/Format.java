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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by victor on 13.01.2017.
 */
public class Format {
    public static int colPadding = 2;

    /**
     * Formats a result set into a table like such:
     * header1   header2
     * -------   -------
     * Row1Col1  Row1Col2
     * Row2Col1  Row2Col2
     *
     * @param result
     * @param checkNullTermination
     * @return
     */
    public static String resultSetToTable(ResultSet result, boolean checkNullTermination) throws SQLException {
        return resultSetToTable(result, Calendar.getInstance(), checkNullTermination);
    }

    /**
     * Returns column number col from the current result set and returns as string
     *
     * @param rs                   the result set on current row
     * @param col                  1-indexed column number
     * @param checkNullTermination perform the null termination check on a string (eg. netcool)
     * @return
     * @throws SQLException
     */
    private static String getColumn(ResultSet rs, int col, boolean checkNullTermination) throws SQLException {

        String value = rs.getString(col);
        if (value != null) {
            if (checkNullTermination)
                value = processNullTerminatedString(value);
        } else
            value = "null";
        return value;
    }

    /**
     * Chop off '\0' character if present in a String.
     * <p/>
     * This only removes 1 trailing '\0' (null) character.
     * TODO: rename to match @see replaceInvalidXMLCharacters
     * Only caller: com.opsware.pas.content.commons.util.sql.Format
     *
     * @param s String that may have a null character at the end
     * @return a String without a trailing '\0' character.
     */
    public static String processNullTerminatedString(String s) {
        String returnValue = s;

        char[] charArray = s.toCharArray();

        // Case 1: Empty null terminated String
        if (charArray.length == 1 && (int) charArray[0] <= 0) {
            return "null";
        } else { // Case 2: Non-empty null terminated String
            // Strip trailing '\0' character if any
            if ((int) charArray[charArray.length - 1] <= 0)
                returnValue = s.substring(0, s.length() - 1);
        }

        return returnValue;
    }


    /**
     * Returns tabular form of resultSet similar to what you would get from
     * running a query from the command line
     *
     * @param result populated result set
     * @param cal    calendar to use to format
     * @return
     * @throws SQLException
     */
    private static String resultSetToTable(ResultSet result, Calendar cal, boolean checkNullTermination) throws SQLException {
        assert (result != null);

        ResultSetMetaData md = result.getMetaData();
        int nCols = md.getColumnCount();
        String[] headers = new String[nCols];
        int[] headerSz = new int[nCols]; // Note: Eclipse has a friendly getDisplaySizes() function
        int maxWidth = 1; // maximum column width - initially set at 1 to prevent an edge case

        ArrayList<String[]> rows = new ArrayList<String[]>(); // columns
        int rowCount = 0;
        // This is fairly space intensive because we don't want to turn this into an O^2(n) problem
        // instead of a O(n) problem. It's O(n) space but with a k of 2, and same with space.
        // Ugh, stupid time-memory tradeoffs
        // We're storing it ALL in Java data structures to figure out width of columns first, then padding later
        // for serialization
        for (int colheader = 0; colheader < nCols; colheader++) {
            headers[colheader] = md.getColumnLabel(colheader + 1);
            headerSz[colheader] = headers[colheader].length();
        }

        // setup columns to be width of column labels
        while (result.next()) {
            String[] row = new String[headers.length];
            for (int colN = 0; colN < nCols; colN++) {
                String colVal = getColumn(result, colN + 1, checkNullTermination);
                headerSz[colN] = colVal.length() > headerSz[colN] ? colVal.length() : headerSz[colN];
                row[colN] = colVal;
            }
            rows.add(row);
            rowCount++;
        }
        // column widths set, now start populating the string builder
        StringBuilder resultSb = new StringBuilder(headers.length * maxWidth / 2);
        // construct the headers
        for (int colheader = 0; colheader < nCols; colheader++) {
            resultSb.append(headers[colheader]);
            for (int count = 0; count < headerSz[colheader] - headers[colheader].length() + colPadding; count++)
                resultSb.append(" ");
        }
        resultSb.append("\n");
        for (int colheader = 0; colheader < nCols; colheader++) {
            for (int count = 0; count < headerSz[colheader]; count++)
                resultSb.append("-");
            for (int count = 0; count < colPadding; count++)
                resultSb.append(" ");
        }
        resultSb.append("\n");
        // now append the data itself
        for (String[] row : rows) {
            for (int col = 0; col < nCols; col++) {
                resultSb.append(row[col]);
                for (int padIdx = 0; padIdx < headerSz[col] - row[col].length() + colPadding; padIdx++)
                    resultSb.append(" ");
            }
            resultSb.append("\n");
        }

        return resultSb.toString();
    }

    /**
     * Returns tabular form of resultSet similar to what you would get from
     * running a query from the command line
     *
     * @param resultSet populated result set
     * @return
     * @throws SQLException
     */
    public static String resultSetToDelimitedColsAndRows(ResultSet resultSet, boolean checkNullTermination, String colDelimiter, String rowDelimiter) throws SQLException {
//        assert (resultSet != null);

        StringBuilder delimitedResult = new StringBuilder();
        if (resultSet != null) {
            ResultSetMetaData md = resultSet.getMetaData();
            int nCols = md.getColumnCount();
            // populate rows and cols
            while (resultSet.next()) {
                for (int colN = 0; colN < nCols; colN++) {
                    String colVal = getColumn(resultSet, colN + 1, checkNullTermination);
                    if (colN == 0) {
                        delimitedResult.append(colVal);
                    } else {
                        delimitedResult.append(colDelimiter)
                                .append(colVal);
                    }
                }
                delimitedResult.append(rowDelimiter);
            }
        }

        //If a multi-char delimiter is used, removing the last character is not enough
        int length = rowDelimiter.length();
        String delimitedResultString = "";
        if ((delimitedResult.length() - length) >= 0)
            delimitedResultString = delimitedResult.substring(0, delimitedResult.length() - length);
        else
            delimitedResultString = "";
        return delimitedResultString;
    }

}
