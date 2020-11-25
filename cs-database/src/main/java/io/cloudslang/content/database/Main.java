/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.database;

import io.cloudslang.content.database.actions.SQLCommand;

import java.util.Map;

public class Main {

    private static void Custom() {
        Map<String, String> outputs = new SQLCommand().execute(
                "16.32.6.248",
                "Custom",
                "mssql_automation",
                "B33f34t3r",
                "",
                "1433",
                "automation",
                "sql",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://16.32.6.248:1433;DatabaseName=automation",
                "SELECT * FROM information_schema.tables;",
                "true",
                "",
                "",
                "",
                "",
                "",
                "CONCUR_READ_ONLY",
                "file:///mssql-jdbc.jar"
        );
        for(String key : outputs.keySet()) {
            System.out.println(key + ": " + outputs.get(key));
        }
    }

    private static void MSSql() {
        Map<String, String> outputs = new SQLCommand().execute(
                "16.32.6.248",
                "MSSQL",
                "mssql_automation",
                "B33f34t3r",
                "",
                "1433",
                "automation",
                "sql",
                "",
                "",
                "SELECT * FROM information_schema.tables;",
                "true",
                "",
                "",
                "",
                "",
                "",
                "CONCUR_READ_ONLY",
                "file:///mssql-jdbc.jar"
        );
        for(String key : outputs.keySet()) {
            System.out.println(key + ": " + outputs.get(key));
        }
    }

    private static void MySql() {
        Map<String, String> outputs = new SQLCommand().execute(
                "16.32.6.7",
                "MySQL",
                "test",
                "test",
                "",
                "3306",
                "mysql",
                "sql",
                "",
                "",
                "SELECT * FROM user;",
                "true",
                "",
                "",
                "",
                "",
                "",
                "CONCUR_READ_ONLY",
                "file:///C:/mysql-connector-java.jar"
        );
        for(String key : outputs.keySet()) {
            System.out.println(key + ": " + outputs.get(key));
        }
    }

    private static void PostgreSql() {
        Map<String, String> outputs = new SQLCommand().execute(
                "16.77.8.5",
                "PostgreSQL",
                "psql_automation",
                "B33f34t3r",
                "",
                "5432",
                "automation",
                "sql",
                "",
                "",
                "SELECT * FROM information_schema.tables;",
                "true",
                "",
                "",
                "",
                "",
                "",
                "CONCUR_READ_ONLY",
                "file:///C:/postgresql.jar"
        );
        for(String key : outputs.keySet()) {
            System.out.println(key + ": " + outputs.get(key));
        }
    }

    private static void Oracle() {
        Map<String, String> outputs = new SQLCommand().execute(
                "16.32.7.148",
                "Oracle",
                "SYSTEM",
                "B33f34t3r",
                "",
                "1521",
                "orcl",
                "sql",
                "",
                "",
                "SELECT * FROM recipestest",
                "true",
                "",
                "",
                "",
                "",
                "",
                "CONCUR_READ_ONLY",
                "file:///C:/ojdbc8.jar"
        );
        for(String key : outputs.keySet()) {
            System.out.println(key + ": " + outputs.get(key));
        }
    }

    public static void main(String[] args) {
        MySql();
    }
}
