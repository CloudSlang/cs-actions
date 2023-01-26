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
/*
 * (c) Copyright 2020 Micro Focus, L.P.
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


package io.cloudslang.content.google.utils.service


import java.util
import java.util.{Map => JMap}

import com.google.gson.Gson
import io.cloudslang.content.google.utils.action.InputNames.CreateSQLDatabaseInstanceInputs._

object Utility {
  type JsonToMap = String => JMap[String, String]

  val jsonToMap: JsonToMap = {
    val jsonObject: Gson = new Gson()
    x => jsonObject.fromJson(x, new util.LinkedHashMap[String, String]().getClass)
  }

  def getInstanceStatus(activationPolicy: String): String = {
    if (activationPolicy.equalsIgnoreCase(ACTIVATION_POLICY_ALWAYS)) {
      SQL_INSTANCE_RUNNING
    } else {
      SQL_INSTANCE_STOPPED
    }
  }


}
