package io.cloudslang.content.google.utils

import com.google.api.services.compute.model.Operation

object OperationStatus {
  def apply(operation: Operation): OperationStatus =
    if (operation.getError == null) SuccessOperation(operation) else ErrorOperation(operation.getError.toPrettyString)
}

sealed abstract class OperationStatus extends Serializable

case class SuccessOperation(operation: Operation) extends OperationStatus

case class ErrorOperation(error: String) extends OperationStatus