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
import com.google.api.services.sqladmin.model.{DatabaseInstance, InstancesListResponse, MaintenanceWindow, Operation, Settings}
import io.cloudslang.content.google.services.databases.DatabaseService

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
             , activationPolicy: String, labels: java.util.Map[String, String]): Operation =
    DatabaseService.sqlDatabaseInstanceService(httpTransport, jsonFactory, credential)
      .insert(projectId, new DatabaseInstance().setProject(projectId).setName(databaseInstanceId).setRegion(region).
        setDatabaseVersion(databaseVersion).setRootPassword(password).setGceZone(zone).
        setSettings(new Settings().setTier(tier).setUserLabels(labels).setDataDiskType(dataDiskType)
          .setDataDiskSizeGb(dataDiskSizeInGB).setStorageAutoResize(storageAutoResize).
          setAvailabilityType(availabilityType)
          .setMaintenanceWindow(new MaintenanceWindow().setDay(maintenanceWindowDay).setHour(maintenanceWindowHour))
          .setActivationPolicy(activationPolicy))).execute()


}
