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



package io.cloudslang.content.google.utils.service

import java.net.Proxy.Type
import java.net.{Authenticator, InetSocketAddress, PasswordAuthentication, Proxy}

import com.google.api.client.googleapis.GoogleUtils
import com.google.api.client.http.javanet.NetHttpTransport
import org.apache.commons.lang3.StringUtils.EMPTY

/**
  * Created by victor on 2/25/17.
  */
object HttpTransportUtils {

  def getNetHttpTransport(proxyHost: Option[String] = None, proxyPort: Int = 80, proxyUser: Option[String] = None, proxyPassword: String = EMPTY): NetHttpTransport =
    new NetHttpTransport.Builder()
      .trustCertificates(GoogleUtils.getCertificateTrustStore)
      .setProxy(getProxy(proxyHost, proxyPort, proxyUser, proxyPassword))
      .build()

  def getProxy(proxyHostOpt: Option[String], proxyPort: Int, proxyUserOpt: Option[String], proxyPassword: String): Proxy = proxyHostOpt match {
    case None => Proxy.NO_PROXY
    case Some(proxyHost) =>
      proxyUserOpt match {
        case None => Unit
        case Some(proxyUser) =>
          Authenticator.setDefault(ProxyAuthenticator(proxyUser, proxyPassword))
      }
      new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort))
  }

  case class ProxyAuthenticator(user: String, password: String) extends Authenticator {
    override def getPasswordAuthentication = new PasswordAuthentication(user, password.toCharArray)
  }

}
