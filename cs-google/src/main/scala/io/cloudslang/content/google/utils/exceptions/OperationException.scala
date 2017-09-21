package io.cloudslang.content.google.utils.exceptions

import com.google.api.services.compute.model.Operation
import org.apache.commons.lang3.StringUtils.EMPTY

import scala.util.Try


class OperationException private(ex: RuntimeException) extends RuntimeException(ex) {
  def this(operation: Operation) = this(new RuntimeException(Try(operation.getError.toPrettyString).getOrElse(EMPTY)))

  def this(message: String) = this(new RuntimeException(message))

  def this(message: String, throwable: Throwable) = this(new RuntimeException(message, throwable))
}

object OperationException {
  def apply(operation: Operation): RuntimeException = new OperationException(operation)

  def apply(message: String): RuntimeException = new OperationException(message)

  def apply(message: String, throwable: Throwable): RuntimeException = new OperationException(message, throwable)
}