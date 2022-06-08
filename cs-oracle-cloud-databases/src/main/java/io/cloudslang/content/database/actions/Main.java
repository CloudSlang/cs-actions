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

import io.cloudslang.content.database.service.OracleCloudQueryService;
import io.cloudslang.content.database.entities.OracleCloudInputs;

import java.util.Map;


public class Main {
    public static void main(String[] args) throws Exception {

        OracleCloudInputs inputs = new OracleCloudInputs(
                "(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.eu-frankfurt-1.oraclecloud.com))(connect_data=(service_name=gde34672fa25a61_db202203311236_low.adb.oraclecloud.com))(security=(ssl_server_cert_dn=\"CN=adwc.eucom-central-1.oraclecloud.com, OU=Oracle BMCS FRANKFURT, O=Oracle Corporation, L=Redwood City, ST=California, C=US\")))",
                "ADMIN",
                "B33f34t3r!123",
                "C:\\Users\\boicu\\Desktop\\Wallet_DB202203311236",
                "select * from persons",
                ";",
                "",
                "",
                "",
                "",
                0);

           Map<String,String> result = OracleCloudQueryService.executeSqlCommand(inputs);
           result.forEach((k, v) -> System.out.println((k + ":" + v)));

        OracleCloudQueryService.executeSqlQuery(inputs);
        System.out.println(inputs.getColumnNames());
        System.out.println(inputs.getRowsLeft());




    }
}