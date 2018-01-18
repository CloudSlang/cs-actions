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

package io.cloudslang.content.google.services.compute.compute_engine

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.Compute

/**
  * Created by victor on 2/26/17.
  */
object ComputeService {

  def instancesService: (HttpTransport, JsonFactory, Credential) => Compute#Instances = computeService(_, _, _).instances()

  def zoneOperationsService: (HttpTransport, JsonFactory, Credential) => Compute#ZoneOperations = computeService(_, _, _).zoneOperations()

  def globalOperationsService: (HttpTransport, JsonFactory, Credential) => Compute#GlobalOperations = computeService(_, _, _).globalOperations()

  private def computeService(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential): Compute = new Compute(httpTransport, jsonFactory, credential)

  def disksService: (HttpTransport, JsonFactory, Credential) => Compute#Disks = computeService(_, _, _).disks()

  def networksService: (HttpTransport, JsonFactory, Credential) => Compute#Networks = computeService(_, _, _).networks()
}
