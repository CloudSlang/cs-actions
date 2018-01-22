/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca;

import io.cloudslang.content.dca.actions.authentication.GetAuthenticationToken;
import io.cloudslang.content.dca.actions.templates.DeployTemplate;
import io.cloudslang.content.dca.actions.templates.GetDeployment;
import io.cloudslang.content.dca.actions.utils.CreateResourceJSON;
import io.cloudslang.content.dca.models.DcaAuthModel;
import io.cloudslang.content.dca.models.DcaBaseResourceModel;
import io.cloudslang.content.dca.models.DcaDeploymentParameterModel;
import io.cloudslang.content.dca.models.DcaResourceModel;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static java.lang.String.format;

public class Main {
    public static void main(String[] args) {

        final Map<String, String> result = new GetAuthenticationToken().execute(
                "pluto.dev.opsware.com",
                "5443",
                "https",
                "idmTransportUser",
                "idmTransportUser",
                "dcaadmin",
                "HP.password1",
                "Provider",
                "true",
                "",
                "",
                "",
                "",
                "true",
                "allow_all",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        for (Map.Entry<String, String> entry : result.entrySet()) {
            System.out.println(entry);
        }

        final Map<String, String> resource1 = new CreateResourceJSON().execute(
            "2461f26d-e1cc-44ed-8443-9d8978ede341",
            "1",
            "449e67da3b15a8178ed815386ff2618e",
            "node",
            " ",
            "credentialId,media_source,kickstart",
            "355a64a1-be94-40f8-9b1e-6ae2c96720ce,http://osprovmedia.dev.opsware.com/media/TA/linux/RHEL7.3-Server-x86_64/,http://osprovmedia.dev.opsware.com/media/TA/tmp/miruna/ks_cfg_rhel7.txt",
            ","
        );

        final String resources = "[" + resource1.get(RETURN_RESULT) + "]";

        final Map<String, String> deployment = new DeployTemplate().execute(
                "pluto.dev.opsware.com",
                "443",
                "https",
                result.get("authToken"),
                "",
                "DeployRobertAPI",
                "RHEL 7 deployment using the API",
                "4c0214f7-4d10-4d7c-b122-a43cffc3e71c",
                resources,
                "false",
                "2400",
                "2",
                "",
                "",
                "",
                "",
                "true",
                "allow_all",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

//        final Map<String, String> getDeployment = new GetDeployment().execute(
//                "pluto.dev.opsware.com",
//                "443",
//                "https",
//                result.get("authToken"),
//                "",
//                "a87a7da7-d4aa-4011-9f8f-ed5f4e46ac91",
//                "",
//                "",
//                "",
//                "",
//                "true",
//                "allow_all",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                "",
//                ""
//        );

        System.out.println(deployment);

    }
}
