package io.cloudslang.content.database.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: bancl
 * Date: 12/8/14
 */
public class OracleDbmsOutput {

    private CallableStatement retrieveOutputStatement;

    public OracleDbmsOutput(Connection conn) throws SQLException {
        retrieveOutputStatement = conn.prepareCall(
                "declare " +
                        "    one_line varchar2(255); " +
                        "    is_done number; " +
                        "    the_buffer long; " +
                        "begin " +
                        "  loop " +
                        "    exit when length(the_buffer)+255 > :maxbytes OR is_done = 1; " +
                        "    dbms_output.get_line( one_line, is_done ); " +
                        "    the_buffer := the_buffer || one_line || chr(10); " +
                        "  end loop; " +
                        " :done := is_done; " +
                        " :buffer := the_buffer; " +
                        "end;");
    }

    public String getOutput() throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();

        int done = 0;

        retrieveOutputStatement.registerOutParameter(2, java.sql.Types.INTEGER);
        retrieveOutputStatement.registerOutParameter(3, java.sql.Types.VARCHAR);

        for (; ; ) {
            retrieveOutputStatement.setInt(1, 32000);
            retrieveOutputStatement.executeUpdate();
            stringBuilder.append(retrieveOutputStatement.getString(3));
            done = retrieveOutputStatement.getInt(2);
            if (done == 1) break;
        }

        return stringBuilder.toString();
    }

    public void close() throws SQLException {
        retrieveOutputStatement.close();
    }
}
