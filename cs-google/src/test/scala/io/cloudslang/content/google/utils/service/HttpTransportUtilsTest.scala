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
