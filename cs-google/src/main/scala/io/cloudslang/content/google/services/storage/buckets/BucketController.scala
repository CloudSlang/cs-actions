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

import com.google.api.services.storage.model.Bucket
import com.google.api.services.storage.model.Bucket.IamConfiguration.UniformBucketLevelAccess
import com.google.api.services.storage.model.Bucket.{IamConfiguration, RetentionPolicy}
import io.cloudslang.content.google.utils.Constants.StorageBucketConstants.UNIFORM_ACCESS_CONTROL
import io.cloudslang.content.google.utils.action.DefaultValues.StorageBucket.{RETENTION_PERIOD_TYPE_DAYS, RETENTION_PERIOD_TYPE_MONTHS, RETENTION_PERIOD_TYPE_YEARS}
import io.cloudslang.content.utils.NumberUtilities.toLong

object BucketController {

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
