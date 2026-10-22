/*
 * Copyright 2019-2024 Open Text
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
package io.cloudslang.content.amazon.services;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AmazonRDSService {
    public static DescribeDbInstancesResponse getDBDetails(String dbInstanceIdentifier, RdsClient client) {
        return client.describeDBInstances(DescribeDbInstancesRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier).build());
    }

    public static DBInstance createRDSInstance(
            String dbEngineName, String dbEngineVersion, String dbUsername, String dbPassword,
            String vpcSecurityGroupIds, String dbSubnetGroupName, String dbInstanceIdentifier,
            String dbInstanceSize, int dbStorageSize, String licenseModel, String availabilityZone,
            String tagKeyList, String tagValueList, RdsClient client) {
        CreateDbInstanceRequest.Builder request = CreateDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier)
                .engine(dbEngineName)
                .engineVersion(dbEngineVersion)
                .masterUsername(dbUsername)
                .masterUserPassword(dbPassword)
                .dbInstanceClass(dbInstanceSize)
                .allocatedStorage(dbStorageSize);
        if (!isEmpty(licenseModel)) request.licenseModel(licenseModel);
        if (!isEmpty(availabilityZone)) request.availabilityZone(availabilityZone);
        if (!isEmpty(vpcSecurityGroupIds)) {
            request.vpcSecurityGroupIds(new ArrayList<>(List.of(vpcSecurityGroupIds.split(","))));
        }
        if (!isEmpty(dbSubnetGroupName)) request.dbSubnetGroupName(dbSubnetGroupName);
        request.tags(toTags(tagKeyList, tagValueList));
        return client.createDBInstance(request.build()).dbInstance();
    }

    public static DBInstance deleteRDSInstance(String dbInstanceIdentifier, Boolean skipFinalSnapshot,
            String finalDBSnapshotIdentifier, Boolean deleteAutomatedBackups, RdsClient client) {
        DeleteDbInstanceRequest.Builder request = DeleteDbInstanceRequest.builder()
                .dbInstanceIdentifier(dbInstanceIdentifier);
        if (Boolean.TRUE.equals(skipFinalSnapshot)) request.skipFinalSnapshot(true);
        if (!isEmpty(finalDBSnapshotIdentifier)) request.finalDBSnapshotIdentifier(finalDBSnapshotIdentifier);
        if (Boolean.TRUE.equals(deleteAutomatedBackups)) request.deleteAutomatedBackups(true);
        return client.deleteDBInstance(request.build()).dbInstance();
    }

    public static DBInstance stopRDSInstance(String dbInstanceIdentifier, String finalDBSnapshotIdentifier, RdsClient client) {
        StopDbInstanceRequest.Builder request = StopDbInstanceRequest.builder().dbInstanceIdentifier(dbInstanceIdentifier);
        if (!isEmpty(finalDBSnapshotIdentifier)) request.dbSnapshotIdentifier(finalDBSnapshotIdentifier);
        return client.stopDBInstance(request.build()).dbInstance();
    }

    public static AddTagsToResourceResponse addTagsToDBInstance(
            String resourceName, String tagKeyList, String tagValueList, RdsClient client) {
        return client.addTagsToResource(AddTagsToResourceRequest.builder()
                .resourceName(resourceName).tags(toTags(tagKeyList, tagValueList)).build());
    }

    private static Collection<Tag> toTags(String tagKeyList, String tagValueList) {
        String[] keys = tagKeyList.split(",");
        String[] values = tagValueList.split(",");
        Collection<Tag> tags = new ArrayList<>();
        if (!StringUtils.isEmpty(tagKeyList) && keys.length == values.length) {
            for (int i = 0; i < keys.length; i++) {
                tags.add(Tag.builder().key(keys[i]).value(values[i]).build());
            }
        }
        return tags;
    }
}
