/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.utils

import com.google.api.services.compute.model.Operation

object OperationStatus {
  def apply(operation: Operation): OperationStatus =
    if (operation.getError == null) SuccessOperation(operation) else ErrorOperation(operation.getError.toPrettyString)
}

sealed abstract class OperationStatus extends Serializable

case class SuccessOperation(operation: Operation) extends OperationStatus

case class ErrorOperation(error: String) extends OperationStatus