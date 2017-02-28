package io.cloudslang.content.gcloud.utils

import com.google.api.client.json.jackson2.JacksonFactory
import io.cloudslang.content.gcloud.utils.service.JsonFactoryUtils
import org.junit.Test
import org.specs2.matcher.JUnitShouldMatchers

/**
  * Created by victor on 2/25/17.
  */
class JsonFactoryUtilsTest extends JUnitShouldMatchers {

  @Test
  def defaultJacksonFactoryDefault(): Unit = {
    JsonFactoryUtils.getDefaultJacksonFactory shouldEqual JacksonFactory.getDefaultInstance
  }
}
