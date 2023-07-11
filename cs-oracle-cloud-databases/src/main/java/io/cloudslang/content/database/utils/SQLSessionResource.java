/*
 * Copyright 2022-2023 Open Text
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

import com.hp.oo.sdk.content.plugin.SessionResource;

import java.io.Serializable;
import java.util.Map;

public class SQLSessionResource extends SessionResource<Map<String, Object>> implements Serializable {

    private Map<String, Object> sqlConnectionMap;

    public SQLSessionResource(final Map<String, Object> sqlConnectionMap) {
        this.sqlConnectionMap = sqlConnectionMap;
    }

    @Override
    public Map<String, Object> get() {
        return sqlConnectionMap;
    }

    @Override
    public void release() {
        sqlConnectionMap = null;
    }
}
