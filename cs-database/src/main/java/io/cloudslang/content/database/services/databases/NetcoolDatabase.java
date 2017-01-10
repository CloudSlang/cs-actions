package io.cloudslang.content.database.services.databases;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by vranau on 12/10/2014.
 */
public class NetcoolDatabase {

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {


        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        //Attempt to load jconn3 driver first, then jconn2 driver
        try {
            Class.forName("com.sybase.jdbc3.jdbc.SybDriver");

        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
            } catch (ClassNotFoundException ex) {
                throw new ClassNotFoundException
                        ("Could not locate either jconn2.jar or jconn3.jar file in the classpath!");
            }
        }
        dbUrls.add("jdbc:sybase:Tds:" + dbServer + ":" + dbPort + dbName);
    }
}
