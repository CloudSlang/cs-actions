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
package io.cloudslang.content.database.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.service.OracleCloudQueryService;

import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {



//        Map<String, String> result = new SQLCommand().execute("db20220621115401_high",
//                "ADMIN",
//                "B33f34t3r!123",
//                "C:\\Users\\boicu\\Desktop\\work\\oracle cloud databases\\Wallet_DB20220621115401.zip",
//                "INSERT INTO Persons\n" +
//                        "VALUES (4, 'John', 'Joe', 'SUA')",
//                "false",
//                "",
//                "B33f34t3r!123",
//                "",
//                "B33f34t3r!123",
//                "90");
//        result.forEach((k, v) -> System.out.println((k + ":" + v)));


        Map<String, String> resultQuery = new SQLQuery().execute("db20220621115401_high",
                "ADMIN",
                "B33f34t3r!123",
                "Select * from Persons",
                "C:\\Users\\boicu\\Desktop\\work\\oracle cloud databases\\Wallet_DB20220621115401.zip",
                "false",
                ";",
                "ceva",
                "",
                "",
                "",
                "",
                "90",
                "",
                "",
                new GlobalSessionObject<>());

        //result.forEach((k, v) -> System.out.println((k + ":" + v)));

        resultQuery.forEach((k, v) -> System.out.println((k + ":" + v)));



        //"CREATE TABLE Persons ( PersonID int, LastName varchar(255), FirstName varchar(255),City varchar(255))",

    }
}