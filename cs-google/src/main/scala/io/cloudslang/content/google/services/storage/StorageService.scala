/*
 * Copyright 2024 Open Text
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



package io.cloudslang.content.google.services.storage

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.storage.Storage


object StorageService {
  private def storageService(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential): Storage = new Storage(httpTransport, jsonFactory, credential)
  def bucketService: (HttpTransport, JsonFactory, Credential) => Storage#Buckets = storageService(_, _, _).buckets()

}
