package io.cloudslang.content.gcloud.utils

import java.net.{Authenticator, InetSocketAddress, Proxy}

import com.google.api.client.http.javanet.NetHttpTransport
import org.junit.Test
import org.mockito.verification.VerificationMode
import org.specs2.matcher.JUnitShouldMatchers
import org.specs2.mock.MockitoMocker


/**
  * Created by victor on 2/25/17.
  */

class HttpTransportTest extends JUnitShouldMatchers with MockitoMocker {

  @Test
  def netHttpTransportValid(): Unit = {
    HttpTransport.getNetHttpTransport() should beAnInstanceOf[NetHttpTransport]
  }

  @Test
  def proxyNoProxy(): Unit = {
    HttpTransport.getProxy(None, 0, None, "") shouldEqual Proxy.NO_PROXY
  }

  @Test
  def proxyHTTP(): Unit = {
    val resultProxy = HttpTransport.getProxy(Some("abc"), 123, None, "")
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
