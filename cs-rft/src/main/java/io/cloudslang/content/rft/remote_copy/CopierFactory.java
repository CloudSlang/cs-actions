/*
 * Copyright 2021-2023 Open Text
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

package io.cloudslang.content.rft.remote_copy;

import java.util.HashMap;


public class CopierFactory {

    private static HashMap<copiers, Class<? extends ICopier>> executorMap = new HashMap<>();

    static {
        executorMap.put(copiers.scp, ScpCopier.class);
        executorMap.put(copiers.local, LocalCopier.class);
        executorMap.put(copiers.sftp, SftpCopier.class);
        executorMap.put(copiers.smb3, SmbCopier.class);
    }

    public static ICopier getExecutor(String name) throws Exception {
        try {
            return getExecutor(copiers.valueOf(name));
        } catch (Exception e) {
            throw (new Exception("Protocol " + name + " not supported!"));
        }
    }

    public static ICopier getExecutor(copiers name) throws Exception {
        return executorMap.get(name).newInstance();
    }

    public enum copiers {local, scp, sftp, smb3, LOCAL, SCP, SFTP, SMB3}
}