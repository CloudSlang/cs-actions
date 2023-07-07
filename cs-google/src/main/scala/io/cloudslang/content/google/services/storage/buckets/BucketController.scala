/*
 * Copyright 2023 Open Text
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

import com.google.api.services.storage.model.Bucket
import com.google.api.services.storage.model.Bucket.IamConfiguration.UniformBucketLevelAccess
import com.google.api.services.storage.model.Bucket.{IamConfiguration, RetentionPolicy, Versioning}
import io.cloudslang.content.google.utils.Constants.FALSE
import io.cloudslang.content.google.utils.Constants.StorageBucketConstants.UNIFORM_ACCESS_CONTROL
import io.cloudslang.content.google.utils.action.DefaultValues.StorageBucket.{RETENTION_PERIOD_TYPE_DAYS, RETENTION_PERIOD_TYPE_MONTHS, RETENTION_PERIOD_TYPE_YEARS}
import io.cloudslang.content.utils.BooleanUtilities.toBoolean
import io.cloudslang.content.utils.NumberUtilities.toLong

import scala.collection.JavaConversions._

object BucketController {

  def updateBucket(storageClass: String, defaultEventBasedHold: String, versioningEnabled: String,
                   accessControl: String, retentionPeriodType: String, retentionPeriod: String,
                   removeRetentionPolicy: String, labels: java.util.Map[String, String]): Bucket = {

    val bucket = new Bucket().setStorageClass(storageClass)

    if (defaultEventBasedHold.nonEmpty) {
      bucket.setDefaultEventBasedHold(toBoolean(defaultEventBasedHold))
    }

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
    bucket
  }

  def createBucket(bucketName: String, location: String, locationType: String, storageClass: String,
                   accessControlType: String, retentionPeriodType: String, retentionPeriod: String,
                   isVersioningEnabled: String, labels: java.util.Map[String, String],
                   isDefaultEventBasedHoldEnabled: String): Bucket = {

    val createBucket = new Bucket().setName(bucketName)

    if (labels.nonEmpty) {
      createBucket.setLabels(labels)
    }

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
      createBucket.setIamConfiguration(BucketController.getIamConfiguration(accessControlType))
    }

    if (isVersioningEnabled.nonEmpty) {
      createBucket.setVersioning(new Versioning().setEnabled(toBoolean(isVersioningEnabled)))
    }

    if (retentionPeriod.nonEmpty) {
      createBucket.setRetentionPolicy(BucketController.getRetentionPolicy(retentionPeriodType, retentionPeriod))
    }

    if (isDefaultEventBasedHoldEnabled.nonEmpty) {
      createBucket.setDefaultEventBasedHold(toBoolean(isDefaultEventBasedHoldEnabled))
    }
    createBucket
  }

  def getIamConfiguration(bucketPolicy: String): IamConfiguration = {
    new IamConfiguration().setUniformBucketLevelAccess(
      if (bucketPolicy.equalsIgnoreCase(UNIFORM_ACCESS_CONTROL)) {
        new UniformBucketLevelAccess().setEnabled(true)
      } else {
        new UniformBucketLevelAccess().setEnabled(false)
      })
  }

  def getRetentionPolicy(retentionPeriodType: String, retentionPeriod: String): RetentionPolicy = {
    val retentionPolicy = new Bucket.RetentionPolicy()
    if (retentionPeriod.nonEmpty) {
      retentionPolicy.setRetentionPeriod(
        if (retentionPeriodType.equalsIgnoreCase(RETENTION_PERIOD_TYPE_DAYS)) {
          (toLong(retentionPeriod) * 86400)
        } else if (retentionPeriodType.equalsIgnoreCase(RETENTION_PERIOD_TYPE_MONTHS)) {
          (toLong(retentionPeriod) * 2678400)
        } else if (retentionPeriodType.equalsIgnoreCase(RETENTION_PERIOD_TYPE_YEARS)) {
          (toLong(retentionPeriod) * 31557600)
        } else {
          toLong(retentionPeriod)
        })
    }
    retentionPolicy
  }

}
