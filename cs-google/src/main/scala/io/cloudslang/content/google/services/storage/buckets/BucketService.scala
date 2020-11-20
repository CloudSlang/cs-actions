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
import com.google.api.services.storage.model.{Bucket, Buckets}
import io.cloudslang.content.google.services.storage.StorageService
import com.google.api.services.storage.model.Objects

object BucketService {

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String): Bucket =
    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .get(bucketName)
      .execute()

  def get(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String): Buckets =
    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .list(projectId)
      .execute()

  //  def create(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, projectId: String, bucketName: String , bucketLocation:String, storageClass: String):
  //  Bucket = StorageService.bucketService(httpTransport, jsonFactory, credential)
  //    .insert(projectId, new Bucket()
  //      .setName(bucketName)
  //      .setLocation(bucketLocation)
  //        .setLocationType()
  //        .setStorageClass(storageClass)
  //        .setAcl()
  //        .setRetentionPolicy()
  //        .setLabels()
  //
  //      .setVersioning(new Bucket.Versioning().setEnabled(true)))
  //    .set("storageClass ",storageClass)
  //    //.setPredefinedAcl()
  //    //.setPredefinedDefaultObjectAcl()
  //    //.setProjection()
  //    .execute()


  def delete(httpTransport: HttpTransport, jsonFactory: JsonFactory, credential: Credential, bucketName: String) {
    StorageService.bucketService(httpTransport, jsonFactory, credential)
      .delete(bucketName)
      .execute()
  }


}
