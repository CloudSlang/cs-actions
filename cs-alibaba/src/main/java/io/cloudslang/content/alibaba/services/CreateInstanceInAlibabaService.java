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

package io.cloudslang.content.alibaba.services;

import io.cloudslang.content.alibaba.actions.signature.Signature;
import io.cloudslang.content.alibaba.entities.CreateInstanceInputs;
import io.cloudslang.content.alibaba.entities.SignatureInputs;
import io.cloudslang.content.alibaba.entities.inputs.CommonInputs;
import io.cloudslang.content.alibaba.execute.QueryApiExecutor;
import io.cloudslang.content.constants.ReturnCodes;


import java.util.Map;

import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.INSTANCE_ID;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.alibaba.utils.OutputsUtil.putResponseIn;

public class CreateInstanceInAlibabaService {


    public Map<String, String> createInstance(Map<String, String> parameters, CommonInputs commonInputs, CreateInstanceInputs createInstanceInputs) throws Exception {

        SignatureInputs signatureInputs = Signature.getSignature(parameters, commonInputs);
        Map<String, String> queryMapResult = new QueryApiExecutor().execute(commonInputs, createInstanceInputs, signatureInputs);
        final String INSTANCE_ID_X_PATH_QUERY = "/CreateInstanceResponse/InstanceId";
        if ((ReturnCodes.SUCCESS).equals(queryMapResult.get(RETURN_CODE))) {

            putResponseIn(queryMapResult, INSTANCE_ID, INSTANCE_ID_X_PATH_QUERY);
        }

        return queryMapResult;
    }
}
