/*
 * Copyright 2023 Open Text
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





package io.cloudslang.content.google.utils.service

import com.google.api.client.json.jackson2.JacksonFactory
import org.junit.Test
import org.specs2.matcher.JUnitShouldMatchers

/**
  * Created by victor on 2/25/17.
  */
class JsonFactoryUtilsTest extends JUnitShouldMatchers {

  @Test
  def defaultJacksonFactoryDefault(): Unit = {
    JsonFactoryUtils.getDefaultJacksonFactory shouldEqual JacksonFactory.getDefaultInstance
  }
}
