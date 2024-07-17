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

import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AmazonRDSService {

    public static DescribeDBInstancesResult getDBDetails(
            final String dbInstanceIdentifier,
            final AmazonRDS amazonRDSClient) {
        DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
        describeDBInstancesRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        return amazonRDSClient.describeDBInstances(describeDBInstancesRequest);
    }

    public static DBInstance createRDSInstance(
            final String dbEngineName, final String dbEngineVersion, final String dbUsername,
            final String dbPassword, final String dbInstanceIdentifier,final String vpcSecurityGroupIds, final String dbInstanceSize, final int dbStorageSize,
            final String licenseModel, final String availabilityZone, final String tagKeyList, final String tagValueList,
            final AmazonRDS amazonRDSClient) {

        CreateDBInstanceRequest createDBInstanceRequest = new CreateDBInstanceRequest();
        createDBInstanceRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        createDBInstanceRequest.setEngine(dbEngineName);
        createDBInstanceRequest.setEngineVersion(dbEngineVersion);
        createDBInstanceRequest.setMasterUsername(dbUsername);
        createDBInstanceRequest.setMasterUserPassword(dbPassword);
        createDBInstanceRequest.setDBInstanceClass(dbInstanceSize);
        createDBInstanceRequest.setAllocatedStorage(dbStorageSize);
        if (!isEmpty(licenseModel))
            createDBInstanceRequest.setLicenseModel(licenseModel);
        if (!isEmpty(availabilityZone))
            createDBInstanceRequest.setAvailabilityZone(availabilityZone);
        if (!isEmpty(vpcSecurityGroupIds))
        {
            String[] vpcSecurityGroupIdList =  vpcSecurityGroupIds.split(",");
            Collection<String> vpcSecurityGroupIdCollection = new ArrayList<>();
            for (int i = 0; i < vpcSecurityGroupIdList.length; i++){
                vpcSecurityGroupIdCollection.add(vpcSecurityGroupIdList[i]);
            }
            createDBInstanceRequest.setVpcSecurityGroupIds(vpcSecurityGroupIdCollection);
        }

        String[] keyList = tagKeyList.split(",");
        String[] valueList = tagValueList.split(",");
        if (!StringUtils.isEmpty(tagKeyList) && (keyList.length == valueList.length)) {
            Collection<Tag> tagCollection = new ArrayList<>();
            for (int i = 0; i < keyList.length; i++) {
                Tag tag = new Tag();
                tag.setKey(keyList[i]);
                tag.setValue(valueList[i]);
                tagCollection.add(tag);
            }
            createDBInstanceRequest.setTags(tagCollection);
        }
        return amazonRDSClient.createDBInstance(createDBInstanceRequest);
    }

    public static DBInstance deleteRDSInstance(final String dbInstanceIdentifier,
                                               final Boolean skipFinalSnapshot, final String finalDBSnapshotIdentifier, final Boolean deleteAutomatedBackups,
                                               final AmazonRDS amazonRDSClient) {

        DeleteDBInstanceRequest deleteDBInstanceRequest = new DeleteDBInstanceRequest();
        deleteDBInstanceRequest.setDBInstanceIdentifier(dbInstanceIdentifier);
        if (skipFinalSnapshot)
            deleteDBInstanceRequest.setSkipFinalSnapshot(skipFinalSnapshot);
        if (!isEmpty(finalDBSnapshotIdentifier))
            deleteDBInstanceRequest.setFinalDBSnapshotIdentifier(finalDBSnapshotIdentifier);
        if (deleteAutomatedBackups)
            deleteDBInstanceRequest.setDeleteAutomatedBackups(deleteAutomatedBackups);

        return amazonRDSClient.deleteDBInstance(deleteDBInstanceRequest);
    }


    public static DBInstance stopRDSInstance(final String dbInstanceIdentifier,
                                             final String finalDBSnapshotIdentifier, final AmazonRDS amazonRDSClient) {

        StopDBInstanceRequest stopDBInstanceRequest = new StopDBInstanceRequest();
        stopDBInstanceRequest.setDBInstanceIdentifier(dbInstanceIdentifier);

        if (!isEmpty(finalDBSnapshotIdentifier))
            stopDBInstanceRequest.setDBSnapshotIdentifier(finalDBSnapshotIdentifier);

        return amazonRDSClient.stopDBInstance(stopDBInstanceRequest);
    }

    public static AddTagsToResourceResult addTagsToDBInstance(final String RDSResourceName,
                                                               final String tagKeyList, final String tagValueList, final AmazonRDS amazonRDSClient) {

        AddTagsToResourceRequest addTagsToResourceRequest = new AddTagsToResourceRequest();
        addTagsToResourceRequest.setResourceName(RDSResourceName);
        String[] keyList = tagKeyList.split(",");
        String[] valueList = tagValueList.split(",");
        if (!StringUtils.isEmpty(tagKeyList) && (keyList.length == valueList.length)) {
            Collection<Tag> tagCollection = new ArrayList<>();
            for (int i = 0; i < keyList.length; i++) {
                Tag tag = new Tag();
                tag.setKey(keyList[i]);
                tag.setValue(valueList[i]);
                tagCollection.add(tag);
            }
            addTagsToResourceRequest.setTags(tagCollection);
        }
        return amazonRDSClient.addTagsToResource(addTagsToResourceRequest);
    }
}
