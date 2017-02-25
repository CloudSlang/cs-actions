package io.cloudslang.content.gcloud.utils

import com.google.api.client.json.jackson2.JacksonFactory

/**
  * Created by victor on 2/25/17.
  */
object JsonFactory {
  def getDefaultJacksonFactory: JacksonFactory = JacksonFactory.getDefaultInstance
}
