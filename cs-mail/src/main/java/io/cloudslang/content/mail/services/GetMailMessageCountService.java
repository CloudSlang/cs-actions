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
package io.cloudslang.content.mail.services;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.utils.MessageStoreUtils;

import javax.mail.*;
import java.util.HashMap;
import java.util.Map;

public class GetMailMessageCountService {

    protected GetMailMessageCountInput input;

    private Map<String, String> results = new HashMap<>();

    public Map<String, String> execute(GetMailMessageCountInput getMailMessageCountInput) throws Exception {
        this.results = new HashMap<>();
        this.input = getMailMessageCountInput;

        try (Store store = MessageStoreUtils.createMessageStore(input)) {
            Folder folder = store.getFolder(input.getFolder());
            if (!folder.exists()) {
                throw new Exception(ExceptionMsgs.THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
            }
            folder.open(Folder.READ_ONLY);

            results.put(OutputNames.RETURN_RESULT, String.valueOf(folder.getMessageCount()));

            return results;
        }
    }
}
