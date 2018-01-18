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

import java.net.{InetSocketAddress, Proxy}

import com.google.api.client.http.javanet.NetHttpTransport
import org.junit.Test
import org.specs2.matcher.JUnitShouldMatchers
import org.specs2.mock.MockitoMocker


/**
  * Created by victor on 2/25/17.
  */

class HttpTransportUtilsTest extends JUnitShouldMatchers with MockitoMocker {

  @Test
  def netHttpTransportValid(): Unit = {
    HttpTransportUtils.getNetHttpTransport() should beAnInstanceOf[NetHttpTransport]
  }

  @Test
  def proxyNoProxy(): Unit = {
    HttpTransportUtils.getProxy(None, 0, None, "") shouldEqual Proxy.NO_PROXY
  }

  @Test
  def proxyHTTP(): Unit = {
    val resultProxy = HttpTransportUtils.getProxy(Some("abc"), 123, None, "")
    resultProxy.address() match {
      case inetSock: InetSocketAddress =>
        inetSock.getHostName shouldEqual "abc"
        inetSock.getPort shouldEqual 123
      case _ => true shouldEqual false
    }
  }

  @Test
  def proxyHTTPWithPassword(): Unit = {
// todo mock static method
//    val resultProxy = HttpTransport.getProxy(Some("abc"), 123, Some("a"), "")
//    mock[Authenticator]
//    when(Authenticator.setDefault(any[ProxyAuthenticator])).thenReturn()
//    doNothing.when[Authenticator] _
//    resultProxy.address() match {
//      case inetSock: InetSocketAddress =>
//        inetSock.getHostName shouldEqual "abc"
//        inetSock.getPort shouldEqual 123
//      case _ => true shouldEqual false
//    }
  }

}
