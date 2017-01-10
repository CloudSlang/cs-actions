package io.cloudslang.content.database.services.databases;

import com.hp.oo.content.commons.util.Address;
import com.iconclude.content.actions.sql.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by vranau on 12/10/2014.
 */
public class MSSqlDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls, final String authenticationType, final String instance, String windowsDomain) throws ClassNotFoundException, SQLException {
        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        if (StringUtils.isEmpty(dbServer)) {
            throw new IllegalArgumentException("host   not valid");
        }

        Class.forName("net.sourceforge.jtds.jdbc.Driver");

        String dbUrlMSSQL = "";
        String host = "";

        //compute the host value that will be used in the url
        String[] serverInstanceComponents = null;
        if (dbServer.contains(Constants.ESCAPED_BACKSLASH)) { //instance is included in the dbServer value
            serverInstanceComponents = dbServer.split("\\\\");
            host = serverInstanceComponents[0];
        } else {
            host = dbServer;
        }
        Address address = new Address(host);
        host = address.getURIIPV6Literal();

        //instance is included in the host name

        if (serverInstanceComponents != null) {
            //removed username and password form the url, since
            //driver manager will use url , username and password later
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + serverInstanceComponents[1] + ";";
        }
        //has instance input
        else if (StringUtils.isNoneEmpty(instance)) {
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + instance + ";";
        }
        //no instance
        else {
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName;
        }
        if (Constants.AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
            String domain = "CORP";
            // If present and the user name and password are provided, jTDS uses Windows (NTLM) authentication instead of the usual SQL Server authentication
            if (windowsDomain != null) {
                domain = windowsDomain;
            }
            //instance is included in the host name
            if (serverInstanceComponents != null) {
                dbUrlMSSQL =
                        Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + serverInstanceComponents[1] + ";domain=" + domain;
            }
            //has instance input
            else if (StringUtils.isNoneEmpty(instance)) {
                dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + instance + ";domain=" + domain;
            }
            //no instance
            else {
                dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";domain=" + domain;
            }
            // Set to true to send LMv2/NTLMv2 responses when using Windows authentication
            dbUrlMSSQL += ";useNTLMv2=true";
        } else if (!Constants.AUTH_SQL.equalsIgnoreCase(authenticationType)) //check invalid authentication type
        {
            //if it is something other than sql or empty,
            //we supply the empty string with sql
            throw new SQLException("Invalid authentication type for MS SQL : " + authenticationType);
        }

        dbUrls.add(dbUrlMSSQL);
    }
}
