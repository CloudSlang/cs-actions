package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by vranau on 12/10/2014.
 */
public class DB2Database {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {
        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        dbUrls.add("jdbc:db2://" + SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(dbServer) + ":" + dbPort + dbName);
    }
}
