/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.google.utils.action

import org.apache.commons.lang3.StringUtils.isEmpty

/**
  * Created by victor on 28.02.2017.
  */
object InputUtils {
  def verifyEmpty(value: String): Option[String] = if (isEmpty(value)) None else Some(value)

  def convertSecondsToMilli(value: Double): Long = (value * 1000 ceil) toLong
}
