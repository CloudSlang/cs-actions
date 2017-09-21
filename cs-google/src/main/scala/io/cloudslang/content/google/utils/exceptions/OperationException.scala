package io.cloudslang.content.google.utils.exceptions


case class OperationException(message: String) extends RuntimeException(message)