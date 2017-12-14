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

package io.cloudslang.content.google.utils.service

import java.io.InputStream

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory

import scala.collection.JavaConversions._

/**
  * Created by victor on 2/26/17.
  */
object GoogleAuth {

  def fromJsonWithScopes(jsonInputStream: InputStream, transport: HttpTransport, jsonFactory: JsonFactory, scopes: Iterable[String], timeout: Long): Credential =
    GoogleCredential.fromStream(jsonInputStream, transport, jsonFactory)
      .setExpiresInSeconds(timeout)
      .createScoped(scopes)

  def getAccessTokenFromCredentials(credential: Credential): String = {
    credential.refreshToken()
    credential.getAccessToken
  }

  def fromAccessToken(accessToken: String): Credential =
    new GoogleCredential().setAccessToken(accessToken)
}
