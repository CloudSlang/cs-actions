/*
 * Copyright 2021-2024 Open Text
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
package io.cloudslang.content.ssh.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.ssh.entities.SSHConnection;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        SSHShellCommandAction shellCommandAction = new SSHShellCommandAction();
        Map<String, String> result = shellCommandAction.runSshShellCommand("16.32.4.245",
                "",
                "root",
                "changeit",
                "C:\\Users\\boicu\\Desktop\\id_rsa",
                "",
                "allow",
                "",
                "",
                "echo 'hello'",
                "",
                "",
                "",
                "",
                "",
                "",
                new GlobalSessionObject<Map<String, SSHConnection>>(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "");

//        Map<String, String> result = shellCommandAction.runSshShellCommand("16.32.6.88",
//                "",
//                "ubuntu",
//                "changeit",
//                "C:\\Users\\boicu\\Desktop\\id_rsa",
//                "",
//                "allow",
//                "",
//                "",
//                "ifconfig",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                new GlobalSessionObject<Map<String, SSHConnection>>(),
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "");

        for(String s: result.keySet()){
            System.out.println(s+": "+result.get(s));
        }
    }
}
