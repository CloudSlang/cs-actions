/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils.service

import io.cloudslang.content.google.utils.service.HttpTransportUtils.ProxyAuthenticator
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
