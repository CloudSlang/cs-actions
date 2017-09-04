package io.cloudslang.content.google.utils.action

import com.google.api.client.json.GenericJson

/**
  * Created by marisca on 4/9/2017.
  */
object OutputUtils {
  def toPretty(prettyPrint: Boolean, genericJson: GenericJson): String =
    if (prettyPrint) genericJson.toPrettyString else genericJson.toString
}
