package io.cloudslang.content.gcloud.utils

import java.io.FileInputStream

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.ComputeScopes
import org.junit.Test
import org.specs2.matcher.JUnitShouldMatchers
import org.specs2.mock.Mockito

/**
  * Created by victor on 2/26/17.
  */

class GoogleAuthTest extends JUnitShouldMatchers with Mockito {
  @Test
  def fromJsonWithScopesDefault(): Unit = {
//    val httpTransportMock = mock[HttpTransport]
//    val jsonFactoryMock = mock[JsonFactory]
//
//
//
//    val googleCredentialMock =
//    doReturn(googleCredentialMock).when(googleCredentialMock).fromStream(any[FileInputStream], httpTransportMock, jsonFactoryMock)
//
//
//    val resultCred = GoogleAuth.fromJsonWithScopes(mock[FileInputStream], httpTransportMock, jsonFactoryMock, List(ComputeScopes.COMPUTE))
//
//    resultCred.getJsonFactory shouldEqual jsonFactoryMock
//    resultCred.getTransport shouldEqual httpTransportMock
  }
}
