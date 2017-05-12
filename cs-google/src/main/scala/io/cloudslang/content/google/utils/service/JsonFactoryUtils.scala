package io.cloudslang.content.google.utils.service

import com.google.api.client.json.jackson2.JacksonFactory

/**
  * Created by victor on 2/25/17.
  */
object JsonFactoryUtils {
  def getDefaultJacksonFactory: JacksonFactory = JacksonFactory.getDefaultInstance
}
