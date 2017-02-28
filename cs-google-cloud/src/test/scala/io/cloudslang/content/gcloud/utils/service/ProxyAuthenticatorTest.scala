package io.cloudslang.content.gcloud.utils.service

import io.cloudslang.content.gcloud.utils.service.HttpTransportUtils.ProxyAuthenticator
import org.junit.Test
import org.specs2.matcher.JUnitMustMatchers

/**
  * Created by victor on 2/25/17.
  */
class ProxyAuthenticatorTest extends JUnitMustMatchers {

  @Test
  def proxyAuthenticatorNewInstance(): Unit = {
    ProxyAuthenticator("a", "b") mustEqual ProxyAuthenticator("a", "b")
  }

  @Test
  def passwordAuthenticationValid(): Unit = {
    val myProxyAuthenticatorMock = ProxyAuthenticator("a", "b")
    val result = myProxyAuthenticatorMock.getPasswordAuthentication
    result.getPassword mustEqual "b".toCharArray
    result.getUserName mustEqual "a"
  }
}
