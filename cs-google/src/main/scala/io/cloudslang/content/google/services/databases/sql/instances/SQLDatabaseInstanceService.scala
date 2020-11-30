/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.google.services.databases.sql.instances

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.sqladmin.model._
import io.cloudslang.content.google.services.databases._
import io.cloudslang.content.google.utils.action.InputNames.CreateSQLDatabaseInstanceInputs.{ACTIVATION_POLICY_ALWAYS, ACTIVATION_POLICY_NEVER, RESTART_INSTANCE, START_INSTANCE}

object SQLDatabaseInstanceService {

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential,
          projectId: String, databaseInstanceId: String): DatabaseInstance =
    DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
      .get(projectId, databaseInstanceId)
      .execute()

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String):
  InstancesListResponse = DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
    .list(projectId).execute()

  def create(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String,
             databaseInstanceId: String, password: String, region: String, zone: String, databaseVersion: String,
             tier: String, dataDiskType: String, dataDiskSizeInGB: Long,
             storageAutoResize: Boolean, availabilityType: String, maintenanceWindowDay: Int, maintenanceWindowHour: Int
             , activationPolicy: String, labels: java.util.Map[String, String], async: Boolean, timeout: Long,
             pollingInterval: Long): Operation = {

    val operation = DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
      .insert(projectId, new DatabaseInstance().setProject(projectId).setName(databaseInstanceId).setRegion(region).
        setDatabaseVersion(databaseVersion).setRootPassword(password).setGceZone(zone).
        setSettings(new Settings().setTier(tier).setUserLabels(labels).setDataDiskType(dataDiskType)
          .setDataDiskSizeGb(dataDiskSizeInGB).setStorageAutoResize(storageAutoResize).
          setAvailabilityType(availabilityType)
          .setMaintenanceWindow(new MaintenanceWindow().setDay(maintenanceWindowDay).setHour(maintenanceWindowHour))
          .setActivationPolicy(activationPolicy)
          .setBackupConfiguration(
            if (databaseVersion.contains("MYSQL")) new BackupConfiguration().setBinaryLogEnabled(true).setEnabled(true)
            else new BackupConfiguration().setEnabled(true))
        )).execute()
    DatabaseController.awaitSuccessOperation(httpTransport, jsonFactory, credential, projectId, operation,
      Some(databaseInstanceId)
      , async, timeout, pollingInterval)
  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential,
             projectId: String, databaseInstanceId: String, async: Boolean, timeout: Long,
             pollingInterval: Long): Operation = {
    val operation = DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
      .delete(projectId, databaseInstanceId)
      .execute()
    DatabaseController.awaitSuccessOperation(httpTransport, jsonFactory, credential, projectId, operation,
      Some(databaseInstanceId)
      , async, timeout, pollingInterval)
  }

  def instanceOperation(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential,
                        projectId: String, databaseInstanceId: String, instanceOperation: String, async: Boolean, timeout: Long,
                        pollingInterval: Long): Operation = {
    val operation =
      if (instanceOperation.equalsIgnoreCase(RESTART_INSTANCE)) {
        DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential).restart(projectId, databaseInstanceId)
          .execute()
      } else {
        DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
          .patch(projectId, databaseInstanceId, new DatabaseInstance()
            .setSettings(
              if (instanceOperation.equalsIgnoreCase(START_INSTANCE)) new Settings().setActivationPolicy(ACTIVATION_POLICY_ALWAYS)
              else new Settings().setActivationPolicy(ACTIVATION_POLICY_NEVER)
            )).execute()
      }

    DatabaseController.awaitSuccessOperation(httpTransport, jsonFactory, credential, projectId, operation,
      Some(databaseInstanceId)
      , async, timeout, pollingInterval)
  }


}
