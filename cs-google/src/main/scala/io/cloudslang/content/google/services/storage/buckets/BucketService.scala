/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
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



package io.cloudslang.content.google.services.storage.buckets

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.storage.model.Bucket.IamConfiguration.UniformBucketLevelAccess
import com.google.api.services.storage.model.Bucket.{IamConfiguration, RetentionPolicy, Versioning}
import com.google.api.services.storage.model.{Bucket, Buckets}
import io.cloudslang.content.google.services.storage.StorageService
import io.cloudslang.content.google.utils.Constants.FALSE
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toLong

import scala.collection.JavaConversions._

object BucketService {

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String,
          ifMetagenerationMatch: String, ifMetagenerationNotMatch: String, projection: String): Bucket = {

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential).get(bucketName).
      setProjection(projection)

    if (ifMetagenerationMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(ifMetagenerationMatch))
    }
    if (ifMetagenerationNotMatch.nonEmpty) {
      request.setIfMetagenerationNotMatch(toLong(ifMetagenerationNotMatch))
    }
    request.execute()

  }

  def create(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String,
             bucketName: String, predefinedAcl: String, predefinedDefaultObjectAcl: String, projection: String,
             location: String, locationType: String, storageClass: String, accessControlType: String,
             retentionPeriodType: String, retentionPeriod: String, isVersioningEnabled: String,
             labels: java.util.Map[String, String], isDefaultEventBasedHoldEnabled: String): Bucket = {

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential)
      .insert(projectId, BucketController.createBucket(bucketName,location,locationType, storageClass,
        accessControlType,retentionPeriodType,retentionPeriod,isVersioningEnabled,labels,isDefaultEventBasedHoldEnabled))

    if (predefinedAcl.nonEmpty) {
      request.setPredefinedAcl(predefinedAcl)
    }
    if (predefinedDefaultObjectAcl.nonEmpty) {
      request.setPredefinedDefaultObjectAcl(predefinedDefaultObjectAcl)
    }
    if (projection.nonEmpty) {
      request.setProjection(projection)
    }
    request.execute()
  }

  def getBucket(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String,
                projection: String): Bucket = {
    StorageService.bucketService(httpTransport, jsonFactory, credential).get(bucketName).
      setProjection(projection).execute()
  }

  def update(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String,
             ifMetagenerationMatch: String, ifMetagenerationNotMatch: String, predefinedAcl: String,
             predefinedDefaultObjectAcl: String, projection: String, storageClass: String,
             defaultEventBasedHold: String, versioningEnabled: String, accessControl: String, retentionPeriodType: String,
             retentionPeriod: String, removeRetentionPolicy: String, labels: java.util.Map[String, String]): Bucket = {

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential)
      .update(bucketName, BucketController.updateBucket(storageClass,defaultEventBasedHold,versioningEnabled
        ,accessControl,retentionPeriodType,retentionPeriod,removeRetentionPolicy,labels)).setProjection(projection)

    if (ifMetagenerationMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(ifMetagenerationMatch))
    }
    if (ifMetagenerationNotMatch.nonEmpty) {
      request.setIfMetagenerationNotMatch(toLong(ifMetagenerationNotMatch))
    }
    if (predefinedAcl.nonEmpty) {
      request.setPredefinedAcl(predefinedAcl)
    }
    if (predefinedDefaultObjectAcl.nonEmpty) {
      request.setPredefinedDefaultObjectAcl(predefinedDefaultObjectAcl)
    }
    request.execute()
  }

  def list(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String, maxResults: String, prefix: String, pageToken: String, projection: String): Buckets = {
    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .list(projectId).setMaxResults(maxResults.toLong).setPrefix(prefix)
      .setPageToken(pageToken).setProjection(projection)
      .execute()
  }

  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String,
             metagenerationMatch: String, metagenerationNotMatch: String): Void = {

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential).delete(bucketName)

    if (metagenerationNotMatch.nonEmpty) {
      request.setIfMetagenerationNotMatch(toLong(metagenerationNotMatch))
    }

    if (metagenerationMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(metagenerationMatch))
    }
    request.execute()
  }
}
