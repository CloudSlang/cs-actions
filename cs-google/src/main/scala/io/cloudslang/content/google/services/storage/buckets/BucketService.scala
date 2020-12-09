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
import com.google.api.services.storage.model.Bucket.IamConfiguration
import com.google.api.services.storage.model.Bucket.IamConfiguration.{BucketPolicyOnly, UniformBucketLevelAccess}
import com.google.api.services.storage.model.{Bucket, Buckets}
import io.cloudslang.content.google.services.storage.StorageService
import io.cloudslang.content.utils.NumberUtilities.toLong

object BucketService {

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String,
          ifMetagenerationMatch: String, ifMetagenerationNotMatch: String, projection: String): Bucket = {

    val request = StorageService.bucketService(httpTransport, jsonFactory, credential).get(bucketName).
      setProjection(projection)
    if (ifMetagenerationMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(ifMetagenerationMatch))
    }
    if (ifMetagenerationNotMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(ifMetagenerationNotMatch))
    }

    request.execute()

  }
  

  def create(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String, bucketName: String, location: String
             , locationType: String, storageClass: String, accessControlType: String, labels: java.util.Map[String, String]): Bucket = {


    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .insert(projectId, new Bucket().setName(bucketName).setLocation(location).setLocationType(locationType).setStorageClass(
        storageClass).setLabels(labels).setIamConfiguration(getIamConfiguration(accessControlType)))
      .execute()
  }

  def update(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String, bucketName: String, location: String
             , locationType: String, storageClass: String, accessControlType: String, labels: java.util.Map[String, String]): Bucket = {


    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .update(projectId, new Bucket().setStorageClass(storageClass).setLabels(labels).setIamConfiguration(
        getIamConfiguration(accessControlType)))
      .execute()
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
    if (metagenerationMatch.nonEmpty) {
      request.setIfMetagenerationMatch(toLong(metagenerationMatch))
    }
    request.execute()
  }

  def getIamConfiguration(bucketPolicy: String): IamConfiguration = {

    if (bucketPolicy.equalsIgnoreCase("uniform")) {
      new IamConfiguration().setBucketPolicyOnly(
        new BucketPolicyOnly().setEnabled(true)).setUniformBucketLevelAccess(new UniformBucketLevelAccess().setEnabled(true))
    } else {
      new IamConfiguration()
    }

  }
}
