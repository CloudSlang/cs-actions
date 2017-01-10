package io.cloudslang.content.database.services.databases;

import com.hp.oo.content.commons.util.Address;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

public class PostgreSqlDatabase {
    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls) throws ClassNotFoundException, SQLException {

        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        Class.forName("org.postgresql.Driver");
        Address address = new Address(dbServer);
        if (address.isIPV6Literal()) {//the host is an IPv6 literal
            dbUrls.add("jdbc:postgresql://[host=" + dbServer + "]" + ":" + dbPort + dbName);
        } else {//the host is an IPv4 literal or a Host Name
            dbUrls.add("jdbc:postgresql://" + dbServer + ":" + dbPort + dbName);
        }
    }
}
