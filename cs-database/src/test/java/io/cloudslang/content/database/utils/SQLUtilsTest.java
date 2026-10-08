/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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

import static org.hamcrest.MatcherAssert.assertThat;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.jetbrains.annotations.NotNull;

import org.junit.jupiter.api.Test;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.database.utils.SQLUtils.getStrColumns;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by victor on 10.01.2017.
 */
public class SQLUtilsTest {

    public static final String SQL_STATE = "s1000";
    //    public static final String MSSQL_URL = "jdbc:jtds:sqlserver://";
    public static final String DB_SERVER_IPV6_LITERAL = "2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net";
    public static String CUSTOM_URL = "jdbc:h2:tcp://localhost/~/test";

    @Test
    public void testProcessNullTerminatedString() {
        String value = SQLUtils.processNullTerminatedString("\0");
        assertEquals("null", value);

        value = SQLUtils.processNullTerminatedString("value\0");
        assertEquals("value", value);

        value = SQLUtils.processNullTerminatedString("value");
        assertEquals("value", value);

        value = SQLUtils.processNullTerminatedString(null);
        assertEquals("null", value);

        value = SQLUtils.processNullTerminatedString("");
        assertEquals("null", value);
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHost() {
        String host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("localhost");
        assertEquals("localhost", host);

        host = SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("2001:db8:85a3:0:0:8a2e:370:7334");
        assertEquals("[2001:db8:85a3:0:0:8a2e:370:7334]", host);
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHostInvalid() {
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> SQLUtils.getIPv4OrIPv6WithSquareBracketsHost("[" + DB_SERVER_IPV6_LITERAL));
        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("host [2001-0db8-85a3-0042-1000-8a2e-0370-7334.ipv6-literal.net  not valid"));
    }

    @Test
    public void testGetIPv4OrIPv6WithSquareBracketsHostNull() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> SQLUtils.getIPv4OrIPv6WithSquareBracketsHost(null));
        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("host   not valid"));
    }

    @Test
    public void testToString() {
        final String toString = SQLUtils.toString(new SQLException("test ex"));
        assertTrue(toString.startsWith("java.sql.SQLException: test ex"));
    }

    @Test
    public void testComputeSessionId() {
        final String sessionIdInput = SQLUtils.computeSessionId("sessionIdInput");
        assertEquals("SQLQuery:920c83186d589d993633e3e0ddf6f25b7815cccd241b5a8ef1569558b07764b7", sessionIdInput);
    }


    @Test
    public void testProcessDumpException() throws SQLException {

        SQLException sqlException = new SQLException("dump is complete") {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        final String dumpException = SQLUtils.processDumpException(sqlException);
        assertEquals("dump is complete", dumpException);
    }

    @Test
    public void testProcessDumpExceptionNoState() throws SQLException {
        SQLException exception = org.junit.jupiter.api.Assertions.assertThrows(
                SQLException.class, () -> SQLUtils.processDumpException(new SQLException("test")));
        assertEquals("test", exception.getMessage());
    }

    @Test
    public void testProcessDumpExceptionNoMessage() throws SQLException {
        SQLException sqlException = new SQLException() {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        org.junit.jupiter.api.Assertions.assertThrows(SQLException.class, () -> SQLUtils.processDumpException(sqlException));
    }

    @Test
    public void testProcessLoadException() throws SQLException {

        SQLException sqlException = new SQLException("load is complete") {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }

        };
        final String loadException = SQLUtils.processLoadException(sqlException);
        assertEquals("load is complete", loadException);
    }

    @Test
    public void testProcessLoadExceptionNoState() throws SQLException {
        SQLException exception = org.junit.jupiter.api.Assertions.assertThrows(
                SQLException.class, () -> SQLUtils.processLoadException(new SQLException("test")));
        assertEquals("test", exception.getMessage());
    }

    @Test
    public void testProcessLoadExceptionNoMessage() throws SQLException {
        SQLException sqlException = new SQLException() {
            @Override
            public String getSQLState() {
                return SQL_STATE;
            }
        };
        org.junit.jupiter.api.Assertions.assertThrows(SQLException.class, () -> SQLUtils.processLoadException(sqlException));
    }

    @Test
    public void getStrColumnsValid() throws Exception {
        final String aKey = "aKey";

        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        final Map<String, Object> stringMap = new HashMap<>();
        stringMap.put(aKey, aKey);
        globalSessionObject.setResource(new SQLSessionResource(stringMap));

        assertThat(getStrColumns(globalSessionObject, aKey), is(aKey));
    }

}