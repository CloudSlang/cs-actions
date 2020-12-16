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
package io.cloudslang.content.google.services.storage.buckets

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.storage.model.Bucket.{IamConfiguration, RetentionPolicy, Versioning}
import com.google.api.services.storage.model.Bucket.IamConfiguration.UniformBucketLevelAccess
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


  def create(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String, bucketName: String, predefinedAcl: String, predefinedDefaultObjectAcl: String, projection: String,location: String, locationType: String, storageClass: String, accessControlType: String, retentionPeriodType: String, retentionPeriod: String, isVersioningEnabled: String,labels: java.util.Map[String, String],  isDefaultEventBasedHoldEnabled: String, metageneration: String): Bucket = {

    val createBucket = new Bucket().setName(bucketName).setLabels(labels)

    if (location.nonEmpty) {
      createBucket.setLocation(location)
    }

    if (locationType.nonEmpty) {
      createBucket.setLocationType(locationType)
    }

    if (storageClass.nonEmpty) {
      createBucket.setStorageClass(storageClass)
    }

    if (accessControlType.nonEmpty) {
      createBucket.setIamConfiguration(getIamConfiguration(accessControlType))
    }

    if (isVersioningEnabled.nonEmpty) {
      createBucket.setVersioning(new Versioning().setEnabled(toBoolean(isVersioningEnabled)))
    }

    if (retentionPeriodType.nonEmpty) {
      createBucket.setRetentionPolicy(BucketController.getRetentionPolicy(retentionPeriodType, retentionPeriod))
    }
    if (retentionPeriod.nonEmpty) {
      createBucket.setRetentionPolicy(new RetentionPolicy().setRetentionPeriod(toLong(retentionPeriod)))
    }

    if (isDefaultEventBasedHoldEnabled.nonEmpty) {
      createBucket.setDefaultEventBasedHold(toBoolean(isDefaultEventBasedHoldEnabled))
    }

    if (metageneration.nonEmpty) {
      createBucket.setMetageneration(toLong(metageneration))
    }

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential).insert(projectId, createBucket)

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
             defaultEventBasedHold: Boolean, versioningEnabled: String, accessControl: String, retentionPeriodType: String,
             retentionPeriod: String, removeRetentionPolicy: String, labels: java.util.Map[String, String]): Bucket = {

    val bucket = new Bucket().setDefaultEventBasedHold(defaultEventBasedHold)
      .setStorageClass(storageClass)

    if (labels.nonEmpty) {
      bucket.setLabels(labels)
    }

    if (versioningEnabled.nonEmpty) {
      bucket.setVersioning(new Bucket.Versioning().setEnabled(toBoolean(versioningEnabled)))
    }

    if (accessControl.nonEmpty) {
      bucket.setIamConfiguration(BucketController.getIamConfiguration(accessControl))
    }

    if (removeRetentionPolicy.isEmpty || removeRetentionPolicy.equalsIgnoreCase(FALSE)) {
      if (retentionPeriod.nonEmpty) {
        bucket.setRetentionPolicy(BucketController.getRetentionPolicy(retentionPeriodType, retentionPeriod))
      }
    }

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential)
      .patch(bucketName, bucket).setProjection(projection)

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

  def getIamConfiguration(bucketPolicy: String): IamConfiguration = {

    if (bucketPolicy.equalsIgnoreCase("uniform")) {
      new IamConfiguration().setUniformBucketLevelAccess(new UniformBucketLevelAccess().setEnabled(true))
    } else {
      new IamConfiguration()
    }

  }

}
