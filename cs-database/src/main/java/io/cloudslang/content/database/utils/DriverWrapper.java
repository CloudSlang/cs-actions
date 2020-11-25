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
package io.cloudslang.content.database.utils;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverWrapper implements Driver {
    private final Driver driver;


    DriverWrapper(Driver d) {
        this.driver = d;
    }


    @Override
    public boolean acceptsURL(String u) throws SQLException {
        return this.driver.acceptsURL(u);
    }


    @Override
    public Connection connect(String u, Properties p) throws SQLException {
        return this.driver.connect(u, p);
    }


    @Override
    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }


    @Override
    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }


    @Override
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
        return this.driver.getPropertyInfo(u, p);
    }


    @Override
    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }


    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
}
