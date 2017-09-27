/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.google.services.compute.compute_engine.operations

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.compute.model.Operation
import io.cloudslang.content.google.services.compute.compute_engine.ComputeService

/**
  * Created by sandorr 
  * 3/1/2017.
  */
object ZoneOperationService {
  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, project: String, zone: String, operationName: String): Operation =
    ComputeService.zoneOperationsService(httpTransport, jsonFactory, credential)
      .get(project, zone, operationName)
      .execute()
}
