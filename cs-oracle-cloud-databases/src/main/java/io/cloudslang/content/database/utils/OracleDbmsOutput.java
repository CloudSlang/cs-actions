/*
 * (c) Copyright 2022 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.cloudslang.content.database.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class OracleDbmsOutput implements AutoCloseable{
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
