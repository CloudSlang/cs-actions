package io.cloudslang.content.gcloud.utils

/**
  * Created by sandorr 
  * 2/28/2017.
  */
object InputValidator {

  def validate(inputValue: String) (validator: (String) => (Option[String])): Stream[String] =
    validator(inputValue).toStream

  def proxyPortValidator: (String) => Stream[String] = validate(_) { value =>
    Option.apply(value)
  }
}
